package com.mdgd.commons.components.repo.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mdgd.commons.components.repo.network.schemas.QuakeSchema;
import com.mdgd.commons.components.repo.network.schemas.QuakesSchema;
import com.mdgd.commons.dto.Quake;
import com.mdgd.commons.result.ICallback;
import com.mdgd.commons.retrofit_support.BasicNetwork;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Max
 * on 30-Apr-17.
 */
public class NetworkManager extends BasicNetwork implements INetwork {
    private final IQuakesAPI mRetrofitInterface;
    private final SimpleDateFormat mSDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()); // 2017-05-02T10:52:57

    public NetworkManager() {
        final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        final Interceptor interceptor = (Interceptor.Chain chain) -> {
            final Request original = chain.request();
            final Request.Builder rBuilder = original.newBuilder()
                    .header("Accept", "application/json")
                    .method(original.method(), original.body());
            return chain.proceed(rBuilder.build());
        };

        final OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(loggingInterceptor)
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .build();

        final Gson gson = new GsonBuilder().serializeNulls().create();
        final Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(Urls.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        mRetrofitInterface = mRetrofit.create(IQuakesAPI.class);
    }

    /**
     * @param start - date in format yyyy-mm-dd
     * @param end - date in format yyyy-mm-dd
     */
    private void getEarthquakes(String start, String end, @NotNull ICallback<List<Quake>> listener){
        execAsync(mRetrofitInterface.getQuakes(start, end), listener, (@Nullable QuakesSchema body) -> {
            final List<Quake> dtos = new ArrayList<>();
            if(body == null) return dtos;
            for(QuakeSchema qs : body.getEarthquakes()){
                dtos.add(qs.fillQuake(new Quake()));
            }
            return dtos;
        });
    }

    public void getEarthquakes(@NotNull Date start, @NotNull Date end, @NotNull ICallback<List<Quake>> listener){
        getEarthquakes(mSDF.format(start), mSDF.format(end), listener);
    }

    private void getEarthquakes(long startDate, @NotNull ICallback<List<Quake>> listener) {
        final Calendar cal = Calendar.getInstance();
        if(startDate != -1) cal.setTimeInMillis(startDate);

        getEarthquakes(mSDF.format(cal.getTime()), "NOW", listener);
    }

    public void checkNewEarthquakes(long lastUpdate, @NotNull ICallback<List<Quake>> listener){
        getEarthquakes(lastUpdate, listener);
    }
}
