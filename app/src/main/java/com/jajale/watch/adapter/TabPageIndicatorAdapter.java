package com.jajale.watch.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.jajale.watch.fragment.QADetailsFragment;

public class TabPageIndicatorAdapter extends FragmentPagerAdapter {

	private  String[] title;
	private String[] args;
	private QADetailsFragment[] fragments;
	public TabPageIndicatorAdapter(FragmentManager fm, String[] title, String[] args) {
		super(fm);
		this.title = title;
		this.args=args;
		fragments = new QADetailsFragment[args.length];
	}
	public void play(int position) {
//		fragments[position].playOrStopMusic();
		fragments[position].playOrDownLoad();
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		QADetailsFragment fragment = (QADetailsFragment) super.instantiateItem(container, position);
		fragments[position] = fragment;
		return fragment;
	}
	@Override
	public QADetailsFragment getItem(int position) {
//		Fragment fragment=fragments.get(arg0);
		QADetailsFragment fragment=QADetailsFragment.newInstance(args[position]);
//		L.i("guokm","new le ji ci");
//		Bundle bundle=new Bundle();
//		bundle.putString("arg", args[position]);
//		fragment.setArguments(bundle);

		return fragment;
	}

	@Override
	public int getCount() {
		return title.length;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return title[position%title.length];
	}

}
