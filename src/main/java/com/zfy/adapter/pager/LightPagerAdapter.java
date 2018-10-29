package com.zfy.adapter.pager;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zfy.adapter.LightHolder;
import com.zfy.adapter.VALUE;

import java.util.List;

/**
 * CreateAt : 2018/3/4
 * Describe :
 *
 * @author chendong
 */
public abstract class LightPagerAdapter<D> extends PagerAdapter {

    private List<D> mDatas;
    private int mItemLayout;

    public LightPagerAdapter(List<D> datas, int itemLayout) {
        mDatas = datas;
        mItemLayout = itemLayout;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(mItemLayout, container, false);
        container.addView(view);
        LightHolder holder = new LightHolder(null, VALUE.NONE, view);
        onBindView(holder, mDatas.get(position));
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public abstract void onBindView(LightHolder holder, D data);

}
