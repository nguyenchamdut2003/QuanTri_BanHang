package com.example.quantri_banhang.actitvity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quantri_banhang.Adapter.Adapter_QlySanPham;
import com.example.quantri_banhang.DTO.CategoryDTO;
import com.example.quantri_banhang.DTO.DTO_QlySanPham;
import com.example.quantri_banhang.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class QLySanPhamActivity extends AppCompatActivity implements Adapter_QlySanPham.updateSanPham{

    String abc;
    private SearchView sv_search;
    private RecyclerView recyclerView;
    private Adapter_QlySanPham madapter_qlySanPham;
    private List<DTO_QlySanPham> mlist;


    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> adapter1;

    private ArrayList<String> listSpin;
    private ArrayList<String> listSpin1;
    private String nameCat;

    private FloatingActionButton fab_pro;
    private ImageView btn_back;
    private Uri filePath; // đường dẫn file
    // khai báo request code để chọn ảnh
    private final int PICK_IMAGE_REQUEST = 22;
    private FirebaseStorage storage;

    private StorageReference storageReference;
    private ImageView img_preview;
    public static final String TAG = "chuongdk";
    private String link_anh;
    private Spinner spn_filter;
    LinearLayoutManager layoutManager;
    DividerItemDecoration dividerItemDecoration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qly_san_pham);
        sv_search = findViewById(R.id.sv_search);
        sv_search.clearFocus();
        sv_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                getSanpham(s);
                return true;
            }
        });
        storage = FirebaseStorage.getInstance("gs://duanbanhangthuctap-94f71.appspot.com");
        storageReference = storage.getReference();
        initView();

        listSpin = new ArrayList<>();
        listSpin1 = new ArrayList<>();

        adapter = new ArrayAdapter<>(this, R.layout.item_spinner_add, listSpin);
        getDataCat();

        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        mlist= new ArrayList<>();
        madapter_qlySanPham = new Adapter_QlySanPham(mlist,getApplicationContext(),this);
        recyclerView.setAdapter(madapter_qlySanPham);

        adapter1 = new ArrayAdapter<>(this,R.layout.item_spinner_add,listSpin1);
        spn_filter.setAdapter(adapter1);
        spn_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (listSpin1.get(i).equals("All")){
                    getSanpham("");
                }else{
                    getDataByCat(listSpin1.get(i));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        fab_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAdd();
            }
        });

    }

    private void initView() {

        fab_pro = findViewById(R.id.fab_addPro);
        btn_back = findViewById(R.id.img_back);
        img_preview = findViewById(R.id.img_pro_add);
        spn_filter = findViewById(R.id.spn_filter);

        recyclerView= findViewById(R.id.rcv_pro);

    }
    private void getDataByCat(String value) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("Products").orderByChild("category").equalTo(value);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mlist.clear();
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        DTO_QlySanPham dto_qlySanPham = issue.getValue(DTO_QlySanPham.class);
                        mlist.add(dto_qlySanPham);
                    }
                    madapter_qlySanPham.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void showDialogAdd() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Products");

        final Dialog dialog = new Dialog(QLySanPhamActivity.this);
        dialog.setContentView(R.layout.dialog_add_pro);
        dialog.setCancelable(false);

        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        }

        EditText ed_name = dialog.findViewById(R.id.ed_name_add);
        EditText ed_des = dialog.findViewById(R.id.ed_des_add);
        EditText ed_price = dialog.findViewById(R.id.ed_price_add);
        EditText ed_number = dialog.findViewById(R.id.ed_number_add);
        TextView btn_add = dialog.findViewById(R.id.dialog_btn_add);
        TextView btn_huy = dialog.findViewById(R.id.btn_huy);
        img_preview = dialog.findViewById(R.id.img_pro_add);
        Spinner spn_cat = dialog.findViewById(R.id.spn_cat);





        spn_cat.setAdapter(adapter);

        spn_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                nameCat = listSpin.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        img_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();

            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namePro = ed_name.getText().toString();
                String des = ed_des.getText().toString();
                String price = ed_price.getText().toString();

                String id = myRef.push().getKey();
                if (namePro.isEmpty()){
                    ed_name.setError("Không được để trống!!");
                }else if (des.isEmpty()){
                    ed_des.setError("Không được để trống!!");
                } else if (price.isEmpty()) {
                    ed_price.setError("Không được để trống!!");
                } else if (ed_number.getText().toString().isEmpty()) {
                    ed_number.setError("Không được để trống!!");
                } else if (!ed_price.getText().toString().matches("\\d+(?:\\.\\d+)?")) {
                    ed_price.setError("Giá tiền phải là số!!");
                } else if (!ed_number.getText().toString().matches("\\d+(?:\\.\\d+)?")) {
                    ed_number.setError("Số lượng phải là số!!");
                }else{
                    int number = Integer.parseInt(ed_number.getText().toString());
                    DTO_QlySanPham sanPham = new DTO_QlySanPham(id,link_anh,namePro,price,des,nameCat,number);


                    myRef.child(id).setValue(sanPham, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            Toast.makeText(QLySanPhamActivity.this, "Add Product Success!", Toast.LENGTH_SHORT).show();
                        }
                    });


                    dialog.dismiss();
                }

            }
        });

        btn_huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }



    private void getSanpham(String key){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Products");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mlist.clear();
                for (DataSnapshot snapshot2 :
                        snapshot.getChildren()) {

                    DTO_QlySanPham dto_qlySanPham = snapshot2.getValue(DTO_QlySanPham.class);
                        if (dto_qlySanPham.getName().contains(key)){
                            mlist.add(dto_qlySanPham);
                        }
                }

                madapter_qlySanPham.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QLySanPhamActivity.this, "lay san pham loi ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDataCat() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("category");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listSpin.clear();
                listSpin1.clear();
                listSpin1.add("All");
                for (DataSnapshot snapshot1 :
                        snapshot.getChildren()) {

                    CategoryDTO categoryDTO = snapshot1.getValue(CategoryDTO.class);
                    listSpin.add(categoryDTO.getName());
                    listSpin1.add(categoryDTO.getName());

                }
                adapter1.notifyDataSetChanged();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SelectImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {


            // Lấy dữ liệu từ màn hình chọn ảnh truyền về
            filePath = data.getData();
            Log.d("zzzzz", "onActivityResult: " + filePath.toString());


            try {
                // xem thử ảnh , nếu không muốn xem thử thì bỏ đoạn code này

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                img_preview.setImageBitmap(bitmap);
                if (filePath != null) {

                    // Hiển thị dialog
                    ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();

                    // Tạo đường dẫn lưu trữ file, images/ là 1 thư mục trên firebase, chuỗi uuid... là tên file, tạm thời có thể phải lên web firebase tạo sẵn thư mục images
                    StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
                    Log.d(TAG, "uploadImage: " + ref.getPath());

                    // Tiến hành upload file
                    ref.putFile(filePath)
                            .addOnSuccessListener(
                                    new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            // upload thành công, tắt dialog


                                            progressDialog.dismiss();
                                            Toast.makeText(QLySanPhamActivity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();


                                        }
                                    })

                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    e.printStackTrace(); // có lỗi upload
                                    progressDialog.dismiss();
                                    Toast.makeText(QLySanPhamActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(
                                    new OnProgressListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                            // cập nhật tiến trình upload
                                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                                        }
                                    })
                            .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw task.getException();
                                    }
                                    // gọi task để lấy URL sau khi upload thành công
                                    return ref.getDownloadUrl();
                                }
                            })
                            .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Uri downloadUri = task.getResult();
                                        // upload thành công, lấy được url ảnh, ghi ra log. Bạn có thể ghi vào CSdl....
                                        link_anh = downloadUri.toString();
                                        Log.d(TAG, "onComplete: url download = " + downloadUri.toString());
                                    } else {
                                        // lỗi lấy url download
                                    }
                                }
                            });
                }

            } catch (IOException e) {

                e.printStackTrace();
            }
        }

    }
    private void uploadImage() {

    }

    @Override
    public void update1(DTO_QlySanPham dto_qlySanPham) {
        // Xử lý khi người dùng chọn action_popup_edit


        final Dialog dialog = new Dialog(QLySanPhamActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_update_pro);

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        }
        EditText ed_name = dialog.findViewById(R.id.ed_name_update);
        EditText ed_des = dialog.findViewById(R.id.ed_des_update);
        EditText ed_price = dialog.findViewById(R.id.ed_price_update);
        EditText ed_number = dialog.findViewById(R.id.ed_number_update);
        img_preview = dialog.findViewById(R.id.img_pro_update);

        // Lấy dữ liệu từ DTO_QlySanPham
        String id = dto_qlySanPham.getId();
        String namePro = dto_qlySanPham.getName();
        String des = dto_qlySanPham.getInformation();
        String price = dto_qlySanPham.getPrice();
        String imageUrl = dto_qlySanPham.getImage();
        int number = dto_qlySanPham.getNumber();

