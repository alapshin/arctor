package com.alapshin.arctor.sample.di.modules;

import android.accounts.AccountManager;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.provider.Settings;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SystemServicesModule {
    private final Application application;

    public SystemServicesModule(Application app) {
        this.application = app;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return application;
    }

    @Provides
    @Singleton
    AccountManager provideAccountManager(Context context) {
        return  (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
    }

    @Provides
    @Singleton
    AssetManager provideAssetManager(Context context) {
        return context.getAssets();
    }

    @Provides
    @Singleton
    ConnectivityManager provideConnectivityManager(Context context) {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Provides
    @Singleton
    ContentResolver provideContentResolver(Context context) {
        return context.getContentResolver();
    }

    @Provides
    @Singleton
    LocationManager provideLocationManager(Context context) {
        return (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    @Provides
    @Singleton
    Settings provideSettings() {
        return null;
    }

    @Provides
    @Singleton
    SharedPreferences providePreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
