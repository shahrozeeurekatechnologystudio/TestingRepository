package com.engagement.UIViews;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.engagement.R;
import com.engagement.utils.ConstantFunctions;
import com.engagement.utils.Constants;

public class DialogBannerTopBottom extends Dialog {

    private WebView webViewBanner;
    private Context context;

    public DialogBannerTopBottom(Activity context, String msg, String position) {
        super(context, R.style.engagement_dialog_style_animation_top_bottom);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        this.setCanceledOnTouchOutside(false);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        if (position.equals("top")) {
            setContentView(R.layout.layout_banner_top);
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.rl_top_view);
            relativeLayout.getLayoutParams().height = ConstantFunctions.getHeightPixels(context) / 3;
        } else {
            setContentView(R.layout.layout_banner_bottom);
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.rl_bottom_view);
            relativeLayout.getLayoutParams().height = ConstantFunctions.getHeightPixels(context) / 3;
        }
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

            window.clearFlags(LayoutParams.FLAG_DIM_BEHIND);
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            LayoutParams lp = window.getAttributes();
            lp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
            if (position.equals("top")) {
                lp.gravity = Gravity.TOP;
                getWindow().setWindowAnimations(R.style.engagement_translateDialogAnimation_slow);
            } else {
                lp.gravity = Gravity.BOTTOM;
                getWindow().setWindowAnimations(R.style.engagement_translateDialogAnimation_slow_bottom);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setScreenViews() {
        webViewBanner = (WebView) findViewById(R.id.webView_banner_top);
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
                DialogBannerTopBottom.this.dismiss();
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
                if (url.startsWith(Constants.CLOSE_DIALOG)) {
                    DialogBannerTopBottom.this.dismiss();
                } else {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        getContext().startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        };
        webViewBanner.setWebViewClient(client);
    }
}
