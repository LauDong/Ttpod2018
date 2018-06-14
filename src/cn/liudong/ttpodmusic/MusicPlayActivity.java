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

	// �����ؼ�
	private ListView lv = null;
	/* ������ */
	// ����Դ
	ArrayList<String> musics = null;
	// ��ȡ��������SD��·��
	private File dir = null;
	private BroadcastReceiver receiver;
	// �����������Ż���ͣ��ť
	private ImageButton imbPlay;
	// ��һ�װ�ť
	private ImageButton imbPrevious;
	// ��һ�װ�ť
	private ImageButton imbNext;
	// ��������
	private TextView tvMusicName;
	// ������ʱ��
	private TextView tvTotalTime;
	// ��ǰʱ��
	private TextView tvCurrentTime;
	// ������
	private SeekBar sb;
	// ���ؼ�
	private ImageView imgMPABack;
	// �໬�˵�
	private ListView lvDrawer;
	// �໬�˵���ť
	private ImageView imgDrawer;
	// ���벼��
	private DrawerLayout drawer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music_play);
		setupView();
		// ��Ӽ�����
		addListener();
		receiver = new MyServiceReceiver();
		// �㲥������
		IntentFilter filter = new IntentFilter();
		filter.addAction("setImagePause");
		filter.addAction("setImgPlay");
		filter.addAction("MusicName&MusicTotalTime");
		filter.addAction("UpdateProgress");
		filter.addAction("seekToPausePosition");

		// ע��㲥������
		registerReceiver(receiver, filter);
	}

	private void addListener() {

		lvDrawer.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// ���ܵ���һ�����رճ���
				drawer.closeDrawer(lvDrawer);
				switch (position) {
				case 2:
					// ʵ�ֻ���
					drawer.setBackgroundResource(bgs[getThemeBg()]);
					// ��̬index
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
				// ���Ϸ�ֹͣλ�ò���
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
				// ���͹㲥����Service�����Ż���ͣ
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
				// ���͹㲥��Service������һ�׸�
				Intent intent = new Intent("PlayMusicPrevious");
				sendBroadcast(intent);
			}
		});
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// ���͹㲥,Я����ǰ�����и������±�
				Intent intent = new Intent("CurrentMusicIndex");
				// Я�����ݣ����ͳ�ȥ
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
		// ���еĸ����ļ�
		// �о������е��ļ�
		File[] files = dir.listFiles();
		// ������
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				musics.add(files[i].getName());
			}
		}
		// �����¹�
		// ����
		MyAdapter adapter = new MyAdapter(musics, this);
		lv.setAdapter(adapter);
		// ��������ȥִ�к�ʱ����
		Intent intent = new Intent(MusicPlayActivity.this, MusicPlayService.class);
		// Я������
		intent.putStringArrayListExtra("musics", musics);
		startService(intent);

		// �໬�˵�����Դ
		String[] strDrawer = { "��¼", "����ģʽ", "����Ƥ��", "����", "����", "�˳���¼" };
		// Adater������
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1,
				strDrawer);
		lvDrawer.setAdapter(adapter2);
		// ���ñ���
		drawer.setBackgroundResource(bgs[index]);
		lvDrawer.setBackgroundResource(bgs[index]);
	}

	// �㲥��������ר�Ž���Service�����Ĺ㲥
	class MyServiceReceiver extends BroadcastReceiver {

		int totalTime = 0;

		@Override
		public void onReceive(Context context, Intent intent) {
			// �õ����й��˵��Ĺ㲥
			String action = intent.getAction();
			if ("setImagePause".equals(action)) {
				// �ı䰴ťͼ��
				imbPlay.setImageResource(android.R.drawable.ic_media_pause);
			} else if ("setImgPlay".equals(action)) {
				// �ı�ͼ��Ϊ����
				imbPlay.setImageResource(android.R.drawable.ic_media_play);
			} else if ("MusicName&MusicTotalTime".equals(action)) {
				// ��ȡ����(��ʱ��intentΪ�������Ĳ����Ǹ�)
				String musicName = intent.getStringExtra("MusicName");
				// addFunc
				String[] onlyName = musicName.split(".mp3");
				tvMusicName.setText("���ڲ��ţ�" + onlyName[0]);
				// ��ȡ������ʱ��(Ĭ��ֵ��Ϊ1���������Ϊ0)
				totalTime = intent.getIntExtra("MusicTotalTime", 1);
				String strTotalTime = Global.setTime(totalTime);
				tvTotalTime.setText(strTotalTime);
			} else if ("UpdateProgress".equals(action)) {
				// ÿ��һ�����ʱ�������
				int currentTime = intent.getIntExtra("currentPosition", 0);
				// �Ѻ���ֵת��Ϊ��ʽ�ַ���mm:ss
				String strCurrentTime = Global.setTime(currentTime);
				// ��ʾʱ��
				tvCurrentTime.setText(strCurrentTime);
				// ���ý�����
				// ��ȡ������
				if (totalTime != 0) {
					int percent = currentTime * 100 / totalTime;
					// ����seekbar����
					sb.setProgress(percent);
				}

			} else if ("seekToPausePosition".equals(action)) {
				// �������õ�ǰʱ��
				int currenttime = intent.getIntExtra("seekPosition", 0);
				String strCurrentTime = Global.setTime(currenttime);
				tvCurrentTime.setText(strCurrentTime);
			}
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// ���ע��
		unregisterReceiver(receiver);
	}

	// ����±�(��̬����)
	private static int index;
	// �������ͼƬ��Դ
	int[] bgs = { R.drawable.bg1, R.drawable.bg2, R.drawable.bg3, R.drawable.bg4, R.drawable.bg5 };

	// ���������(���Ƥ���±�)
	public int getThemeBg() {
		Random random = new Random();
		index = random.nextInt(bgs.length);
		// ���͹㲥
		Intent intent = new Intent("ThemeBg");
		intent.putExtra("index", index);
		sendBroadcast(intent);
		return index;
	}
}
