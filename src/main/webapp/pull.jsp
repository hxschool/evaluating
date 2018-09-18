

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="https://cdn.bootcss.com/jquery/1.8.1/jquery.min.js"></script>
<script src="layui/layui.js" charset="utf-8"></script>
<link rel="stylesheet" href="layui/css/layui.css">
<script type="text/javascript">
    $(function(){
        $("#btn").click(function(){
        	
        	var flag = false;
        	var othis = $(this);
       		var DISABLED = 'layui-btn-disabled';
       		othis.addClass(DISABLED);
            var Timer;
            $.ajax({
                url:"ProServlet",
                type:"post",
                success:function(){
                	othis.removeClass(DISABLED);
                	window.clearInterval(Timer);
                	flag = true;
                }                                
            })
            var Timer=setInterval("doPro()",1000);
        });    
    });
    function doPro(){
    	
        $.ajax({
            url:"Pro",
            type:"post",
            dataType:"json",
            success:function(rs){
                layui.use('element', function(){
                    var $ = layui.jquery
                    ,element = layui.element;
                    element.progress('demo',((rs.success/rs.total)*100)+'%');    
                })
            }                
        })
        
    }
</script>

   
</head>
<body>
    <%

	//HttpSession session1=request.getSession(true);
	if (session.getAttribute("nowusername") == null) {
		out.println("<p>请重新登陆！3秒后自动跳转...</p>");
	%>
		<script>
		window.location.href="Login.jsp";
		</script>
	<% 
	} else {
    %>
    <div class="layui-progress layui-progress-big" lay-showpercent="true" lay-filter="demo">
    <div class="layui-progress-bar layui-bg-red" lay-percent="0%"></div>
    </div>
    <div class="site-demo-button" style="margin-top: 20px; margin-bottom: 0;">

    <button id="btn" class="layui-btn site-demo-active" >同步数据</button>
    </div>
    <%} %>
</body>
</html>