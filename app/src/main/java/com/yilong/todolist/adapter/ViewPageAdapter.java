package com.yilong.todolist.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ViewPageAdapter extends PagerAdapter {

    private List<Fragment> fragments = null;

    private FragmentManager manager;

    public ViewPageAdapter(FragmentManager fm) {
        manager = fm;
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }

    public Fragment getFragment(int position) {
        return fragments.get(position);
    }

    public void setFragments(List<Fragment> fragments) {
        this.fragments = fragments;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        if (position < fragments.size()) {
            container.removeView(fragments.get(position).getView());
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = fragments.get(position);
        if (!fragment.isAdded()) {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(fragment, fragment.getClass().getSimpleName());
            ft.commitAllowingStateLoss();
            manager.executePendingTransactions();
        }
        if (null == fragment.getView().getParent()) {
            container.addView(fragment.getView());
        }
        return fragment.getView();
    }

}
