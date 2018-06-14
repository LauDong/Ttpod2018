package cn.liudong.ttpodmusic.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.liudong.ttpodmusic.R;
import cn.liudong.ttpodmusic.R.layout;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class RecommandFragment extends Fragment {

	public RecommandFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_recommand, container, false);
	}

}
