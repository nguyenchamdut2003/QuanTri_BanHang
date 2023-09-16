package com.example.quantri_banhang.fragment;

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

import com.example.quantri_banhang.R;
import com.example.quantri_banhang.actitvity.LoginActivity;
import com.example.quantri_banhang.actitvity.QLySanPhamActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class fragment_trangchu extends Fragment {

    private FirebaseAuth auth;
    View view;
    TextView tv_tenUser;
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

        auth = FirebaseAuth.getInstance();
        checkUser();
        img_qlsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), QLySanPhamActivity.class));
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
}
