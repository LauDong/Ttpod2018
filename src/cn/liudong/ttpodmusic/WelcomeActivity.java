package cn.liudong.ttpodmusic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class WelcomeActivity extends Activity {
	
	//声明控件
	private ImageView imgWelcome;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		setupView();
	}

	private void setupView() {
		imgWelcome=(ImageView) findViewById(R.id.mg_welcome01);
		//（拿到）加载动画
		Animation anim=
				AnimationUtils.loadAnimation(this, R.anim.alpha);
		imgWelcome.startAnimation(anim);
		//给动画添加监听器
		anim.setAnimationListener(new AnimationListener() {
			//导包ctrl shift o
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				//界面跳转
				Intent intent=
						new Intent(WelcomeActivity.this,MainActivity.class);
				//启动意图
				startActivity(intent);
				//回退时直接结束
				finish();
			}
		});
	}

}
