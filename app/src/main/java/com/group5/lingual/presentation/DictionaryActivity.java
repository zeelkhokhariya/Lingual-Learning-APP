package com.group5.lingual.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import com.group5.lingual.R;

//Displays a page of an online dictionary using a given URL
public class DictionaryActivity extends AppCompatActivity
{
    //Label for the activity's intent argument
    private static final String URL_ARG = "url";

    //Getter for the activity's argument labels
    public static String getURLArgument() { return URL_ARG; }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        WebView page = findViewById(R.id.dict_web_lookup_page);
        page.getSettings().setJavaScriptEnabled(true);
        page.loadUrl(getIntent().getStringExtra(URL_ARG));
    }
}