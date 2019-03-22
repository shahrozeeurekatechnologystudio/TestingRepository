package com.engagement.restkit;

public class ApiUrl {
    private static final String USER_ACTION_LINK = "/";
    private static final String COMPANY_LOGIN_LINK = "/login";
    private static final String COMPANY_LOGOUT_LINK = "/logout";
    private static final String API_TRIGGER_LINK = "/";
    private static final String REGISTER_USER_URL = "/initialize/platform";
    private static final String NEWS_FEED_URL = "/";
    private static final String CAMPAIGN_TRIGGER_EVENT_URL = "/";
    private static final String PUSH_TRACK_VIEW_URL = "/";
    private static final String LIST_TRIGGER_EVENT_URL = "/";
    private static final String LIST_USER_ACTIONS_CONVERSIONS_RECORDED = "/get/user/actions";
    private static final String CONVERSION_TRIGGER_EVENT_URL = "/";
    private static String SITE_PREFIX = null;

    public static String getSitePrefix() {
        return SITE_PREFIX;
    }

    public static void setSitePrefix(String sitePrefix) {
        SITE_PREFIX = sitePrefix;
    }


    public static String getUserActionLink() {
        return getSitePrefix() + USER_ACTION_LINK;
    }

    public static String getRegisterUserUrl() {
        return getSitePrefix() + REGISTER_USER_URL;
    }

    public static String getNewsFeedUrl() {

        return getSitePrefix() + NEWS_FEED_URL;
    }

    public static String getCampaignTriggerEventUrl() {
        return getSitePrefix() + CAMPAIGN_TRIGGER_EVENT_URL;
    }

    public static String getCompanyLogoutLink() {
        return getSitePrefix() +  COMPANY_LOGOUT_LINK;
    }

    public static String getApiTriggerLink() {
        return getSitePrefix() + API_TRIGGER_LINK;
    }

    public static String getCompanyLoginLink() {
        return getSitePrefix() + COMPANY_LOGIN_LINK;
    }

    public static String getPushTrackViewUrl() {
        return getSitePrefix() + PUSH_TRACK_VIEW_URL;
    }

    public static String getListTriggerEventUrl() {
        return getSitePrefix() + LIST_TRIGGER_EVENT_URL;
    }

    public static String getListUserActionsConversionsRecorded() {
        return getSitePrefix() + LIST_USER_ACTIONS_CONVERSIONS_RECORDED;
    }

    public static String getConversionTriggerEventUrl() {
        return getSitePrefix() + CONVERSION_TRIGGER_EVENT_URL;
    }
}

