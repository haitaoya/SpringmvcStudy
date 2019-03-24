##1、需要的jar包

spring-aop.jar
spring-bean.jar
spring-context.jar
spring-core.jar
spring-web.jar
commons-logging.jar
spring-expression.jar
spring-webmvc.jar

##2、第一个springMVC程序（装spring插件）
新建 springmvc配置文件
选择常用的namespce 打勾 aop context  mvc

普通的servlet需要到web.xml中找url-parttern 交给对应的servlet去处理

现在用springmvc 不需要配置的（怎么去定位serlvet呢？）
####1.配置springmvc自带的servlet
（查看源码 需要关联 source（源码）包 不是jar包！！！）
 配置web.xml的servlet，拦截全部请求 交给springmvc处理
 
 	&lt;servlet&gt;
  	&lt;servlet-name&gt;springDispatcherServlet&lt;/servlet-name&gt;
  	&lt;servlet-class&gt;org.springframework.web.servlet.DispatcherServlet&lt;/servlet-class&gt;
	&lt;/servlet&gt;
	  &lt;servlet-mapping&gt;
	   &lt;servlet-name&gt;springDispatcherServlet&lt;/servlet-name&gt;
	   &lt;url-pattern&gt;/&lt;/url-pattern&gt;
	  &lt;/servlet-mapping&gt;
  ------------------完善---------------------
  
	   &lt;servlet&gt;
	  	&lt;servlet-name&gt;springDispatcherServlet&lt;/servlet-name&gt;
	  	&lt;servlet-class&gt;org.springframework.web.servlet.DispatcherServlet&lt;/servlet-class&gt;
  	&lt;init-param&gt;
  		&lt;param-name&gt;contextConfigLocation&lt;/param-name&gt;
  		&lt;param-value&gt;classpath:springmvc.xml&lt;/param-value&gt;//关联spirng文件
  		//也可 把springmvc文件直接放大web-inf下边  需要重命名--springDispatcherServlet-servlet.xml
  	&lt;/init-param&gt;
  	&lt;load-on-startup&gt;1&lt;/load-on-startup&gt;//自启
	  &lt;/servlet&gt;
	  &lt;servlet-mapping&gt;
	   &lt;servlet-name&gt;springDispatcherServlet&lt;/servlet-name&gt;
	   &lt;url-pattern&gt;/&lt;/url-pattern&gt;
    &lt;!-- 斜杠表示拦截一切请求  也可以拦截特定请求
     &lt;url-pattern&gt;/user&lt;/url-pattern&gt;
      &lt;url-pattern&gt;/user/aaaa.action&lt;/url-pattern&gt;
       &lt;url-pattern&gt;.action&lt;/url-pattern&gt;   这样配置可以兼容普通servlet 	       	  
       需要springmvc处理就加.action后缀找@requestmapping 
        默认不加就发送到普通servlet找url-mapping 或者 @webservlet
     --&gt;
	  &lt;/servlet-mapping&gt;
	  
####2.控制机 c   --- 》命名规范  handler 、action、servlet、controller等等
  新建一个class  注解@controller
  
  方法前加@RequestMapping
  类前也可以加  @RequestMapping  
  RequestMapping 可以设置method 控制拦截的请求形式
	  eg：
		@RequestMapping(value="welcome/abc",method=RequestMethod.POST)
 RequestMapping 可以设置params= {"name"}  强调必须有这个参数
  RequestMapping 可以设置params= {"name=zs"}  强调必须有这个参数且必须为zs
	@RequestMapping(value="welcome/abc",method=RequestMethod.POST,params= {"name=zs","age!=23"})强调必须有这个参数且必须为zs ，age不能为23  但是可以为空可以没有
	@RequestMapping(value="welcome/abc",method=RequestMethod.POST,params= {"name=zs","age!=23","!height"})
