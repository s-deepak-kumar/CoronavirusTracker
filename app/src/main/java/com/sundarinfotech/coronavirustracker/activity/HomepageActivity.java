package com.sundarinfotech.coronavirustracker.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.sundarinfotech.coronavirustracker.R;
import com.sundarinfotech.coronavirustracker.adapter.NCVidCasesAdapter;
import com.sundarinfotech.coronavirustracker.util.NetworkHelper;
import com.sundarinfotech.coronavirustracker.util.ViewUtil;
import com.sundarinfotech.coronavirustracker.viewmodel.CoronaVirus;
import com.sundarinfotech.coronavirustracker.viewmodel.CoronaVirusResume;
import com.sundarinfotech.coronavirustracker.viewmodel.CoronaVirusViewModel;
import com.sundarinfotech.floatingactionmenu.FloatingActionMenu;

import java.util.List;

public class HomepageActivity extends AppCompatActivity implements NCVidCasesAdapter.onListener {

    private TextView tv_country;
    private RecyclerView recyclerView;
    private NCVidCasesAdapter mAdapter;
    private CoronaVirusViewModel mCoronaViewModel;

    private TextView tv_statistics, tv_totConfirmedCases, tv_totalDeaths, tv_totalRecovered, country, version;
    private CardView pt_cases, pt_deaths, pt_recovered;
    private String today = null;
    private long death = 0;
    private long recovered = 0;
    public static long cases = 0;
    public static long updated = 0;

    public static long confirmedCases = 0;
    public static long totalDeaths = 0;
    public static long totalRecovers = 0;
    private ProgressBar progressBar;
    Dialog dDialogLoading, dDialog;
    ImageButton bShare, bRate;
    View searchConatainer;

    private final String TAG = HomepageActivity.class.getSimpleName();

    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        bShare = findViewById(R.id.share);
        bRate =  findViewById(R.id.rate);

        searchConatainer = findViewById(R.id.searchContainer);

        interstitialAd = new InterstitialAd(HomepageActivity.this, "2987572304613026_2987577617945828");

        loadIntersitialAds();

