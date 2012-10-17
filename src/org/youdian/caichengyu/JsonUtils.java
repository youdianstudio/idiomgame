package org.youdian.caichengyu;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.util.List;


import com.google.gson.stream.*;

public class JsonUtils {
	  List<String> list=new ArrayList<String>();
	  List<Map<String,String>> detail_list=new ArrayList<Map<String,String>>();
	  Map<String,String> map;
	  String idiom;
	  String value;
	  
	  public List<String> getData(){
		  
		  return list;
	  }
	  public Map<String,String> getMap(){
		  return map;
	  }
	  public List<Map<String,String>> getDetailList(){
		  return detail_list;
	  }
      public void parseJson(String jsonData){
    	  //avoid duplicate data
    	  if(!list.isEmpty())list.clear();
    	  try{
    	  JsonReader reader=new JsonReader(new StringReader(jsonData));
    	  reader.beginArray();
    	  while(reader.hasNext()){
    		  reader.beginObject();
    		  
    		  while(reader.hasNext()){
    			  String tagName=reader.nextName();
    			  
    			  if(tagName.equals("chengyu")){
    				  idiom=reader.nextString();
    				  list.add(idiom);
    			  }
    			  
    			  
    		  }
    		  
    		  reader.endObject();
    	  }
    	  reader.endArray();
    	  reader.close();
    	  }catch(Exception e){
    		  e.printStackTrace();
    	  }
      }
      public void parseDetailJson(String jsonData){
    	  //avoid duplicate data
    	  if(!detail_list.isEmpty())detail_list.clear();
    	  try{
    	  JsonReader reader=new JsonReader(new StringReader(jsonData));
    	  reader.beginArray();
    	  while(reader.hasNext()){
    		  reader.beginObject();
    		  map=new HashMap<String,String>();
    		  while(reader.hasNext()){
    			  String tagName=reader.nextName();
    			  
    			  if(tagName.equals("chengyu")){
    				  value=reader.nextString();
    				  map.put("chengyu", value);
    			  }
    			  if(tagName.equals("pinyin")){
    				  value=reader.nextString();
    				  map.put("pinyin", value);
    			  }
    			  if(tagName.equals("meaning")){
    				  value=reader.nextString();
    				  map.put("meaning", value);
    			  }
    			  if(tagName.equals("comefrom")){
    				  value=reader.nextString();
    				  map.put("comefrom", value);
    			  }
    			  if(tagName.equals("example")){
    				  value=reader.nextString();
    				  map.put("example", value);
    			  }
    			  
    			  
    		  }
    		  detail_list.add(map);
    		  reader.endObject();
    	  }
    	  reader.endArray();
    	  reader.close();
    	  }catch(Exception e){
    		  e.printStackTrace();
    	  }
      }
}
