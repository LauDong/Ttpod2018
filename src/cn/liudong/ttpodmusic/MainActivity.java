package cn.liudong.ttpodmusic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import cn.liudong.ttpodmusic.fragment.FindMusicFragment;
import cn.liudong.ttpodmusic.fragment.MineFragment;
import cn.liudong.ttpodmusic.fragment.RecommandFragment;
import cn.liudong.ttpodmusic.fragment.SearchFragment;

public class MainActivity extends FragmentActivity {

	// 声明控件
	private ViewPager vpMain;
	private RadioGroup rgMain;
	private BroadcastReceiver receiver;
	private RelativeLayout rl_activitymain;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// 初始化
		setupView();
		// 添加监听器
		addListener();
		// 注册广播
		receiver = new MyThemeBgReciver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("ThemeBg");

		registerReceiver(receiver, filter);
	}

	private void addListener() {
		// 给RadioGroup添加监听器
		rgMain.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_mine:
					vpMain.setCurrentItem(0);
					break;
				case R.id.rb_findMisic:
					vpMain.setCurrentItem(1);
					break;
				case R.id.rb_search:
					vpMain.setCurrentItem(2);
					break;
				case R.id.rb_recommend:
					vpMain.setCurrentItem(3);
					break;

				default:
					break;
				}

			}
		});

		// 给ViewPager华东添加监听器
		vpMain.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				switch (position) {
				case 0:
					rgMain.check(R.id.rb_mine);
					break;
				case 1:
					rgMain.check(R.id.rb_findMisic);
					break;
				case 2:
					rgMain.check(R.id.rb_search);
					break;
				case 3:
					rgMain.check(R.id.rb_recommend);
					break;

				default:
					break;
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	private void setupView() {
		rl_activitymain = (RelativeLayout) findViewById(R.id.activity_main);
		vpMain = (ViewPager) findViewById(R.id.vp_main);
		rgMain = (RadioGroup) findViewById(R.id.rg_main);
		// 饺子下锅
		MyAdapter adapter = new MyAdapter(getSupportFragmentManager());
		vpMain.setAdapter(adapter);

		rl_activitymain.setBackgroundResource(bgs[index]);
	}

	// Adapter适配器
	class MyAdapter extends FragmentPagerAdapter {

		public MyAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		// 获取某一页
		@Override
		public Fragment getItem(int postion) {
			Fragment fm = null;
			switch (postion) {
			case 0:
				fm = new MineFragment();
				break;
			case 1:
				fm = new FindMusicFragment();
				break;
			case 2:
				fm = new SearchFragment();
				break;
			case 3:
				fm = new RecommandFragment();
				break;

			default:
				break;
			}
			return fm;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 4;
		}

	}

	// 随机换肤图片资源
	int[] bgs = { R.drawable.bg1, R.drawable.bg2, R.drawable.bg3, R.drawable.bg4, R.drawable.bg5 };
	private static int index;

	// 广播接收器
	class MyThemeBgReciver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if ("ThemeBg".equals(action)) {
				index = intent.getIntExtra("index", 0);
				rl_activitymain.setBackgroundResource(bgs[index]);
				
			}
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 解除注册
		unregisterReceiver(receiver);
	}
}
