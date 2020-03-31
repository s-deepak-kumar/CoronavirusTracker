package com.sundarinfotech.coronavirustracker.viewmodel;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.haipq.android.flagkit.FlagImageView;
import com.sundarinfotech.coronavirustracker.R;

public class CasesViewHolder extends RecyclerView.ViewHolder {
    public TextView country_name, total_case, recovered, critical, deaths;
    public FlagImageView country_flag;
    public Button btnMore;


    public CasesViewHolder(View view) {
        super(view);
        country_name = view.findViewById(R.id.country);
        total_case = view.findViewById(R.id.caseValue);
        country_flag = view.findViewById(R.id.img_location);

        recovered = view.findViewById(R.id.recovered);
        critical = view.findViewById(R.id.critical);
        deaths = view.findViewById(R.id.deaths);
        btnMore = view.findViewById(R.id.btnmore);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

}
