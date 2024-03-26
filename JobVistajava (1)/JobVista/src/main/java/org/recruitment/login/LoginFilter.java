package org.recruitment.login;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.recruitment.organization.SignUpFilter;
import org.util.CommonLogger;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@WebFilter("/Login")
public class LoginFilter implements Filter {

	Logger logger = CommonLogger.getCommon().getLogger(SignUpFilter.class);
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        
    	HttpServletRequest httpRequest = (HttpServletRequest) request;
	    HttpServletResponse httpResponse = (HttpServletResponse) response;
	  
	    if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            httpResponse.setHeader("Access-Control-Allow-Origin", "*");
            httpResponse.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
            httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type");
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }
	    StringBuilder sb = new StringBuilder();
        BufferedReader reader = httpRequest.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
        	sb.append(line);
        }
        
		String jsonData = sb.toString();
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(jsonData);
		} 
		catch (JSONException e) {
			logger.error("json exception in login filter while parsing json object\n"+e.getMessage());
		}
		
		String email = null;
		String password = null;
		
		try {
			
			email = jsonObject.getString("email");
			password = jsonObject.getString("password");
			
		} 
		
		catch (JSONException e) {
			logger.error("User "+email+"\njson exception in login filter while parsing json object\n"+e.getMessage());
		}
		
		String passwordRegex = "^(?=.*\\d)(?=.*[a-zA-Z].*[a-zA-Z].*[a-zA-Z]).{8,}$";
		String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

	    
			if (!password.matches(passwordRegex) || !email.matches(emailRegex)) {
				
			    JSONObject responseObject = new JSONObject();
			    try {
					responseObject.put("statusCode", 500);
					responseObject.put("message", "Invalid password or email format");
				} 
			    catch (JSONException e) {
			    	e.printStackTrace();
					logger.error("User "+email+"\njson exception in login filter while parsing json object\n"+e.getMessage());
				}
			  
			    response.getWriter().write(responseObject.toString());
			    return;
			}
		
	    request.setAttribute("object", jsonObject);
	    chain.doFilter(request, response);
    }

   

    @Override
    public void destroy() {
        // Clean-up code if needed
    }
}
