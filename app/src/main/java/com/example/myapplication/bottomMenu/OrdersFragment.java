//package com.example.myapplication.bottomMenu;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.fragment.app.Fragment;
//import androidx.viewpager.widget.ViewPager;
//
//import com.example.myapplication.R;
//import com.google.android.material.tabs.TabLayout;
//
//public class OrdersFragment extends Fragment {
//    View rootView;
//
//    TabLayout tabLayout;
//    ViewPager viewPager;
//    PagerAdapterOrders pagerAdapterOrders;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        rootView = inflater.inflate(R.layout.fragment_orders, container, false);
//
//        initComponents();
//        componentEvents();
//        setupTabLayout();
//        return rootView;
//    }
//
//    private void initComponents() {
//        tabLayout = rootView.findViewById(R.id.tabLayout);
//        viewPager = rootView.findViewById(R.id.viewPager);
//    }
//
//    private void componentEvents() {
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tabLayout.getSelectedTabPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//            }
//        });
//    }
//
//    private void setupTabLayout() {
//        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.past_orders)));
//        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.upcoming_orders)));
//
//        pagerAdapterOrders = new PagerAdapterOrders(getFragmentManager(), tabLayout.getTabCount());
//        viewPager.setOffscreenPageLimit(2);
//        viewPager.setAdapter(pagerAdapterOrders);
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//    }
//}
