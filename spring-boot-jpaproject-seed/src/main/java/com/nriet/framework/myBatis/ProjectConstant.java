package com.nriet.framework.myBatis;

import static com.nriet.framework.myBatis.ProjectConstant.BASE_PACKAGE;

import com.nriet.framework.util.PropertiesUtil;

/**
 * 项目常量
 */
public final class ProjectConstant {
	private static final PropertiesUtil preu = PropertiesUtil.Instances("generator.properties");
    public static final String BASE_PACKAGE = preu.get("basePackage");//项目基础包名称，根据自己公司的项目修改

    public static final String MODEL_PACKAGE = BASE_PACKAGE + ".model";//Model所在包
    public static final String MAPPER_PACKAGE = BASE_PACKAGE + ".dao";//Mapper所在包
    public static final String SERVICE_PACKAGE = BASE_PACKAGE + ".service";//Service所在包
    public static final String SERVICE_IMPL_PACKAGE = SERVICE_PACKAGE + ".impl";//ServiceImpl所在包
    public static final String CONTROLLER_PACKAGE = BASE_PACKAGE + ".controller";//Controller所在包

    public static final String JPA_BASIC_SERVICE = BASE_PACKAGE.substring(0, BASE_PACKAGE.lastIndexOf(".")) + ".framework" + ".springjpa.BasicJpaService";//Mapper插件基础接口的完全限定名
}
