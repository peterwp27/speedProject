/**
 * @author: ${author}
 * @version: 1.0
 * @since ${date}
 */
package ${packagePrefix}.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Entity;
import javax.persistence.Table;
<#if pkResult?size gt 1>
import javax.persistence.EmbeddedId;
<#else>
import javax.persistence.SequenceGenerator;
</#if>

@Entity
@Table(name = "${tableName}")
<#if pkResult?size == 1 >
@SequenceGenerator(name = "${tableName}_SEQ", sequenceName = "SEQ_${tableName}_ID" ,allocationSize=1)
</#if>
public  class ${className}{

<#if pkResult?size != 1 >
	@EmbeddedId
	private PK  pk = new PK();
<#else>
	private ${keyType}  ${keyVar} ;
</#if>
<#list columnResult as prop>
    <#if pkResult?size != 1 >
    @Column(name = "$prop.name")
    </#if>
    private ${prop.dataType} ${prop.javaName};
</#list>


<#list pkResult as prop>
	<#if pkResult?size == 1>
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="${tableName}_SEQ")
	@Column(name = "${prop.name}")
</#if>
	public ${prop.dataType} ${prop.getterMethod}(){
<#if pkResult?size gt 1 >
		return pk.${prop.javaName};
<#else>
		return this.${prop.javaName};
</#if>
	}
</#list>

 <#list columnResult as prop>
    <#if pkResult?size==1 >
    @Column(name = "${prop.name}")
    </#if>
    public ${prop.dataType} ${prop.getterMethod}(){
		return ${prop.javaName};
	}
</#list>

<#list pkResult as prop>
	public void ${prop.setterMethod} (${prop.dataType} ${prop.javaName}){
	<#if pkResult?size gt 1 >
		this.pk.${prop.javaName} = ${prop.javaName};
	<#else>
		this.${prop.javaName} = ${prop.javaName};
	</#if>
	}
</#list>

<#list columnResult as prop>
    public void ${prop.setterMethod} (${prop.dataType} ${prop.javaName}){
		this.${prop.javaName} = ${prop.javaName};
	}
</#list>

<#if pkResult?size gt 1 >
	@Embeddable
	public static class PK implements Serializable {
	<#list pkResult as prop >
		@Column(name = "${prop.name}")
		private ${prop.dataType} ${prop.javaName};
	</#list>
	}
</#if>
}