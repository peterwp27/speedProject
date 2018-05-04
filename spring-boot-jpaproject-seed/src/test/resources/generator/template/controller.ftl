/**
 * @author: ${author}
 * @version: 1.0
 * @since ${date}
 */
package ${packagePrefix}.controller;

import java.util.List;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import ${packagePrefix}.service.${className}Service;
import ${packagePrefix}.model.${className};
import ${baseFrameworkPackage}.core.vo.Result;
import ${baseFrameworkPackage}.core.vo.ResultGenerator;

@RestController
@RequestMapping("/${classVar}")
public class ${className}Controller {
	private static final Logger logger = Logger.getLogger(${className}Controller.class);

	@Resource(name = "${classVar}Service")
	private ${className}Service  ${classVar}Service;

	@PostMapping(value = "/save")
	public  @ResponseBody Result save(@RequestBody ${className} ${classVar}){
		${classVar} = ${classVar}Service.save(${classVar});
		return	ResultGenerator.genSuccessResult(${classVar});
	}

	@PostMapping(value = "/delete")
   	public  @ResponseBody Result delete(@RequestBody ${className} ${classVar}){
   		${classVar}Service.delete(${classVar});
		return	ResultGenerator.genSuccessResult();
   	}

	@GetMapping("/{id}")
    public Result detail(@PathVariable Integer id) {
        ${className} ${classVar} = ${classVar}Service.findOne(id);
        return ResultGenerator.genSuccessResult(${classVar});
    }

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<${className}> list = ${classVar}Service.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}



