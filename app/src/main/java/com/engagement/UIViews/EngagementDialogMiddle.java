package com.engagement.UIViews;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
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
import com.engagement.utils.ConstantFunctions;
import com.engagement.utils.Constants;

import org.json.JSONObject;


public class EngagementDialogMiddle extends Dialog {

    private WebView webViewBanner;
    private Context context;
    private boolean isAutoClose = true;

    public EngagementDialogMiddle(Activity context, String msg, String displyType,boolean isAutoClose) {
        super(context, R.style.engagement_dialog_style_animation_middle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setWindowAnimations(R.style.engagement_translateDialogAnimation_slow_top);
        setCancelable(false);
        this.isAutoClose = isAutoClose;
        this.setCanceledOnTouchOutside(false);
        setContentView(R.layout.engagement_layout_message_center);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        CardView cardView = (CardView) findViewById(R.id.card_view_middle_container);
        cardView.getLayoutParams().height = ConstantFunctions.getHeightPixels(context) / 3;
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
                EngagementDialogMiddle.this.dismiss();

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

            private void performAction(String url) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                getContext().startActivity(intent);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith(Constants.CLOSE_DIALOG) || url.startsWith(Constants.CLOSE_DIALOG_WITH_HTTPS)) {
                    EngagementDialogMiddle.this.dismiss();
                }else  {
                    try {
                        if (isAutoClose) {
                            EngagementDialogMiddle.this.dismiss();
                            performAction(url);
                        } else {
                            performAction(url);
                        }
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                    PushSeenViewApiController.getSingletonInstance().hitSeenApi(Constants.MODE_CLICKED,url,new UserActionsListener() {
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
