package com.geziuygulamasi.All_Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.geziuygulamasi.All_Activities.MainActivity;
import com.geziuygulamasi.All_Utils.Fragment.OnBackPressListener;
import com.geziuygulamasi.All_Utils.Fragment.RootFragment;
import com.geziuygulamasi.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MainFragment extends RootFragment {
    @SuppressLint("StaticFieldLeak")
    private static View view;
    private static ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private BottomNavigationView bottomNavigationView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        initView();
        return view;
    }

    private void initView(){
        viewPager = view.findViewById(R.id.viewPager);
        bottomNavigationView = view.findViewById(R.id.bottom_navigation);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.getMenu().findItem(position == 0 ? R.id.categories : R.id.favorites).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPagerAdapter = new ViewPagerAdapter(getActivity(), getChildFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            changeMenu(item.getItemId() == R.id.categories ? 0 : 1);
            return false;
        });
    }

    private static void changeMenu(int position){
        MainActivity.backPressAction();
        viewPager.setCurrentItem(position, false);
    }

    public boolean onBackPressed() {
        OnBackPressListener currentFragment = (OnBackPressListener) viewPagerAdapter.getRegisteredFragment(viewPager.getCurrentItem());
        if (currentFragment != null)
            return currentFragment.onBackPressed();
        return false;
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final Activity activity;
        private final SparseArray<Fragment> registeredFragments = new SparseArray<>();

        ViewPagerAdapter(Activity activity, FragmentManager fm) {
            super(fm);
            this.activity = activity;
        }

        @NotNull
        @Override
        public Fragment getItem(int position) {
            final Fragment result;
            switch (position) {
                case 0:
                    result = new CategoryFragment();
                    break;
                case 1:
                    result = new FavoriteFragment();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + position);
            }

            return Objects.requireNonNull(result);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return activity.getString(position == 0 ? R.string.categories : R.string.favorites);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @NotNull
        @Override
        public Object instantiateItem(@NotNull ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(@NotNull ViewGroup container, int position, @NotNull Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }
    }
}
