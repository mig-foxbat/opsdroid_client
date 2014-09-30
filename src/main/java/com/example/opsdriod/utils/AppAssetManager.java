package com.example.opsdriod.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Created by chlr on 9/23/14.
 */
public class AppAssetManager {

    public String readFileFromAsset(String file) {

        Context context = AppObjectRepository.getContext();
        StringBuilder builder = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(context.getAssets().open(file)));
            String line;
            while((line = br.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        }
        catch (IOException e) {
            Log.e(this.getClass().getName(),e.getMessage());
            e.printStackTrace();
            return null;
        }


    }

}
