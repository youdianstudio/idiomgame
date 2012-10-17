package org.youdian.caichengyu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

public class NetOperation {
	  
	  public String postData(Map<String,String> map,String url) {
			 
			 
		  
          
		DefaultHttpClient httpClient = new DefaultHttpClient();
		
		HttpPost post = new HttpPost(url);
		
		List< BasicNameValuePair> postData = new ArrayList< BasicNameValuePair> ();
		for (Map.Entry< String, String> entry : map.entrySet()) {
		postData.add(new BasicNameValuePair(entry.getKey(),
		entry.getValue()));}
		
		UrlEncodedFormEntity entity=null;
		try {
			entity = new UrlEncodedFormEntity(
					postData, HTTP.UTF_8);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return null;
		}
		post.setEntity(entity);
		
		HttpResponse response=null;
		try {
			response = httpClient.execute(post);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return null;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
		HttpEntity httpEntity = response.getEntity();
		
		InputStream is=null;
		try {
			is = httpEntity.getContent();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return null;
		}

		StringBuffer sb = new StringBuffer();
		
BufferedReader br = new BufferedReader(new InputStreamReader(is));
	    String line ="";
		try {
			while((line=br.readLine())!=null){
			sb.append(line);}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return null;
		}
		
		return sb.toString();
				
		
	     }
	
}
