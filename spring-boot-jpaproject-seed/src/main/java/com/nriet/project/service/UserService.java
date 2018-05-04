/**
 * @author: CodeGenerator
 * @version: 1.0
 * @since 2018/05/04
 */
package com.nriet.project.service;
import org.springframework.stereotype.Service;
import com.nriet.framework.springjpa.BasicJpaService;
import com.nriet.project.dao.UserDao;
import com.nriet.project.model.User;

@Service("userService")
public class UserService extends BasicJpaService<User,java.lang.Integer,UserDao> {

}
