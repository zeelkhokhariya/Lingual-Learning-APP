package com.group5.lingual.presentation;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.group5.lingual.dso.languages.ILanguage;

//Finds selected text within a view hierarchy and generates a dictionary lookup based on that selection
public class DictionaryLookup
{
    //Given the root view of an activity, and the active language,
    //construct an intent to open a web page of an online dictionary for that language,
    //looking up selected text within that activity
    //Returns null if no selected text was found
    public static Intent createLookupIntent(View root, ILanguage language)
    {
        String selection = getSelectedText(root);
        if(selection.length() > 0)
        {
            Intent intent = new Intent(root.getContext(), DictionaryActivity.class);
            intent.putExtra(DictionaryActivity.getURLArgument(), language.getDictionaryURL(selection));
            return intent;
        }
        else
            return null;
    }

    //Given a view, find an instance of selected text within the view and its children, searching recursively
    //If no selected text could be found, return an empty string
    private static String getSelectedText(View root)
    {
        if(root instanceof TextView && ((TextView)root).hasSelection()) //If we've found a TextView, check it for a selection
        {
            TextView textRoot = (TextView)root;
            return textRoot.getText().toString().substring(textRoot.getSelectionStart(), textRoot.getSelectionEnd()).trim();
        }
        else if(root instanceof ViewGroup) //If this view has children, check its children
        {
            ViewGroup rootGroup = (ViewGroup)root;
            for(int i = 0; i < rootGroup.getChildCount(); i++)
            {
                String result = getSelectedText(rootGroup.getChildAt(i));
                if(result.length() > 0)
                    return result;
            }
        }

        return ""; //No selection found under this root
    }
}
