package momenify.proconnect.adapter;

import android.support.v4.app.Fragment;

import momenify.proconnect.fragment.InOtherQueueDashboard;
import momenify.proconnect.fragment.OrionDffQueueDashboard;
import momenify.proconnect.fragment.OrionGenQueueDashboard;
import momenify.proconnect.fragment.OrionTrainingDashboard;
import momenify.proconnect.fragment.OrionLunchDashboard;
import momenify.proconnect.fragment.OrionOnSpecialProjectDashboard;
import momenify.proconnect.fragment.OrionShiftDashboard;

public class TabPagerItem {
	
	private final CharSequence mTitle;
    private final int position;
        
    private Fragment[] listFragments;
    public TabPagerItem(int position, CharSequence title) {
        this.mTitle = title;
        this.position = position;

        listFragments = new Fragment[] {new OrionShiftDashboard(), new OrionGenQueueDashboard(),
                new InOtherQueueDashboard(), new OrionLunchDashboard()
                ,new OrionTrainingDashboard(),new OrionOnSpecialProjectDashboard()};
    }

    public Fragment createFragment() {
		return listFragments[position];
    }

    public CharSequence getTitle() {
        return mTitle;
    }
}
