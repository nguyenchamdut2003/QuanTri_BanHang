package com.example.quantri_banhang.fragment;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.quantri_banhang.DTO.BillDTO;
import com.example.quantri_banhang.Package_Bill.Activity.Hoanthanhdon_Activity;
import com.example.quantri_banhang.Package_Bill.Adapter.Bill_Adapter;
import com.example.quantri_banhang.R;
import com.example.quantri_banhang.actitvity.LoginActivity;
import com.example.quantri_banhang.actitvity.QLySanPhamActivity;
import com.example.quantri_banhang.actitvity.QLyUserActivity;
import com.example.quantri_banhang.actitvity.ThongKeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class fragment_trangchu extends Fragment {

    private FirebaseAuth auth;
    View view;
    TextView tv_tenUser, tv_xemchitiet;
    TextView tv_tongdoanhthu, tv_donhang, tv_dagiao, tv_chuagiao;

    ArrayList<BillDTO> list;
    Bill_Adapter adapter;
    public fragment_trangchu() {
        // Required empty public constructor
    }

    public static fragment_trangchu newInstance() {
        fragment_trangchu fragment = new fragment_trangchu();

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewok = inflater.inflate(R.layout.fragment_trangchu, container, false);
        ImageView img_qlsp = viewok.findViewById(R.id.img_qlsp);
        tv_tenUser = viewok.findViewById(R.id.tv_tenUser);
        ImageView img_qluser = viewok.findViewById(R.id.img_qlUser);
        tv_xemchitiet = viewok.findViewById(R.id.tv_xemchitiet);

        tv_tongdoanhthu = viewok.findViewById(R.id.tv_doanhthu);
        tv_donhang = viewok.findViewById(R.id.tv_donhang);
        tv_dagiao = viewok.findViewById(R.id.tv_dagiao);
        tv_chuagiao = viewok.findViewById(R.id.tv_chuagiao);

        getDataDaGiao();

        getDataTongDoanhThu();

        getDataChuaGiao();

        list = new ArrayList<>();
        adapter = new Bill_Adapter(getContext(), list);


        auth = FirebaseAuth.getInstance();
        checkUser();
        img_qlsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), QLySanPhamActivity.class));
            }
        });
        img_qluser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), QLyUserActivity.class));
            }
        });

        tv_xemchitiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ThongKeActivity.class);
                startActivity(intent);
            }
        });
        return viewok;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


    private void checkUser() {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }else{
            String name = user.getDisplayName();
            if (name == null){
                tv_tenUser.setText("");
            }else{
                tv_tenUser.setText(""+name);
            }
            Log.e("zzz", "checkUser: "+ name);
        }
    }

    public void getDataDaGiao(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefId = database.getReference("BillProduct");
        myRefId.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    BillDTO billDTO = dataSnapshot.getValue(BillDTO.class);
                    if (billDTO.getStatus() == 4){
                        list.add(billDTO);
                    }
                }
                tv_dagiao.setText(list.size()+"");
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "err: " + error);
            }
        });
    }

    public void getDataChuaGiao(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefId = database.getReference("BillProduct");
        myRefId.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    BillDTO billDTO = dataSnapshot.getValue(BillDTO.class);
                    if (billDTO.getStatus() == 2){
                        list.add(billDTO);
                    }
                }
                adapter.notifyDataSetChanged();
                tv_chuagiao.setText(list.size()+"");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "err: " + error);
            }
        });
    }

    public void getDataTongDoanhThu(){
        final float[] tongdoanhthu = {0};

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("BillProduct");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                        list.clear();
                    for (DataSnapshot issue : snapshot.getChildren()) {

                        BillDTO billDTO = issue.getValue(BillDTO.class);
                        list.add(billDTO);
                        tongdoanhthu[0] += billDTO.getTotalPrice();
                    }

                    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                    float giatien = Float.parseFloat(String.valueOf(tongdoanhthu[0]));
                    tv_tongdoanhthu.setText((decimalFormat.format(giatien)+" vnđ"));
                    tv_donhang.setText(list.size()+"");
                }
                Log.d("zzzzz", "doanh thu: " + tongdoanhthu[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
