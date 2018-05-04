/**
 * @author: ${author}
 * @version: 1.0
 * @since ${date}
 */
package ${packagePrefix}.service;
import org.springframework.stereotype.Service;
import com.nriet.framework.springjpa.BasicJpaService;
import ${packagePrefix}.dao.${className}Dao;
import ${packagePrefix}.model.${className};

@Service("${classVar}Service")
public class ${className}Service extends BasicJpaService<${className},<#if pkResult?size gt 1>${className}.PK<#else>${keyType}</#if>,${className}Dao> {

}
