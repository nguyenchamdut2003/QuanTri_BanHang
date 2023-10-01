package com.example.quantri_banhang.actitvity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quantri_banhang.Adapter.AdapterCategory;
import com.example.quantri_banhang.DTO.CategoryDTO;
import com.example.quantri_banhang.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Category_Activity extends AppCompatActivity implements AdapterCategory.OnClick{
    String TAG = "categoryactivity";
    View mview;
    private ListView listView;
    ImageView btnback;
    private AdapterCategory categoryadapter;

    private ArrayList<CategoryDTO> mlistCategory;
    public void Anhxa(){
        btnback = findViewById(R.id.id_back);
        listView = findViewById(R.id.lv_loai);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Anhxa();
        //Lấy danh sách loại
        mlistCategory = new ArrayList<>();
        categoryadapter = new AdapterCategory(mlistCategory, this, this);
        listView.setAdapter(categoryadapter);
        getListCategory();

        ImageButton btnAdd = findViewById(R.id.imgbtn_themloai);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addcategory();
            }
        });
    }
    public void addcategory(){
        final Dialog dialog1 = new Dialog(this);
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
                        Toast.makeText(Category_Activity.this, "Thêm loại thành công", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClickUpdate(CategoryDTO categoryDTO) {
        final Dialog dialog1 = new Dialog(this);
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
        TextView tv_tieude = dialog1.findViewById(R.id.tv_title);
        btnadd.setText("Sửa");
        tv_tieude.setText("Updete Category");
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dataName = edname.getText().toString().trim();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                Log.d(TAG, "onClick: " + categoryDTO.getIdCate());
                DatabaseReference myRefId = database.getReference("category/" + categoryDTO.getIdCate());
                Map<String, Object> map = new HashMap<>();
                map.put("name", dataName);

                myRefId.updateChildren(map, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(Category_Activity.this, "Update thành công", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClickDelete(CategoryDTO categoryDTO) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa loại")
                .setMessage("Bản có chắc chắc muốn xóa bản ghi này không ?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        Log.d(TAG, "onClick: " + categoryDTO.getIdCate());
                        DatabaseReference myRefId = database.getReference("category/" + categoryDTO.getIdCate());
                        myRefId.removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(Category_Activity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}