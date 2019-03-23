package org.wht.handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.wht.entity.Address;
import org.wht.entity.Student;

//@SessionAttributes("student3")//省略了value(可以放多个) （value="student3,student2"）如果要在request中存放student3对象 同时将该对象放入session域中
@SessionAttributes(types = { Student.class, Address.class }) // 将该类型的都放入session 可以选择多种类型 types=Student.class
//这是一个普通的类  --> 变成 控制器 
@Controller
@RequestMapping(value = "Handler")
/*
 * 这个是类注解 映射路径 先找类再找方法 也是随便写
 * RequestMapping("Handler")等价于RequestMapping(value="Handler")
 */
public class SpringMVCHandler {

	// 调用welcome/abc(这个随便写) 会被拦截
	/*
	 * 可以配置method 控制拦截的请求形式
	 */
	@RequestMapping(value = "welcome/abc", method = RequestMethod.POST, params = { "name=zs", "age!=23", "!height" })
	public String welcome() {
		// 默认是请求转发
		return "success";
	}

	// 限制请求头
	@RequestMapping(value = "welcome2", headers = {
			"Accept= text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8" })
	public String welcome2() {
		return "success";
	}

	@RequestMapping(value = "welcome3/*/test")
	public String welcome3() {
		return "success";
	}

	@RequestMapping(value = "welcome4/**/test")
	public String welcome4() {
		return "success";
	}

	@RequestMapping(value = "welcome5/a?c/test")
	public String welcome5() {
		return "success";
	}

	// 以前需要request.getparam..
	// 现在通过前端传值 welcome6{name} 接收 传到@PathVariable("name")修饰的变量中
	@RequestMapping(value = "welcome6/{name}")
	public String welcome6(@PathVariable("name") String name) {
		System.out.println("*****name传过来啦" + name);
		return "success";
	}

	/**
	 * 以下是普通的增删改查模式
	 */
	/*
	 * @RequestMapping(value="testPost/{id}",method=RequestMethod.POST) public
	 * String testPost(@PathVariable("id") Integer id) {
	 * System.out.println("*****增id："+id); //调service增删改查 return "success"; }
	 * 
	 * @RequestMapping(value="testGet/{id}",method=RequestMethod.GET) public String
	 * testGet(@PathVariable("id") Integer id) { System.out.println("*****查id"+id);
	 * //调service增删改查 return "success"; }
	 * 
	 * @RequestMapping(value="testDelete/{id}",method=RequestMethod.DELETE) public
	 * String testDelete(@PathVariable("id") Integer id) {
	 * System.out.println("*****删id"+id); //调service增删改查 return "success"; }
	 * 
	 * @RequestMapping(value="testPut/{id}",method=RequestMethod.PUT) public String
	 * testPut(@PathVariable("id") Integer id) { System.out.println("*****改id"+id);
	 * //调service增删改查 return "success"; }
	 */
	/**
	 * RESTful风格开始
	 */
	@RequestMapping(value = "testRest/{id}", method = RequestMethod.POST)
	public String testPost(@PathVariable("id") Integer id) {
		System.out.println("*****post增id：" + id);
		// 调service增删改查
		return "success";
	}

	@RequestMapping(value = "testRest/{id}", method = RequestMethod.PUT)
	// @ResponseBody()
	public String testPut(@PathVariable("id") Integer id) {
		System.out.println("*****put改id：" + id);
		// 调service增删改查
		return "success";
	}

	@RequestMapping(value = "testRest/{id}", method = RequestMethod.GET)
	public String testGet(@PathVariable("id") Integer id) {
		System.out.println("*****get查id：" + id);
		// 调service增删改查
		return "success";
	}

	@RequestMapping(value = "testRest/{id}", method = RequestMethod.DELETE)
	public String testDelete(@PathVariable("id") String id) {
		System.out.println("*****delete删id：" + id);
		// 调service增删改查
		return "success";
	}

	/**
	 * 以上的！！！！ delete|put失败的解决方法 三种简单处理的办法！ 第一：tomcat换到7.0以及以下版本
	 * 第二：请求先转给一个Controller,再返回jsp页面 第三种：在你的success页面头部文件将 <%@ page language="java"
	 * contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	 * isErrorPage="true"%> 多加一句话：isErrorPage设置为true，默认为false
	 * 
	 */

