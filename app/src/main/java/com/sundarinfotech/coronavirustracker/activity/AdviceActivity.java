package com.sundarinfotech.coronavirustracker.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sundarinfotech.coronavirustracker.R;
import com.sundarinfotech.coronavirustracker.adapter.AdviceAdapter;
import com.sundarinfotech.coronavirustracker.adapter.FAQAdapter;

public class AdviceActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FAQAdapter faqAdapter;
    LinearLayoutManager linearLayoutManager;
    Button bHindi, bEnglish;
    boolean isHindi = true;
    boolean isEnglish = false;

    View mythContainer, maskContainer;
    TextView tMyth, tMask, tTitle;

    int[] title, desc, title_1, desc_1;
    private Dialog dDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advices_activity);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        updateAdvices();
        bHindi = findViewById(R.id.hindi);
        bEnglish = findViewById(R.id.english);
        mythContainer = findViewById(R.id.myth);
        maskContainer = findViewById(R.id.maskContainer);
        tMyth = findViewById(R.id.myth_text);
        tMask = findViewById(R.id.mask_text);

        bHindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tMyth.setText("काल्पनिक तथ्य");
                tMask.setText("मास्क का उपयोग कब और कैसे करें?");
                bHindi.setBackgroundResource(R.drawable.btn_bk);
                bEnglish.setBackgroundResource(R.drawable.active_bk);
                isHindi = true;
                updateAdvices();
            }
        });

        bEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tMyth.setText("Myth Busters");
                tMask.setText("When And How To Use Masks?");
                bHindi.setBackgroundResource(R.drawable.active_bk);
                bEnglish.setBackgroundResource(R.drawable.btn_bk);
                isHindi = false;
                updateAdvices();
            }
        });

        mythContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dDialog = new Dialog(AdviceActivity.this, android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
                dDialog.setContentView(R.layout.advice_dialog);

                updateMyth();
                updateMythMask(dDialog, title_1, desc_1);
                Button bHindi = dDialog.findViewById(R.id.hindi);
                Button bEnglish = dDialog.findViewById(R.id.english);
                tTitle = dDialog.findViewById(R.id.title);

                if (isEnglish){
                    tTitle.setText("Myth Busters");
                    bHindi.setBackgroundResource(R.drawable.active_bk);
                    bEnglish.setBackgroundResource(R.drawable.btn_bk);
                }else {
                    tTitle.setText("काल्पनिक तथ्य ");
                    bHindi.setBackgroundResource(R.drawable.btn_bk);
                    bEnglish.setBackgroundResource(R.drawable.active_bk);
                }

                bHindi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tTitle.setText("काल्पनिक तथ्य ");
                        bHindi.setBackgroundResource(R.drawable.btn_bk);
                        bEnglish.setBackgroundResource(R.drawable.active_bk);
                        isEnglish = false;
                        updateMyth();
                        updateMythMask(dDialog, title_1, desc_1);
                    }
                });

                bEnglish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tTitle.setText("Myth Busters");
                        bHindi.setBackgroundResource(R.drawable.active_bk);
                        bEnglish.setBackgroundResource(R.drawable.btn_bk);
                        isEnglish = true;
                        updateMyth();
                        updateMythMask(dDialog, title_1, desc_1);
                    }
                });

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

        maskContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dDialog = new Dialog(AdviceActivity.this, android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
                dDialog.setContentView(R.layout.advice_dialog);

                updateMask();
                updateMythMask(dDialog, title_1, desc_1);
                Button bHindi = dDialog.findViewById(R.id.hindi);
                Button bEnglish = dDialog.findViewById(R.id.english);
                tTitle = dDialog.findViewById(R.id.title);

                if (isEnglish){
                    tTitle.setText("When And How To Use Masks?");
                    bHindi.setBackgroundResource(R.drawable.active_bk);
                    bEnglish.setBackgroundResource(R.drawable.btn_bk);
                }else {
                    tTitle.setText("मास्क का उपयोग कब और कैसे करें?");
                    bHindi.setBackgroundResource(R.drawable.btn_bk);
                    bEnglish.setBackgroundResource(R.drawable.active_bk);
                }

                bHindi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tTitle.setText("मास्क का उपयोग कब और कैसे करें?");
                        bHindi.setBackgroundResource(R.drawable.btn_bk);
                        bEnglish.setBackgroundResource(R.drawable.active_bk);
                        isEnglish = false;
                        updateMask();
                        updateMythMask(dDialog, title_1, desc_1);
                    }
                });

                bEnglish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tTitle.setText("When And How To Use Masks?");
                        bHindi.setBackgroundResource(R.drawable.active_bk);
                        bEnglish.setBackgroundResource(R.drawable.btn_bk);
                        isEnglish = true;
                        updateMask();
                        updateMythMask(dDialog, title_1, desc_1);
                    }
                });

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

    public void updateMask(){
        if (isEnglish) {
            title_1 = new int[]{R.string.mask_title1, R.string.mask_title2};
            desc_1 = new int[]{R.string.myask_desc1, R.string.myask_desc2};
        }else {
            title_1 = new int[]{R.string.hindi_mask_title1, R.string.hindi_mask_title2};
            desc_1 = new int[]{R.string.hindi_myask_desc1, R.string.hindi_myask_desc2};
        }
    }

    public void updateMyth(){
        if (isEnglish) {
            title_1 = new int[]{R.string.myth_title1, R.string.myth_title2, R.string.myth_title3, R.string.myth_title4, R.string.myth_title5, R.string.myth_title6,
                    R.string.myth_title7, R.string.myth_title8, R.string.myth_title9, R.string.myth_title10, R.string.myth_title11, R.string.myth_title12,
                    R.string.myth_title13, R.string.myth_title14};

            desc_1 = new int[]{R.string.myth_desc1, R.string.myth_desc2, R.string.myth_desc3, R.string.myth_desc4, R.string.myth_desc5, R.string.myth_desc6,
                    R.string.myth_desc7, R.string.myth_desc8, R.string.myth_desc9, R.string.myth_desc10, R.string.myth_desc11, R.string.myth_desc12,
                    R.string.myth_desc13, R.string.myth_desc14};
        }else {
            title_1 = new int[]{R.string.hindi_myth_title1, R.string.hindi_myth_title2, R.string.hindi_myth_title3, R.string.hindi_myth_title4, R.string.hindi_myth_title5, R.string.hindi_myth_title6,
                    R.string.hindi_myth_title7, R.string.hindi_myth_title8, R.string.hindi_myth_title9, R.string.hindi_myth_title10, R.string.hindi_myth_title11, R.string.hindi_myth_title12,
                    R.string.hindi_myth_title13, R.string.hindi_myth_title14};

            desc_1 = new int[]{R.string.hindi_myth_desc1, R.string.hindi_myth_desc2, R.string.hindi_myth_desc3, R.string.hindi_myth_desc4, R.string.hindi_myth_desc5, R.string.hindi_myth_desc6,
                    R.string.hindi_myth_desc7, R.string.hindi_myth_desc8, R.string.hindi_myth_desc9, R.string.hindi_myth_desc10, R.string.hindi_myth_desc11, R.string.hindi_myth_desc12,
                    R.string.hindi_myth_desc13, R.string.hindi_myth_desc14};
        }
    }

    public void updateMythMask(Dialog dDialog, int[] title, int[] desc){
        RecyclerView recyclerView = dDialog.findViewById(R.id.recyleview);
        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(AdviceActivity.this, LinearLayoutManager.VERTICAL, false);
        AdviceAdapter helplineAdapter =  new AdviceAdapter(title, desc, AdviceActivity.this);

        recyclerView.setAdapter(helplineAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public void updateAdvices(){
        if (!isHindi) {
            title = new int[]{R.string.advice_title1, R.string.advice_title2, R.string.advice_title3, R.string.advice_title4, R.string.advice_title5, R.string.advice_title6,
                    R.string.advice_title7};
            desc = new int[]{R.string.advice_desc1, R.string.advice_desc2, R.string.advice_desc3, R.string.advice_desc4, R.string.advice_desc5, R.string.advice_desc6,
                    R.string.advice_desc7};
        }else {
            title = new int[]{R.string.hindi_advice_title1, R.string.hindi_advice_title2, R.string.hindi_advice_title3, R.string.hindi_advice_title4, R.string.hindi_advice_title5, R.string.hindi_advice_title6,
                    R.string.hindi_advice_title7};
            desc = new int[]{R.string.hindi_advice_desc1, R.string.hindi_advice_desc2, R.string.hindi_advice_desc3, R.string.hindi_advice_desc4, R.string.hindi_advice_desc5, R.string.hindi_advice_desc6,
                    R.string.hindi_advice_desc7};
        }

        recyclerView = findViewById(R.id.recyleview);
        faqAdapter = new FAQAdapter(title, desc, AdviceActivity.this);
        linearLayoutManager = new LinearLayoutManager(AdviceActivity.this, LinearLayoutManager.VERTICAL, false);

        recyclerView.setAdapter(faqAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
}
