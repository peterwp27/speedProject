/**
 * @author: CodeGenerator
 * @version: 1.0
 * @since 2018/05/04
 */
package com.nriet.project.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.SequenceGenerator;

@Entity
@Table(name = "user")
@SequenceGenerator(name = "user_SEQ", sequenceName = "SEQ_user_ID" ,allocationSize=1)
public  class User{

	private java.lang.Integer  id ;
    private java.lang.String username;
    private java.lang.String password;
    private java.lang.String nickName;
    private java.lang.Integer sex;
    private java.util.Date registerDate;


	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="user_SEQ")
	@Column(name = "id")
	public java.lang.Integer getId(){
		return this.id;
	}

    @Column(name = "username")
    public java.lang.String getUsername(){
		return username;
	}
    @Column(name = "password")
    public java.lang.String getPassword(){
		return password;
	}
    @Column(name = "nick_name")
    public java.lang.String getNickName(){
		return nickName;
	}
    @Column(name = "sex")
    public java.lang.Integer getSex(){
		return sex;
	}
    @Column(name = "register_date")
    public java.util.Date getRegisterDate(){
		return registerDate;
	}

	public void setId (java.lang.Integer id){
		this.id = id;
	}

    public void setUsername (java.lang.String username){
		this.username = username;
	}
    public void setPassword (java.lang.String password){
		this.password = password;
	}
    public void setNickName (java.lang.String nickName){
		this.nickName = nickName;
	}
    public void setSex (java.lang.Integer sex){
		this.sex = sex;
	}
    public void setRegisterDate (java.util.Date registerDate){
		this.registerDate = registerDate;
	}

}