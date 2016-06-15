package com.example.alex.pluggedin.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.example.alex.pluggedin.R;
import com.example.alex.pluggedin.constants.Constants;

import static com.example.alex.pluggedin.constants.Constants.APP_PREFERENCES;
import static com.example.alex.pluggedin.constants.Constants.APP_PREFERENCES_FONT_SIZE;




public class ChoiceFontDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

    final static int LARGE_FONT_SIZE = 2;
    final static int NORMAL_FONT_SIZE = 1;
    final static int SMALL_FONT_SIZE = 0;

    SharedPreferences pref;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        pref = getActivity()
                .getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        float fontSize = pref.getFloat(APP_PREFERENCES_FONT_SIZE, Constants.FONT_SIZE_NORMAL);

        Resources res = getActivity().getResources();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(res.getString(R.string.choiceFontSize))
                .setSingleChoiceItems(res.getStringArray(R.array.typesFontSize), getNumItem(fontSize), this)
                .setNegativeButton(res.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });

        return builder.create();

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {


        SharedPreferences.Editor editor = pref.edit();
        switch (which) {
            case LARGE_FONT_SIZE:
                editor.putFloat(APP_PREFERENCES_FONT_SIZE, Constants.FONT_SIZE_LARGE);
                break;
            case NORMAL_FONT_SIZE:
                editor.putFloat(APP_PREFERENCES_FONT_SIZE, Constants.FONT_SIZE_NORMAL);
                break;
            case SMALL_FONT_SIZE:
                editor.putFloat(APP_PREFERENCES_FONT_SIZE, Constants.FONT_SIZE_SMALL);
                break;
        }

        String setUpFontSize = getActivity().getResources().getString(R.string.fontSizeSetUp);

        editor.apply();
        Toast.makeText(getActivity(), setUpFontSize, Toast.LENGTH_SHORT).show();
        dismiss();
    }

    // по коэффициенту возвращает номер пункта в диалоге
    private int getNumItem(float coefficient) {
        if (coefficient == Constants.FONT_SIZE_LARGE) {
            return LARGE_FONT_SIZE;
        } else if (coefficient == Constants.FONT_SIZE_NORMAL) {
            return NORMAL_FONT_SIZE;
        } else if (coefficient == Constants.FONT_SIZE_SMALL) {
            return SMALL_FONT_SIZE;
        }

        return -1;
    }
}
