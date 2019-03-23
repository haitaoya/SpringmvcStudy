package org.wht.entity;

import java.util.Date;

import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Email;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

public class Student {
	@NumberFormat(pattern="###,#")//这个是控制前端输入的格式 必须为###.#  然后就可转换成int型
	private int id;
	private String name;
	private Address address;
	private int age;
	@Email//必须满足邮箱格式
	private String email;
	@DateTimeFormat(pattern="yyyy-MM-dd")//这个是控制前端输入的格式 必须为yyyy-MM-dd  然后就可以转换成Date类型
	@Past//必须是过去的日期
	private Date birthday;
	
	public Student(){}
	public Student(String name){
		this.name=name;
	}
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

}
