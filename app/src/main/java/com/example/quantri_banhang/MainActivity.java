package com.example.quantri_banhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;
import com.example.quantri_banhang.fragment.fragment_trangchu;
import com.example.quantri_banhang.fragment.fragment_taikhoan;
import com.example.quantri_banhang.fragment.fragment_loai;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //code giao diện
        fragment = getSupportFragmentManager();
        fragment_trangchu trangChuFragment = new fragment_trangchu();

        fragment.beginTransaction().add(R.id.id_frame, trangChuFragment).commit();

        BottomNavigationView bottomNavigationView;
        bottomNavigationView = findViewById(R.id.id_bottom_nav);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_trangchu){
                    replaceFragment(fragment_trangchu.newInstance());
                }else if (id == R.id.nav_taikhoan){
                    replaceFragment(fragment_taikhoan.newInstance());
                }else if (id == R.id.nav_loai){
                    replaceFragment(fragment_loai.newInstance());
                }
                return true;
            }
        });
    }
    public void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.id_frame,fragment);
        transaction.commit();
    }
}