	/**
	 * RESTful风格结束
	 */
	@RequestMapping(value = "testParam")
	public String testParam(@RequestParam("uname") String name,
			@RequestParam(value = "uage", required = false, defaultValue = "23") Integer age) {
		// 等价于String name = request.getParamter("uname");
		System.out.println("*****param获取表单的值uname：" + name + age);
		return "success";
	}

	@RequestMapping(value = "testRequetHeader")
	public String testRequetHeader(@RequestHeader("Accept-Language") String al) {
		// 等价于String name = request.getParamter("uname");
		System.out.println("*****获取RequetHeader：" + al);
		return "success";
	}

	@RequestMapping(value = "testRequetHeader1")
	public String testRequetHeader1(@RequestHeader("Cookie") String cookie) {
		System.out.println("*****获取RequetHeader中的cookie：" + cookie);
		return "success";
	}

	@RequestMapping(value = "testCookieValue")
	public String testCookieValue(@CookieValue("JSESSIONID") String jsessionid) {
		// 等价于String name = request.getParamter("uname");
		System.out.println("*****获取JSESSIONID：" + jsessionid);
		return "success";
	}

	// 只要name值对应 就会自动传入实体中，类似于struts2 中的 action传值
	@RequestMapping(value = "testObjectProperties")
	public String testObjectProperties(Student student) {// student的属性必须和form表单中name属性一致，支持级联
		// 等价于String name = request.getParamter("uname");
		System.out.println("*****获取student信息：" + student.getId() + "," + student.getName() + ","
				+ student.getAddress().getHomeAddress() + "," + student.getAddress().getSchoolAddress());
		return "success";
	}

	// 测试原生态servlet的调用
	@RequestMapping(value = " ")
	public String testServletAPI(HttpServletRequest request, HttpServletResponse response) {// student的属性必须和form表单中name属性一致，支持级联
		String name = request.getParameter("name");
		System.out.println(request);
		return "success";
	}

	/**
	 * 以下 测试 返回值带数据
	 */
	@RequestMapping(value = "testModelAndView")
	public ModelAndView testModelAndView() {// ModelAndView 既有数据又有视图
		ModelAndView mv = new ModelAndView("success");// 同样会自动加前缀后缀
		Student student = new Student();
		student.setId(2);
		student.setName("zd");
		mv.addObject("student", student);// 相当于request.setAttribute中
		return mv;
	}

	@RequestMapping(value = "testModelMap")
	public String testModelMap(ModelMap mm) {
		Student student = new Student();
		student.setId(3);
		student.setName("zd1");
		mm.put("student1", student);// 相当于request.setAttribute中
		// 请求转发forward 这样写 不会加前缀后缀
		// 重定向redirect
		// return "success"
		// return "forward:/views/success.jsp";
		return "redirect:/views/success.jsp";
	}

	@RequestMapping(value = "testMap")
	public String testMap(Map<String, Object> map) {
		Student student = new Student();
		student.setId(4);
		student.setName("zd2");
		map.put("student2", student);// 相当于request.setAttribute中
		return "success";
	}

	@RequestMapping(value = "testModel")
	public String testModel(Model model) {
		Student student = new Student();
		student.setId(6);
		student.setName("zd3");
		model.addAttribute("student3", student);// 相当于request.setAttribute中
		return "success";
	}

	// @ModelAttribute//在任何一次请求前都会先执行 @ModelAttribute 修饰的 方法 一般 一个控制器中 只设计一个功能
	public void queryStudentById(@RequestParam("id") Integer id, Map<String, Object> map) {
		// StudentServicr stuService = new St....
		// Student student=stuService.query......by(id)
		// 模拟三层查询 下边是绑定好的
		Student student = new Student();
		student.setId(id);
		student.setName("zs");
		student.setAge(43);
		map.put("stu", student);// 约定：map key的值如果等于 方法参数类型的首字母小写 就会自动将值传入
		// 可以有多种 值传递方式 可以 通过返回值 可以通过Model.addAttribute...
		// map.put("student",student)如果不一致 需要在 对应参数前 加 @ModelAttribute("key的值")
	}

	// 修改 zs->ls
	@RequestMapping(value = "testModelAttribute")
	public String testModelAttribute(@ModelAttribute("nstu") Student student) {
		// 现在的student 是在先查询queryStudentById 查询到的实体， 之后在返回的实体信息基础上 ， 通过表单传值 进行覆盖 （相当于
		// 修改，更新） 只写入数据库即可
		System.out.println(student.getId() + "," + student.getName() + "," + student.getAge());
		// model.addAttribute("student3", student);// 相当于request.setAttribute中
		return "success";
	}

