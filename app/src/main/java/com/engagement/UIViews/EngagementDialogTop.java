package com.engagement.UIViews;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
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

public class EngagementDialogTop extends Dialog {

    private WebView webViewBanner;
    private Context context;

    public EngagementDialogTop(Activity context, String msg, String position) {
        super(context, R.style.engagement_dialog_style_animation_top);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        this.setCanceledOnTouchOutside(false);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        setContentView(R.layout.engagement_layout_banner_top);
        CardView cardView = (CardView) findViewById(R.id.card_view_top_view);
        cardView.getLayoutParams().height = ConstantFunctions.getHeightPixels(context) / 3;
        this.context = context;
        setThemeStyle(position);
        setScreenViews();
        setWebViewClient();
        setScreenValues(msg);
    }

    private void setThemeStyle(String position) {
        try {
            Window window = getWindow();
            window.setFlags(
                    LayoutParams.FLAG_NOT_TOUCH_MODAL,
                    LayoutParams.FLAG_NOT_TOUCH_MODAL);
            window.setFlags(
                    LayoutParams.FLAG_NOT_FOCUSABLE,
                    LayoutParams.FLAG_NOT_FOCUSABLE);
            window.setFlags(LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
            LayoutParams lp = window.getAttributes();
            lp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
            lp.gravity = Gravity.TOP;
            getWindow().setWindowAnimations(R.style.engagement_translateDialogAnimation_slow_top);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setScreenViews() {
        webViewBanner = (WebView) findViewById(R.id.webView_banner_top);
        webViewBanner.setOverScrollMode(View.OVER_SCROLL_NEVER);
        webViewBanner.setVerticalScrollBarEnabled(false);
        webViewBanner.setHorizontalScrollBarEnabled(false);
        WebSettings webSettings = webViewBanner.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setDomStorageEnabled(true);
        ImageView ivClose = (ImageView) findViewById(R.id.iv_close);
        ivClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EngagementDialogTop.this.dismiss();
            }
        });
    }

    private void setScreenValues(String msg) {
        if (msg != null) {
            //webViewBanner.loadDataWithBaseURL(null, msg, "text/html", "UTF-8", null);
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
                if (url.startsWith(Constants.CLOSE_DIALOG) || url.startsWith(Constants.CLOSE_DIALOG_WITH_HTTPS)) {
                    EngagementDialogTop.this.dismiss();
                }else  {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        getContext().startActivity(intent);
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
