package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.Distributor;

import java.util.ArrayList;
import java.util.List;

public class DistributorAdapter extends RecyclerView.Adapter<DistributorAdapter.DistributorViewHolder> {
    private List<Distributor> list = new ArrayList<>();
    private OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onItemClick(String id);
        void updateItem(String id, String name);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    public void setData(List<Distributor> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DistributorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.distributor_item, parent, false);
        return new DistributorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DistributorViewHolder holder, int position) {
        Distributor distributor = list.get(position);
        holder.tvID.setText(String.valueOf(position + 1));
        holder.tvName.setText(distributor.getName());

        holder.btnEdit.setOnClickListener(view -> clickListener.updateItem(distributor.getId(), distributor.getName()));
        holder.btnDelete.setOnClickListener(view -> clickListener.onItemClick(distributor.getId()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class DistributorViewHolder extends RecyclerView.ViewHolder {
        private TextView tvID, tvName;
        private ImageButton btnDelete, btnEdit;

        public DistributorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvID = itemView.findViewById(R.id.tv10);
            tvName = itemView.findViewById(R.id.tvName);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }
}
