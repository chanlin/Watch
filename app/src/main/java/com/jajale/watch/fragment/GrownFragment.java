package com.jajale.watch.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jajale.watch.R;
import com.jajale.watch.utils.L;

public class GrownFragment extends Fragment {


		public static GrownFragment newInstance() {

		Bundle args = new Bundle();

		GrownFragment fragment = new GrownFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_math, null);

		Bundle bundle=getArguments();

		L.e("guokm", bundle.getString("arg"));
//		tv.setText(bundle.getString("arg"));
		return view;
	}
}
