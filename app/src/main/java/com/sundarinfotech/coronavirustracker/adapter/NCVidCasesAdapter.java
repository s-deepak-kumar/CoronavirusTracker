package com.sundarinfotech.coronavirustracker.adapter;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdIconView;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.haipq.android.flagkit.FlagImageView;
import com.sundarinfotech.coronavirustracker.activity.HomepageActivity;
import com.sundarinfotech.coronavirustracker.R;
import com.sundarinfotech.coronavirustracker.viewmodel.CasesViewHolder;
import com.sundarinfotech.coronavirustracker.viewmodel.CoronaVirus;
import com.sundarinfotech.countrylist.CountryList;

import java.util.ArrayList;
import java.util.List;

public class NCVidCasesAdapter extends RecyclerView.Adapter<CasesViewHolder>
        implements Filterable {

    private Context context;
    private List<CoronaVirus> coronaVirusList;
    private List<CoronaVirus> coronaVirusListFiltered;
    private onListener listener;
    private Dialog dDialog;

    private NativeAd nativeAd;
    private NativeAdLayout nativeMainAdLayout;
    private LinearLayout adView;
    private final String TAG = NCVidCasesAdapter.class.getSimpleName();

    public NCVidCasesAdapter(Context context, List<CoronaVirus> coronaVirusList, onListener listener) {
        this.context = context;
        this.listener = listener;
        this.coronaVirusList = coronaVirusList;
        this.coronaVirusListFiltered = coronaVirusList;
    }

    public List<CoronaVirus> getCoronaVirusListFiltered() {
        return coronaVirusListFiltered;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    coronaVirusListFiltered = coronaVirusList;
                } else {
                    List<CoronaVirus> filteredList = new ArrayList<>();
                    for (CoronaVirus row : coronaVirusList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getCountry().toLowerCase().contains(charString.toLowerCase()) || row.getCases().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    coronaVirusListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = coronaVirusListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                coronaVirusListFiltered = (ArrayList<CoronaVirus>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @NonNull
    @Override
    public CasesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycler_cases_shimmer, parent, false);

        return new CasesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CasesViewHolder holder, int position) {
        final CoronaVirus coronaVirus = coronaVirusListFiltered.get(position);
        String code = CountryList.convertNameToCode(context, coronaVirus.getCountry());
        holder.country_flag.setCountryCode(code); //  with Locale
        holder.country_name.setText(coronaVirus.getCountry().toUpperCase());
        holder.total_case.setText(String.valueOf(coronaVirus.getCases()));
        //holder.todayCases.setText(String.valueOf(coronaVirus.getTodayCases()));
        holder.deaths.setText(String.valueOf(coronaVirus.getDeaths()));
        //holder.todayDeaths.setText(String.valueOf(coronaVirus.getTodayDeaths()));
        holder.recovered.setText(String.valueOf(coronaVirus.getRecovered()));
        holder.critical.setText(String.valueOf(coronaVirus.getCritical()));
        //holder.active.setText(String.valueOf(coronaVirus.getActive()));

        holder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemView.performClick();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dDialog = new Dialog(context, android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
                dDialog.setContentView(R.layout.download_dialogue);
                nativeMainAdLayout = dDialog.findViewById(R.id.native_ad_container);
                loadNativeAd();

                TextView country_name = dDialog.findViewById(R.id.country);
                TextView total_case = dDialog.findViewById(R.id.caseValue);
                FlagImageView country_flag = dDialog.findViewById(R.id.img_location);

                TextView recovered = dDialog.findViewById(R.id.recovered);
                TextView critical = dDialog.findViewById(R.id.critical);
                TextView deaths = dDialog.findViewById(R.id.deaths);
                TextView todayCases = dDialog.findViewById(R.id.todayCases);
                TextView todayDeaths = dDialog.findViewById(R.id.todayDeaths);
                TextView active = dDialog.findViewById(R.id.active);
                TextView tv_fatality = dDialog.findViewById(R.id.fatality);

                country_flag.setCountryCode(code); //  with Locale
                country_name.setText(coronaVirus.getCountry().toUpperCase());
                total_case.setText(String.valueOf(coronaVirus.getCases()));
                todayCases.setText(String.valueOf(coronaVirus.getTodayCases()));
                deaths.setText(String.valueOf(coronaVirus.getDeaths()));
                todayDeaths.setText(String.valueOf(coronaVirus.getTodayDeaths()));
                recovered.setText(String.valueOf(coronaVirus.getRecovered()));
                critical.setText(String.valueOf(coronaVirus.getCritical()));
                active.setText(String.valueOf(coronaVirus.getActive()));

                double percentage = 100 * Double.parseDouble(coronaVirus.getDeaths())/ HomepageActivity.cases;
                String percentageText = String.format("%2.02f", percentage) +"%";
                tv_fatality.setText(percentageText);

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

    private void loadNativeAd() {
        // Instantiate a NativeAd object.
        // NOTE: the placement ID will eventually identify this as your App, you can ignore it for
        // now, while you are testing and replace it later when you have signed up.
        // While you are using this temporary code you will only get test ads and if you release
        // your code like this to the Google Play your users will not receive ads (you will get a no fill error).
        nativeAd = new NativeAd(context, "2987572304613026_2987573571279566");

        nativeAd.setAdListener(new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
                // Native ad finished downloading all assets
                Log.e(TAG, "Native ad finished downloading all assets.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Native ad failed to load
                Log.e(TAG, "Native ad failed to load: " + adError.getErrorMessage());
                //Toast.makeText(getApplicationContext(), "native Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Native ad is loaded and ready to be displayed
                Log.d(TAG, "Native ad is loaded and ready to be displayed!");
                //Toast.makeText(getApplicationContext(), "native Loaded", Toast.LENGTH_SHORT).show();

                if (nativeAd == null || nativeAd != ad) {
                    return;
                }
                // Inflate Native Ad into Container
                //lNativeAdsLayout.setVisibility(View.VISIBLE);
                inflateAd(nativeAd);
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Native ad clicked
                Log.d(TAG, "Native ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Native ad impression
                Log.d(TAG, "Native ad impression logged!");
            }
        });

        // Request an ad
        nativeAd.loadAd();
    }

    private void inflateAd(NativeAd nativeAd) {

        nativeAd.unregisterView();

        // Add the Ad view into the ad container.
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        adView = (LinearLayout) inflater.inflate(R.layout.native_ad_layout, nativeMainAdLayout, false);
        nativeMainAdLayout.addView(adView);

        // Add the AdOptionsView
        LinearLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(context, nativeAd, nativeMainAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        AdIconView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

        // Create a list of clickable views
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);

        // Register the Title and CTA button to listen for clicks.
        nativeAd.registerViewForInteraction(
                adView,
                nativeAdMedia,
                nativeAdIcon,
                clickableViews);
    }

    @Override
    public int getItemCount() {
        return coronaVirusListFiltered.size();
    }

    public interface onListener {
        void onItemSelected(CoronaVirus cases);
    }
}
