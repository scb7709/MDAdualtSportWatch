package com.headlth.management.myview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Created by abc on 2017/8/25.
 */
public class PubLicDialog {
    public interface PubLicDialogOnClickListener {
        void setPositiveButton();
    }

    public interface PubLicDialogOnClickListener2 {
        void setPositiveButton();

        void setNegativeButton();
    }

    static DialogInterface dialogInterface;

    // public static PubLicDialogOnClickListener PubLicDialogOnClickListener;
    public static DialogInterface showNotDialog(final Activity activity, String[] str, final PubLicDialogOnClickListener pubLicDialogOnClickListener) {
        // PubLicDialogOnClickListener = pubLicDialogOnClickListener;

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(str[0]);
        builder.setMessage(str[1]);
        builder.setPositiveButton(str[2],
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (pubLicDialogOnClickListener != null) {
                            pubLicDialogOnClickListener.setPositiveButton();
                        }
                        dialogInterface = dialog;
                        dialog.dismiss();
                    }
                });
        if (str.length > 3) {
            builder.setNegativeButton(str[3], new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialogInterface = dialog;
                }
            });
        }
        builder.setCancelable(false);
        builder.show();
        return dialogInterface;
    }

    public static AlertDialog dialog;

    public static void showNotDialog2(final Activity activity, String[] str, final PubLicDialogOnClickListener2 pubLicDialogOnClickListener) {
        if (dialog == null || !dialog.isShowing()) {

            dialog = null;
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(str[0]);
            builder.setMessage(str[1]);
            builder.setPositiveButton(str[2],
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (pubLicDialogOnClickListener != null) {
                                pubLicDialogOnClickListener.setPositiveButton();
                            }
                        }
                    });
            builder.setNegativeButton(str[3], new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (pubLicDialogOnClickListener != null) {
                        pubLicDialogOnClickListener.setNegativeButton();
                    }
                }
            });
            builder.setCancelable(false);
            dialog = builder.create();
            dialog.show();
        }
    }
    public static void CancleshowNotDialog2(){
        if (dialog == null ||dialog.isShowing()) {
            dialog.dismiss();
            dialog=null;
        }
    }
}
