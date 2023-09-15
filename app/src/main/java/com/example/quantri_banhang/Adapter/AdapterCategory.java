package com.example.quantri_banhang.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.quantri_banhang.DTO.CategoryDTO;
import com.example.quantri_banhang.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AdapterCategory extends BaseAdapter {
    ArrayList<CategoryDTO> list;
    Context context;

    public AdapterCategory(ArrayList<CategoryDTO> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        CategoryDTO categoryDTO = list.get(i);
        return categoryDTO;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View viewok;
        if (view == null){
            viewok = View.inflate(viewGroup.getContext(), R.layout.item_addcategory, null);
        }else{
            viewok = view;
        }
        CategoryDTO categoryDTO = list.get(i);

        TextView tvname = viewok.findViewById(R.id.tv_tenloai);
        tvname.setText(categoryDTO.getName());
        return viewok;
    }
}
