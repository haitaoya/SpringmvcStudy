package org.wht.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 自定义异常显示页面
 * 捕获数组越界异常
 * @author Administrator
 *
 */
@ResponseStatus(value=HttpStatus.FORBIDDEN,reason="数组越界啦！！！")
public class MyArrayIndexOutofBoundsException extends Exception{//变成一个自定义异常
	
}
