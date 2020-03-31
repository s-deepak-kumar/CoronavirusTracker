package com.sundarinfotech.coronavirustracker.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sundarinfotech.coronavirustracker.R;
import com.sundarinfotech.coronavirustracker.adapter.NCVidCasesAdapter;
import com.sundarinfotech.coronavirustracker.util.NetworkHelper;
import com.sundarinfotech.coronavirustracker.util.ViewUtil;
import com.sundarinfotech.coronavirustracker.viewmodel.CoronaVirus;
import com.sundarinfotech.coronavirustracker.viewmodel.CoronaVirusViewModel;

import java.util.List;

public class SearchActivity extends AppCompatActivity implements NCVidCasesAdapter.onListener, SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    private TextView tv_totConfirmedCases, tv_totalDeaths, tv_totalRecovered;
    private float totalCase = HomepageActivity.confirmedCases + HomepageActivity.totalDeaths + HomepageActivity.totalRecovers;
    private LinearLayout lFoundLay, lNotFoundLay, lCouutryLay;

    private TextView tv_country;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private NCVidCasesAdapter mAdapter;

    private CoronaVirusViewModel mCoronaViewModel;

    private View root;

    public SearchActivity() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tv_totConfirmedCases = findViewById(R.id.tot_cnfm_cases);
        tv_totalDeaths = findViewById(R.id.tot_deaths);
        tv_totalRecovered = findViewById(R.id.cases_recovered);

        lFoundLay = findViewById(R.id.layoutfound);
        lNotFoundLay= findViewById(R.id.nofoundlayout);
        lCouutryLay = findViewById(R.id.countrylay);

        tv_totConfirmedCases.setText(String.format("%.2f", (HomepageActivity.confirmedCases/totalCase) * 100) + " %");
        tv_totalDeaths.setText(String.format("%.2f", (HomepageActivity.totalDeaths/totalCase) * 100) + " %");
        tv_totalRecovered.setText(String.format("%.2f", (HomepageActivity.totalRecovers/totalCase) * 100) + " %");

        initView();
        initViewModel();
    }

    private void initView() {
        searchView = findViewById(R.id.search_view);
        tv_country = findViewById(R.id.country);
        recyclerView = findViewById(R.id.recyleview);

        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);

        if (searchView != null) {
            ViewUtil.show(SearchActivity.this);
            searchView.clearFocus();
        }

    }

    private void initViewModel() {
        mCoronaViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(CoronaVirusViewModel.class);
        if (!NetworkHelper.CheckNetwork()) {
            Toast.makeText(SearchActivity.this, R.string.check_connection, Toast.LENGTH_SHORT).show();
        } else {
            mCoronaViewModel.getCoronaCompleteInformation();
            mCoronaViewModel.mutableCompleteLiveData.observe(this, new Observer<List<CoronaVirus>>() {
                @Override
                public void onChanged(List<CoronaVirus> coronaVirus) {
                    setUpRecyclerView(coronaVirus);
                }
            });
        }


    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        if (searchView != null) {
            ViewUtil.hide(SearchActivity.this, searchView);
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (searchView != null) {
            ViewUtil.hide(SearchActivity.this, searchView);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void setUpRecyclerView(List<CoronaVirus> virusList) {
        mAdapter = new NCVidCasesAdapter(SearchActivity.this, virusList, this);
        runAnimationAgain();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SearchActivity.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemSelected(CoronaVirus cases) {

    }

    private void runAnimationAgain() {
        int resId = R.anim.layout_animation_fall_down;
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(SearchActivity.this, resId);

        recyclerView.setLayoutAnimation(controller);
        mAdapter.notifyDataSetChanged();
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.scheduleLayoutAnimation();

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // filter recycler view when query submitted
        if (!query.equals("")) {
            mAdapter.getFilter().filter(query);
            searchView.clearFocus();
            if (mAdapter.getItemCount() <= 0) {
                lFoundLay.setVisibility(View.GONE);
                lNotFoundLay.setVisibility(View.VISIBLE);
                lCouutryLay.setVisibility(View.GONE);
            } else {
                lFoundLay.setVisibility(View.GONE);
                lNotFoundLay.setVisibility(View.GONE);
                lCouutryLay.setVisibility(View.VISIBLE);
            }
        }else {
            lFoundLay.setVisibility(View.VISIBLE);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // filter recycler view when text is changed
        if (!newText.equals("")) {
            mAdapter.getFilter().filter(newText);
            if (mAdapter.getItemCount() <= 0) {
                lFoundLay.setVisibility(View.GONE);
                lNotFoundLay.setVisibility(View.VISIBLE);
                lCouutryLay.setVisibility(View.GONE);
            } else {
                lFoundLay.setVisibility(View.GONE);
                lNotFoundLay.setVisibility(View.GONE);
                lCouutryLay.setVisibility(View.VISIBLE);
            }
        }else {
            lFoundLay.setVisibility(View.VISIBLE);
        }
        //UpdateTotalRegions(mAdapter.getItemCount());
        return false;
    }

    @Override
    public boolean onClose() {
        hideKeyboard();
        return false;
    }


    /**
     * Hide Soft KeyBoard When Click on crossIcon of SearchView
     */

    private boolean hideKeyboard() {
        try {
            if (searchView.hasFocus()) {
                Log.d("alert", "hiding and removing focus");
                searchView.clearFocus();

                searchView.setIconified(true);
                //   used to again closed searchview  after clear text from click on cross button
            }

            InputMethodManager imm =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

            imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
