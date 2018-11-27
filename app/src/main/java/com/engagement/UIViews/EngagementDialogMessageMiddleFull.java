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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.engagement.R;
import com.engagement.utils.ConstantFunctions;
import com.engagement.utils.Constants;


public class EngagementDialogMessageMiddleFull extends Dialog {

    private WebView webViewBanner;
    private Context context;

    public EngagementDialogMessageMiddleFull(Activity context, String msg, String displyType) {
        super(context, R.style.engagement_dialog_style_animation);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setWindowAnimations(R.style.engagement_translateDialogAnimation_slow);
        setCancelable(false);
        this.setCanceledOnTouchOutside(false);

        if (displyType.equals("full")) {
            setContentView(R.layout.engagement_layout_message_full_screen);
            getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        } else {
            setContentView(R.layout.engagement_layout_message_center);
            getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            CardView cardView = (CardView) findViewById(R.id.card_view_middle_container);
            cardView.getLayoutParams().height = ConstantFunctions.getHeightPixels(context) / 3;
        }
        this.context = context;
        setScreenViews();
        setWebViewClient();
        setScreenValues(msg);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setScreenViews() {
        webViewBanner = (WebView) findViewById(R.id.webView_msg_center);
        WebSettings webSettings = webViewBanner.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setDomStorageEnabled(true);
        ImageView ivClose = (ImageView) findViewById(R.id.iv_close);
        ivClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EngagementDialogMessageMiddleFull.this.dismiss();

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
                if (url.startsWith(Constants.CLOSE_DIALOG)) {
                    EngagementDialogMessageMiddleFull.this.dismiss();
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
