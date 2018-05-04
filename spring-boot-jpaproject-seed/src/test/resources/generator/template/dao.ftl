/**
 * @author: ${author}
 * @version: 1.0
 * @since ${date}
 */
package ${packagePrefix}.dao;

import ${packagePrefix}.model.${className};
<#if pkResult?size gt 1>
import ${packagePrefix}.model.${className}.PK;
</#if>
import org.springframework.data.jpa.repository.JpaRepository;

public interface ${className}Dao extends JpaRepository<${className},<#if pkResult?size gt 1>${className}.PK<#else>${keyType}</#if>>{

}