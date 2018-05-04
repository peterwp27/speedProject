import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.base.CaseFormat;
import com.nriet.framework.myBatis.ProjectConstant;
import com.nriet.framework.util.PropertiesUtil;

import freemarker.template.TemplateExceptionHandler;

/**
 * 代码生成器，根据数据表名称生成对应的Model、Mapper、Service、Controller简化开发。
 */
public class CodeGenerator {
	private final static Log log = LogFactory.getLog(CodeGenerator.class);
	private static final PropertiesUtil preu = PropertiesUtil.Instances("generator.properties");
    //JDBC配置，请修改为你项目的实际配置
    private static final String JDBC_URL = preu.get("jdbc_url");
    private static final String JDBC_USERNAME = preu.get("jdbc_username");
    private static final String JDBC_PASSWORD = preu.get("jdbc_password");
    private static final String JDBC_DIVER_CLASS_NAME = preu.get("jdbc_driver");

    private static final String PROJECT_PATH = System.getProperty("user.dir");//项目在硬盘上的基础路径
    private static final String TEMPLATE_FILE_PATH = PROJECT_PATH + preu.get("template_path");//模板位置

    private static final String JAVA_PATH = preu.get("targetProject"); //java文件路径
    private static final String RESOURCES_PATH = preu.get("targetSource");//资源文件路径

    private static final String PACKAGE_PATH_SERVICE = packageConvertPath(ProjectConstant.SERVICE_PACKAGE);//生成的Service存放路径
    private static final String PACKAGE_PATH_SERVICE_IMPL = packageConvertPath(ProjectConstant.SERVICE_IMPL_PACKAGE);//生成的Service实现存放路径
    private static final String PACKAGE_PATH_CONTROLLER = packageConvertPath(ProjectConstant.CONTROLLER_PACKAGE);//生成的Controller存放路径
    private static final String PACKAGE_PATH_MODEL = packageConvertPath(ProjectConstant.MODEL_PACKAGE);//生成的Controller存放路径
    private static final String PACKAGE_PATH_DAO = packageConvertPath(ProjectConstant.MAPPER_PACKAGE);//生成的Controller存放路径

    private static final String AUTHOR = "CodeGenerator";//@author
    private static final String DATE = new SimpleDateFormat("yyyy/MM/dd").format(new Date());//@date
	private static String pkgPrefix =  ProjectConstant.BASE_PACKAGE.substring(0, ProjectConstant.BASE_PACKAGE.lastIndexOf("."));

	private static String module = "";

	private static String subModule = ProjectConstant.BASE_PACKAGE.substring(ProjectConstant.BASE_PACKAGE.lastIndexOf(".") + 1);
	
	private static String tablePrefix="";

	private static String tableName = "";

	private static String classVar;

	private static String className;

	private static String keyVar;

	private static String keyType;

	private static String packageName;
    public static void main(String[] args) {
//    	System.out.println(preu.get("jdbc_url"));
        genCode("user");
        //genCodeByCustomModelName("输入表名","输入自定义Model名称");
    }

