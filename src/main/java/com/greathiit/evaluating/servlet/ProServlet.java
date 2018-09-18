package com.greathiit.evaluating.servlet;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.greathiit.common.util.SecureUtil;
import com.greathiit.evaluating.function.ConnUtils;
import com.greathiit.evaluating.function.Sql_op;
import com.greathiit.evaluating.servlet.vo.BaseResponse;
import com.greathiit.evaluating.servlet.vo.Major;
import com.greathiit.evaluating.servlet.vo.Student;

/**
 * Servlet implementation class ProServlet
 */

public class ProServlet extends HttpServlet {
	
	private static RestTemplate restTemplate = new RestTemplate();
    private static final long serialVersionUID = 1L;
    public static Map<String,Major> majors = new HashMap<String,Major>();
    static {
    	ResponseEntity<String> responseEntity = restTemplate.postForEntity(ConnUtils.SYSTEM_URL.concat("api/getMajor"), "", String.class);
    	Type listType = new TypeToken<BaseResponse<List<Major>>>() {}.getType();
    	Gson g = new Gson();
    	BaseResponse<List<Major>> baseResponse = g.fromJson(responseEntity.getBody(), listType);
    	for(Major major:baseResponse.getResult()) {
    		majors.put(major.getId(), major);
    	}
    	
    }
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	System.out.println("准备开始处理数据...");
        response.setContentType("text/text;charset=utf-8");
        response.setCharacterEncoding("UTF-8");
    	Progress pr = new Progress();
    	Map<String, String> resultMap = new HashMap<String, String>();
    	resultMap.put("appid", ConnUtils.APPID);
    	String tradeJson = new Gson().toJson(resultMap);
		Map<String, String> requestMap;
		try {
			System.out.println("数据加密准备中...");
			requestMap = SecureUtil.encryptTradeInfo(ConnUtils.APPID, tradeJson, ConnUtils.PRIVATEKEY,
					ConnUtils.SYSTEM_PUBLICKEY);
			HttpHeaders headers = new HttpHeaders();
			MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
			headers.setContentType(type);
			headers.add("Accept", MediaType.APPLICATION_JSON.toString());
	        HttpEntity<String> formEntity = new HttpEntity<String>(new Gson().toJson(requestMap), headers);
	        System.out.println("准备数据请求...");
			ResponseEntity<String> responseEntity = restTemplate.postForEntity(ConnUtils.SYSTEM_URL.concat("api/getStudents"), formEntity, String.class);
			Gson gson = new Gson();
			Type listType = new TypeToken<BaseResponse<List<Student>>>() {}.getType();
			BaseResponse<List<Student>> baseResponse = gson.fromJson(responseEntity.getBody(), listType);
			Thread.sleep(2000);
			if (baseResponse.getStatus() != null && baseResponse.getStatus().equals("00000000")) {
				List<Student> list = baseResponse.getResult();
				pr.setTotal(list.size());
				int k = 1;
				
				SimpleDateFormat SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				for(Student student : list) {
					Sql_op op = new Sql_op();
					ResultSet rs = op.find("student", "count(*) as cnt", "user_number='"+student.getStudentNumber()+"'");
					if(rs.next()) {
						int count = rs.getInt("cnt");
						if(count>0) {
							op.close();
							continue;
						}else {
							Map<String,String> data = new HashMap<String,String>();
							data.put("user_number", student.getStudentNumber());
							data.put("user_name", student.getName());
							data.put("password", "zhaojunfei@773152");
							data.put("access", "0");
							if(!StringUtils.isEmpty(majors.get(student.getClassno().substring(4, 6)))) {
								data.put("student_major", majors.get(student.getClassno().substring(4, 6)).getName());
							}else {
								data.put("student_major", "未知专业");
							}
							
							data.put("student_class", student.getClassno());
							data.put("student_age_class", student.getClassno().substring(0, 4));
							data.put("registration_time", SimpleDateFormat.format(new Date()));
							data.put("now_role", "1");
							data.put("Sfill_access", "0");
							op.insert("student", data);
							
							Map<String,String> data1 = new HashMap<String,String>();
							
							data1.put("user_number", student.getStudentNumber());
							data1.put("user_name", student.getName());
							data1.put("examine", "0");
							data1.put("student_sum_mark", "0");
							data1.put("time", SimpleDateFormat.format(new Date()));
							op.insert("student_basic_news", data1);
						}
					}
					pr.setSuccess(k);
					request.getSession().setAttribute("pr", pr);
					k++;
					op.close();
				}
				
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
    }
}