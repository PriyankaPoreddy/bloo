package uk.ac.tees.w9585141.blooplus.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import uk.ac.tees.w9585141.blooplus.home.fragments.DateFragment;
import uk.ac.tees.w9585141.blooplus.home.fragments.DoctorListFragment;
import uk.ac.tees.w9585141.blooplus.home.fragments.SpecializeFragment;

public class pagerAdapter extends FragmentPagerAdapter {

    public pagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                SpecializeFragment specializeFragment = new SpecializeFragment();
                return specializeFragment;

            case 1:
                DoctorListFragment doctorListFragment = new DoctorListFragment();
                return doctorListFragment;

            case 2:
                DateFragment dateFragment = new DateFragment();
                return dateFragment;

            default:
                return null;

        }
    }

    @Override
    public int getCount() { return 0;}

        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "SPECIALIZATION";
                case 1:
                    return "DOCTOR";
                case 2:
                    return "DATE";

                default:
                    return null;
            }
        }

    }

