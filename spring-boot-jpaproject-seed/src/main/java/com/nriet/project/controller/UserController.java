/**
 * @author: CodeGenerator
 * @version: 1.0
 * @since 2018/05/04
 */
package com.nriet.project.controller;

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

import com.nriet.project.service.UserService;
import com.nriet.project.model.User;
import com.nriet.framework.core.vo.Result;
import com.nriet.framework.core.vo.ResultGenerator;

@RestController
@RequestMapping("/user")
public class UserController {
	private static final Logger logger = Logger.getLogger(UserController.class);

	@Resource(name = "userService")
	private UserService  userService;

	@PostMapping(value = "/save")
	public  @ResponseBody Result save(@RequestBody User user){
		user = userService.save(user);
		return	ResultGenerator.genSuccessResult(user);
	}

	@PostMapping(value = "/delete")
   	public  @ResponseBody Result delete(@RequestBody User user){
   		userService.delete(user);
		return	ResultGenerator.genSuccessResult();
   	}

	@GetMapping("/{id}")
    public Result detail(@PathVariable Integer id) {
        User user = userService.findOne(id);
        return ResultGenerator.genSuccessResult(user);
    }

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<User> list = userService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}



