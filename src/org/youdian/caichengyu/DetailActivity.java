package org.youdian.caichengyu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DetailActivity extends Activity implements OnClickListener{
    
	String idiom_name;
	TextView title,content;
	Button close;
	String url="http://caichengyu.sinaapp.com/detail";
	public  static final int DATA_OK=1;
	public  static final int DATA_ERROR=0;
	public  static final int NET_ERROR=-1;
	String jsonData;
	Message msg;
	NetOperation netOperation=new NetOperation();
	JsonUtils jsonUtils=new JsonUtils();
	List<String> list=new ArrayList<String>();
	Map<String,String> idiom_map=new HashMap<String,String>();
	Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			
			case DATA_OK:
				
				content.setText("【拼音】:"+idiom_map.get("pinyin")+"\n\n"+"【解释】："+idiom_map.get("meaning")+"\n\n"
						+"【出自】："+idiom_map.get("comefrom")+"\n\n"+"【示例】："+idiom_map.get("example")+"\n\n");
				break;
			case DATA_ERROR:
				content.setText("无相关数据");
				content.setTextSize(12);
				content.setTextColor(Color.GRAY);
				break;
			case NET_ERROR:
				content.setText("网络不可用");
				content.setTextSize(12);
				content.setTextColor(Color.GRAY);
				break;
			}
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail);
		Intent intent=this.getIntent();
		idiom_name=intent.getStringExtra("idiom");
		title=(TextView)findViewById(R.id.detail_title);
		title.setText(idiom_name);
		content=(TextView)findViewById(R.id.detail_content);
		close=(Button)findViewById(R.id.detail_close);
		close.setOnClickListener(this);
		MyRunnable runnable=new MyRunnable();
		Thread thread=new Thread(runnable);
		thread.start();
	}
	
	class MyRunnable implements Runnable{

		public void run() {
			Looper.prepare();
			// TODO Auto-generated method stub
			if(NetState.isNetworkConnected(DetailActivity.this)){
				fetchData();
				
			}else{
				msg=new Message();
	        	msg.what = NET_ERROR;
	        	handler.sendMessage(msg);
			}
			
			//handler.handleMessage(msg);
			Looper.loop();
		}
		
	}
	private void fetchData(){
		Map<String,String> map=new HashMap<String,String>();
		map.put("getdetail", idiom_name);
		
		jsonData=netOperation.postData(map,url);
		
		
	
		if(jsonData==null){
			msg=new Message();
			msg.what=NET_ERROR;
			
		}else if(!(jsonData.startsWith("["))){
			msg=new Message();
			msg.what=DATA_ERROR;
			
		}else{
			jsonUtils.parseDetailJson(jsonData);
			idiom_map=jsonUtils.getMap();
			
		    if(idiom_map.size()==0){
		    	msg=new Message();
		    	msg.what=DATA_ERROR;
		    	
		    }else{
		    	msg=new Message();
		    	msg.what=DATA_OK;
		    }
		}
		System.out.println(msg.what);
		handler.sendMessage(msg);
		
		
	}
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if(view==close){
			this.finish();
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
		}
	}

}
