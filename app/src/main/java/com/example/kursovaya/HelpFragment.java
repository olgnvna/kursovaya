package com.example.kursovaya;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;

public class HelpFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_help, container, false);

        TabLayout tabLayout = rootView.findViewById(R.id.tab_layout);
        ViewPager viewPager = rootView.findViewById(R.id.view_pager);

        TabAdapter adapter = new TabAdapter(getChildFragmentManager());

        adapter.addFragment(new ProgramInfoFragment(), "Информация");
        adapter.addFragment(new InstructionFragment(), "Инструкция");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        return rootView;
    }
}