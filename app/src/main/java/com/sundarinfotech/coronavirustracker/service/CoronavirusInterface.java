package com.sundarinfotech.coronavirustracker.service;

import com.sundarinfotech.coronavirustracker.viewmodel.CoronaVirus;
import com.sundarinfotech.coronavirustracker.viewmodel.CoronaVirusResume;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CoronavirusInterface {

    @GET("all")
    public Call<CoronaVirusResume> getCoronaVirusTotalWorldWide();

    @GET("countries")
    public Call<List<CoronaVirus>> getCoronaVirusCompleteInformation();

    @GET("countries/vietnam")
    public Call<CoronaVirus> getCoronaVirusTotalVietName();
}
