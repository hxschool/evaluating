package com.greathiit.evaluating.servlet;


import java.io.IOException;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Pro
 */

public class Pro extends HttpServlet {
    private static final long serialVersionUID = 1L;
      

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        doPost(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/json; charset=utf-8");
        Progress pr = (Progress)request.getSession().getAttribute("pr");
        if(pr==null) {
        	 String json="{\"success\":\"1\",\"total\":\"1\"}";
             response.getWriter().print(json);
        }else {
        	 String json="{\"success\":\""+pr.getSuccess()+"\",\"total\":\""+pr.getTotal()+"\"}";
             response.getWriter().print(json);
        }
       
    }
}