// Sử dụng setter để hiển thị dữ liệu trong EditText
        ed_name.setText(namePro);
        ed_des.setText(des);
        ed_price.setText(price);
        ed_number.setText(String.valueOf(number));
        Picasso.get().load(imageUrl).into(img_preview);

        TextView btn_update = dialog.findViewById(R.id.dialog_btn_update);
        TextView btn_huy = dialog.findViewById(R.id.btn_huy);
        Spinner spn_cat = dialog.findViewById(R.id.spn_cat);
        spn_cat.setAdapter(adapter);
        spn_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                nameCat = listSpin.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        img_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();

            }
        });


        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Products");
                String namePro = ed_name.getText().toString().trim();

                String des = ed_des.getText().toString().toString();
                String price = ed_price.getText().toString().trim();

                String id = dto_qlySanPham.getId();
                if (namePro.isEmpty()){
                    ed_name.setError("Không được để trống!!");
                }else if (des.isEmpty()){
                    ed_des.setError("Không được để trống!!");
                } else if (price.isEmpty()) {
                    ed_price.setError("Không được để trống!!");
                } else if (ed_number.getText().toString().isEmpty()) {
                    ed_number.setError("Không được để trống!!");
                } else if (!ed_price.getText().toString().matches("\\d+(?:\\.\\d+)?")) {
                    ed_price.setError("Giá tiền phải là số!!");
                } else if (!ed_number.getText().toString().matches("\\d+(?:\\.\\d+)?")) {
                    ed_number.setError("Số lượng phải là số!!");
                }else{
                    int number = Integer.parseInt(ed_number.getText().toString().trim());
                    DTO_QlySanPham sanPham = new DTO_QlySanPham(id,link_anh,namePro,price,des,nameCat,number);

                    myRef.child(id).setValue(sanPham, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            Toast.makeText(QLySanPhamActivity.this, "update thanh cong", Toast.LENGTH_SHORT).show();
                        }
                    });

                    dialog.dismiss();
                }

            }
        });

        btn_huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    @Override
    public void delete1(DTO_QlySanPham dto_qlySanPham) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.app_name))
                .setMessage("Bạn có chắc muốn xóa hay không?")

                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef1 = database.getReference("Products");
                        myRef1.child(String.valueOf(dto_qlySanPham.getId())).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(QLySanPhamActivity.this, "xoa thanh cong", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }

                })

                .setNegativeButton("Cancel",null)
                .show();



    }
}