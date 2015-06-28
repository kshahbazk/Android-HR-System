package momenify.proconnect.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by shahbazkhan on 5/22/15.
 */
public class LeadViewPagerAdapter extends FragmentPagerAdapter {
    private List<LeadTabPagerItem> mTabs;

    public LeadViewPagerAdapter(FragmentManager fragmentManager, List<LeadTabPagerItem> mTabs) {
        super(fragmentManager);
        this.mTabs = mTabs;
    }

    @Override
    public Fragment getItem(int i) {
        return mTabs.get(i).createFragment();
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabs.get(position).getTitle();
    }
}
