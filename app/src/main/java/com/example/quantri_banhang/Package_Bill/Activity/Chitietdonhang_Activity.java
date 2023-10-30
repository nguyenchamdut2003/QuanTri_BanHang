package com.example.quantri_banhang.Package_Bill.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quantri_banhang.DTO.CartOrderDTO;
import com.example.quantri_banhang.Package_Bill.Adapter.Chitietdonhang_Adapter;
import com.example.quantri_banhang.R;
import com.example.quantri_banhang.actitvity.Category_Activity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Chitietdonhang_Activity extends AppCompatActivity {
    String TAG = "ChitietdonhangActivity";
    ArrayList<CartOrderDTO> list;
    Chitietdonhang_Adapter adapter;
    ImageView id_back;
    ListView lvhoadon;
    String idbill_hoadon, id_userbill;
    int status;

    int upstatus;
    Button btnhuydonhang;
    public void Anhxa(){
        lvhoadon = findViewById(R.id.lv_hoadon);
        id_back = findViewById(R.id.id_back);
        btnhuydonhang = findViewById(R.id.btn_huydonhang);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitietdonhang);
        Anhxa();
        id_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        
        Intent intent = getIntent();
        idbill_hoadon = intent.getStringExtra("id_bill");
        status = intent.getIntExtra("status", 1);
        id_userbill = intent.getStringExtra("");
        Log.d(TAG, "id_bill: " + status);
        list = new ArrayList<>();
        adapter = new Chitietdonhang_Adapter(Chitietdonhang_Activity.this, list);
        lvhoadon.setAdapter(adapter);

        if (status == 3){
            upstatus = 4;
            btnhuydonhang.setText("Đơn hàng vận chuyển xong");
        }else if (status == 4){
            btnhuydonhang.setVisibility(View.GONE);
        } else if (status == 1) {
            upstatus = 2;
            btnhuydonhang.setText("Xác nhận đơn");
        } else if (status == 2) {
            upstatus = 3;
            btnhuydonhang.setText("Xác nhận đóng gói xong");
        }
        btnhuydonhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xacnhan();
            }
        });
        getdata();
    }
    public void getdata(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefId = database.getReference("CartOrder");
        myRefId.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    CartOrderDTO cartOrderDTO = dataSnapshot.getValue(CartOrderDTO.class);
                    if (idbill_hoadon.equals(cartOrderDTO.getIdBill())){
                        list.add(cartOrderDTO);
                    }
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "err: " + error);
            }
        });
    }

    public void xacnhan(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefId = database.getReference("BillProduct/" + idbill_hoadon);
        Map<String, Object> map = new HashMap<>();
        map.put("status", upstatus);

        myRefId.updateChildren(map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(Chitietdonhang_Activity.this, "Đã xác nhận", Toast.LENGTH_SHORT).show();
            }
        });
    }
}