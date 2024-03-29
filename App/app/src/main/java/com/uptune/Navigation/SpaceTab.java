package com.uptune.Navigation;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.uptune.Account.Account;
import com.uptune.Catalog.Catalog;
import com.uptune.Chart.ChartsSelector;
import com.uptune.R;
import com.uptune.Used.Used;
import java.util.ArrayList;
import java.util.List;
import eu.long1.spacetablayout.SpaceTabLayout;

public class SpaceTab extends AppCompatActivity {

    SpaceTabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spacetab);

        //add the fragments you want to display in a List
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new Catalog());
        fragmentList.add(new Account());
        fragmentList.add(new ChartsSelector());
        fragmentList.add(new Used());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (SpaceTabLayout) findViewById(R.id.spaceTabLayout);
        //we need the savedInstanceState to get the position
        tabLayout.initialize(viewPager, getSupportFragmentManager(),
                fragmentList, savedInstanceState);
        tabLayout.setTabFourIcon(R.drawable.ic_recycle);
    }

    //we need the outState to save the position
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        tabLayout.saveState(outState);
        super.onSaveInstanceState(outState);
    }


}