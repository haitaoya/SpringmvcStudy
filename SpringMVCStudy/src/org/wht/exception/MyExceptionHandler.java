package org.wht.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
//annotations(), basePackageClasses(), basePackages() 方法定制用于选择控制器子集
@ControllerAdvice() // 专门处理异常 可以处理任何类中的异常  Spring能扫描到的地方。就可以实现全局异常的回调
public class MyExceptionHandler {

	// 该方法可以捕获 本类中 其他方法抛出的ArithmeticException,ArrayIndexOutOfBoundsException异常
	// 也可以 写自己定义的异常类，进行捕获
	@ExceptionHandler({ ArithmeticException.class, ArrayIndexOutOfBoundsException.class })
	public ModelAndView handlerArithmeticException(Exception e) {// 异常会自动放入e中 只能写异常参数
		ModelAndView mv = new ModelAndView("error");// error 是view 与前端交互
		mv.addObject("e", e);// model
		System.out.println(e + "=========可以捕获任何类的异常=======");
		return mv;
	}

	@ExceptionHandler(MyArrayIndexOutofBoundsException.class)
	public ModelAndView MyArrayIndexOutofBoundsException(MyArrayIndexOutofBoundsException e) {
		ModelAndView mv = new ModelAndView("error");// error 是view与前端交互
		mv.addObject("e", e);// model
		System.out.println(e + "=========捕获自定义的异常，自定义的异常，只能被当前类捕获，无法实现全局捕获(原因未知，应该是能够捕获的)=======");
		return mv;
	}

	@ExceptionHandler(Exception.class)
	public ModelAndView Exception(Exception e) {// 异常会自动放入e中 只能写异常参数 
		ModelAndView mv = new ModelAndView("error");// error是view 与前端交互
		mv.addObject("e", e);// model
		System.out.println(e + "=========捕获自=======");
		return mv;
	}

}
