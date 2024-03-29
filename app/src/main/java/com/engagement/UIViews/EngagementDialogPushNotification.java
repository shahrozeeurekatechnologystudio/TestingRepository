package com.engagement.UIViews;


import android.app.Activity;
import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.engagement.EngagementSdk;
import com.engagement.R;
import com.engagement.controllers.PushSeenViewApiController;
import com.engagement.interfaces.DeepLinkActionsListener;
import com.engagement.interfaces.UserActionsListener;
import com.engagement.models.registeruser.EngagementUser;
import com.engagement.utils.ConstantFunctions;
import com.engagement.utils.Constants;
import com.engagement.utils.LoginUserInfo;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;


public class EngagementDialogPushNotification extends Dialog implements View.OnClickListener {

    private Activity context;
    private ImageView ivClose;
    private TextView tvTitle;
    private TextView tvMsg;
    private String deepLinkUri;
    private DeepLinkActionsListener deepLinkActionsListener;
    private String icon;
    private String backgroundColor;
    private String title;
    private String msg;

    public EngagementDialogPushNotification(Activity context, String title, String msg, String icon, String backgroundColor, String deepLinkUri, DeepLinkActionsListener deepLinkActionsListener) {
        super(context, R.style.engagement_dialog_style_animation_push);
        this.context = context;
        this.icon = icon;
        this.backgroundColor = backgroundColor;
        this.deepLinkUri = deepLinkUri;
        this.deepLinkActionsListener = deepLinkActionsListener;
        this.title = title;
        this.msg = msg;
        setContentView(R.layout.engagement_top_generic_msg_view_push_view_dialog);
        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        window.setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.TOP);
        getWindow().setWindowAnimations(R.style.engagement_translateDialogAnimation_slow_top);
        setCancelable(false);
        this.setCanceledOnTouchOutside(false);
        setScreenViews();
        if (this.getWindow() != null)
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    private void setScreenViews() {
        CardView cardView = (CardView) findViewById(R.id.card_view_Notification_bar);
        ImageView ivImagePush = (ImageView) findViewById(R.id.iv_push);
        ivClose = (ImageView) findViewById(R.id.iv_close);
        ivClose.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setOnClickListener(this);
        tvMsg = (TextView) findViewById(R.id.tv_msg);
        tvMsg.setMovementMethod(new ScrollingMovementMethod());
        tvMsg.setOnClickListener(this);
        if (EngagementSdk.getSingletonInstance() != null && EngagementSdk.getSingletonInstance().getEngagementUser() != null) {
            EngagementUser engagementUser = EngagementSdk.getSingletonInstance().getEngagementUser();

            if (backgroundColor != null && !backgroundColor.equalsIgnoreCase("")) {
                cardView.setCardBackgroundColor(Color.parseColor(backgroundColor));
            } else if (engagementUser.getPushNotificationHeaderBackgroundColor() != null
                    && !engagementUser.getPushNotificationHeaderBackgroundColor().equalsIgnoreCase("")) {
                cardView.setCardBackgroundColor(Color.parseColor(engagementUser.getPushNotificationHeaderBackgroundColor()));
            }
            if (engagementUser.isPushBannerIconHidden()) {
                ivImagePush.setVisibility(View.GONE);
            } else {
                ivImagePush.setVisibility(View.VISIBLE);
                if (icon != null && !icon.equalsIgnoreCase("")) {
                    ConstantFunctions.loadRGBImage(context, icon, ivImagePush);
                } else {
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
                }
            }
            if (engagementUser.getPushNotificationHeaderTitleTextColor() != null
                    && !engagementUser.getPushNotificationHeaderTitleTextColor().equalsIgnoreCase("")) {
                tvTitle.setTextColor(Color.parseColor(engagementUser.getPushNotificationHeaderTitleTextColor()));
            }
            if (engagementUser.getPushNotificationHeaderBodyTextColor() != null
                    && !engagementUser.getPushNotificationHeaderBodyTextColor().equalsIgnoreCase("")) {
                tvMsg.setTextColor(Color.parseColor(engagementUser.getPushNotificationHeaderBodyTextColor()));
            }
            if (engagementUser.getPushNotificationHeaderTitleTextSize() != null
                    && !engagementUser.getPushNotificationHeaderTitleTextSize().equalsIgnoreCase("")) {
                tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(engagementUser.getPushNotificationHeaderTitleTextSize()));
            }
            if (engagementUser.getPushNotificationHeaderBodyTextSize() != null
                    && !engagementUser.getPushNotificationHeaderBodyTextSize().equalsIgnoreCase("")) {
                tvMsg.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(engagementUser.getPushNotificationHeaderBodyTextSize()));
            }
        }

        tvTitle.setText(title);
        tvMsg.setText(msg);

    }

    public void showDialog() {
        show();
        if (EngagementSdk.getSingletonInstance() != null &&
                EngagementSdk.getSingletonInstance().getEngagementUser() != null &&
                EngagementSdk.getSingletonInstance().getEngagementUser().getPushNotificationBarHideTimeInMilliseconds() != null && !EngagementSdk.getSingletonInstance().getEngagementUser().getPushNotificationBarHideTimeInMilliseconds().equalsIgnoreCase("")
                && Long.parseLong(EngagementSdk.getSingletonInstance().getEngagementUser().getPushNotificationBarHideTimeInMilliseconds()) != 0) {

            new Timer().schedule(
                    new TimerTask() {
                        @Override
                        public void run() {
                            dismiss();
                        }
                    }, Long.parseLong(EngagementSdk.getSingletonInstance().getEngagementUser().getPushNotificationBarHideTimeInMilliseconds()));
        }

    }

    @Override
    public void onClick(View v) {
        if (v == ivClose) {
            dismiss();
        } else if (v == tvMsg || v == tvTitle) {
            if (deepLinkUri != null && !deepLinkUri.equalsIgnoreCase("")) {
                if (deepLinkActionsListener != null) {
                    deepLinkActionsListener.onDeepLinkReturn(deepLinkUri.trim());
                    if (!LoginUserInfo.getValueForKey(Constants.TRACK_KEY, "").equalsIgnoreCase("")){
                        PushSeenViewApiController.getSingletonInstance().hitSeenApi(Constants.MODE_CLICKED,deepLinkUri, LoginUserInfo.getValueForKey(Constants.TRACK_KEY, ""),new UserActionsListener() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onCompleted(JSONObject object) {

                            }

                            @Override
                            public void onError(String exception) {

                            }
                        });
                    }
                    dismiss();
                }
            }
        }

    }
}
