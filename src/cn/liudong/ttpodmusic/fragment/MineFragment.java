package cn.liudong.ttpodmusic.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import cn.liudong.ttpodmusic.MusicPlayActivity;
import cn.liudong.ttpodmusic.R;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class MineFragment extends Fragment {

	// ����Ϊ��Ա����
	View view = null;
	// �����ؼ�
	private RelativeLayout rl1One, rl1Two, rl1Three, rl2One, rl2Two, rl2Three, rl3One, rl3Two, rl3Three, rl4One, rl4Two,
			rl4Three;

	public MineFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		view = inflater.inflate(R.layout.fragment_mine, container, false);
		// ��ʼ��
		setupView();
		// ��ȡ�ֻ���Ļ�Ŀ��
		WindowManager wm = getActivity().getWindowManager();
		int width = wm.getDefaultDisplay().getWidth();

		// ����һ�еĵ�һ�����ÿ�Ⱥ͸߶�
		LayoutParams lp1O = rl1One.getLayoutParams();
		lp1O.width = width / 3 - 2;
		lp1O.height = width / 3 - 2;
		// ����һ�еĵڶ������ÿ�Ⱥ͸߶�
		LayoutParams lp1T = rl1Two.getLayoutParams();
		lp1T.width = width / 3 - 2;
		lp1T.height = width / 3 - 2;
		// ����һ�еĵ��������ÿ�Ⱥ͸߶�
		LayoutParams lp1S = rl1Three.getLayoutParams();
		lp1S.width = width / 3 - 2;
		lp1S.height = width / 3 - 2;

		// ���ڶ��еĵ�һ�����ÿ�Ⱥ͸߶�
		LayoutParams lp2O = rl2One.getLayoutParams();
		lp2O.width = width / 3 - 2;
		lp2O.height = width / 3 - 2;
		// ���ڶ��еĵڶ������ÿ�Ⱥ͸߶�
		LayoutParams lp2T = rl2Two.getLayoutParams();
		lp2T.width = width / 3 - 2;
		lp2T.height = width / 3 - 2;
		// ���ڶ��еĵ��������ÿ�Ⱥ͸߶�
		LayoutParams lp2S = rl2Three.getLayoutParams();
		lp2S.width = width / 3 - 2;
		lp2S.height = width / 3 - 2;

		// �������еĵ�һ�����ÿ�Ⱥ͸߶�
		LayoutParams lp3O = rl3One.getLayoutParams();
		lp3O.width = width / 3 - 2;
		lp3O.height = width / 3 - 2;
		// �������еĵڶ������ÿ�Ⱥ͸߶�
		LayoutParams lp3T = rl3Two.getLayoutParams();
		lp3T.width = width / 3 - 2;
		lp3T.height = width / 3 - 2;
		// �������еĵ��������ÿ�Ⱥ͸߶�
		LayoutParams lp3S = rl3Three.getLayoutParams();
		lp3S.width = width / 3 - 2;
		lp3S.height = width / 3 - 2;

		// �������еĵ�һ�����ÿ�Ⱥ͸߶�
		LayoutParams lp4O = rl4One.getLayoutParams();
		lp4O.width = width / 3 - 2;
		lp4O.height = width / 3 - 2;
		// �������еĵڶ������ÿ�Ⱥ͸߶�
		LayoutParams lp4T = rl4Two.getLayoutParams();
		lp4T.width = width / 3 - 2;
		lp4T.height = width / 3 - 2;
		// �������еĵ��������ÿ�Ⱥ͸߶�
		LayoutParams lp4S = rl4Three.getLayoutParams();
		lp4S.width = width / 3 - 2;
		lp4S.height = width / 3 - 2;

		return view;
	}

	private void setupView() {
		rl1One = (RelativeLayout) view.findViewById(R.id.rl_one_one);
		rl1Two = (RelativeLayout) view.findViewById(R.id.rl_one_two);
		rl1Three = (RelativeLayout) view.findViewById(R.id.rl_one_three);

		rl2One = (RelativeLayout) view.findViewById(R.id.rl_two_one);
		rl2Two = (RelativeLayout) view.findViewById(R.id.rl_two_two);
		rl2Three = (RelativeLayout) view.findViewById(R.id.rl_two_three);

		rl3One = (RelativeLayout) view.findViewById(R.id.rl_three_one);
		rl3Two = (RelativeLayout) view.findViewById(R.id.rl_three_two);
		rl3Three = (RelativeLayout) view.findViewById(R.id.rl_three_three);

		rl4One = (RelativeLayout) view.findViewById(R.id.rl_four_one);
		rl4Two = (RelativeLayout) view.findViewById(R.id.rl_four_two);
		rl4Three = (RelativeLayout) view.findViewById(R.id.rl_four_three);

		// ���ҵ�������ӵ���¼�
		rl1One.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), MusicPlayActivity.class);
				startActivity(intent);

			}
		});
	}

}
