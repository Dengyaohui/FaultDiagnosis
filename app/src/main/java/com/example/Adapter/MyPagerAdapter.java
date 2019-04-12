package com.example.Adapter;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.List;

/**
 * ViewPager滑动页的适配器
 * @author Administrator
 *这个ViewPager想干嘛？
 */
public class MyPagerAdapter extends PagerAdapter{

	private List<View> listViews=null;
	
	public MyPagerAdapter(List<View> listViews){
		this.listViews=listViews;
	}
	
	/*确定有多少个切换页*/
	@Override
	public int getCount() {
		return listViews.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0==(arg1);
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView(listViews.get(position));
	}

	@Override
	public void finishUpdate(View container) {
		super.finishUpdate(container);
	}

	/*给每个item设置view*/
	@Override
	public Object instantiateItem(View container, int position) {
		((ViewPager) container).addView(listViews.get(position), 0);
	        return listViews.get(position);
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
		super.restoreState(state, loader);
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View container) {
		super.startUpdate(container);
	}

	
}
