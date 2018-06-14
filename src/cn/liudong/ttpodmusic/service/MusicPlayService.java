package cn.liudong.ttpodmusic.service;

import java.io.File;
import java.util.ArrayList;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Environment;
import android.os.IBinder;

/**
 * Service:Android四大核心组件之一 主要用来实现歌曲的播放，暂停，上一首，下一首等耗时操作 选用启动模式的Service
 * 
 * @author 刘东
 *
 */
public class MusicPlayService extends Service {

	// 歌曲资源
	private ArrayList<String> musics;
	// 媒体播放工具
	private MediaPlayer player;
	// 歌曲所在的SD卡路劲
	private File dir = null;
	// 注册广播接收器的对象
	private BroadcastReceiver receiver;
	// 当前歌曲下标
	private int currentMusicIndex = 0;
	// 暂停位置
	private int pausePostion;
	// 控制流程的布尔类型变量
	private boolean isRunning;
	// 布尔类型变量判断歌曲是否正在播放
	private boolean isStarted;

	@Override
	public void onCreate() {
		super.onCreate();
		player = new MediaPlayer();
		dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
		receiver = new MyActivityRecevier();
		// (局部变量)广播过滤器
		IntentFilter filter = new IntentFilter();
		filter.addAction("CurrentMusicIndex");
		filter.addAction("PlayMusicPrevious");
		filter.addAction("PlayMusicNext");
		filter.addAction("MakeMusicPlayOrPause");
		filter.addAction("CurrentMusicPosition");

		// 动态注册广播接收器
		registerReceiver(receiver, filter);
		// 开启工作线程，每隔一秒发送广播
		isRunning = true;
		new UpdateProgressThread().start();
		// 循环播放
		player.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				// 播放下一首
				if (isStarted) {
					next();
				}
			}
		});
	}

	// （在主线程）执行耗时操作
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// 获取intent里面传来的歌曲
		musics = intent.getStringArrayListExtra("musics");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	// 播放
	public void play() {
		// 重置媒体播放工具
		player.reset();
		// 设置播放资源
		try {
			player.setDataSource(dir + "/" + musics.get(currentMusicIndex));
			// 准备播放
			player.prepare();
			// 移动到暂停位置
			player.seekTo(pausePostion);
			// 开始播放
			player.start();
			isStarted = true;
			// 发送广播，告诉Activity去改变按钮的状态为暂停
			Intent intent = new Intent("setImagePause");
			sendBroadcast(intent);

			// 发送广播，携带歌曲名称和歌曲总时长
			Intent intent2 = new Intent("MusicName&MusicTotalTime");
			intent2.putExtra("MusicName", musics.get(currentMusicIndex).toString());
			intent2.putExtra("MusicTotalTime", player.getDuration());
			sendBroadcast(intent2);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 暂停
	public void pause() {
		if (player.isPlaying()) {
			player.pause();
			// 获取歌曲暂停位置
			pausePostion = player.getCurrentPosition();
			// 发送广播，改变图标
			Intent intent = new Intent("setImgPlay");
			sendBroadcast(intent);
		}
	}

	// 上一首
	public void previous() {
		currentMusicIndex--;
		if (currentMusicIndex < 0) {
			currentMusicIndex = musics.size() - 1;
		}
		pausePostion = 0;
		play();
	}

	// 下一首
	public void next() {
		currentMusicIndex++;
		if (currentMusicIndex > musics.size() - 1) {
			currentMusicIndex = 0;
		}
		pausePostion = 0;
		play();
	}

	// 广播接收器，专门接收Activity发来的广播
	class MyActivityRecevier extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 接收广播
			String action = intent.getAction();
			if ("CurrentMusicIndex".equals(action)) {
				// 取到携带的歌曲下标
				currentMusicIndex = intent.getIntExtra("position", currentMusicIndex);
				pausePostion = 0;
				// 播放歌曲
				play();
			} else if ("PlayMusicPrevious".equals(action)) {
				// 播放上一首
				previous();
			} else if ("PlayMusicNext".equals(action)) {
				// 播放下一首
				next();
			} else if ("MakeMusicPlayOrPause".equals(action)) {
				// 使音乐播放或暂停
				if (player.isPlaying()) {
					// 暂停
					pause();
				} else {
					play();
				}
			} else if ("CurrentMusicPosition".equals(action)) {
				// 拿到seekBar停止后的位置
				int percent = intent.getIntExtra("seekBarPosition", 0);
				pausePostion = (percent * player.getDuration()) / 100;
				// 发送广播
				Intent intent2 = new Intent("seekToPausePosition");
				intent2.putExtra("seekPosition", pausePostion);
				sendBroadcast(intent2);

				if (player.isPlaying()) {
					play();
				} else {
					pause();
				}
			}

		}

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// 解除注册
		unregisterReceiver(receiver);
	}

	// 开启工作线程，每个一秒钟发送一次有关各区当前位置的广播
	class UpdateProgressThread extends Thread {
		//
		Intent intent = new Intent("UpdateProgress");

		@Override
		public void run() {
			//
			while (isRunning) {
				if (player.isPlaying()) {
					try {
						//
						Thread.sleep(1000);
						intent.putExtra("currentPosition", player.getCurrentPosition());
						sendBroadcast(intent);

					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
			super.run();
		}
	}

}
