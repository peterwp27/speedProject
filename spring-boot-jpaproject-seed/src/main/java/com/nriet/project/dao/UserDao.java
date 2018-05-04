/**
 * @author: CodeGenerator
 * @version: 1.0
 * @since 2018/05/04
 */
package com.nriet.project.dao;

import com.nriet.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User,java.lang.Integer>{

}