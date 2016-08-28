package ru.tulupov.alex.pluggedin.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import ru.tulupov.alex.pluggedin.AppRater;
import ru.tulupov.alex.pluggedin.R;

import static ru.tulupov.alex.pluggedin.constants.Constants.PACKAGE_APP;

public class RaterDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final Resources res = getContext().getResources();


        builder.setTitle(res.getString(R.string.titleRaterDialog));
        builder.setPositiveButton(res.getString(R.string.btnRaterPositive), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AppRater.dontShowRaterDialogAgain(getContext());

                Uri uri = Uri.parse(res.getString(R.string.linkToPlayMarket) + PACKAGE_APP);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                getContext().startActivity(intent);
                dismiss();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });


        return builder.create();
    }
}