	@RequestMapping(value = "testI18n")
	public String testI18n() {
		return "success";
	}

	@RequestMapping(value = "testConverter") // 满足前置条件了 从字符串 转换到 Student 会触发自己写的转换器
	public String testConverter(@RequestParam("studentInfo") Student student) {// 前端接收字符串 3-zs-23
		System.out.println("*****字符串 转 学生信息 ：" + student.getId() + "-" + student.getName() + "-" + student.getAge());
		return "success";
	}

	@RequestMapping(value = "testFormatDateAndNum")
	public String testFormatDateAndNum(@Valid Student student, BindingResult result, Map map) {// 如果格式化报错 ，
																								// 错误信息会放入第二个参数中，相当于try-catch，前端就不会报错
																								// 了
		System.out.println("*****格式化后的 学生信息 ：" + student.getId() + "-" + student.getName() + "-" + student.getAge()
				+ "-" + student.getBirthday());
		if (result.getErrorCount() > 0) {
			map.put("errors", result.getFieldErrors());// 传入request域中
			for (FieldError error : result.getFieldErrors()) {
				System.out.println(error.getDefaultMessage());
			}

		}
		return "success";
	}

	@ResponseBody // 加了之就可以在ajax中调用json对象了 谁调反馈给谁 集合的话就会自动用js可操作的json数组 不需要手动转换
	@RequestMapping(value = "testJson")
	public List<Student> testJson() {
		// 调service层 进行操作
		// 假设 查询出一个学生list 下方是模拟
		List<Student> students = new ArrayList<>();
		Student stu1 = new Student("zs");
		Student stu2 = new Student("ls");
		Student stu3 = new Student("ww");
		students.add(stu1);
		students.add(stu2);
		students.add(stu3);
		return students;
	}

	@ResponseBody
	@RequestMapping(value = "testResponseBody")
	public Student testResponseBody() {
		Student stu1 = new Student("zs");
		return stu1;
	}

	// 处理文件上传 ---上传到本地路径
	@RequestMapping(value = "testUpload")
	public String testUpload(@RequestParam("desc") String desc, @RequestParam("file") MultipartFile file)
			throws IOException {// 用 MultipartFile类型 接收file类型数据
		System.out.println("文件描述：" + desc);
		// jsp中上传的文件：file 拿到流信息 就拿到一切了 流里边有一切信息
		// 拿到输入流
		InputStream input = file.getInputStream();
		// 来一个输出流 先把路径写死
		//OutputStream out = new FileOutputStream("d:\\文件名.类型");
		String prefix = UUID.randomUUID().toString();
        prefix = prefix.replace("-","");
        String filename = prefix+"_"+file.getOriginalFilename();//获取上传的文件名 包含类型的 并添加唯一前缀
        System.out.println(filename);
        OutputStream out = new FileOutputStream("d:\\"+filename);
		// 输入流转到输出流 因为是字节流 需要一个人数组做缓冲区
		byte[] bs = new byte[1024];
		int len = -1;
		//从输入流读取 字节数组
		while ((len = input.read(bs)) != -1) {
			//输出流 输出
			out.write(bs,0,len);
		}
		out.close();
		input.close();
		System.out.println("上传成功！");
		// 文件上传到服务器中的某个文件里
		return "success";
	}
	@RequestMapping(value="testUpload1",method=RequestMethod.POST)
	//这个是上传到tomcat服务器上
    public String testUpload(HttpServletRequest request,@RequestParam(value="desc",required=false) String desc,@RequestParam(value="file") CommonsMultipartFile file) throws Exception{
        System.out.println(desc);
        ServletContext servletContext = request.getServletContext();
        String realPath = servletContext.getRealPath("/upload");//获取服务器路径
        File file1 = new File(realPath);
        if(!file1.exists()){
            file1.mkdir();
        }
        OutputStream out;
        InputStream in;
        String prefix = UUID.randomUUID().toString();
        prefix = prefix.replace("-","");
        String fileName = prefix+"_"+file.getOriginalFilename();//添加唯一前缀
        System.out.println(fileName);
        out = new FileOutputStream(new File(realPath+"\\"+fileName));
        in = file.getInputStream();
        IOUtils.copy(in, out);//类似于用缓存数组复制
        out.close();
        in.close();
        return "success";
    }
}
