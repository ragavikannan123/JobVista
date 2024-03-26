package org.faceReg;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.json.JSONObject;
import org.util.ConnectionClass;

import java.util.List;

public class GetAuthToken {
	
	public String getTokenFromApi(){
		
		try {
            
            HttpClient httpClient = HttpClients.createDefault();

            
            HttpPost httpPost = new HttpPost("https://accounts.zoho.com/oauth/v2/token");

            
            List<NameValuePair> urlParameters = new ArrayList<>();
            urlParameters.add(new BasicNameValuePair("client_id", "1000.7772NCP1ZNJK3OWYVKBJMVQL6K031L"));
            urlParameters.add(new BasicNameValuePair("grant_type", "refresh_token"));
            urlParameters.add(new BasicNameValuePair("refresh_token", "1000.2005a945335ead22ba0394112b671198.2437ccf22be01f9660b4aa1d2751eaf5"));
            urlParameters.add(new BasicNameValuePair("client_secret", "2f760d012328032b0a6f47daa12c6bed5cbb364228"));

            // Set the URL parameters to the post request
            httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));

            // Execute the request
            HttpResponse response = httpClient.execute(httpPost);

            // Extract the response entity
            String responseString = EntityUtils.toString(response.getEntity());
            
            
            JSONObject resp  = new JSONObject(responseString); 
            
            String token = resp.getString("access_token");
            
            
            System.out.println("Response Status Line: " + response.getStatusLine());
            System.out.println("Response Body: " + responseString);
            
            
            return token;
            
            

        } catch (Exception e) {
            e.printStackTrace();
        }
		
		
		
		return null;
		
	}
	
	
	public String getToken() {
		try {
			Connection conn = ConnectionClass.CreateCon().getConnection();
			PreparedStatement getTokenPs = conn.prepareStatement("SELECT * FROM FaceAuthToken WHERE time >= NOW() - INTERVAL 30 MINUTE");
			ResultSet rs = getTokenPs.executeQuery();
			if(rs.next()) {
				System.out.println(rs.getString("Token"));
				return rs.getString("Token");
			}
			else {
				String ApiToken = getTokenFromApi();
				PreparedStatement setToken = conn.prepareStatement("UPDATE FaceAuthToken SET Token = ?, time = CURRENT_TIMESTAMP");
				setToken.setString(1, ApiToken);
				setToken.execute();
				System.out.println(ApiToken);
				return ApiToken;
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	
	
	public static void main(String[] args) {
        try {
            // Create HttpClient instance
            HttpClient httpClient = HttpClients.createDefault();

            // Create HttpPost instance with the target URL
            HttpPost httpPost = new HttpPost("https://accounts.zoho.com/oauth/v2/token");

            // Create list to hold URL parameters
            List<NameValuePair> urlParameters = new ArrayList<>();
            urlParameters.add(new BasicNameValuePair("client_id", "1000.7772NCP1ZNJK3OWYVKBJMVQL6K031L"));
            urlParameters.add(new BasicNameValuePair("grant_type", "refresh_token"));
            urlParameters.add(new BasicNameValuePair("refresh_token", "1000.2005a945335ead22ba0394112b671198.2437ccf22be01f9660b4aa1d2751eaf5"));
            urlParameters.add(new BasicNameValuePair("client_secret", "2f760d012328032b0a6f47daa12c6bed5cbb364228"));

            // Set the URL parameters to the post request
            httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));

            // Execute the request
            HttpResponse response = httpClient.execute(httpPost);

            // Extract the response entity
            String responseString = EntityUtils.toString(response.getEntity());

            // Print the response status line and body
            System.out.println("Response Status Line: " + response.getStatusLine());
            System.out.println("Response Body: " + responseString);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
