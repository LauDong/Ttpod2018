package cn.liudong.ttpodmusic;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import cn.liudong.ttpodmusic.adapter.MyAdapter;
import cn.liudong.ttpodmusic.service.MusicPlayService;
import cn.liudong.ttpodmusic.utils.Global;

public class MusicPlayActivity extends Activity {

	// 声明控件
	private ListView lv = null;
	/* 包饺子 */
	// 数据源
	ArrayList<String> musics = null;
	// 获取歌曲所在SD卡路径
	private File dir = null;
	private BroadcastReceiver receiver;
	// （声名）播放或暂停按钮
	private ImageButton imbPlay;
	// 上一首按钮
	private ImageButton imbPrevious;
	// 下一首按钮
	private ImageButton imbNext;
	// 歌曲名称
	private TextView tvMusicName;
	// 歌曲总时长
	private TextView tvTotalTime;
	// 当前时间
	private TextView tvCurrentTime;
	// 进度条
	private SeekBar sb;
	// 返回键
	private ImageView imgMPABack;
	// 侧滑菜单
	private ListView lvDrawer;
	// 侧滑菜单按钮
	private ImageView imgDrawer;
	// 抽屉布局
	private DrawerLayout drawer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music_play);
		setupView();
		// 添加监听器
		addListener();
		receiver = new MyServiceReceiver();
		// 广播过滤器
		IntentFilter filter = new IntentFilter();
		filter.addAction("setImagePause");
		filter.addAction("setImgPlay");
		filter.addAction("MusicName&MusicTotalTime");
		filter.addAction("UpdateProgress");
		filter.addAction("seekToPausePosition");

		// 注册广播接收器
		registerReceiver(receiver, filter);
	}

	private void addListener() {

		lvDrawer.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// 不管点哪一个都关闭抽屉
				drawer.closeDrawer(lvDrawer);
				switch (position) {
				case 2:
					// 实现换肤
					drawer.setBackgroundResource(bgs[getThemeBg()]);
					// 静态index
					lvDrawer.setBackgroundResource(bgs[index]);
					break;

				default:
					break;
				}
			}
		});

		imgDrawer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				drawer.openDrawer(lvDrawer);
			}
		});

		// addFunc
		imgMPABack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MusicPlayActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});

		sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// 从拖放停止位置播放
				int percent = seekBar.getProgress();
				Intent intent = new Intent("CurrentMusicPosition");
				intent.putExtra("seekBarPosition", percent);
				sendBroadcast(intent);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// TODO Auto-generated method stub

			}
		});

		imbPlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 发送广播告诉Service，播放或暂停
				Intent intent = new Intent("MakeMusicPlayOrPause");
				sendBroadcast(intent);
			}
		});
		imbNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent("PlayMusicNext");
				sendBroadcast(intent);
			}
		});
		imbPrevious.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 发送广播给Service播放上一首歌
				Intent intent = new Intent("PlayMusicPrevious");
				sendBroadcast(intent);
			}
		});
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// 发送广播,携带当前被点中歌曲的下标
				Intent intent = new Intent("CurrentMusicIndex");
				// 携带数据，发送出去
				intent.putExtra("position", position);
				sendBroadcast(intent);
			}
		});
	}

	private void setupView() {

		drawer = (DrawerLayout) findViewById(R.id.drawer);
		imgDrawer = (ImageView) findViewById(R.id.img_list_menu);
		lvDrawer = (ListView) findViewById(R.id.lv_drawer);
		imgMPABack = (ImageView) findViewById(R.id.img_list_back);
		sb = (SeekBar) findViewById(R.id.sb);
		tvCurrentTime = (TextView) findViewById(R.id.tv_list_currenttime);
		tvTotalTime = (TextView) findViewById(R.id.tv_list_totaltime);
		tvMusicName = (TextView) findViewById(R.id.tv_list_musicname);
		imbPlay = (ImageButton) findViewById(R.id.imb_list_play);
		imbPrevious = (ImageButton) findViewById(R.id.imb_list_previous);
		imbNext = (ImageButton) findViewById(R.id.imb_list_next);

		lv = (ListView) findViewById(R.id.lv_list);
		musics = new ArrayList<String>();
		dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
		// 所有的歌曲文件
		// 列举你所有的文件
		File[] files = dir.listFiles();
		// 饺子馅
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				musics.add(files[i].getName());
			}
		}
		// 饺子下锅
		// 导包
		MyAdapter adapter = new MyAdapter(musics, this);
		lv.setAdapter(adapter);
		// 启动服务去执行耗时操作
		Intent intent = new Intent(MusicPlayActivity.this, MusicPlayService.class);
		// 携带数据
		intent.putStringArrayListExtra("musics", musics);
		startService(intent);

		// 侧滑菜单数据源
		String[] strDrawer = { "登录", "播放模式", "个性皮肤", "分享", "设置", "退出登录" };
		// Adater适配器
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1,
				strDrawer);
		lvDrawer.setAdapter(adapter2);
		// 设置背景
		drawer.setBackgroundResource(bgs[index]);
		lvDrawer.setBackgroundResource(bgs[index]);
	}

	// 广播接收器，专门接收Service发来的广播
	class MyServiceReceiver extends BroadcastReceiver {

		int totalTime = 0;

		@Override
		public void onReceive(Context context, Intent intent) {
			// 拿到所有过滤到的广播
			String action = intent.getAction();
			if ("setImagePause".equals(action)) {
				// 改变按钮图标
				imbPlay.setImageResource(android.R.drawable.ic_media_pause);
			} else if ("setImgPlay".equals(action)) {
				// 改变图标为播放
				imbPlay.setImageResource(android.R.drawable.ic_media_play);
			} else if ("MusicName&MusicTotalTime".equals(action)) {
				// 获取歌名(此时的intent为传过来的参数那个)
				String musicName = intent.getStringExtra("MusicName");
				// addFunc
				String[] onlyName = musicName.split(".mp3");
				tvMusicName.setText("正在播放：" + onlyName[0]);
				// 获取歌曲总时长(默认值改为1，避免除数为0)
				totalTime = intent.getIntExtra("MusicTotalTime", 1);
				String strTotalTime = Global.setTime(totalTime);
				tvTotalTime.setText(strTotalTime);
			} else if ("UpdateProgress".equals(action)) {
				// 每隔一秒更新时间进度条
				int currentTime = intent.getIntExtra("currentPosition", 0);
				// 把毫秒值转化为格式字符串mm:ss
				String strCurrentTime = Global.setTime(currentTime);
				// 显示时间
				tvCurrentTime.setText(strCurrentTime);
				// 设置进度条
				// 获取进度条
				if (totalTime != 0) {
					int percent = currentTime * 100 / totalTime;
					// 设置seekbar进度
					sb.setProgress(percent);
				}

			} else if ("seekToPausePosition".equals(action)) {
				// 重新设置当前时间
				int currenttime = intent.getIntExtra("seekPosition", 0);
				String strCurrentTime = Global.setTime(currenttime);
				tvCurrentTime.setText(strCurrentTime);
			}
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 解除注册
		unregisterReceiver(receiver);
	}

	// 随机下标(静态！！)
	private static int index;
	// 随机换肤图片资源
	int[] bgs = { R.drawable.bg1, R.drawable.bg2, R.drawable.bg3, R.drawable.bg4, R.drawable.bg5 };

	// 随机数方法(随机皮肤下标)
	public int getThemeBg() {
		Random random = new Random();
		index = random.nextInt(bgs.length);
		// 发送广播
		Intent intent = new Intent("ThemeBg");
		intent.putExtra("index", index);
		sendBroadcast(intent);
		return index;
	}
}
