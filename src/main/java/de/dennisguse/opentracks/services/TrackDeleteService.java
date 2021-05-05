package de.dennisguse.opentracks.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.dennisguse.opentracks.content.data.Track;
import de.dennisguse.opentracks.content.provider.ContentProviderUtils;

public class TrackDeleteService extends Service {

    static final String EXTRA_TRACK_IDS = "extra_track_ids";

    private final Binder binder = new Binder();
    private ExecutorService serviceExecutor;
    private MutableLiveData<Boolean> deleteResultObservable;

    @Override
    public void onCreate() {
        super.onCreate();
        serviceExecutor = Executors.newSingleThreadExecutor();
        deleteResultObservable = new MutableLiveData<>();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (serviceExecutor != null) {
            serviceExecutor.shutdownNow();
            serviceExecutor = null;
        }
        deleteResultObservable = null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    void deleteTracks(ArrayList<Track.Id> trackIds) {
        serviceExecutor.execute(() -> {
            ContentProviderUtils contentProviderUtils = new ContentProviderUtils(this);
            contentProviderUtils.deleteTracks(this, trackIds);
            deleteResultObservable.postValue(true);
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class Binder extends android.os.Binder {

        private Binder() {
            super();
        }

        public TrackDeleteService getService() {
            return TrackDeleteService.this;
        }
    }
}
