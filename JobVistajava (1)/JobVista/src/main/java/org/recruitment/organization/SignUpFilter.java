package org.recruitment.organization;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.util.CommonLogger;
import org.util.ConnectionClass;

/**
 * Servlet Filter implementation class SignUpFilter
 */
@WebFilter("/SignUp")
public class SignUpFilter extends HttpFilter implements Filter {
       
    /**
     * @see HttpFilter#HttpFilter()
     */
	Logger logger = CommonLogger.getCommon().getLogger(SignUpFilter.class);
    public SignUpFilter() {
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
	
	    
	    if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            httpResponse.setHeader("Access-Control-Allow-Origin", "*");
            httpResponse.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
            httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type");
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }
	    
	    BufferedReader reader = httpRequest.getReader();
		StringBuilder builder = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			builder.append(line);
		}
		String jsonData = builder.toString();
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(jsonData);
		} 
		catch (JSONException e) {
			logger.error("json exception in signup filter while parsingo json object");
		}
		
		String orgName = null;
		String adminEmail = null;
		String adminPassword = null;
		JSONObject organization = null;
		JSONObject admin = null;
		
		try {
			
			organization = jsonObject.getJSONObject("organization");
			admin = jsonObject.getJSONObject("admin");
			
			orgName = organization.getString("orgName");
			adminEmail = admin.getString("adminEmail");
			adminPassword = admin.getString("adminPassword");
			
		} 
		
		catch (JSONException e) {
			logger.error("New Admin "+adminEmail+"\njson exception in signUp filter while parsing json object");
			e.printStackTrace();
		}
		
		String passwordRegex = "^(?=.*\\d)(?=.*[a-zA-Z].*[a-zA-Z].*[a-zA-Z]).{8,}$";
		String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
	    // Validate if user or organization already exists
	    try {
			if (isUserExists(adminEmail) || isOrganizationExists(orgName) || !adminPassword.matches(passwordRegex) || !adminEmail.matches(emailRegex)) {
			    httpResponse.sendError(HttpServletResponse.SC_CONFLICT, "User or organization already exists");
			    return;
			}
		} 
	    catch (Exception e) {
			logger.error("New Admin "+adminEmail+"sql exception in signup filter while retrieving data from DB");
			e.printStackTrace();
		}

	    // Proceed with the filter chain
	    System.out.println("signup checking");
	    request.setAttribute("object", jsonObject.toString());
	    chain.doFilter(request, response);
	}

	public boolean isUserExists(String email) throws SQLException {
	    try (Connection connection = ConnectionClass.CreateCon().getConnection();
	        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Admin WHERE Email = ?")) {
	        preparedStatement.setString(2, email);
	        try (ResultSet resultSet = preparedStatement.executeQuery()) {
	        	if(resultSet.next()) {
	            	logger.info("Admin already exists");
	            	return true;
	            }
	            logger.info("Admin doesn't exists");
	            return false;
	        }
	    }
	}

	public boolean isOrganizationExists(String orgName) throws SQLException {
	    try (Connection connection = ConnectionClass.CreateCon().getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Organization WHERE Name = ?")) {
	        preparedStatement.setString(1, orgName);
	        try (ResultSet resultSet = preparedStatement.executeQuery()) {
	            if(resultSet.next()) {
	            	logger.info("Organisation already exists");
	            	return true;
	            }
	            logger.info("Organisation doesn't exists");
	            return false;
	        }
	    }
	}



	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