不能有name=height这个属性！！！
####3.spirngmvc.xml中加自动扫描
&lt;!-- 扫描有注解的包 --&gt;

	&lt;context:component-scan base-package="org.wht.handler"&gt;&lt;/context:component-scan&gt;

	&lt;!-- 配置视图解析器(InternalResourceViewResolver)
		id 为了别的调用 这个不需要 id也就不需要写
	 --&gt;
	&lt;bean class="org.springframework.web.servlet.view.InternalResourceViewResolver"&gt;
		&lt;property name="prefix" value="/view/"&gt;&lt;/property&gt;
		&lt;property name="suffix" value=".jsp"&gt;&lt;/property&gt;
	&lt;/bean&gt;
####4.建一个view/success.jsp 进行测试

ant风格请求路径
？ 单字符
*任意个字符 0个或多个
**  任意目录

restFul风格
可以用同一个mapping 通过不同的request请求方法 跳转不同的方法中
需要配置 filter

	  &lt;!-- 增加 HiddenHttpMethodFilter 过滤器：目的是给普通浏览器增加 put/delete请求方法--&gt;
	  &lt;filter&gt;
	  	&lt;filter-name&gt;HiddenHttpMethodFilter&lt;/filter-name&gt;
  	&lt;filter-class&gt;org.springframework.web.filter.HiddenHttpMethodFilter&lt;/filter-class&gt;
	  &lt;/filter&gt;
	  &lt;filter-mapping&gt;
  	&lt;filter-name&gt;HiddenHttpMethodFilter&lt;/filter-name&gt;
  	&lt;url-pattern&gt;/*&lt;/url-pattern&gt;
	  &lt;/filter-mapping&gt;
  
  通过表单的hidden属性 name=_method  value的值需要大写  
 
	  &lt;form action="Handler/testRest/1234" method="post"&gt;
			&lt;input type="submit" value="增" &gt;
	&lt;/form&gt;
	&lt;form action="Handler/testRest/1234" =&gt;
		&lt;input type="hidden" name="_method" value="DELETE"/&gt;&lt;!--delete是枚举值 大写  --&gt;
		&lt;input type="submit" value="删" &gt;
	&lt;/form&gt;
	&lt;form action="Handler/testRest/1234" method="post"&gt;
		&lt;input type="hidden" name="_method" value="PUT"/&gt;
		&lt;input type="submit" value="改" &gt;
	&lt;/form&gt;
	&lt;form action="Handler/testRest/1234" method="get"&gt;
		&lt;input type="submit" value="查" &gt;
	&lt;/form&gt;
	
--------------------------------------
	//对应handler层
	@RequestMapping(value="testRest/{id}",method=RequestMethod.POST)
	public String testPost(@PathVariable("id") Integer id) {
		System.out.println("*****post增id："+id);
		//调service增删改查
		return "success";
	}
	@RequestMapping(value="testRest/{id}",method=RequestMethod.PUT)
	//@ResponseBody()
	public String testPut(@PathVariable("id") Integer id) {
		System.out.println("*****put改id："+id);
		//调service增删改查
		return "success";
	}
	@RequestMapping(value="testRest/{id}",method=RequestMethod.GET)
	public String testGet(@PathVariable("id") Integer id) {
		System.out.println("*****get查id："+id);
		//调service增删改查
		return "success";
	}
	@RequestMapping(value="testRest/{id}",method=RequestMethod.DELETE)
	public String testDelete(@PathVariable("id") Integer id) {
		System.out.println("*****delete删id："+id);
		//调service增删改查
		return "success";
	}
以上的！！！！
	 delete|put失败的解决方法
	三种简单处理的办法！ 第一：tomcat换到7.0以及以下版本 
	 第二：请求先转给一个Controller,再返回jsp页面  
	 第三种：在你的success页面头部文件将 &lt;%@ 			       pagelanguage="java"contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%&gt; 多加一句话：isErrorPage设置为true，默认为false

------------------------------------------------
	 表单传值
	 &lt;form action="Handler/testParam" method="get"&gt;
		name&lt;input name="uname"&gt;
		&lt;!-- age&lt;input name="uage"&gt; --&gt;&lt;!-- 能多不能少 --&gt;
		&lt;input type="submit" value="提交" &gt;
	&lt;/form&gt;
	
-------------
	@RequestMapping(value="testParam")
	public String testParam(@RequestParam("uname") String name,@RequestParam(value="uage",required=false,defaultValue="23") Integer age) {
		//等价于String name = request.getParamter("uname");
		System.out.println("*****param获取表单的值uname："+name+age);
		return "success";
	}
	 
	 ------------------------
	 @RequestHeader("Accept-Language")取请求头信息  写key就可以
	 
	 @CookieValue
	 （前置知识：服务端在接受客户端第一次请求时，会给客户端分配一个session
	 该session包含一个sessionid    并且服务端会在第一次响应客户端时，将该sessionid赋值给JSESSIONID并传递给客户端的cookie中）
	 
	 @RequestMapping(value="testCookieValue")
	public String testCookieValue(@CookieValue("JSESSIONID") String jsessionid) {
		//等价于String name = request.getParamter("uname");
		System.out.println("*****获取JSESSIONID："+jsessionid);
		return "success";
	}
	
-----------------------------
####springmvc 处理逻辑 、流程
请求：前端发请求 a -&gt; @RequestMapping("a")
	处理请求中的参数xyz：
		@RequestMapping("a")
		public String aa(@Xxx注解("xyz") String xyz){
		return ....
		}
	使用对象（实体类）接受请求参数
	只要form表单的name和实体属性名一致 即可
	
实现原生servlet 只需要在参数中写	
	@RequestMapping(value = "testServletAPI")
	public String testServletAPI(HttpServletRequest request,HttpServletResponse response) {//student的属性必须和form表单中name属性一致，支持级联
		System.out.println(request);
		return "success";
	}
	 
--------------------------------------------
####处理模型数据
如果跳转时需要带数据：V、M, 则可以使用一下方式：
	数据放在request 作用域：
		ModelAndView    ModelMap    Map    ModelAndView
	如何将数据放在session中？ 加注解@SessionAttributes 在类前
	
 @ModelAttribute  经常在更新时使用
	  默认先调用 再进入相应方法  会覆盖
	  ---------------------
#### 视图
InternalResourceView（和JstlView 是父子关系  默认使用 父类 如果发现jsp中发现包含jstl语言 自动强转为 子类JstlView）
	  JstlView  可以解析jstl 实现国际化操作
####国际化
不同地区 不同国家 进行不同的显示
			具体实现国际化的步骤：
#####a 创建资源文件----文件名格式 --- 
base_zh_CN_properties 
				  		基名_ 语言_地区_properties
				  		可以省略地区  		
			i18n_zh_CH_properties
#####b 配置 在spirngmvc 
	 &lt;!-- 加载国际化文件  找i18n 开头的文件
	1.将ResourceBundleMessageSource在程序加载时 加入springmvc springmvc在启动时 会自动查找id=messageSource的bean
	2.如果配置了ResourceBundleMessageSource 则 该类会在程序响应时 介入
	--&gt;
	&lt;bean id="messageSource" class="org.springframework.context.support.ResourceBundleMessageSource"&gt;
		&lt;property name="basename" value="i18n"&gt;&lt;/property&gt;
	&lt;/bean&gt;
	
--------	
#####c. 显示
通过jstl使用国际化
		需要两个jar  jstl .jar  standard.har
		通过调整浏览器的语言 可以控制不同的显示 ascii码的转换
	  视图解析器
	  InternalResourceViewResolver
		  要解析视图 之前必须通过controller 现在 可以通过一个springMVc标签替代
	
	  &lt;mvc：view-controller path="Handler/welcome" view-name="success"/&gt;
	  
 如果controller中也有 相同的方法  都会屏蔽掉controller 层的requestmapping
如果需要共存  需要加注解 ：

		 &lt;mvc:annotation-driven&gt;&lt;/mvc:annotation-driven&gt;
这是一个基础配置 ，一般 要用mvc 都要加这句话

####处理请求方式：
请求转发 变成 重定向 ：  请求转发forward  这样写 不会加前缀后缀 
									 重定向redirect
		//return "success"
		//return "forward:/views/success.jsp";
		return "redirect:/views/success.jsp";		 

		
####处理静态资源：
		 
		 可以和用户进行交互的资源 为动态资源 例如 天气 等
		 静态资源 例如图片 123.jpg
		   但是直接访问 404  因为 会被web.xml配置的 / 拦截 进入 springDispatcherServlet 交给 了springmvc 找对应的RequestMapping("123.jpg")
		   所以404
		   
		   解决方法：
		   如果需要springmvc处理 则交给 RequestMapping处理
		   如果不需要 则交给tomcat默认的servlet 去处理 如果有 对应请求拦截 则交给相应的servlet去处理
		   &lt;servlet&gt;
  	&lt;servlet-name&gt;abc&lt;/servlet-name&gt;
  	&lt;servlet-class&gt;xx.xx.abc&lt;/servlet-class&gt;
	  &lt;/servlet&gt;
	  &lt;servlet-mapping&gt;
  	&lt;servlet-name&gt;abc&lt;/servlet-name&gt;
  	&lt;url-pattern&gt;/abc&lt;/url-pattern&gt;
	  &lt;/servlet-mapping&gt;
	  
####使用tomcat的Servlet
   默认的Tomcat的servlet在/conf/web.xml中
   ？？？怎么实现？
			加两个配置
			
	&lt;mvc:default-servlet-handler&gt;&lt;/mvc:default-servlet-handler&gt;
	&lt;mvc:annotation-driven&gt;&lt;/mvc:annotation-driven&gt;
	-----------------------------
####类型转换
spirngmvc内置的一些类型转换
		也可以自己创建一个类型转换器：
#####a: 编写 自定义类型转换器的类（实现 Converter接口）
	public class MyConverter implements Converter&lt;String, Student&gt;{
	@Override
	public Student convert(String source) {
	//source 是一个字符串 2-zs-23
		//接收前端传来的字符串
		String[] studentStrArr = source.split("-");		
		
		Student student = new Student();
		student.setId(Integer.parseInt(studentStrArr[0]));
		student.setName(studentStrArr[1]);
		student.setAge(Integer.parseInt(studentStrArr[2]));
		return null;
	}
}
#####2.配置
将MyConverter加入到spirngmvc中

	 &lt;!-- 配置自己的类型转换器 需要3步 --&gt;
	&lt;!-- 1.将自定义转换器 纳入SpringIOC容器 --&gt;
	&lt;bean id="myConverter" class="org.wht.converter.MyConverter"&gt;&lt;/bean&gt;
	&lt;!-- 2.将myConverter 纳入SpringMVC提供的转换器Bean --&gt;
	&lt;bean id="conversionService" class="org.springframework.context.support.ConversionServiceFactoryBean"&gt;
		&lt;property name="converters"&gt;
			&lt;set&gt;
				&lt;ref bean="myConverter"/&gt;
			&lt;/set&gt;
		&lt;/property&gt;
	&lt;/bean&gt;
	&lt;!-- 3.将conversionService注册到 annotation-driven中--&gt;
	&lt;mvc:annotation-driven conversion-service="conversionService"&gt;&lt;/mvc:annotation-driven&gt;
#####3.测试
		@RequestMapping(value = "testConverter")
		//满足前置条件了   从字符串 转换到 Student 会触发自己写的转换器
	public String testConverter(@RequestParam("studentInfo") Student student) {
	//前端接收字符串  3-zs-23
		System.out.println("*****字符串 转 学生信息 ："
		 + student.getId()+"-"+student.getName()+"-"
		 + +student.getAge());
		return "success";
	}
	
--------------------------------------
#####数据格式化---这个包含了  类型转换 的第一个bean
	
springmvc：
	
	&lt;!-- 	FormattingConversionServiceFactoryBean这个包含了ConversionServiceFactoryBean
	所有要用数据格式化和类型转换，只需要配置这一个依赖即可
	
	这个是开启了 数据格式化的注解
	 --&gt;
	&lt;bean id="conversionService" class="org.springframework.format.support.FormattingConversionServiceFactoryBean"&gt;
		&lt;property name="converters"&gt;
			&lt;set&gt;
				&lt;ref bean="myConverter"/&gt;
			&lt;/set&gt;
		&lt;/property&gt;
	&lt;/bean&gt; 
	
---
用注解放在model层对应实体属性上
		@NumberFormat(pattern="###.#")//这个是控制前端输入的格式 必须为###.#  然后就可转换成int型	  ###，# 逗号也行		@DateTimeFormat(pattern="yyyy-MM-dd")//这个是控制前端输入的格式 必须为yyyy-MM-dd  然后就可以转换成Date类型
.....

	  @RequestMapping(value = "testFormatDateAndNum")
	public String testFormatDateAndNum(Student student,BindingResult result) {
	//如果格式化报错 ， 错误信息会放入第二个参数中（参数顺序绝对不能改变），相当于try-catch，前端就不会报错 了
		System.out.println("*****格式化后的 学生信息 ：" + 
		student.getId()+"-"+student.getName()+"-"+
		student.getAge()+"-"+student.getBirthday());
		
		if(result.getErrorCount()&gt;0)
		{
			for(FieldError error: result.getFieldErrors()) {
				System.out.println(error.getDefaultMessage());
			}
			
		}
		return "success";
	}
可以将错误信息result 传到前端jsp 通过jstl（导包）进行显示,	可以放入map

------------------------------------------------
#### 数据校验： 用的JSR 303 标准注解  附 博客地址
 https://www.cnblogs.com/rocky-AGE-24/p/5245022.html
 还有hibernate validator 相当于JSR303的扩展  附博客地址
 https://blog.csdn.net/danielzhou888/article/details/74740817
	使用hibernate validator的步骤
#####1. jar 去mvn下载 
 //报错noclassdefound 还有NoSuchMethod..NoSuchClass 一般就是少jar包 相依赖的jar版本要一致 要兼容
		Hibernate-validator.jar（版本5.x 会和 validation-api.-1.1.0jar冲突）
		classmate.jar
		jboss-logging.jar
		validation-api.jar
		Hibernate-validator-annotation-processor.jar
		报错 NoClassDeFound  一般是缺失jar 或者 是jar包的版本不兼容了  需要更替其他版本
#####2.配置 spirngmvc.xml
			&lt;mvc:annotation-driven&gt;&lt;/mvc:annotation-driven&gt;
此时mvc:annotation-driven的作用：要实现hibernate validator、JSR 303 或其他各种校验，必须实现springmvc提供的一个接口ValidatorFactory
			springmvc以及把这个接口实现了  LocalValidatorFactoryBean（ValidatorFactory是标准实现类），该注解会在springmvc容器中自动加载这个实现类，实现数据校验
#####3.使用注解
	eg：1.@Past  在model实体的Date类型属性前
			2.在Controller类中 给校验 的 对象加 @Valid
			会显示错误
	eg：1.@Email 在model实体的Date类型属性前

------------------------------------------------

#### Ajax请求Springmvc 并返回json格式数据
 需要jar包 jackson-annotations.jar
					jackson-core.jar
					jackson-databind.jar
		
		@ResponseBody//加了之就可以在ajax中调用json对象了  谁调反馈给谁 集合的话就会自动用js可操作的json数组 不需要手动转换
	@RequestMapping(value = "testJson")
	public List&lt;Student&gt; testJson(){ 
		//调service层 进行操作
		//假设 查询出一个学生list 下方是模拟
		List&lt;Student&gt; students = new ArrayList&lt;&gt;();
		Student stu1= new Student("zs");
		Student stu2= new Student("ls");
		Student stu3= new Student("ww");
		students.add(stu1);
		students.add(stu2);
		students.add(stu3);
		return students;
	}
 js
	
	 &lt;script type="text/javascript" src="js/jquery-1.4.4.js"&gt;&lt;/script&gt;
	&lt;script type="text/javascript"&gt;
	$(document).ready(function(){
		$("#testJson").click(function(){
			//通过ajax请求springmvc
			$.post(
				"Handler/testJson",//服务器地址
				 {"name":"zs"},//传值{"name":"zs","age":23}
				
				function(result){//服务端处理完毕后的回调函数 是一个List&lt;Student&gt; students -&gt;  result
					 //之前eval(result)  将json对象变成js能操作的对象
					//现在在handler层对应方法加@ResponseBody 就可直接操作了 返回的js数组
					 for(var i=0;i&lt;result.length;i++){
						console.log(result[i].name)
					} 
				}
			);	
		});
	});
	&lt;/script&gt; 
成功接收数据

-----------------------------------------------------
####springmvc实现文件上传，和servlet本质一样 只是简化了
 jar 包
  commons-fileuload.jar 
  commons-io.jar
  
前提条件：必须实现MultiparResolver接口  实现类springmvc已经提供CommonsMultipartResolver
具体步骤：直接使用pringmvc已经提供CommonsMultipartResolver的类实现上传
#####a.导包
#####b.配置  CommonsMultipartResolver加入ioc容器
	&lt;!-- 文件上传所需的CommonsMultipartResolver,id值固定写法 Spring初始化 会寻找该id 并加入ioc--&gt;
	&lt;bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver"&gt;
		&lt;property name="defaultEncoding" value="UTF-8"&gt;&lt;/property&gt;
		&lt;!-- 102400  单位是字节kb  =100M  单个文件最大体积   -1 表示无限制 --&gt;
		&lt;property name="maxUploadSize" value="102400"&gt;&lt;/property&gt;
	&lt;/bean&gt;
#####3.处理方法
两种

	//1  处理文件上传 ---上传到本地路径
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
	
----------
	//2  这个是上传到tomcat服务器上
	@RequestMapping(value="testUpload1",method=RequestMethod.POST)
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
   
   -----------------------------------------
####配置拦截器（原理同过滤器）（拦截器链）

#####1.springmvc 必须实现一个HandlerInterceptor接口 有三个方法
  preHandle 请求时拦截
	   postHandle  响应时拦截
	   afterCompletion  渲染完毕后拦截
#####2.配置到springmvc.xml中
	 
		&lt;!-- 配置拦截器 写在上边  --&gt;
	&lt;mvc:interceptors&gt;
		&lt;mvc:interceptor&gt;
			&lt;!-- 指定拦截的目录 基于ant风格 --&gt;
			&lt;mvc:mapping path="/**"/&gt;
			&lt;!-- 指定不拦截的目录  --&gt;
			&lt;!--   取交集 --&gt;
			&lt;mvc:exclude-mapping path="/Handler/testUpload1"/&gt;
			&lt;bean class="org.wht.interceptor.MyInterceptor"&gt;&lt;/bean&gt;
		&lt;/mvc:interceptor&gt;
	&lt;/mvc:interceptors&gt;
#####3. 多个拦截器 
 顺序 preHandle1 preHandle2  进去处理方法 postHandle2 postHandle1  				afterCompletion2 afterCompletion1
		
------------------------------------------------
####异常处理：
 SpringMVC：HandlerExceptionResolver接口
		该接口的每一个实现类都是一种异常处理的方式：
		 ExceptionHandlerExceptionResolver实现类提供了注解@ExceptionHandler
#####A.实现捕获异常：

	@RequestMapping("testExceptionHandler2")
	public String testExceptionHandler2() {
		int[] nums = new int[2];
		System.out.println( nums[2] );
		//存在 数组越界异常ArrayIndexOutOfBoundsException
		return "success";
	}	 
	@ExceptionHandler({ArithmeticException.class,ArrayIndexOutOfBoundsException.class})
	//该方法可以捕获 ----本类中---- 其他方法抛出的ArithmeticException异常
	public ModelAndView handlerArithmeticException(Exception e) {
	//异常会自动放入e中  只能写异常参数否则失败
		ModelAndView mv = new ModelAndView("error");
		//error 是view   与前端交互
		mv.addObject("e",e);//model
		System.out.println(e+"================");
		return mv;
	}

处理路径： 最短优先
		(两个捕获的方法 取最接近的进行捕获 ，尽量不取父类异常----执行效率高)

如果发生异常的方法不在当前异常处理类中怎么办呢？
	只需要将捕获异常的类加注解 (可以单独写一个异常处理类)

	@ControllerAdvice() 
	 //annotations(), basePackageClasses(), basePackages() 方法定制用于选择控制器子集
      // 专门处理异常 可以处理任何类中的异常  Spring能扫描到的地方。就可以实现全局异常的回调

#####B .  用到ResponseStatusExceptionResolver   异常状态的提示 提供该注解@ResponseStatus
   自定义异常显示页面	改变前端错误页面的 404 nofound 之类的信息
######1.   创建一个异常类（继承Exception）
 加类注解
	 
	 @ResponseStatus(value=HttpStatus.FORBIDDEN,reason="数组越界啦！！！")	 
并在具体方法 new 该异常 

	throw new MyArrayIndexOutofBoundsException(); // 创建一个异常

######2.   也可以加在方法前

	  @RequestMapping(value = "testMyException2") // throws 抛出去
	public String testMyException2(@RequestParam("i") Integer i) {
		if (i == 3) {
			return "forward:testResponseStatus"; 
			//跳转到当前类下一个方法进行异常显示 
			//return "redirect:testResponseStatus";
			 //跳转到下一个方法进行异常显示 
		}
		return "success";
	}
	@ResponseStatus(value=HttpStatus.CONFLICT,reason="测试！！！")
	@RequestMapping(value = "testResponseStatus") // throws 抛出去
	public String testResponseStatus(){
		return "success";
	}
######3. 异常处理的实现类 DefaultHandlerExceptionResolver--spring默认实现的一些异常类
默认处理   例如：HTTP Status 405 – Method Not Allowed ....默认添加的一些异常处理
		还有....
	
		  *@see #handleNoSuchRequestHandlingMethod
		 * @see #handleHttpRequestMethodNotSupported
		 * @see #handleHttpMediaTypeNotSupported------------405
		 * @see #handleMissingServletRequestParameter
		 * @see #handleServletRequestBindingException
		 * @see #handleTypeMismatch
		 * @see #handleHttpMessageNotReadable
		 * @see #handleHttpMessageNotWritable
		 * @see #handleMethodArgumentNotValidException
		 * @see #handleMissingServletRequestParameter
		 * @see #handleMissingServletRequestPartException
		 * @see #handleBindException
######4. 通过配置，来实现异常处理 
 SimpleMappingExceptionResolver
	
	&lt;!--SimpleMappingExceptionResolver  (这个是全局的 ， 优先级没有类异常处理 高)以配置的方式处理异常  --&gt;
	&lt;bean class="org.springframework.web.servlet.handler.SimpleMappingExceptionResolver"&gt;
		&lt;property name="exceptionMappings" &gt;
			&lt;!-- 相当于catch(ArithmeticException e) --&gt;
			&lt;props&gt;
				&lt;prop key="java.lang.ArithmeticException"&gt;
					&lt;!-- 跳转到-error --&gt;
					error2
				&lt;/prop&gt;
				&lt;prop key="org.wht.handler.SceondSpringMVCHandler"&gt;
					&lt;!-- 跳转到-error --&gt;
					error2
				&lt;/prop&gt;
			&lt;/props&gt;
		&lt;/property&gt;
		&lt;!-- 会将异常放入value中 --&gt;
		&lt;property name="exceptionAttribute" value="e"&gt;
		
		&lt;/property&gt;
	&lt;/bean&gt;
