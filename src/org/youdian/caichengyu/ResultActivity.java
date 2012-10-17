package org.youdian.caichengyu;

import java.util.Iterator;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ResultActivity extends Activity implements OnClickListener{
    int len=0;
    int right=0;
    RelativeLayout[] layout=new RelativeLayout[6];
    TextView []idiom_name=new TextView[6];
    TextView[] idiom_judge=new TextView[6];
    TextView score_text;
    ImageView score_image;
    Button close_btn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);
		
		Intent intent=this.getIntent();
		Bundle bundle=intent.getBundleExtra("result");
		len=bundle.size();
		initIdiomLayout();
		Set<String> keys=bundle.keySet();
		
		Iterator<String> it = keys.iterator(); 
		int i=0;
		while (it.hasNext()) {  
		  String str = it.next();  
		   idiom_name[i].setText(str);
		   String judge=bundle.getString(str);
		   if(judge.equals("right")){
			   idiom_judge[i].setText("正确");
			   right++;
		   }else{
			   idiom_judge[i].setText("错误");
		   }
		   
		   i++;
		}
		
		updateScore(len,right);
	}
	private void updateScore(int len,int right){
		if(len==4){
		if(right==4){
			score_image.setImageResource(R.drawable.grade1);
			score_text.setText("恭喜你,你的成绩为:满分");
		}
		
		else if(right>=2){
			score_image.setImageResource(R.drawable.grade2);
			score_text.setText("恭喜你,你的成绩为:优秀");
		}else if(len-right==1){
			score_image.setImageResource(R.drawable.grade3);
			score_text.setText("很遗憾,你的成绩为:及格");
		}else{
			score_image.setImageResource(R.drawable.grade4);
			score_text.setText("很遗憾,你的成绩为:不及格");
		}
	}else if(len==5){
		if(right==5){
			score_image.setImageResource(R.drawable.grade1);
			score_text.setText("恭喜你,你的成绩为:满分");
		}else if(right>=3){
			score_image.setImageResource(R.drawable.grade2);
			score_text.setText("恭喜你,你的成绩为:优秀");
		}else if(right==2){
			score_image.setImageResource(R.drawable.grade3);
			score_text.setText("很遗憾,你的成绩为:及格");
		}else{
			score_image.setImageResource(R.drawable.grade4);
			score_text.setText("很遗憾,你的成绩为:不及格");
		}
	}else{//len==6
		if(right==6){
			score_image.setImageResource(R.drawable.grade1);
			score_text.setText("恭喜你,你的成绩为:满分");
		}else if(right>=4){
			score_image.setImageResource(R.drawable.grade2);
			score_text.setText("恭喜你,你的成绩为:优秀");
		}else if(right==3){
			score_image.setImageResource(R.drawable.grade3);
			score_text.setText("很遗憾,你的成绩为:及格");
		}else{
			score_image.setImageResource(R.drawable.grade4);
			score_text.setText("很遗憾,你的成绩为:不及格");
		}
	}
	}
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if(view==close_btn){
			this.finish();
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
		}
		for(int i=0;i<6;i++){
			if(view==layout[i]){
				Intent intent=new Intent();
				intent.setClass(this, DetailActivity.class);
				intent.putExtra("idiom", idiom_name[i].getText().toString());
				startActivity(intent);
				overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
				
			}
		}
	}
    private void initIdiomLayout(){
    	score_text=(TextView)findViewById(R.id.result_score_text);
    	score_image=(ImageView)findViewById(R.id.result_score_emotion);
    	close_btn=(Button)findViewById(R.id.result_close);
    	close_btn.setOnClickListener(this);
    	layout[0]=(RelativeLayout)findViewById(R.id.result_idiom_1);
    	layout[1]=(RelativeLayout)findViewById(R.id.result_idiom_2);
    	layout[2]=(RelativeLayout)findViewById(R.id.result_idiom_3);
    	layout[3]=(RelativeLayout)findViewById(R.id.result_idiom_4);
    	layout[4]=(RelativeLayout)findViewById(R.id.result_idiom_5);
    	layout[5]=(RelativeLayout)findViewById(R.id.result_idiom_6);
    	idiom_name[0]=(TextView)findViewById(R.id.result_idiom_text_1);
    	idiom_name[1]=(TextView)findViewById(R.id.result_idiom_text_2);
    	idiom_name[2]=(TextView)findViewById(R.id.result_idiom_text_3);
    	idiom_name[3]=(TextView)findViewById(R.id.result_idiom_text_4);
    	idiom_name[4]=(TextView)findViewById(R.id.result_idiom_text_5);
    	idiom_name[5]=(TextView)findViewById(R.id.result_idiom_text_6);
    	idiom_judge[0]=(TextView)findViewById(R.id.result_idiom_judge_1);
    	idiom_judge[1]=(TextView)findViewById(R.id.result_idiom_judge_2);
    	idiom_judge[2]=(TextView)findViewById(R.id.result_idiom_judge_3);
    	idiom_judge[3]=(TextView)findViewById(R.id.result_idiom_judge_4);
    	idiom_judge[4]=(TextView)findViewById(R.id.result_idiom_judge_5);
    	idiom_judge[5]=(TextView)findViewById(R.id.result_idiom_judge_6);
    	layout[0].setVisibility(View.VISIBLE);
    	layout[1].setVisibility(View.VISIBLE);
    	layout[2].setVisibility(View.VISIBLE);
    	layout[3].setVisibility(View.VISIBLE);
    	if(len>=5)layout[4].setVisibility(View.VISIBLE);
    	if(len>=6)layout[5].setVisibility(View.VISIBLE);
    	for(int i=0;i<6;i++){
    		layout[i].setOnClickListener(this);
    	}
    }
	
}