        findViewById(R.id.goButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchConatainer.performClick();
            }
        });

        searchConatainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomepageActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        bShare.setOnClickListener(new View.OnClickListener() {
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

        bRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("market://details?id=" + getPackageName()));
                startActivity(i);
            }
        });

        loading();

        FloatingActionMenu menu = findViewById(R.id.floatingMenu);
        menu.setClickEvent(new FloatingActionMenu.ClickEvent() {
            @Override
            public void onClick(int index) {
                //Log.d("TAG", String.valueOf(index)); // index of clicked menu item
                //Toast.makeText(MainActivity.this, ""+index, Toast.LENGTH_SHORT).show();
                if (index == 0){
                    dDialog = new Dialog(HomepageActivity.this, android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
                    dDialog.setContentView(R.layout.mb_warning);

                    Button bHindi = dDialog.findViewById(R.id.hindi);
                    Button bEnglish = dDialog.findViewById(R.id.english);
                    TextView tWarning = dDialog.findViewById(R.id.warning);

                    bHindi.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tWarning.setText(R.string.warning);
                            bHindi.setBackgroundResource(R.drawable.btn_bk);
                            bEnglish.setBackgroundResource(R.drawable.active_bk);
                        }
                    });

                    bEnglish.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tWarning.setText(R.string.warning_english);
                            bHindi.setBackgroundResource(R.drawable.active_bk);
                            bEnglish.setBackgroundResource(R.drawable.btn_bk);
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
                if (index == 1){
                    Intent intent = new Intent(HomepageActivity.this, SearchActivity.class);
                    startActivity(intent);
                    if (interstitialAd.isAdLoaded()) {
                        interstitialAd.show();
                    }
                }
                if (index == 2){
                    Intent intent = new Intent(HomepageActivity.this, AdviceActivity.class);
                    startActivity(intent);
                    if (interstitialAd.isAdLoaded()) {
                        interstitialAd.show();
                    }
                }
                if (index == 3){
                    Intent intent = new Intent(HomepageActivity.this, SettingActivity.class);
                    startActivity(intent);
                    if (interstitialAd.isAdLoaded()) {
                        interstitialAd.show();
                    }
                }
            }
        });

        initView();
        initViewModel();
    }

    public void loadIntersitialAds(){
        // Set listeners for the Interstitial Ad
        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.");
                interstitialAd.loadAd();
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
                interstitialAd.loadAd();
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad
                //interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        });

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialAd.loadAd();
    }

    private void initView() {
        tv_country = findViewById(R.id.country);
        recyclerView = findViewById(R.id.recyclerCase);

        tv_statistics = findViewById(R.id.tv_statistics);
        tv_totConfirmedCases = findViewById(R.id.tot_cnfm_cases);
        tv_totalDeaths = findViewById(R.id.tot_deaths);
        tv_totalRecovered = findViewById(R.id.cases_recovered);
        country = findViewById(R.id.country);

        pt_cases = findViewById(R.id.pt_cases);
        pt_deaths = findViewById(R.id.pt_deaths);
        pt_recovered = findViewById(R.id.pt_recovered);


        tv_statistics.setText("•   WORLDWIDE STATISTICS");

    }

    private void initViewModel() {
        mCoronaViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(CoronaVirusViewModel.class);
        if (!NetworkHelper.CheckNetwork()) {
            Toast.makeText(HomepageActivity.this, R.string.check_connection, Toast.LENGTH_SHORT).show();
        } else {
            mCoronaViewModel.getCoronaCompleteInformation();
            mCoronaViewModel.mutableCompleteLiveData.observe(this, new Observer<List<CoronaVirus>>() {
                @Override
                public void onChanged(List<CoronaVirus> coronaVirus) {
                    UpdateTotalRegions(coronaVirus.size());
                    setUpRecyclerView(coronaVirus);
                }
            });

            mCoronaViewModel.getCoronaResumeInformation();
            mCoronaViewModel.mutableResumeLiveData.observe(this, new Observer<CoronaVirusResume>() {
                @Override
                public void onChanged(CoronaVirusResume coronaVirusResume) {
                    today = ViewUtil.showDatetime(true, coronaVirusResume.getUpdated());
                    tv_totConfirmedCases.setText(String.valueOf(coronaVirusResume.getCases()));
                    tv_totalDeaths.setText(String.valueOf(coronaVirusResume.getDeaths()));
                    tv_totalRecovered.setText(String.valueOf(coronaVirusResume.getRecovered()));
                    updated = coronaVirusResume.getUpdated();
                    death = coronaVirusResume.getDeaths();
                    recovered = coronaVirusResume.getRecovered();
                    cases = coronaVirusResume.getCases();

                    confirmedCases = coronaVirusResume.getCases();
                    totalDeaths = coronaVirusResume.getDeaths();
                    totalRecovers = coronaVirusResume.getRecovered();
                    dDialogLoading.dismiss();
                }
            });
        }


    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void UpdateTotalRegions(int size) {
        tv_country.setText(String.format("•   TOTAL %d REGIONS  AT OF %s", size, ViewUtil.showDatetime(true, updated)));
    }

    private void setUpRecyclerView(List<CoronaVirus> virusList) {
        mAdapter = new NCVidCasesAdapter(HomepageActivity.this, virusList, this);
        runAnimationAgain();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(HomepageActivity.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        //swipeRefreshLayout.setRefreshing(false);
    }

    private void runAnimationAgain() {
        int resId = R.anim.layout_animation_fall_down;
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(HomepageActivity.this, resId);

        recyclerView.setLayoutAnimation(controller);
        mAdapter.notifyDataSetChanged();
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.scheduleLayoutAnimation();

    }

    private void loading(){
        dDialogLoading = new Dialog(HomepageActivity.this);
        dDialogLoading.setContentView(R.layout.loading_for_detailed_page);
        dDialogLoading.setCancelable(false);
        dDialogLoading.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dDialogLoading.show();
    }

    @Override
    public void onItemSelected(CoronaVirus cases) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        System.exit(0);
    }
}