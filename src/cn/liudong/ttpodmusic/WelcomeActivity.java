package cn.liudong.ttpodmusic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class WelcomeActivity extends Activity {
	
	//�����ؼ�
	private ImageView imgWelcome;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		setupView();
	}

	private void setupView() {
		imgWelcome=(ImageView) findViewById(R.id.mg_welcome01);
		//���õ������ض���
		Animation anim=
				AnimationUtils.loadAnimation(this, R.anim.alpha);
		imgWelcome.startAnimation(anim);
		//��������Ӽ�����
		anim.setAnimationListener(new AnimationListener() {
			//����ctrl shift o
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				//������ת
				Intent intent=
						new Intent(WelcomeActivity.this,MainActivity.class);
				//������ͼ
				startActivity(intent);
				//����ʱֱ�ӽ���
				finish();
			}
		});
	}

}
