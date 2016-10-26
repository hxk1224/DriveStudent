package com.drive.student.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.drive.student.MainApplication;
import com.drive.student.R;

import java.util.HashMap;

public class SoundUtil {
    private static final String TAG = "SoundUtil";
    private static SoundUtil instance;
    private static SoundPool soundPool;
    private static HashMap<String, Integer> soundMap;
    private Context mContext;

    public static SoundUtil getInstance() {
        if (instance == null) {
            instance = new SoundUtil(MainApplication.getInstance());
        }
        return instance;
    }

    private SoundUtil(Context context) {
        mContext = context.getApplicationContext();
        init();
    }

    private void init() {
        if (soundPool == null) {
            LogUtil.e(TAG, "soundPool is null -->>");
            soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
            initSoundMap();
        }
        if (soundMap == null) {
            initSoundMap();
        }
    }

    private void initSoundMap() {
        LogUtil.e(TAG, "soundMap is null -->>");
        soundMap = new HashMap<>();
        soundMap.put("Msg", soundPool.load(mContext, R.raw.msg, 1));
        soundMap.put("0", soundPool.load(mContext, R.raw.voice_0, 1));
        soundMap.put("1", soundPool.load(mContext, R.raw.voice_1, 1));
        soundMap.put("2", soundPool.load(mContext, R.raw.voice_2, 1));
        soundMap.put("3", soundPool.load(mContext, R.raw.voice_3, 1));
        soundMap.put("4", soundPool.load(mContext, R.raw.voice_4, 1));
        soundMap.put("5", soundPool.load(mContext, R.raw.voice_5, 1));
        soundMap.put("6", soundPool.load(mContext, R.raw.voice_6, 1));
        soundMap.put("7", soundPool.load(mContext, R.raw.voice_7, 1));
        soundMap.put("8", soundPool.load(mContext, R.raw.voice_8, 1));
        soundMap.put("9", soundPool.load(mContext, R.raw.voice_9, 1));
    }

    /** 播放消息提示音 */
    public void playMsgSound() {
        init();
        soundPool.play(soundMap.get("Msg"), 1, 1, 0, 0, 1);
    }

    /**
     * 播放数字声音
     *
     * @param digit 数字"0"-"9",输入要读的数字
     */
    public void playDigitSound(String digit) {
        init();
        soundPool.play(soundMap.get(digit), 1, 1, 0, 0, 1);
    }

    public void release() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
        if (soundMap != null) {
            soundMap.clear();
            soundMap = null;
        }
    }
}
