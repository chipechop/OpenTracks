package de.dennisguse.opentracks.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class TrackDeleteService extends Service {

    private final Binder binder = new Binder();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
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
