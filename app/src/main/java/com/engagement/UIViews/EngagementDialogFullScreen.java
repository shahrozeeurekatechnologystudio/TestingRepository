package com.engagement.UIViews;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.engagement.R;
import com.engagement.controllers.PushSeenViewApiController;
import com.engagement.interfaces.UserActionsListener;
import com.engagement.utils.Constants;

import org.json.JSONObject;


public class EngagementDialogFullScreen extends Dialog {

    private WebView webViewBanner;
    private Context context;
    private  boolean isAutoClose;
    private String trackKey;

    public EngagementDialogFullScreen(Activity context, String msg, String displyType, boolean isAutoClose,String trackKey) {
        super(context, R.style.engagement_dialog_style_animation_full_screen);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setWindowAnimations(R.style.engagement_translateDialogAnimation_slow_top);
        setCancelable(false);
        this.isAutoClose = isAutoClose;
        this.trackKey = trackKey;
        this.setCanceledOnTouchOutside(false);
            setContentView(R.layout.engagement_layout_message_full_screen);
            getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        window.setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
        this.context = context;
        setScreenViews();
        setWebViewClient();
        setScreenValues(msg);
    }


    private void performAction(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        getContext().startActivity(intent);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setScreenViews() {
        webViewBanner = (WebView) findViewById(R.id.webView_msg_center);
        webViewBanner.setOverScrollMode(View.OVER_SCROLL_NEVER);
        WebSettings webSettings = webViewBanner.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setDomStorageEnabled(true);
        ImageView ivClose = (ImageView) findViewById(R.id.iv_close);
        ivClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EngagementDialogFullScreen.this.dismiss();

            }
        });
    }

    private void setScreenValues(String msg) {
        if (msg != null) {
            //webViewBanner.loadDataWithBaseURL(null, msg, "text/html", "utf-8", null);
            webViewBanner.loadUrl(msg);
        }
    }

    private void setWebViewClient() {
        WebViewClient client = new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                show();
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith(Constants.CLOSE_DIALOG) || url.startsWith(Constants.CLOSE_DIALOG_WITH_HTTPS)|| url.contains(Constants.CLOSE)) {
                    EngagementDialogFullScreen.this.dismiss();
                } else  {
                    try {
                        if (isAutoClose) {
                            EngagementDialogFullScreen.this.dismiss();
                            performAction(url);
                        } else {
                            performAction(url);
                        }
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                    PushSeenViewApiController.getSingletonInstance().hitSeenApi(Constants.MODE_CLICKED,url,trackKey,new UserActionsListener() {
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
                return true;
            }
        };
        webViewBanner.setWebViewClient(client);
    }
}
