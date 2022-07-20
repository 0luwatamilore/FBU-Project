package com.example.news_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.news_app.fragment.ComposeFragment;
import com.example.news_app.fragment.HomeFragment;
import com.example.news_app.fragment.LibraryFragment;
import com.example.news_app.fragment.ProfileFragment;
import com.example.news_app.fragment.SearchFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class MainActivity extends AppCompatActivity {

    private final int[] icon = new int[] {R.drawable.ic_baseline_home_24, R.drawable.ic_baseline_create_24, R.drawable.ic_baseline_search_24, R.drawable.ic_baseline_person_24, R.drawable.ic_baseline_video_library_24};
    TabLayout tabLayout;
    ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new ViewPagerFragmentStateAdapter(this));
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setIcon(icon[position])).attach();
    }

    public  class ViewPagerFragmentStateAdapter extends FragmentStateAdapter {
        public ViewPagerFragmentStateAdapter(FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new HomeFragment();
                case 1:
                    return new ComposeFragment();
                case 2:
                    return new SearchFragment();
                case 3:
                    return new ProfileFragment();
                case 4:
                    return new LibraryFragment();
            }
            return new HomeFragment();
        }
        @Override
        public int getItemCount() {
            return icon.length;
        }
    }

}