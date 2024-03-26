package org.faceReg;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.util.ConnectionClass;

import com.mysql.cj.protocol.Resultset;
import com.mysql.cj.xdevapi.JsonArray;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

/**
 * Servlet implementation class FaceTest
 */

@WebServlet("/FaceTest")
@MultipartConfig
public class FaceTest extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FaceTest() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
		// TODO Auto-generated method stub
//		doGet(request, response);
		
		
		JSONObject repJson = new JSONObject();
		
		System.out.println("before prt");
		Part filePart = request.getPart("img");
		System.out.println("AfterPart prt");
		System.out.println(request.getParameter("id"));
		int idPart = Integer.parseInt(request.getParameter("id"));
		
		
		
		System.out.println(idPart + "Jith");
		
		
		filePart.getSubmittedFileName();
		

		File file = new File("Temp.png");
		file.createNewFile();
		
		byte[] fileContent = readFromInputStream(filePart.getInputStream());

		
		System.out.println(file.getAbsolutePath());
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		InputStream input = filePart.getInputStream();
        try (FileOutputStream output = new FileOutputStream(file)) {
				byte[] buffer = new byte[1024];
				int bytesRead;
				while ((bytesRead = input.read(buffer)) != -1) {
    output.write(buffer, 0, bytesRead);
				}
			}
		System.out.println("FileCreated");

		MultipartEntityBuilder builder =  MultipartEntityBuilder.create();
		
		
		
		String contentType = Files.probeContentType(Paths.get(file.getName()));
		System.out.println("Before InputStream");
		FileInputStream fis = new FileInputStream(file);
		System.out.println("After FileInputStream :"+contentType);
		ContentType ct = ContentType.create(contentType);
		System.out.println("After CT");
		
		InputStreamBody inputStrmBody = new InputStreamBody(fis , ct,file.getName());
		System.out.println("After InputStream");
		builder.addPart("image", inputStrmBody);
		builder.addTextBody("dominant_face", "true");
		
//		builder.addTextBody(idPart, contentType);
		System.out.println("Api request Created");
		
		try (final CloseableHttpClient httpclient = HttpClientBuilder.create().build();) {
			
			
			String token = new GetAuthToken().getToken();
            final HttpPost post = new HttpPost("https://dl.zoho.com/cv/face/embedding");
            post.setHeader("Authorization", "Bearer "+token);
            post.setEntity(builder.build());
            System.out.println("Before execute");
            HttpResponse httpResponse = httpclient.execute(post);
            System.out.println("After execute");
            JSONObject output = getResponseData(httpResponse);
            
            System.out.println("Api RequestSend");
            System.out.println(output.toString());
            if(!output.get("executionMessage").equals("No face found")) {
            	JSONArray facedetails = output.getJSONArray("data").getJSONObject(0).getJSONArray("faceEmbedding");
            	boolean isSimilar = checkFace(facedetails,idPart);
            	System.out.println(isSimilar);
            	repJson.put("isSimilar", isSimilar);
            	System.out.println(repJson);
            	response.getWriter().write(repJson.toString());
            }else {
            
            
            
//          System.out.println(output.toString());
            repJson.put("error", "nofaceFound");
        	response.getWriter().write(repJson.toString());
            }
        } catch (Exception e) {
        	try {
        	repJson.put("error", e.getMessage());
        	response.getWriter().write(repJson.toString());
        	System.out.println(e.getMessage());
        	}
        	catch(JSONException ei) {}
        }
		finally {
			file.delete();
		}
	}
	
	
	static JSONObject getResponseData(HttpResponse httpResponse) throws IOException, JSONException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        return new JSONObject(result.toString());
    }
	
	
	
	static boolean checkFace(JSONArray detail, int id) {
		
		try {
		Connection conn = ConnectionClass.CreateCon().getConnection();
		PreparedStatement stmt = conn.prepareStatement("SELECT * FROM FaceReg WHERE RegisteredDate >= CURDATE() - INTERVAL 6 MONTH");

		ResultSet rs = stmt.executeQuery();
		
		
		while(rs.next()) {
			double similarity = cosineSimilarity(detail, new JSONArray(rs.getString("FaceBlob")));
			System.out.println(similarity);
			if(similarity > 0.70) {
				return false;
			}
		}
		
		
		
		
		PreparedStatement inserStatement = conn.prepareStatement("INSERT INTO `FaceReg` (`OrganizationId`, `FaceBlob`, `RegisteredDate`) VALUES (?, ?, ?)");
		inserStatement.setInt(1, id);
		inserStatement.setString(2, detail.toString());
		inserStatement.setDate(3, new java.sql.Date(System.currentTimeMillis()));
		
		inserStatement.executeUpdate();

		return true;
		
		
		
		}catch (Exception e) {
			
		}
		
		
		
		
		return true;
	}
	
	
	static double cosineSimilarity(JSONArray face1, JSONArray face2) throws JSONException {
	    double dot=0, norm1=0, norm2=0;
	    for (int i = 0; i < face1.length(); i++){
	        dot += (face1.getDouble(i) * face2.getDouble(i));
	        norm1 += Math.pow(face1.getDouble(i), 2);
	        norm2+= Math.pow(face2.getDouble(i), 2);
	    }
	    return dot / (norm1 * norm2);
	}
	
	private byte[] readFromInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }
        return outputStream.toByteArray();
    }

}
