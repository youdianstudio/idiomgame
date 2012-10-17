package org.youdian.caichengyu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.ColorMatrixColorFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.CountDownTimer;

public class WordActivity extends Activity implements OnClickListener{

	Button b1,b2,b3,b4,b5,b6,b7,b8,b9,b10,b11,b12,b13,b14,b15,b16,b17,b18,b19,b20,b21,b22,b23,b24;
	Button refresh,start_dict;
	TextView result,timer_text;
	LinearLayout linear5,linear6;
	LinearLayout level_linear;
	ImageButton level_imagebtn;
	PopupWindow level_choice;
	LayoutInflater inflater;
	TextView level_tv;
	MediaPlayer mp;
	ArrayList<String> tmp_result=new ArrayList<String>();
	List<Button> tmp_btn=new ArrayList<Button>();
	List<String> result_list=new ArrayList<String>();
	List<String> idiom_list=new ArrayList<String>();
	List<Button> button_list=new ArrayList<Button>();
	String jsonData;
	public  int level=1;
	String whichIdiom="nodata";
	NetOperation netOperation=new NetOperation();
	JsonUtils jsonUtils=new JsonUtils();
	public  static final int DATA_OK=1;
	public  static final int DATA_ERROR=0;
	public  static final int NET_ERROR=-1;
	Message msg;
	Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			refresh.setClickable(true);
			switch(msg.what){
			
			    case DATA_OK:
			    	initButtonWords();
			    	initButtonView();
			    	result.setText("成绩:0/0");
					timer.start();
			    	
				    break;
			    case DATA_ERROR:
			    	Toast.makeText(WordActivity.this, "连接服务器失败,请重试!", Toast.LENGTH_SHORT).show();
			    	break;
			    case NET_ERROR:
			    	Toast.makeText(WordActivity.this, "无网络连接!", Toast.LENGTH_SHORT).show();
			    	break;
			}
			
		}
		
	};
	CountDownTimer timer=new CountDownTimer(60000, 1000){

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			gameOver();
		}

		@Override
		public void onTick(long millisUntilFinished) {
			// TODO Auto-generated method stub
			 timer_text.setText("剩余时间: " + millisUntilFinished / 1000);
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.word);
		linear5=(LinearLayout)findViewById(R.id.word_linear5);
		linear6=(LinearLayout)findViewById(R.id.word_linear6);
		initButton();
		
		
		result=(TextView)findViewById(R.id.word_result);
		timer_text=(TextView)findViewById(R.id.word_timer);
		refresh=(Button)findViewById(R.id.word_refresh);
		start_dict=(Button)findViewById(R.id.word_start_dict);
		
		level_linear=(LinearLayout)findViewById(R.id.word_level_choice_layout);
		
		level_imagebtn=(ImageButton)findViewById(R.id.word_level_choice_btn);
		level_tv=(TextView)findViewById(R.id.word_level_choice_level);
		level_linear.setOnClickListener(this);
		refresh.setOnClickListener(this);
		start_dict.setOnClickListener(this);
		
	}
	

	
	
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		newGame();
		
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		timer.cancel();
		if(mp!=null){
			mp.release();
			mp=null;
		}
		result.setText("");
		timer_text.setText("");
		hideButtons();
		if(level_choice!=null){
			level_choice.dismiss();
			level_choice=null;
		}
	}

	private void newGame(){
		refresh.setClickable(false);
		for(int i=0;i<button_list.size();i++){
			Button b=button_list.get(i);
			b.setSelected(false);
		}
		if(!tmp_result.isEmpty())tmp_result.clear();
		if(!tmp_btn.isEmpty())tmp_btn.clear();
		MyRunnable runnable=new MyRunnable();
		Thread thread=new Thread(runnable);
		thread.start();
	}
	class MyRunnable implements Runnable{

		public void run() {
			Looper.prepare();
			// TODO Auto-generated method stub
			if(NetState.isNetworkConnected(WordActivity.this)){
				fetchData();
				if(jsonData!=null&&jsonData.startsWith("[")){
	    			msg=new Message();
	    			msg.what = DATA_OK;
	    			}
			}else{
				msg=new Message();
	        	msg.what = NET_ERROR;
			}
			handler.sendMessage(msg);
			//handler.handleMessage(msg);
			Looper.loop();
		}
		
	}
	private void fetchData(){
		Map<String,String> map=new HashMap<String,String>();
		map.put("level", String.valueOf(level));
		String url="http://caichengyu.sinaapp.com/chengyu";
		jsonData=netOperation.postData(map,url);
		
		if(jsonData==null||!(jsonData.startsWith("["))){
			msg=new Message();
			msg.what=DATA_ERROR;
			return;
		}
		jsonUtils.parseJson(jsonData);
		idiom_list=jsonUtils.getData();
		if(!result_list.isEmpty())result_list.clear();
		result_list.addAll(idiom_list);

	}
	private void hideButtons(){

		for(int i=0;i<button_list.size();i++){
			Button b=button_list.get(i);
			b.setVisibility(View.INVISIBLE);
		}
	}
	private void initButtonWords(){
		
		//let word_list to be a local variable  to avoid duplicate data
		List<String> word_list=new ArrayList<String>();
		for(int i=0;i<idiom_list.size();i++)
		for(int j=0;j<4;j++){
			char c=idiom_list.get(i).charAt(j);
			
			word_list.add(String.valueOf(c));
		}
		Collections.shuffle(word_list);
	    
		for(int i=0;i<16;i++){
			Button b=button_list.get(i);
			b.setText(word_list.get(i));
		}
	
		if(level>=2){
			b17.setText(word_list.get(16));
			b18.setText(word_list.get(17));
			b19.setText(word_list.get(18));
			b20.setText(word_list.get(19));
		}
		if(level>=3){
			b21.setText(word_list.get(20));
			b22.setText(word_list.get(21));
			b23.setText(word_list.get(22));
			b24.setText(word_list.get(23));
		}
		
	}

	
	private void initButtonView(){
		
		for(int i=0;i<16;i++){
			Button b=button_list.get(i);
			b.setVisibility(View.VISIBLE);
		}
	
		if(level==1){
			if(linear5.getVisibility()==View.VISIBLE){
				linear5.setVisibility(View.GONE);
			}
			if(linear6.getVisibility()==View.VISIBLE){
				linear6.setVisibility(View.GONE);
			}
			
		}
		if(level==2){
			if(linear6.getVisibility()==View.VISIBLE){
				linear6.setVisibility(View.GONE);
			}
		}
		if(level>=2){
			   linear5.setVisibility(View.VISIBLE);
			   b17.setVisibility(View.VISIBLE);
			   b18.setVisibility(View.VISIBLE);
			   b19.setVisibility(View.VISIBLE);
			   b20.setVisibility(View.VISIBLE);
			}
			if(level>=3){
				linear6.setVisibility(View.VISIBLE);
				b21.setVisibility(View.VISIBLE);
				b22.setVisibility(View.VISIBLE);
				b23.setVisibility(View.VISIBLE);
				b24.setVisibility(View.VISIBLE);	
			}
		
			
	
	}
	private void initButton() {
		// TODO Auto-generated method stub
		b1=(Button)findViewById(R.id.word_b1);
		b2=(Button)findViewById(R.id.word_b2);
		b3=(Button)findViewById(R.id.word_b3);
		b4=(Button)findViewById(R.id.word_b4);
		b5=(Button)findViewById(R.id.word_b5);
		b6=(Button)findViewById(R.id.word_b6);
		b7=(Button)findViewById(R.id.word_b7);
		b8=(Button)findViewById(R.id.word_b8);
		b9=(Button)findViewById(R.id.word_b9);
		b10=(Button)findViewById(R.id.word_b10);
		b11=(Button)findViewById(R.id.word_b11);
		b12=(Button)findViewById(R.id.word_b12);
		b13=(Button)findViewById(R.id.word_b13);
		b14=(Button)findViewById(R.id.word_b14);
		b15=(Button)findViewById(R.id.word_b15);
		b16=(Button)findViewById(R.id.word_b16);
		b17=(Button)findViewById(R.id.word_b17);
		b18=(Button)findViewById(R.id.word_b18);
		b19=(Button)findViewById(R.id.word_b19);
		b20=(Button)findViewById(R.id.word_b20);
		b21=(Button)findViewById(R.id.word_b21);
		b22=(Button)findViewById(R.id.word_b22);
		b23=(Button)findViewById(R.id.word_b23);
		b24=(Button)findViewById(R.id.word_b24);
		button_list.add(b1);
		button_list.add(b2);
		button_list.add(b3);
		button_list.add(b4);
		button_list.add(b5);
		button_list.add(b6);
		button_list.add(b7);
		button_list.add(b8);
		button_list.add(b9);
		button_list.add(b10);
		button_list.add(b11);
		button_list.add(b12);
		button_list.add(b13);
		button_list.add(b14);
		button_list.add(b15);
		button_list.add(b16);
		button_list.add(b17);
		button_list.add(b18);
		button_list.add(b19);
		button_list.add(b20);
		button_list.add(b21);
		button_list.add(b22);
		button_list.add(b23);
		button_list.add(b24);
		for(int i=0;i<button_list.size();i++){
			Button b=button_list.get(i);
			b.setOnClickListener(this);
		}

		
	}
    private void notifyResultChanged()
    {
    	int len=tmp_result.size();
    	
    	if(len==4)
    	{    
    		if(checkWord()==true){
    			playSound("success");
    			
    			result.setText("成绩: "+String.valueOf(result_list.size()-idiom_list.size())+
    					"/"+String.valueOf(result_list.size()));
    			
    			Button button;
    			for(int i=0;i<tmp_btn.size();i++){
    				button=(Button)tmp_btn.get(i);
    				button.setVisibility(View.INVISIBLE);
    			}
    			
    			
    		}else{
    			playSound("fail");
    		}
    		    tmp_btn.clear();
    			tmp_result.clear();
    			for(int i=0;i<button_list.size();i++){
    				Button b=button_list.get(i);
    				b.setSelected(false);
    			}
    		
    		if(idiom_list.isEmpty())gameOver();
    	}
    	
    }
    
    private void playSound(String string){
    	
    	if(string.equals("success")){
    		 mp = MediaPlayer.create(this,R.raw.success);
    		try {
				mp.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		mp.start();
    		
    	}
    	if(string.equals("fail")){
    		 mp = MediaPlayer.create(this,R.raw.fail);
    		try {
				mp.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		mp.start();
    	}
    	
    }
    
    private void gameOver(){
    	timer.cancel();
    	Bundle bundle=new Bundle();
    	
    	if(idiom_list.isEmpty()){
    	    for(int i=0;i<result_list.size();i++){
    	        bundle.putString(result_list.get(i), "right");
    		}
    	}else{
			for(int i=0;i<result_list.size();i++){
				boolean isleft=false;
				for(int j=0;j<idiom_list.size();j++){
					if(result_list.get(i).equals(idiom_list.get(j))){
						bundle.putString(result_list.get(i),"wrong");
						isleft=true;
						break;
					}
				}
				if(!isleft){
					bundle.putString(result_list.get(i), "right");
				}
			}
		}
    	Intent intent=new Intent();
    	intent.setClass(WordActivity.this, ResultActivity.class);
    	intent.putExtra("result", bundle);
    	WordActivity.this.startActivity(intent);
    	overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    	
    }
    //检查是否为一个成语
    private boolean checkWord(){
    	boolean isIdiom=false;
    	boolean isInList=false;
    	for(int i=0;i<idiom_list.size();i++){
    		
        	
    		for(int j=0;j<4;j++){
    			if(idiom_list.get(i).contains((String)tmp_result.get(j))){
    				isInList=true;
    				
    			}else{
    				isInList=false;
    				break;
    			}
    			/*if(idiom_list.get(i).contains((String)tmp_result.get(j))){
    				isIdiom=true;
    			}else{
    			    isIdiom=false;
    				break;
    				
    			}*/
    		}
    		if(isInList){
        		break;
        	}
    	}
    	if(!isInList){
    		return false;
    	}
    	
    	
    		
    		String s="";
    		String s1="";
    		String s2="";
    		String s3="";
    	     for(int a=0;a<tmp_result.size();a++){
    	    	 s=tmp_result.get(a);
    	    	 s3=s;
    	    	 for(int b=0;b<tmp_result.size();b++){
    	    		 if(b==a){
    	    			 continue;
    	    		 }else{
    	    			 s=s3+tmp_result.get(b);
    	    			 s1=s;
    	    			 for(int c=0;c<tmp_result.size();c++){
    	    				 if(c==a||c==b){
    	    					 continue;
    	    				 }else{
    	    					 s=s1+tmp_result.get(c);
    	    					 s2=s;
    	    					 for(int d=0;d<tmp_result.size();d++){
    	    						 if(d==a||d==b||d==c){
    	    							 continue;
    	    						 }else{
    	    							 s=s2+tmp_result.get(d);
    	    							 System.out.println(s);
    	    							 for(int i=0;i<idiom_list.size();i++){
    	    							 if(s.equals(idiom_list.get(i))){
    	    								 whichIdiom=idiom_list.get(i);
    	    					    		 idiom_list.remove(i); 
    	    					    		 return true;
    	    							 }
    	    							 }
    	    							
    	    							 s="";
    	    						 }
    	    					 }
    	    				 }
    	    				 
    	    			 }
    	    		 }
    	    	 }
    	     }
    	
    		
       		
    	
    	
    	return isIdiom;
    }
    private void updateList(int buttonid){
    	    	
    	Button b=button_list.get(buttonid-1);
    	if(!b.isSelected()){
    		b.setSelected(true);
    		tmp_btn.add(b);
			tmp_result.add(b.getText().toString());
			notifyResultChanged();
    	}else{
    		b.setSelected(false);
    		tmp_btn.remove(b);
    		tmp_result.remove(b.getText());
    	}
    }
	public void onClick(View view) 
	{   
		// TODO Auto-generated method stub
		switch(view.getId())
		{
		case R.id.word_b1:
			updateList(1);
			break;
		case R.id.word_b2:
			updateList(2);
			break;
		case R.id.word_b3:
			updateList(3);
			break;
		case R.id.word_b4:
			updateList(4);
			break;
		case R.id.word_b5:
			updateList(5);
			break;
		case R.id.word_b6:
			updateList(6);
			break;
		case R.id.word_b7:
			updateList(7);
			break;
		case R.id.word_b8:
			updateList(8);
			break;
		case R.id.word_b9:
			updateList(9);
			break;
		case R.id.word_b10:
			updateList(10);
			break;
		case R.id.word_b11:
			updateList(11);
			break;
		case R.id.word_b12:
			updateList(12);
			break;
		case R.id.word_b13:
			updateList(13);
			break;
		case R.id.word_b14:
			updateList(14);
			break;
		case R.id.word_b15:
			updateList(15);
			break;
		case R.id.word_b16:
			updateList(16);
			break;
		case R.id.word_b17:
			updateList(17);
			break;
		case R.id.word_b18:
			updateList(18);
			break;
		case R.id.word_b19:
			updateList(19);
			break;
		case R.id.word_b20:
			updateList(20);
			break;
		case R.id.word_b21:
			updateList(21);
			break;
		case R.id.word_b22:
			updateList(22);
			break;
		case R.id.word_b23:
			updateList(23);
			break;
		case R.id.word_b24:
			updateList(24);
			break;
		case R.id.word_level_choice_layout:
			
			level_imagebtn.setImageResource(R.drawable.level_unfold);
			if(level_choice!=null){
				level_choice.dismiss();
				level_choice=null;
				level_imagebtn.setImageResource(R.drawable.level_fold);
				return;
			}
			
			inflater=LayoutInflater.from(this);
			View v=inflater.inflate(R.layout.level_dropdown, null,false);
			TextView level_1=(TextView)v.findViewById(R.id.word_level_choice_level1);
			TextView level_2=(TextView)v.findViewById(R.id.word_level_choice_level2);
			TextView level_3=(TextView)v.findViewById(R.id.word_level_choice_level3);
			level_choice=new PopupWindow(v,150,150);
			
			level_choice.showAsDropDown(level_linear, -40, 0);
			level_choice.setOutsideTouchable(true);
			level_1.setOnClickListener(new OnClickListener(){

				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					level_tv.setText(R.string.level1);
					level=1;
					level_choice.dismiss();
					level_choice=null;
					level_imagebtn.setImageResource(R.drawable.level_fold);
					newGame();
				}
				
			});
			level_2.setOnClickListener(new OnClickListener(){

				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					level_tv.setText(R.string.level2);
					level=2;
					level_choice.dismiss();
					level_choice=null;
					level_imagebtn.setImageResource(R.drawable.level_fold);
					newGame();
				}
				
			});
			level_3.setOnClickListener(new OnClickListener(){

				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					level_tv.setText(R.string.level3);
					level=3;
					level_choice.dismiss();
					level_choice=null;
					level_imagebtn.setImageResource(R.drawable.level_fold);
					newGame();
				}
				
			});
			
			break;
		
		case R.id.word_refresh:
			
			newGame();
			
			break;
		case R.id.word_start_dict:
			Intent intent=new Intent();
			intent.setClass(this, DictActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
			break;
		
		}
	}


	
}