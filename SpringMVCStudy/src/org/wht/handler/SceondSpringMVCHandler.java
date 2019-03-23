package org.wht.handler;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.wht.exception.MyArrayIndexOutofBoundsException;
/**
 * 这个类用来测试 怎么捕获异常（自己写）
 * 
 * @author Administrator
 *
 */
@RequestMapping("second")
@Controller
public class SceondSpringMVCHandler {
	@RequestMapping("testExceptionHandler")
	public String testExceptionHandler() {
		// 模拟 try{} catch(e){} mvc不能直接try catch
		System.out.println(1 / 0);// 存在 数学异常ArithmeticException
		return "success";
	}

	@RequestMapping("testExceptionHandler2")
	public String testExceptionHandler2() {
		int[] nums = new int[2];
		System.out.println(nums[2]);// 存在 数组越界异常ArrayIndexOutOfBoundsException
		return "success";
	}

	// 测试 自定义异常类
	@RequestMapping(value = "testMyException") // throws 抛出去
	public String testMyException(@RequestParam("i") Integer i) throws MyArrayIndexOutofBoundsException {
		if (i == 3) {
			throw new MyArrayIndexOutofBoundsException(); // 创建一个异常
		}
		return "success";
	}

//	@ExceptionHandler(MyArrayIndexOutofBoundsException.class)
//	public ModelAndView MyArrayIndexOutofBoundsException(MyArrayIndexOutofBoundsException e) {// 异常会自动放入e中 只能写异常参数
//		ModelAndView mv = new ModelAndView("error");// error 是view 与前端交互
//		mv.addObject("e", e);// model
//		System.out.println(e + "=========捕获自定义的异常，自定义的异常，只能被当前类捕获，无法实现全局捕获(原因未知，应该是能够捕获的)=======");
//		return mv;
//	}

	@RequestMapping(value = "testMyException2") // throws 抛出去
	public String testMyException2(@RequestParam("i") Integer i) {
		if (i == 3) {
			return "forward:testResponseStatus"; // 跳转到当前类下一个方法进行异常显示
			// return "redirect:testResponseStatus"; //跳转到下一个方法进行异常显示
		}
		return "success";
	}

	@ResponseStatus(value = HttpStatus.CONFLICT, reason = "测试！！！")
	@RequestMapping(value = "testResponseStatus") // throws 抛出去
	public String testResponseStatus() {
		return "success";
	}

	// 测试405 异常
	@RequestMapping(value = "welcome1", method = RequestMethod.POST)
	public String welcome1() {
		// 默认是请求转发 经过视图解析器
		return "success";
	}

	@RequestMapping(value = "testSimpleMappingExceptionResolver")
	public String testSimpleMappingExceptionResolver() {
		System.out.println(1 / 0);
		return "success";
	}
}
