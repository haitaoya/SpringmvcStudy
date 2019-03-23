package org.wht.converter;

import org.springframework.core.convert.converter.Converter;
import org.wht.entity.Student;
//转换器的类
public class MyConverter implements Converter<String, Student>{

	@Override
	public Student convert(String source) {//source 是一个字符串 2-zs-23
		//接收前端传来的字符串
		String[] studentStrArr = source.split("-");		
		
		Student student = new Student();
		student.setId(Integer.parseInt(studentStrArr[0]));
		student.setName(studentStrArr[1]);
		student.setAge(Integer.parseInt(studentStrArr[2]));
		return student;
	}


}
