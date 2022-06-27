package com.andresjaramillo.searchcomics;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterList extends RecyclerView.Adapter<AdapterList.ViewHolderList> implements View.OnClickListener {

    ArrayList<ListComicsVo> dataList;
    private View.OnClickListener listener;

    public AdapterList(ArrayList<ListComicsVo> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolderList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, null, false);
        view.setOnClickListener(this);
        return new ViewHolderList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderList holder, int position) {
        holder.tvName.setText(dataList.get(position).getName());
        holder.tvResource.setText(dataList.get(position).getResource());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener=listener;
    }


    @Override
    public void onClick(View view) {
        if (listener!=null){
            listener.onClick(view);
        }
    }

    public class ViewHolderList extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvResource;

        public ViewHolderList(@NonNull View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName_id);
            tvResource = (TextView) itemView.findViewById(R.id.tvResource_id);
        }
    }
}
