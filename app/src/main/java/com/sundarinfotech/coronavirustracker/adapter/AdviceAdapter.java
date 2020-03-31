package com.sundarinfotech.coronavirustracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sundarinfotech.coronavirustracker.R;

public class AdviceAdapter extends RecyclerView.Adapter<AdviceAdapter.ViewHolder> {

    private final Context context;
    private int[] title;
    private int[] desc;

    public AdviceAdapter(int[] title, int[] desc, Context context){
        this.title = title;
        this.desc = desc;
        this.context = context;
    }

    @NonNull
    @Override
    public AdviceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdviceAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_faq_2, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final AdviceAdapter.ViewHolder holder, int position) {
        holder.mTtile.setText(title[position]);
        holder.mDesc.setText(desc[position]);
    }

    @Override
    public int getItemCount() {
        return title.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView mTtile, mDesc;

        ViewHolder(View itemView) {
            super(itemView);
            mTtile = itemView.findViewById(R.id.faq_title);
            mDesc = itemView.findViewById(R.id.faq_desc);
        }

    }
}
