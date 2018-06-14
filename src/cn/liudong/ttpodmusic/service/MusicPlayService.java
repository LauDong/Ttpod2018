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
 * Service:Android�Ĵ�������֮һ ��Ҫ����ʵ�ָ����Ĳ��ţ���ͣ����һ�ף���һ�׵Ⱥ�ʱ���� ѡ������ģʽ��Service
 * 
 * @author ����
 *
 */
public class MusicPlayService extends Service {

	// ������Դ
	private ArrayList<String> musics;
	// ý�岥�Ź���
	private MediaPlayer player;
	// �������ڵ�SD��·��
	private File dir = null;
	// ע��㲥�������Ķ���
	private BroadcastReceiver receiver;
	// ��ǰ�����±�
	private int currentMusicIndex = 0;
	// ��ͣλ��
	private int pausePostion;
	// �������̵Ĳ������ͱ���
	private boolean isRunning;
	// �������ͱ����жϸ����Ƿ����ڲ���
	private boolean isStarted;

	@Override
	public void onCreate() {
		super.onCreate();
		player = new MediaPlayer();
		dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
		receiver = new MyActivityRecevier();
		// (�ֲ�����)�㲥������
		IntentFilter filter = new IntentFilter();
		filter.addAction("CurrentMusicIndex");
		filter.addAction("PlayMusicPrevious");
		filter.addAction("PlayMusicNext");
		filter.addAction("MakeMusicPlayOrPause");
		filter.addAction("CurrentMusicPosition");

		// ��̬ע��㲥������
		registerReceiver(receiver, filter);
		// ���������̣߳�ÿ��һ�뷢�͹㲥
		isRunning = true;
		new UpdateProgressThread().start();
		// ѭ������
		player.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				// ������һ��
				if (isStarted) {
					next();
				}
			}
		});
	}

	// �������̣߳�ִ�к�ʱ����
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// ��ȡintent���洫���ĸ���
		musics = intent.getStringArrayListExtra("musics");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	// ����
	public void play() {
		// ����ý�岥�Ź���
		player.reset();
		// ���ò�����Դ
		try {
			player.setDataSource(dir + "/" + musics.get(currentMusicIndex));
			// ׼������
			player.prepare();
			// �ƶ�����ͣλ��
			player.seekTo(pausePostion);
			// ��ʼ����
			player.start();
			isStarted = true;
			// ���͹㲥������Activityȥ�ı䰴ť��״̬Ϊ��ͣ
			Intent intent = new Intent("setImagePause");
			sendBroadcast(intent);

			// ���͹㲥��Я���������ƺ͸�����ʱ��
			Intent intent2 = new Intent("MusicName&MusicTotalTime");
			intent2.putExtra("MusicName", musics.get(currentMusicIndex).toString());
			intent2.putExtra("MusicTotalTime", player.getDuration());
			sendBroadcast(intent2);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ��ͣ
	public void pause() {
		if (player.isPlaying()) {
			player.pause();
			// ��ȡ������ͣλ��
			pausePostion = player.getCurrentPosition();
			// ���͹㲥���ı�ͼ��
			Intent intent = new Intent("setImgPlay");
			sendBroadcast(intent);
		}
	}

	// ��һ��
	public void previous() {
		currentMusicIndex--;
		if (currentMusicIndex < 0) {
			currentMusicIndex = musics.size() - 1;
		}
		pausePostion = 0;
		play();
	}

	// ��һ��
	public void next() {
		currentMusicIndex++;
		if (currentMusicIndex > musics.size() - 1) {
			currentMusicIndex = 0;
		}
		pausePostion = 0;
		play();
	}

	// �㲥��������ר�Ž���Activity�����Ĺ㲥
	class MyActivityRecevier extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// ���չ㲥
			String action = intent.getAction();
			if ("CurrentMusicIndex".equals(action)) {
				// ȡ��Я���ĸ����±�
				currentMusicIndex = intent.getIntExtra("position", currentMusicIndex);
				pausePostion = 0;
				// ���Ÿ���
				play();
			} else if ("PlayMusicPrevious".equals(action)) {
				// ������һ��
				previous();
			} else if ("PlayMusicNext".equals(action)) {
				// ������һ��
				next();
			} else if ("MakeMusicPlayOrPause".equals(action)) {
				// ʹ���ֲ��Ż���ͣ
				if (player.isPlaying()) {
					// ��ͣ
					pause();
				} else {
					play();
				}
			} else if ("CurrentMusicPosition".equals(action)) {
				// �õ�seekBarֹͣ���λ��
				int percent = intent.getIntExtra("seekBarPosition", 0);
				pausePostion = (percent * player.getDuration()) / 100;
				// ���͹㲥
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
		// ���ע��
		unregisterReceiver(receiver);
	}

	// ���������̣߳�ÿ��һ���ӷ���һ���йظ�����ǰλ�õĹ㲥
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
