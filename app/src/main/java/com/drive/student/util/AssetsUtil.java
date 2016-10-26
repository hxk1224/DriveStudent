package com.drive.student.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AssetsUtil {
    private Activity activity;
    private String[] files;

    public AssetsUtil(Context context) {
        this.activity = (Activity) context;

    }

    public String[] getfileFromAssets(String path) {
        AssetManager assetManager = activity.getAssets();
        try {
            files = assetManager.list(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }

    public String getFromAssets(String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(activity.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null) {
                Result += line;
            }
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void getFromAssetsAndSave(String openFileName, String dirPath, String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(activity.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null) {
                Result += line;
            }
            FileUtil.writeFile(Result, dirPath, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}