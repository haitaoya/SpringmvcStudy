<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="js/jquery-1.4.4.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		$("#testJson").click(function(){
			//通过ajax请求springmvc
			$.post(
				"Handler/testJson",//服务器地址
				 {"name":"zs"},//传值{"name":"zs","age":23}
				
				function(result){//服务端处理完毕后的回调函数 是一个List<Student> students ->  result
					 //之前eval(result)  将json对象变成js能操作的对象
					//现在在handler层对应方法加@ResponseBody 就可直接操作了 返回的js数组
					 for(var i=0;i<result.length;i++){
						console.log(result[i].name)
					} 
				}
			);
			
		});
		
	});
</script>
</head>
<body>
	<input type="button" value="testJson" id="testJson"><br>
	<a href="Handler/welcome/abc">first springmvc</a>
	<form action="Handler/welcome/abc" method="post">
		name<input type="text" name="name"><br> age<input
			type="text" name="age"><br> height<input type="text"
			name="height"> <input type="submit">
	</form>
	<a href="Handler/welcome2">first springmvc headler</a>
	<br>
	<a href="Handler/welcome3/sssss/test">ant 风格 请求路径 *任意个字符</a>
	<br>
	<a href="Handler/welcome4/s/44/44/ss/ss/test">ant 风格 请求路径 **任意个目录</a>
	<br>
	<a href="Handler/welcome5/axc/test">ant 风格 请求?</a>
	<br>
	<a href="Handler/welcome6/zs">ant 风格 动态传参</a>
	<br>
	<br>========
	<br>
	<!-- <form action="Handler/testPost/1234" method="post">
		<input type="submit" value="增" >
	</form>
	<form action="Handler/testDelete/1234" method="post">
		<input type="hidden" name="_method" value="DELETE">delete是枚举值 大写 
		<input type="submit" value="删" >
	</form>
	<form action="Handler/testPut/1234" method="post">
		<input type="hidden" name="_method" value="PUT">
		<input type="submit" value="改" >
	</form>
	<form action="Handler/testGet/1234" method="get">
		<input type="submit" value="查" >
	</form> -->
	<form action="Handler/testRest/1234" method="post">
		<input type="submit" value="增">
	</form>
	<form action="Handler/testRest/1234" method="post">
		<input type="hidden" name="_method" value="DELETE" />
		<!--delete是枚举值 大写  -->
		<input type="submit" value="删">
	</form>
	<form action="Handler/testRest/1234" method="post">
		<input type="hidden" name="_method" value="PUT" /> <input
			type="submit" value="改">
	</form>
	<form action="Handler/testRest/1234" method="get">
		<input type="submit" value="查">
	</form>
	<br>-----------
	<br>
	<form action="Handler/testParam" method="get">
		name<input name="uname">
		<!-- age<input name="uage"> -->
		<!-- 能多不能少 -->
		<input type="submit" value="提交">
	</form>
	<a href="Handler/testRequetHeader">获取RequetHeader</a>
	<br>
	<a href="Handler/testRequetHeader1">获取RequetHeaderCookieValue</a>
	<br>
	<a href="Handler/testCookieValue">获取CookieValue</a>
	<br>
	<form action="Handler/testObjectProperties" method="get">
		id<input name="id"><br> name<input name="name"><br>
		homeAddress<input name="address.homeAddress"><br>
		schoolAddress<input name="address.schoolAddress"><br>
		 <input type="submit" value="提交">
	</form>
	<a href="Handler/testModelAndView">测试ModelAndView</a>
	<br>

	<a href="Handler/testModelMap">测试ModelMap</a>
	<br>
	<a href="Handler/testMap">测试Mapw</a>
	<br>
	<a href="Handler/testModel">测试Model</a>
	<br>
	<form action="Handler/testModelAttribute" method="get">
		编号：<input name="id" type="hidden" value="8"><br />
		名字：<input name="name" type="text"><br> <input type="submit" value="修改">
	</form>
	<a href="Handler/testI18n">测试i18n 国际化</a>
	<br>
	<a href="Handler/testMVCViewController">（不需要写controller层 具体方法）测试 跳过
		controller 层 直接 到view</a>
	<br>
	<form action="Handler/testConverter" method="post">
		学生信息：<input name="studentInfo" type="text"><br> <input type="submit"
			value="转换">
	</form>
	<form action="Handler/testFormatDateAndNum" method="post">
		编号：<input name="id" type="text"><br />
		名字：<input name="name" type="text"><br> 
		出生日期：<input name="birthday" type="text"><br>
		邮箱：<input name="email" type="text"><br>  
		<input type="submit" value="格式化">
	</form>
	<!-- 文件上传表单 -->
	<form action="Handler/testUpload1" method="post" enctype="multipart/form-data">
		<input name="file" type="file">
		描述：<input name="desc" type="text">
		
		<input type="submit" value="上传">
	</form>
</body>
</html>