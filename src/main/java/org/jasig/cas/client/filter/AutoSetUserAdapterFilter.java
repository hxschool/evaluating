package org.jasig.cas.client.filter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.jasig.cas.client.authentication.AttributePrincipal;

import com.greathiit.evaluating.dao.Items;
import com.greathiit.evaluating.function.Sql_op;



public class AutoSetUserAdapterFilter implements Filter {

	private static Logger logger = Logger.getLogger(AutoSetUserAdapterFilter.class);
	
	public FilterConfig config;
	
	private final static String LOGIN_SESSION_ID ="name";
	private final static String LOGIN_ACCESS = "access";
	@Override
	public void destroy() {

	}

	public final void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse,
			final FilterChain filterChain) throws IOException, ServletException {
		final HttpServletRequest request = (HttpServletRequest) servletRequest;
		final HttpServletResponse response = (HttpServletResponse) servletResponse;
		final HttpSession session = request.getSession(false);
		String adminRoleId = config.getInitParameter("adminRoleId");        // 登录登陆页面
		Object sessionid = session.getAttribute(LOGIN_SESSION_ID);
		Object accessid = session.getAttribute(LOGIN_ACCESS);
		if(sessionid!=null&&accessid!=null) {
			return;
		}

		//判断当前用户是否合法,合法直接放行
		
		AttributePrincipal principal = (AttributePrincipal) request.getUserPrincipal();
		java.util.Map<String, Object> attributes = principal.getAttributes();
		String roleId = (String)attributes.get("roleId");
		
		roleId = roleId.replaceAll("[\\[\\]]", "");  
		
		String[] roleIds = roleId.split(",");
		
		String randPwd = randomPassword();
		//如果角色里面包含99,那么认为是学生信息,不包含为教师信息,各业务系统插入各种业务系统的数据表就可以了可以保存用户信息，也可以直接放到session里面

		String loginname = attributes.get("login_name").toString();
		String username = attributes.get("no").toString();

		Items dao = new Items();
		String tables[] = { "manager_form", "student" };
		int resultFlag = 0;
		
		try {
			for (int a = 0; a < tables.length; a++) {
				System.out.println("查询用户...当前操作表:"+tables[a]);
				resultFlag += dao.getStudents_check("select count(*) from " + tables[a] + " where user_number='" + username + "'");
				if (tables[a].equals("manager_form") && !Arrays.asList(roleIds).contains("99")) {
					if (resultFlag == 0) {
						Sql_op op = new Sql_op();
						Map<String,String> data = new HashMap();
						data.put("user_number", username);
						data.put("user_name", attributes.get("name").toString());
						data.put("password", randPwd);
						data.put("role", "1");
						data.put("role_name", "系统管理员");
						data.put("access", "1003");
						data.put("student_class", "*");
						data.put("student_age_class", "*");
						data.put("student_major", "*");
						op.insert(tables[a], data);
						op.close();
						resultFlag = 1;
					}
				}
				System.out.println("查询用户...当前登录用户:"+username);
				if (resultFlag == 1) {
					System.out.println("开始复制~"+username);
					session.setAttribute("nowusername", username);
					session.setAttribute("table", tables[a]);
					dao.setTable(tables[a]);
					dao.setFiled_name("user_number");
					session.setAttribute("name", dao.getFieldValue("user_name", username));
					session.setAttribute("access", dao.getFieldValue("access", username));
					break;
				}
			}
			System.out.println("应该跳转相关操作页面");
		} catch (SQLException e) {
			System.out.println("系统异常,操作数据库失败");
			e.printStackTrace();
		}
		
		
	}
	
	private String randomPassword(){
		return String.valueOf((int)((Math.random()*9+1)*100000));
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		this.config = config;

	}
	public String getUUID() {
		String s = UUID.randomUUID().toString();
		return s.replace("-", "");
	}
}
