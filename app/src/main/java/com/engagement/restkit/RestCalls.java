package com.engagement.restkit;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.engagement.EngagementSdk;
import com.engagement.R;
import com.engagement.utils.Constants;
import com.engagement.utils.LoginUserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class RestCalls extends JsonObjectRequest {

    private Listener<JSONObject> listener;
    private static final String PROTOCOL_CHARSET = "utf-8";
    private JSONObject params;

    public RestCalls(int method, String url, JSONObject params,
                     Listener<JSONObject> reponseListener, ErrorListener errorListener) {
        super(method, url, params, reponseListener, errorListener);
        this.listener = reponseListener;
        this.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        this.params = params;
    }

    @Override
    public Priority getPriority() {
        // TODO Auto-generated method stub
        return super.getPriority();
    }

    @Override
    public RetryPolicy getRetryPolicy() {
        // TODO Auto-generated method stub
        return super.getRetryPolicy();
    }


    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }
    private String createJWT() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getDefault());
        calendar.add(Calendar.MINUTE, Constants.EXPIRE_TIME);
        JwtBuilder builder = null;
        if (EngagementSdk.getSingletonInstance() != null &&
                EngagementSdk.getSingletonInstance().getContext() != null && !LoginUserInfo.getValueForKey(Constants.LOGIN_USER_SESSION_TOKEN_KEY, "").equalsIgnoreCase("") &&
        !LoginUserInfo.getValueForKey(Constants.COMPANY_KEY, "").equalsIgnoreCase("")) {
            builder = Jwts.builder()
                    .setExpiration(calendar.getTime())
                    .claim("company_key", LoginUserInfo.getValueForKey(Constants.COMPANY_KEY, ""))
                    .claim("user_token", LoginUserInfo.getValueForKey(Constants.LOGIN_USER_SESSION_TOKEN_KEY, ""))
                    .signWith(SignatureAlgorithm.HS256, LoginUserInfo.getValueForKey(Constants.COMPANY_KEY, ""));
        } else if (EngagementSdk.getSingletonInstance() != null &&
                EngagementSdk.getSingletonInstance().getContext() != null && !LoginUserInfo.getValueForKey(Constants.COMPANY_KEY, "").equalsIgnoreCase("")) {
            builder = Jwts.builder()
                    .setExpiration(calendar.getTime())
                    .claim("company_key", LoginUserInfo.getValueForKey(Constants.COMPANY_KEY, ""))
                    .signWith(SignatureAlgorithm.HS256, LoginUserInfo.getValueForKey(Constants.COMPANY_KEY, ""));
        }
        return builder.compact();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        // TODO Auto-generated method stub
        Map<String, String> headers = super.getHeaders();

        if (headers == null || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<String, String>();
            //headers.put("Accept", "application/json");
            headers.put("Content-Type", "application/json");
            headers.put("Accept-Language",
                    "en;q=1, fr;q=0.9, de;q=0.8, zh-Hans;q=0.7, zh-Hant;q=0.6, ja;q=0.5");
            headers.put("Authorization", createJWT());
            PackageInfo pInfo = null;
            try {
                pInfo = EngagementSdk.getSingletonInstance().getContext().getPackageManager().getPackageInfo(EngagementSdk.getSingletonInstance().getContext().getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            headers.put("build", pInfo.versionCode + "");
            headers.put("version", pInfo.versionName);
            headers.put("device-type", "android");
            headers.put("timezone", TimeZone.getDefault().getID());
            headers.put("app-id", pInfo.packageName);
            if (EngagementSdk.getSingletonInstance() != null &&
                    EngagementSdk.getSingletonInstance().getContext() != null &&
            !LoginUserInfo.getValueForKey(Constants.LANGUAGE_KEY, "").equalsIgnoreCase(""))  {
                headers.put("lang", LoginUserInfo.getValueForKey(Constants.LANGUAGE_KEY, ""));
            } else {
                headers.put("lang", Locale.getDefault().getLanguage());
            }
            headers.put("app-name", EngagementSdk.getSingletonInstance().getContext().getString(R.string.app_name));
        }
        return headers;
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        // TODO Auto-generated method stub
        listener.onResponse(response);
    }
}
