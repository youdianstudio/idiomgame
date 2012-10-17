package org.youdian.caichengyu;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.Map;



import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DictActivity extends Activity implements OnClickListener{

	String idiom_name;
	LinearLayout layout;
	TextView notes;
	EditText search_name;
	Button close,search_btn;
	ImageButton search_cancel;
	String url="http://caichengyu.sinaapp.com/detail";
	public  static final int DATA_OK=1;
	public  static final int DATA_ERROR=0;
	public  static final int NET_ERROR=-1;
	String jsonData;
	Message msg;
	NetOperation netOperation=new NetOperation();
	JsonUtils jsonUtils=new JsonUtils();
	List<Map<String,String>> list=new ArrayList<Map<String,String>>();
	Map<String,String> map;
	Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			
			case DATA_OK:
				notes.setVisibility(View.GONE);
				updateTextView();
				break;
			case DATA_ERROR:
				notes.setVisibility(View.VISIBLE);
				notes.setText("无相关数据");
				layout.removeAllViews();
				break;
			case NET_ERROR:
				notes.setVisibility(View.VISIBLE);
				notes.setText("网络不可用");
				layout.removeAllViews();
				break;
			}
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dict);
		
		search_name=(EditText)findViewById(R.id.dict_search_text);
		layout=(LinearLayout)findViewById(R.id.dict_search_result);
		notes=(TextView)findViewById(R.id.dict_notes);
		close=(Button)findViewById(R.id.dict_close);
		search_btn=(Button)findViewById(R.id.dict_search_confirm);
		search_cancel=(ImageButton)findViewById(R.id.dict_search_cancel);
		close.setOnClickListener(this);
		search_btn.setOnClickListener(this);
		search_cancel.setOnClickListener(this);
		
	}
	
	private void updateTextView(){
		   
		   layout.removeAllViews();
		for(int i=0;i<list.size();i++){
			TextView title=new TextView(this);
			TextView content=new TextView(this);
			map=list.get(i);
			title.setText("\n"+map.get("chengyu")+"\n\n");
			title.setTextSize(16.0f);
			content.setText("【拼音】:"+map.get("pinyin")+"\n\n"+"【解释】："+map.get("meaning")+"\n\n"
							+"【出自】："+map.get("comefrom")+"\n\n"+"【示例】："+map.get("example")+"\n\n");
			content.setTextSize(14.0f);
			View view1=new View(this);
			view1.setMinimumHeight(1);
			view1.setBackgroundColor(getResources().getColor(R.color.line));
			LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			layout.addView(view1, params);
			layout.addView(title, params);
			layout.addView(content,params);
			
		}
	}
	
	class MyRunnable implements Runnable{

		public void run() {
			Looper.prepare();
			// TODO Auto-generated method stub
			if(NetState.isNetworkConnected(DictActivity.this)){
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
			list=jsonUtils.getDetailList();
			System.out.println(list);
		    if(list.size()==0){
		    	msg=new Message();
		    	msg.what=DATA_ERROR;
		    	
		    }else{
		    	msg=new Message();
		    	msg.what=DATA_OK;
		    }
		}
		
		handler.sendMessage(msg);
		
		
	}
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if(view==close){
			this.finish();
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
		}
		if(view==search_cancel){
			search_name.setText("");
		}
		if(view==search_btn){
			idiom_name=search_name.getText().toString();
			if(idiom_name.length()<3){
				Toast.makeText(this, "请输入完整的成语，不认识的字请以 % 代替", Toast.LENGTH_SHORT).show();
				
			}else{
				MyRunnable runnable=new MyRunnable();
				Thread thread=new Thread(runnable);
				thread.start();
			}
		}
	}

	
}
