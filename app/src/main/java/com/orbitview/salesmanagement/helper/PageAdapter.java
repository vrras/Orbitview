package com.orbitview.salesmanagement.helper;
import com.orbitview.salesmanagement.view.*;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
/**
 * Created by hamnaro on 05/11/18.
 */

public class PageAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PageAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                AttendanceFragment tab1 = new AttendanceFragment();
                return tab1;
            case 1:
                ReportFragment tab2 = new ReportFragment();
                return tab2;
            case 2:
                MyProfileFragment tab3 = new MyProfileFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
