package cn.liudong.ttpodmusic.adapter;

import java.util.ArrayList;
import java.util.List;

import android.R;
import android.R.integer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * ��ע���ȴ�/**�ٻس��� �������� ����Դ��item������ϣ������ӣ�
 * 
 * @author ����
 *
 */

public class MyAdapter extends BaseAdapter {

	// ����MusicPlayActivity���õ�����Դ
	private ArrayList<String> musics;
	// �õ�item����
	private LayoutInflater inflate;// ��ͼ����������xml�ļ�������Ƥ��ֱ�ӱ�ΪView����
	// ͨ�����췽����������
	public MyAdapter(ArrayList<String> musics, Context context) {
		this.musics = musics;
		this.inflate=LayoutInflater.from(context);
	}
	//��ȡitem����
	@Override
	public int getCount() {
		return musics.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	//��ȡĳһ��item�б���
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		if (convertView==null) {
			convertView=
					inflate.inflate(cn.liudong.ttpodmusic.R.layout.layout_item, null);
			//�õ����õ�item�����ϵĿؼ�
			holder =new ViewHolder();
			holder.tvMusicName=(TextView) convertView.findViewById(cn.liudong.ttpodmusic.R.id.tv_item_musicname);
			//��convertView���һ�����
			convertView.setTag(holder);
		}else {
			//�����ֱ�Ӹ��ݱ��������
			holder = (ViewHolder) convertView.getTag();
		}
		//������Դ���õ�Ҫ��ʾ�Ŀؼ���
		holder.tvMusicName.setText(musics.get(position));
		return convertView;
	}
	//׼��һ�����������
	class ViewHolder{
		private TextView tvMusicName;
	}
}
