package com.jajale.watch.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jajale.watch.R;

public class LookPicFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_math, null);

		Bundle bundle=getArguments();
//		tv.setText(bundle.getString("arg"));
		return view;
	}
}
