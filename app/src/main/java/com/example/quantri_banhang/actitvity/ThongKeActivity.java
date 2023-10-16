package com.example.quantri_banhang.actitvity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quantri_banhang.Adapter.AdapterUser;
import com.example.quantri_banhang.Adapter.AdapterThongKe;
import com.example.quantri_banhang.DTO.BillDTO;
import com.example.quantri_banhang.DTO.UserDTO;
import com.example.quantri_banhang.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ThongKeActivity extends AppCompatActivity {
    ImageView img_lich;
    TextView tv_lich, tv_tongdoanhthu, tv_donhang, tv3, tv4;
    int tongdoanhthu;
    private List<BillDTO> billDTOList;
    private RecyclerView rcvTke;
    private AdapterThongKe mAdapterTKe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke);

        img_lich = findViewById(R.id.img_lichtke);
        tv_lich = findViewById(R.id.tv_lich);
        tv_tongdoanhthu = findViewById(R.id.tv_tongdoanhthu);
        tv_donhang = findViewById(R.id.tv_donhang);

        ImageView img_back = findViewById(R.id.img_backtke);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        img_lich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ThongKeActivity.this);
                bottomSheetDialog.setContentView(R.layout.dialog_lich_tke);

                TextView tv_ngaybatdau = bottomSheetDialog.findViewById(R.id.tv_ngaybatdau);
                TextView tv_ngayketthuc = bottomSheetDialog.findViewById(R.id.tv_ngayketthuc);
                ImageView img_done = bottomSheetDialog.findViewById(R.id.img_done);
                tv3 = bottomSheetDialog.findViewById(R.id.tv3);
                tv4 = bottomSheetDialog.findViewById(R.id.tv4);

                tv_ngaybatdau.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDialogDatePicker(tv3);

                    }
                });

                tv_ngayketthuc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDialogDatePicker(tv4);
                    }
                });

                img_done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tv_lich.setText(tv3.getText().toString() + " - " + tv4.getText().toString());
                        Log.d("Timeee", "onClick: "+ tv3.getText().toString());
                        bottomSheetDialog.dismiss();
                        getListThongKe(tv3.getText().toString().trim(), tv4.getText().toString().trim());
                    }
                });

                bottomSheetDialog.show();
            }
        });

        billDTOList = new ArrayList<>();
        rcvTke = findViewById(R.id.rcv_thongke);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvTke.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        rcvTke.addItemDecoration(dividerItemDecoration);

        billDTOList = new ArrayList<>();
        mAdapterTKe = new AdapterThongKe(billDTOList);

        rcvTke.setAdapter(mAdapterTKe);

    }

    public void getListThongKe(String ngaybatdau, String ngayketthuc) {
        tongdoanhthu = 0;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("BillProduct").orderByChild("dateBuy").startAt(ngaybatdau).endAt(ngayketthuc);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    billDTOList.clear();
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        BillDTO billDTO = issue.getValue(BillDTO.class);
                        billDTOList.add(billDTO);
                        tongdoanhthu += billDTO.getTotalPrice();
                    }

                    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                    float giatien = Float.parseFloat(String.valueOf(tongdoanhthu));
                    tv_tongdoanhthu.setText((decimalFormat.format(giatien)+" vnđ"));
                }
                Log.d("zzzzz", "doanh thu: " + tongdoanhthu);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        Query query1 = reference.child("BillProduct").orderByChild("dateBuy").startAt(ngaybatdau).endAt(ngayketthuc);
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                billDTOList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    BillDTO billDTO = dataSnapshot.getValue(BillDTO.class);

                        billDTOList.add(billDTO);

                }
                tv_donhang.setText("( "+billDTOList.size()+" đơn hàng )");
                mAdapterTKe.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ThongKeActivity.this, "Get list users faild", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void showDialogDatePicker(TextView targetTextView) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        DatePickerDialog dialog = new DatePickerDialog(
                ThongKeActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        targetTextView.setText(i2 + "-" + (i1 + 1) + "-" + i);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE)
        );

        dialog.show();
    }
}