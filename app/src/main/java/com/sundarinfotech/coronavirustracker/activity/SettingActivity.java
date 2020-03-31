package com.sundarinfotech.coronavirustracker.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sundarinfotech.coronavirustracker.R;
import com.sundarinfotech.coronavirustracker.adapter.AdviceAdapter;
import com.sundarinfotech.coronavirustracker.adapter.HelplineAdapter;

public class SettingActivity extends AppCompatActivity {

    View helplineContainer, mRateContainerView, mShareContainerView, copyrightContainer;
    private Dialog dDialog;
    ImageButton mBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);

        mBack = findViewById(R.id.back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        helplineContainer = findViewById(R.id.helplineContainer);
        mRateContainerView = findViewById(R.id.rateContainer);
        mShareContainerView = findViewById(R.id.shareContainer);
        copyrightContainer = findViewById(R.id.copyrightContainer);

        mRateContainerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("market://details?id=" + getPackageName()));
                startActivity(i);
            }
        });

        mShareContainerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = "https://play.google.com/store/apps/details?id=com.sundarinfotech.coronavirustracker";
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Download Coronavirus Tracker App\n\n\n" +
                        "To Get Real Statistics/Analysis Of COVID-19 Affected Countries\n" +
                        s);
                startActivity(Intent.createChooser(shareIntent, "Share Movies Buddy With"));
            }
        });

        copyrightContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dDialog = new Dialog(SettingActivity.this, android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
                dDialog.setContentView(R.layout.copyright_dialog);

                int [] title = {R.string.copyright_title1, R.string.copyright_title2};
                int[] desc = {R.string.copyright_desc1, R.string.copyright_desc2};

                RecyclerView recyclerView = dDialog.findViewById(R.id.recyleview);
                LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(SettingActivity.this, LinearLayoutManager.VERTICAL, false);
                AdviceAdapter helplineAdapter =  new AdviceAdapter(title, desc, SettingActivity.this);

                recyclerView.setAdapter(helplineAdapter);
                recyclerView.setLayoutManager(linearLayoutManager);

                dDialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dDialog.cancel();
                    }
                });

                dDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dDialog.show();
            }
        });

        helplineContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dDialog = new Dialog(SettingActivity.this, android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
                dDialog.setContentView(R.layout.helpline_dialog);

                int[] state = {R.string.state0, R.string.state1, R.string.state2, R.string.state3, R.string.state4, R.string.state5, R.string.state6,
                        R.string.state7, R.string.state8, R.string.state9, R.string.state10, R.string.state11, R.string.state12, R.string.state13,
                        R.string.state14, R.string.state15, R.string.state16, R.string.state17, R.string.state18, R.string.state19, R.string.state20,
                        R.string.state21, R.string.state22, R.string.state23, R.string.state24, R.string.state25, R.string.state26, R.string.state27,
                        R.string.state28, R.string.state29, R.string.state30, R.string.state31, R.string.state32, R.string.state33, R.string.state34,
                        R.string.state35, R.string.state36};

                int[] contact = {R.string.contact0, R.string.contact1, R.string.contact2, R.string.contact3, R.string.contact4, R.string.contact5, R.string.contact6,
                        R.string.contact7, R.string.contact8, R.string.contact9, R.string.contact10, R.string.contact11, R.string.contact12, R.string.contact13,
                        R.string.contact14, R.string.contact15, R.string.contact16, R.string.contact17, R.string.contact18, R.string.contact19, R.string.contact20,
                        R.string.contact21, R.string.contact22, R.string.contact23, R.string.contact24, R.string.contact25, R.string.contact26, R.string.contact27,
                        R.string.contact28, R.string.contact29, R.string.contact30, R.string.contact31, R.string.contact32, R.string.contact33, R.string.contact34,
                        R.string.contact35, R.string.contact36};

                RecyclerView recyclerView = dDialog.findViewById(R.id.recyleview);
                LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(SettingActivity.this, LinearLayoutManager.VERTICAL, false);
                HelplineAdapter helplineAdapter =  new HelplineAdapter(state, contact, SettingActivity.this);

                recyclerView.setAdapter(helplineAdapter);
                recyclerView.setLayoutManager(linearLayoutManager);

                dDialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dDialog.cancel();
                    }
                });

                dDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dDialog.show();
            }
        });
    }
}
