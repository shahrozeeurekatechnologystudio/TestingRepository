package com.engagement.UIViews;


import android.app.Activity;
import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.engagement.EngagementSdk;
import com.engagement.R;
import com.engagement.models.registeruser.EngagementUser;
import com.engagement.utils.Constants;

import java.util.Timer;
import java.util.TimerTask;


public class PushNotificationDialog extends Dialog {

    Activity context;

    public PushNotificationDialog(Activity context, String pushNotificationMessage) {
        super(context, R.style.engagement_translateDialogAnimation_slow);
        this.context = context;
        setContentView(R.layout.top_generic_msg_view_push_view_dialog);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.TOP);
        getWindow().setWindowAnimations(R.style.engagement_translateDialogAnimation_slow);
        setCancelable(false);
        this.setCanceledOnTouchOutside(false);
        setScreenViews(pushNotificationMessage);
        if (this.getWindow() != null)
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    private void setScreenViews(String pushNotificationMessage) {
        RelativeLayout rl_Notification_bar = (RelativeLayout) findViewById(R.id.rl_Notification_bar);
        ImageView ivImagePush = (ImageView) findViewById(R.id.iv_push);
        TextView tvPush = (TextView) findViewById(R.id.tv_push);
        if (EngagementSdk.getSingletonInstance() != null && EngagementSdk.getSingletonInstance().getEngagementUser() != null) {
            EngagementUser engagementUser = EngagementSdk.getSingletonInstance().getEngagementUser();
            if (engagementUser.getPushNotificationHeaderBackgroundColor() != null
                    && !engagementUser.getPushNotificationHeaderBackgroundColor().equalsIgnoreCase("")) {
                rl_Notification_bar.setBackgroundColor(Color.parseColor(engagementUser.getPushNotificationHeaderBackgroundColor()));
            }
            if (engagementUser.getPushNotificationHeaderImage() != null
                    && !engagementUser.getPushNotificationHeaderImage().equalsIgnoreCase("")) {
                String imagesDirectoryName;
                if (engagementUser.getImagesDirectoryName() != null
                        && !engagementUser.getImagesDirectoryName().equalsIgnoreCase("")) {
                    imagesDirectoryName = engagementUser.getImagesDirectoryName();
                } else {
                    imagesDirectoryName = Constants.DEFAULT_IMAGES_DIRECTORY_NAME;
                }
                Resources resources = context.getResources();
                int resourceId = resources.getIdentifier(engagementUser.getPushNotificationHeaderImage(), imagesDirectoryName,
                        context.getPackageName());
                //Drawable drawable = resources.getDrawable(resourceId);
                // ivImagePush.setImageDrawable(drawable);
                ivImagePush.setImageResource(resourceId);
            }
            if (engagementUser.getPushNotificationHeaderTextColor() != null
                    && !engagementUser.getPushNotificationHeaderTextColor().equalsIgnoreCase("")) {
                tvPush.setTextColor(Color.parseColor(engagementUser.getPushNotificationHeaderTextColor()));
            }
        }

        tvPush.setText(pushNotificationMessage);

    }

    public void showDialog() {
        show();
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        dismiss();
                    }
                }, 10000);
    }
}
