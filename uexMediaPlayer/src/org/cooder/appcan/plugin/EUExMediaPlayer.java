package org.cooder.appcan.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.cooder.util.StreamUtil;
import org.zywx.wbpalmstar.engine.EBrowserView;
import org.zywx.wbpalmstar.engine.universalex.EUExBase;
import org.zywx.wbpalmstar.engine.universalex.EUExCallback;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;

public class EUExMediaPlayer extends EUExBase implements OnPreparedListener {

    public final static String LOG_TAG = "uexMediaPlayer";
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private Context context;
    private String mediaPath;

    public EUExMediaPlayer(Context context, EBrowserView view) {
        super(context, view);
        this.context = context;
    }

    public void open(String[] param) {
        try {
            mediaPath = context.getCacheDir().getAbsolutePath() + File.separator + param[0];
            File file = new File(mediaPath);
            if (!file.exists()) {
                InputStream is = context.getAssets().open("widget" + File.separator + param[0]);
                FileOutputStream fos = new FileOutputStream(mediaPath);
                StreamUtil.copyStream(is, fos);
            }
            init();
        } catch (Exception e) {
            Log.e(LOG_TAG, "failed to open media.", e);
        }
    }

    private void init() {
        mediaPlayer.reset();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(mediaPath);
            mediaPlayer.setDataSource(fis.getFD());
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            Log.e(LOG_TAG, "failed to prepare media.", e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public void play(String[] params) {
        mediaPlayer.start();
    }

    public void stop(String[] params) {
        mediaPlayer.pause();
        mediaPlayer.seekTo(0);
    }

    public void pause(String[] params) {
        mediaPlayer.pause();
    }

    public void resume(String[] params) {
        mediaPlayer.start();
    }

    public void setVolume(String[] params) {
        if (params == null || params.length != 2) {
            return;
        }
        mediaPlayer.setVolume(Float.parseFloat(params[0]), Float.parseFloat(params[1]));
    }

    public void seekTo(String[] params) {
        if (params == null || params.length != 1) {
            return;
        }
        mediaPlayer.seekTo(Integer.parseInt(params[0]));
    }

    @Override
    protected boolean clean() {
        mediaPlayer.reset();
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        String json = "{\"duration\":" + mp.getDuration() + "}";
        jsCallback("uexMediaPlayer.onPrepared", 0, EUExCallback.F_C_JSON, json);
    }
}
