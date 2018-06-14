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
 * （注释先打/**再回车） 适配器类 数据源和item布局组合（包饺子）
 * 
 * @author 刘东
 *
 */

public class MyAdapter extends BaseAdapter {

	// （从MusicPlayActivity）拿到数据源
	private ArrayList<String> musics;
	// 拿到item布局
	private LayoutInflater inflate;// 视图扩充器（把xml文件（饺子皮）直接变为View对象）
	// 通过构造方法传递数据
	public MyAdapter(ArrayList<String> musics, Context context) {
		this.musics = musics;
		this.inflate=LayoutInflater.from(context);
	}
	//获取item个数
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
	//获取某一个item列表项
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		if (convertView==null) {
			convertView=
					inflate.inflate(cn.liudong.ttpodmusic.R.layout.layout_item, null);
			//拿到复用的item布局上的控件
			holder =new ViewHolder();
			holder.tvMusicName=(TextView) convertView.findViewById(cn.liudong.ttpodmusic.R.id.tv_item_musicname);
			//给convertView添加一个标记
			convertView.setTag(holder);
		}else {
			//如果有直接根据标记拿来用
			holder = (ViewHolder) convertView.getTag();
		}
		//把数据源设置到要显示的控件上
		holder.tvMusicName.setText(musics.get(position));
		return convertView;
	}
	//准备一个对象持有者
	class ViewHolder{
		private TextView tvMusicName;
	}
}
