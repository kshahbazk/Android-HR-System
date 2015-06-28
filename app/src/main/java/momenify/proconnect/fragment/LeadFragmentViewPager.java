package momenify.proconnect.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.parse.Parse;

import java.util.ArrayList;
import java.util.List;

import momenify.proconnect.adapter.LeadTabPagerItem;
import momenify.proconnect.adapter.LeadViewPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeadFragmentViewPager extends Fragment {
    private List<LeadTabPagerItem> mTabs = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTabs.add(new LeadTabPagerItem(0, "Shift Summary"));
        mTabs.add(new LeadTabPagerItem(1, "Gen Queue"));
        mTabs.add(new LeadTabPagerItem(2, "Other Queues"));
        mTabs.add(new LeadTabPagerItem(3, "Lunch"));
        mTabs.add(new LeadTabPagerItem(4, "Training/Coaching"));
        mTabs.add(new LeadTabPagerItem(5, "Special project"));
        mTabs.add(new LeadTabPagerItem(6, "On Shift"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Parse.initialize(getActivity(), "dAs3XsEolRwzDpAhf0L7ZF2kXbId851ir48Pwoe2", "5XinGADJUqC7AEpOvdDoeR3FAmGZmgfo3n8QWDB4");
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
        View rootView = inflater.inflate(momenify.proconnect.navigationviewpagerliveo.R.layout.fragment_viewpager, container, false);
        rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ViewPager mViewPager = (ViewPager) view.findViewById(momenify.proconnect.navigationviewpagerliveo.R.id.pager);
        Parse.initialize(getActivity(), "dAs3XsEolRwzDpAhf0L7ZF2kXbId851ir48Pwoe2", "5XinGADJUqC7AEpOvdDoeR3FAmGZmgfo3n8QWDB4");
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(new LeadViewPagerAdapter(getChildFragmentManager(), mTabs));

        PagerSlidingTabStrip mSlidingTabLayout = (PagerSlidingTabStrip) view.findViewById(momenify.proconnect.navigationviewpagerliveo.R.id.tabs);
        mSlidingTabLayout.setTextColorResource(momenify.proconnect.navigationviewpagerliveo.R.color.nliveo_white);
        mSlidingTabLayout.setViewPager(mViewPager);
    }




}