    /**
     * 通过数据表名称生成代码，Model 名称通过解析数据表名称获得，下划线转大驼峰的形式。
     * 如输入表名称 "t_user_detail" 将生成 TUserDetail、TUserDetailMapper、TUserDetailService ...
     * @param tableNames 数据表名称...
     */
    public static void genCode(String... tableNames) {
        for (String tableName : tableNames) {
            genCodeByCustomModelName(tableName, null);
        }
    }
    /**
     * 
     * @return
     * @throws Exception
     */
	private static Connection getConnection() throws Exception {

	    Properties localProperties = new Properties();
	    localProperties.put("remarksReporting","true");//注意这里
        localProperties.put("user", JDBC_USERNAME);
        localProperties.put("password", JDBC_PASSWORD);

	//	   orcl为数据库的SID
        Class.forName(JDBC_DIVER_CLASS_NAME).newInstance();
	    Connection conn= DriverManager.getConnection(JDBC_URL,localProperties);
	    return conn;
	}
	/**
	 * 获取字段值
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public static List<Column> getLsColumns(String tableName) throws Exception {
		Connection conn = getConnection();
		List<Column> lsColumns = new ArrayList<Column>(10);
		PreparedStatement stmt = conn.prepareStatement("select *  from "+tableName+" where 1=0 ");
		ResultSet resultSet = stmt.executeQuery();
		ResultSetMetaData rsmd = resultSet.getMetaData();
		int n = rsmd.getColumnCount();
		for (int i = 1; i <= n; i++)
		{
			String colName = rsmd.getColumnName(i);
			String fieldName = StringUtil.toBeanPatternStr(colName);
			String dataType = rsmd.getColumnClassName(i);
			if("java.math.BigDecimal".equals(dataType)&&rsmd.getScale(i)==0)
				dataType= "Long";
			if("oracle.sql.CLOB".equals(dataType) )
				dataType= "String";
			Column column = new Column();
			column.setName(colName) ;
			column.setJavaName(fieldName) ;
			column.setDataType(dataType.endsWith("Timestamp")?"java.util.Date":dataType);
			column.setPrecision(String.valueOf(rsmd.getPrecision(i)));
			column.setScale( String.valueOf(rsmd.getScale(i)) );
			column.setLength( String.valueOf(rsmd.getColumnDisplaySize(i)));
			column.setNullable(String.valueOf(1==rsmd.isNullable(i)));

//			获取列注释
			DatabaseMetaData dbmd = conn.getMetaData();
			ResultSet rs = dbmd.getColumns(null, null, tableName, null);
			while (rs.next()) {
				if (colName.equals(rs.getString("COLUMN_NAME"))){
					String  comments = rs.getString("REMARKS");
					column.setComments(StringUtil.asString(comments));
				}
			}
		//				获取主键列
			ResultSet rs2 = dbmd.getPrimaryKeys(null, null, tableName);
			while (rs2.next()) {
				if (colName.equals(rs2.getString("COLUMN_NAME")))
					column.setColumnKey("TRUE");
			}
			log.debug("------------------"+column+"---------------------" );
			log.debug("<td><spring:message code=\""+StringUtil.toBeanPatternStr(tableName)+"."+column.getJavaName()+"\" />"
			+"</td> <td><input name=\""+column.getJavaName()+"\" type=\"text\" id=\""+column.getJavaName()+"\" ltype=\"text\" validate=\"{required:true,minlength:3,maxlength:10}\" />" +"</td> " );
			lsColumns.add(column);
		}
		return lsColumns;
	}
    /**
     * 通过数据表名称，和自定义的 Model 名称生成代码
     * 如输入表名称 "t_user_detail" 和自定义的 Model 名称 "User" 将生成 User、UserMapper、UserService ...
     * @param tableName 数据表名称
     * @param modelName 自定义的 Model 名称
     */
    public static void genCodeByCustomModelName(String tableName, String modelName) {
        genModelAndMapper(tableName, modelName);
        genService(tableName, modelName);
        genController(tableName, modelName);
    }
    /**
     * 
     * @param lsColumns
     * @throws Exception
     */
	public static void initCodeTool(List<Column> lsColumns) throws Exception {
		classVar = StringUtil.toBeanPatternStr(tableName.substring(tablePrefix.length()));
		className = StringUtil.firstCharUpperCase(classVar);
		pkgPrefix =pkgPrefix
		+ (StringUtil.isEmpty(module) ? "" : ("." + module))
		+ (StringUtil.isEmpty(subModule) ? "" : ("." + subModule));
		modelTool.put("tableName", tableName);
		modelTool.put("className", className);
		modelTool.put("module", module);
		modelTool.put("subModule", subModule);
		modelTool.put("classVar", classVar);
		modelTool.put("packagePrefix", pkgPrefix);
		modelTool.put("packageName", packageName);

		//把主键和非主键分别放入tool
		splitColumns(lsColumns);
		List<Column> pkResult = (List<Column>) modelTool.get("pkResult");
		int pkCnt = pkResult.size();
		if (pkCnt > 1 || pkCnt ==0) {
			log.debug("创建复合主键类：");
			keyType = "PK";
			keyVar =  "pk";
			modelTool.put("keyType", keyType);
			modelTool.put("keyVar", keyVar);
		}else if (pkCnt == 1) {
			Column column =   pkResult.get(0);
			keyType =column.getDataType();
			keyVar = column.getJavaName();
			modelTool.put("keyType", keyType);
			modelTool.put("keyVar", keyVar);
		} else {
		//	throw new Exception("该表无主键！！！！！！！！");
		}
//		org.apache.commons.lang.StringUtils  stringUtil = new  org.apache.commons.lang.StringUtils();
//		tool.put("stringUtil",stringUtil );

		modelTool.put("needUpdate",true);
		modelTool.put("date", DateUtil.getCurrentDay());
		modelTool.put("author", System.getProperty("user.name"));

	}
	private static Map<String,Object> modelTool = new HashMap<>();
	/**
	 * 
	 * @param lsColumns
	 */
	private static void splitColumns( List<Column> lsColumns ) {
		List<Column> lsColumnTemp = new ArrayList<Column>(lsColumns.size());
		List<Column> pkColumns = new ArrayList<Column>(2);
		for (Column column : lsColumns) {
			if ("TRUE".equalsIgnoreCase(column.getColumnKey())) {
				pkColumns.add(column);

			}else{
				lsColumnTemp.add(column);
			}
		}
		modelTool.put("columnResult", lsColumnTemp);
		modelTool.put("pkResult", pkColumns);
	}
    public static void genModelAndMapper(String tableName, String modelName) {
    	String modelNameUpperCamel = StringUtils.isEmpty(modelName) ? tableNameConvertUpperCamel(tableName) : modelName;
        try {
            freemarker.template.Configuration cfg = getConfiguration();
            initCodeTool(getLsColumns(tableName));
            modelTool.put("date", DATE);
            modelTool.put("author", AUTHOR);
            modelTool.put("baseRequestMapping", modelNameConvertMappingPath(modelNameUpperCamel));
            modelTool.put("className", modelNameUpperCamel);
            modelTool.put("tableName", tableName);
            modelTool.put("classVar", CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, modelNameUpperCamel));
            modelTool.put("basePackage", ProjectConstant.BASE_PACKAGE);
            modelTool.put("baseFrameworkPackage", ProjectConstant.BASE_PACKAGE.substring(0, ProjectConstant.BASE_PACKAGE.lastIndexOf(".")) + ".framework");

            File file = new File(PROJECT_PATH + JAVA_PATH + PACKAGE_PATH_MODEL + modelNameUpperCamel + ".java");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            cfg.getTemplate("model.ftl").process(modelTool, new FileWriter(file));
            
            File file1 = new File(PROJECT_PATH + JAVA_PATH + PACKAGE_PATH_DAO + modelNameUpperCamel + "Dao.java");
            if (!file1.getParentFile().exists()) {
                file1.getParentFile().mkdirs();
            }
            cfg.getTemplate("dao.ftl").process(modelTool, new FileWriter(file1));

            System.out.println(modelNameUpperCamel + "Controller.java 生成成功");
        } catch (Exception e) {
            throw new RuntimeException("生成Controller失败", e);
        }
        System.out.println(modelNameUpperCamel + ".java 生成成功");
        System.out.println(modelNameUpperCamel + "Mapper.java 生成成功");
        System.out.println(modelNameUpperCamel + "Mapper.xml 生成成功");
    }

    public static void genService(String tableName, String modelName) {
    	String modelNameUpperCamel = StringUtils.isEmpty(modelName) ? tableNameConvertUpperCamel(tableName) : modelName;
        try {
            freemarker.template.Configuration cfg = getConfiguration();

//            initCodeTool(getLsColumns(tableName));
//            modelTool.put("date", DATE);
//            modelTool.put("author", AUTHOR);
//            modelTool.put("baseRequestMapping", modelNameConvertMappingPath(modelNameUpperCamel));
//            modelTool.put("className", modelNameUpperCamel);
//            modelTool.put("tableName", tableName);
//            modelTool.put("classVar", CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, modelNameUpperCamel));
//            modelTool.put("basePackage", ProjectConstant.BASE_PACKAGE);
//            modelTool.put("baseFrameworkPackage", ProjectConstant.BASE_PACKAGE.substring(0, ProjectConstant.BASE_PACKAGE.lastIndexOf(".")) + ".framework");

            File file = new File(PROJECT_PATH + JAVA_PATH + PACKAGE_PATH_SERVICE + modelNameUpperCamel + "Service.java");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            cfg.getTemplate("service.ftl").process(modelTool,
                    new FileWriter(file));
            System.out.println(modelNameUpperCamel + "Service.java 生成成功");

//            File file1 = new File(PROJECT_PATH + JAVA_PATH + PACKAGE_PATH_SERVICE_IMPL + modelNameUpperCamel + "ServiceImpl.java");
//            if (!file1.getParentFile().exists()) {
//                file1.getParentFile().mkdirs();
//            }
//            cfg.getTemplate("service-impl.ftl").process(data,
//                    new FileWriter(file1));
//            System.out.println(modelNameUpperCamel + "ServiceImpl.java 生成成功");
        } catch (Exception e) {
            throw new RuntimeException("生成Service失败", e);
        }
    }

    public static void genController(String tableName, String modelName) {
    	String modelNameUpperCamel = StringUtils.isEmpty(modelName) ? tableNameConvertUpperCamel(tableName) : modelName;
        try {
            freemarker.template.Configuration cfg = getConfiguration();

//          initCodeTool(getLsColumns(tableName));
//          modelTool.put("date", DATE);
//          modelTool.put("author", AUTHOR);
//          modelTool.put("baseRequestMapping", modelNameConvertMappingPath(modelNameUpperCamel));
//          modelTool.put("className", modelNameUpperCamel);
//          modelTool.put("tableName", tableName);
//          modelTool.put("classVar", CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, modelNameUpperCamel));
//          modelTool.put("basePackage", ProjectConstant.BASE_PACKAGE);
//          modelTool.put("baseFrameworkPackage", ProjectConstant.BASE_PACKAGE.substring(0, ProjectConstant.BASE_PACKAGE.lastIndexOf(".")) + ".framework");

            File file = new File(PROJECT_PATH + JAVA_PATH + PACKAGE_PATH_CONTROLLER + modelNameUpperCamel + "Controller.java");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            //cfg.getTemplate("controller-restful.ftl").process(data, new FileWriter(file));
            cfg.getTemplate("controller.ftl").process(modelTool, new FileWriter(file));

            System.out.println(modelNameUpperCamel + "Controller.java 生成成功");
        } catch (Exception e) {
            throw new RuntimeException("生成Controller失败", e);
        }

    }

    private static freemarker.template.Configuration getConfiguration() throws IOException {
        freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_23);
        cfg.setDirectoryForTemplateLoading(new File(TEMPLATE_FILE_PATH));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        return cfg;
    }

    private static String tableNameConvertLowerCamel(String tableName) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, tableName.toLowerCase());
    }

    private static String tableNameConvertUpperCamel(String tableName) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName.toLowerCase());

    }

    private static String tableNameConvertMappingPath(String tableName) {
        tableName = tableName.toLowerCase();//兼容使用大写的表名
        return "/" + (tableName.contains("_") ? tableName.replaceAll("_", "/") : tableName);
    }

    private static String modelNameConvertMappingPath(String modelName) {
        String tableName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, modelName);
        return tableNameConvertMappingPath(tableName);
    }

    private static String packageConvertPath(String packageName) {
        return String.format("/%s/", packageName.contains(".") ? packageName.replaceAll("\\.", "/") : packageName);
    }

}
