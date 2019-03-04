package com.orbitview.salesmanagement.view;

import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.orbitview.salesmanagement.R;

/**
 * Created by hamnaro on 21/11/18.
 */

public class CustomDialog {

    public void showDialog(final Activity activity, String msg){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog);

        TextView text = (TextView) dialog.findViewById(R.id.textDialog);
        text.setText(msg);

        Button btnConfirmYES = (Button) dialog.findViewById(R.id.btnConfirmYES);
        btnConfirmYES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT < 21) {
                    activity.finishAffinity();
                    System.exit(0);
                } else if (Build.VERSION.SDK_INT >= 21) {
                    activity.finishAndRemoveTask();
                    System.exit(0);
                }
            }
        });

        Button btnConfirmNO = (Button) dialog.findViewById(R.id.btnConfirmNO);
        btnConfirmNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}
