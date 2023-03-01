package com.example.a1a22ikeraadefinitiva;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import androidx.annotation.Nullable;

public class Preferences extends Activity {

    public static class AppPreferencesFragment extends PreferenceFragment{
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content , new AppPreferencesFragment()).commit();
    }
}

