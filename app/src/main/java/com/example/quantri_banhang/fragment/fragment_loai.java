package com.example.quantri_banhang.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.quantri_banhang.Adapter.AdapterCategory;
import com.example.quantri_banhang.DTO.CategoryDTO;
import com.example.quantri_banhang.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class fragment_loai extends Fragment {
    View mview;
    private ListView listView;
    private AdapterCategory categoryadapter;

    private ArrayList<CategoryDTO> mlistCategory;
    public fragment_loai() {
        // Required empty public constructor
    }
    public static fragment_loai newInstance() {
        fragment_loai fragment = new fragment_loai();

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewok = inflater.inflate(R.layout.fragment_loai, container, false);
        //Lấy danh sách loại
        listView = viewok.findViewById(R.id.lv_loai);
        mlistCategory = new ArrayList<>();
        categoryadapter = new AdapterCategory(mlistCategory, getActivity());
        listView.setAdapter(categoryadapter);
        getListCategory();

        ImageButton btnAdd = viewok.findViewById(R.id.imgbtn_themloai);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addcategory();
            }
        });
        return viewok;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void addcategory(){
        final Dialog dialog1 = new Dialog(getActivity());
        dialog1.setContentView(R.layout.dialog_addcategory);
        dialog1.setCancelable(false);

        Window window = dialog1.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (dialog1 != null && dialog1.getWindow() != null) {
            dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        }

        EditText edname = dialog1.findViewById(R.id.ed_name);
        Button btnadd = dialog1.findViewById(R.id.btn_them);
        Button btnhuy = dialog1.findViewById(R.id.btn_huy);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UUID uuid = UUID.randomUUID();
                String dataName = edname.getText().toString().trim();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRefId = database.getReference("category/"+uuid.toString().trim());
                CategoryDTO categoryDTO = new CategoryDTO(uuid.toString().trim(), dataName);
                myRefId.setValue(categoryDTO, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(getActivity(), "Thêm loại thành công", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog1.dismiss();
            }
        });
        btnhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });


        dialog1.show();
    }
    public void getListCategory(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefId = database.getReference("category");
        myRefId.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    CategoryDTO categoryDTO = dataSnapshot.getValue(CategoryDTO.class);
                    mlistCategory.add(categoryDTO);
                }
                categoryadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
