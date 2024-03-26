package org.servlet.panelist;

import java.io.BufferedReader;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.recruitment.users.Panelist;
import org.recruitment.users.PanelistManager;
import org.util.CommonLogger;
import org.util.Gender;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 * Servlet Filter implementation class CreateOpeningFilter
 */
@WebFilter("/CreateOpeningFilter")
public class CreateOpeningFilter extends HttpFilter implements Filter {
       
    /**
     * @see HttpFilter#HttpFilter()
     */
    public CreateOpeningFilter() {
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
		// TODO Auto-generated method stub
		// place your code here
		
		
//		Logger log = CommonLogger.getCommon().getLogger(CreateOpeningFilter.class);
		
		// pass the request along the filter chain
//		chain.doFilter(request, response);
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		chain.doFilter(httpRequest, httpResponse);
		if(true) {
			return;
		}
		
		StringBuilder jsonLoad = new StringBuilder();
		JSONObject jsonData;
		try {
			BufferedReader br = httpRequest.getReader();
			for (String line = br.readLine();line != null;line = br.readLine()) {
				jsonLoad.append(line);
			}
			br.mark(4096);
			br.reset();
//			log.info("jsonCreated");
			jsonData = new JSONObject(jsonLoad.toString());
			
//			String title = jsonData.getString()
			
			JSONObject panelistJson = jsonData.getJSONObject("panelist");

			
			 Panelist panelist = new Panelist(
		                panelistJson.getString("name"),
		                panelistJson.getString("email"),
		                Gender.valueOf(panelistJson.getString("gender").toUpperCase()),
		                panelistJson.getString("position"),
		                panelistJson.getString("organistion"), // Fixed typo: organistion -> organization
		                panelistJson.getString("department")
		            );
			 
			 
			 
			 
			 
			 
			 if(PanelistManager.getPanelistManger().isPanelistExist(panelist)) {
				 
				 httpRequest.setAttribute("JSON",jsonData.toString() );
				 chain.doFilter(httpRequest, httpResponse);
			 }
			 
			
		}
		catch(Exception e){
			try {
				jsonData = new JSONObject();
				jsonData.put("status", 500);
				jsonData.put("error", e.getMessage());
//				log.error("Error in Filter"+e.getMessage());
				response.getWriter().write(jsonData.toString());
			} catch (Exception e2) {
				
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
