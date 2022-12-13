package uk.ac.tees.w9585141.blooplus.home;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import uk.ac.tees.w9585141.blooplus.home.fragments.DateFragment;
import uk.ac.tees.w9585141.blooplus.home.fragments.DoctorListFragment;
import uk.ac.tees.w9585141.blooplus.home.fragments.SpecializeFragment;

public class pagerAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public pagerAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    public pagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
//            case 0:
//                SpecializeFragment specializeFragment = new SpecializeFragment();
//                return specializeFragment;

            case 0:
                DoctorListFragment doctorListFragment = new DoctorListFragment();
                return doctorListFragment;

            case 1:
                DateFragment dateFragment = new DateFragment();
                return dateFragment;

            default:
                return null;

        }
    }

    @Override
    public int getCount() { return totalTabs; }

        public CharSequence getPageTitle(int position) {
            switch (position){
//                case 0:
//                    return "SPECIALIZATION";
                case 0:
                    return "DOCTOR";
                case 1:
                    return "DATE";

                default:
                    return null;
            }
        }

    }

