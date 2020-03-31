package com.sundarinfotech.coronavirustracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sundarinfotech.coronavirustracker.R;

public class HelplineAdapter extends RecyclerView.Adapter<HelplineAdapter.ViewHolder> {

    private final Context context;
    private int[] state;
    private int[] contact;

    public HelplineAdapter(int[] state, int[] contact, Context context){
        this.state = state;
        this.contact = contact;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HelplineAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_helpline, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mState.setText(state[position]);
        holder.mContact.setText(contact[position]);
    }

    @Override
    public int getItemCount() {
        return state.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView mState, mContact;

        ViewHolder(View itemView) {
            super(itemView);
            mState = itemView.findViewById(R.id.state);
            mContact = itemView.findViewById(R.id.helpline_number);
        }

    }
}
