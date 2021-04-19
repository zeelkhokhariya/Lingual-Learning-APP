package com.group5.lingual.presentation;

import androidx.appcompat.app.AppCompatActivity;

import com.group5.lingual.logic.SaveManager;

//An activity that ensures data integrity is safe whenever the activity stops
public class DataSafeActivity extends AppCompatActivity
{
    @Override
    protected void onStop()
    {
        super.onStop();
        SaveManager.save();
    }
}
