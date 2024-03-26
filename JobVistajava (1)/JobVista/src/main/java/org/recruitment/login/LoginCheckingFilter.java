package org.recruitment.login;

import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.recruitment.organization.AdminManagement;
import org.util.CommonLogger;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;

/**
 * Servlet Filter implementation class LoginCheckingFilter
 */
//@WebFilter("/*")
public class LoginCheckingFilter extends HttpFilter implements Filter {
	
	Logger logger = CommonLogger.getCommon().getLogger(AdminManagement.class);
       
    /**
     * @see HttpFilter#HttpFilter()
     */
    public LoginCheckingFilter() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
       
        
        
        Cookie[] cookies = httpRequest.getCookies();
        boolean isLoggedIn = false;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("session_Id")) {
                	isLoggedIn = true;
                    break;
                }
            }
        }

        if (isLoggedIn) {
            chain.doFilter(request, response);
        } 
        else {
            JSONObject jsonObject = new JSONObject();
            chain.doFilter(httpRequest, httpResponse);
            if(true) {
            	return;
            }
            try {
				jsonObject.put("statusCode", 500);
				jsonObject.put("message", "User have logged out");
			} catch (JSONException e) {
				logger.error("Error occured while parsing the json object \n"+e.getMessage());
			}
            
            
            response.getWriter().write(jsonObject.toString());
        }
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
