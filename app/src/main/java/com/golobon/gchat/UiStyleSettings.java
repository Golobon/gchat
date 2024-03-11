package com.golobon.gchat;

import android.app.Activity;
import android.graphics.Color;
import android.view.Window;
import android.view.WindowManager;

public class UiStyleSettings {
    public static void setUiStyle(Activity activity) {
        Window window = activity.getWindow();
        //Set background activity
        //window.getDecorView().setBackgroundColor(Color.WHITE);
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        window.setStatusBarColor(activity.getResources()
                .getColor(R.color.my_primary, activity.getTheme()));
    }
}
