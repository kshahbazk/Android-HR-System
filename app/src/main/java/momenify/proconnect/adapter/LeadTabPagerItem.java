package momenify.proconnect.adapter;

import android.support.v4.app.Fragment;

import momenify.proconnect.fragment.InOtherQueueDashboard;
import momenify.proconnect.fragment.OrionGenQueueDashboard;
import momenify.proconnect.fragment.OrionTrainingDashboard;
import momenify.proconnect.fragment.OrionLunchDashboard;
import momenify.proconnect.fragment.OrionOnSpecialProjectDashboard;
import momenify.proconnect.fragment.OrionShiftDashboard;
import momenify.proconnect.fragment.OrionSummary;

/**
 * Created by shahbazkhan on 5/22/15.
 */
public class LeadTabPagerItem {
    private final CharSequence mTitle;
    private final int position;

    private Fragment[] listFragments;
    public LeadTabPagerItem(int position, CharSequence title) {
        this.mTitle = title;
        this.position = position;

        listFragments = new Fragment[] {new OrionSummary(), new OrionGenQueueDashboard(),
                new InOtherQueueDashboard(), new OrionLunchDashboard()
                ,new OrionTrainingDashboard(),new OrionOnSpecialProjectDashboard(),
                new OrionShiftDashboard()};
    }

    public Fragment createFragment() {
        return listFragments[position];
    }

    public CharSequence getTitle() {
        return mTitle;
    }
}
