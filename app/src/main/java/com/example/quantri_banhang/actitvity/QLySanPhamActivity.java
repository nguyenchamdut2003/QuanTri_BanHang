package com.example.quantri_banhang.actitvity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.quantri_banhang.R;

public class QLySanPhamActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qly_san_pham);

        recyclerView=findViewById(R.id.rcv_pro);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }
}