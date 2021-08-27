package com.eval_alixia.githubappli;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GitAdapter extends RecyclerView.Adapter<GitAdapter.ViewHolder>{
    private ArrayList<GitBean> data;

    public GitAdapter(ArrayList<GitBean> data) {
        this.data = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivR;
        TextView tvR;

        public ViewHolder(View itemView) {
            super(itemView);

            ivR = itemView.findViewById(R.id.ivR);
            tvR = itemView.findViewById(R.id.tvR);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(GitAdapter.ViewHolder holder, int position) {
        GitBean datum = data.get(position);
        if (datum.getRef() != null) {
            holder.tvR.setText(datum.getRef());
        }
        else {
            holder.tvR.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
