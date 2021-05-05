package de.dennisguse.opentracks.services;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import java.util.ArrayList;

import de.dennisguse.opentracks.BuildConfig;
import de.dennisguse.opentracks.content.data.Track;

import static de.dennisguse.opentracks.services.TrackDeleteService.EXTRA_TRACK_IDS;

public class TrackDeleteServiceConnection implements ServiceConnection {

    private TrackDeleteService trackDeleteService;
    private ArrayList<Track.Id> trackIds;

    public TrackDeleteServiceConnection(ArrayList<Track.Id> trackIds) {
        this.trackIds = trackIds;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        trackDeleteService = ((TrackDeleteService.Binder) service).getService();
        trackDeleteService.deleteTracks(trackIds);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        trackDeleteService = null;
    }

    public void startAndBind(Context context) {
        if (trackDeleteService != null) {
            return;
        }

        Intent intent = new Intent(context, TrackDeleteService.class)
                .putParcelableArrayListExtra(EXTRA_TRACK_IDS, trackIds);
        context.startService(intent);

        bind(context);
    }

    public void bind(Context context) {
        if (trackDeleteService != null) {
            return;
        }
        context.bindService(new Intent(context, TrackDeleteService.class), this, BuildConfig.DEBUG ? Context.BIND_DEBUG_UNBIND : 0);
    }

    public void unbind(Context context) {
        context.unbindService(this);
        trackDeleteService = null;
    }

    public TrackDeleteService getServiceIfBound() {
        return trackDeleteService;
    }
}
