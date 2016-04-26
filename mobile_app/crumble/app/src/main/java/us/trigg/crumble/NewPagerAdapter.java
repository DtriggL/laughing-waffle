package us.trigg.crumble;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import us.trigg.crumble.fragments.TabFragment1;
import us.trigg.crumble.fragments.TabFragment2;

public class NewPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public NewPagerAdapter(android.support.v4.app.FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new TabFragment1();
            case 1:
                return new TabFragment2();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
