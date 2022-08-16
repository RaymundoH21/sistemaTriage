package com.example.sistematriage;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.sistematriage.fragments.DatosPersonalesFragment;
import com.example.sistematriage.fragments.TriageFragment;

public class MyViewPagerAdapter extends FragmentStateAdapter{

    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new TriageFragment();
            case 1:
                return new DatosPersonalesFragment();
            default:
                return new TriageFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
