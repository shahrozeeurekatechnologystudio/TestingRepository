package com.engagement.models.registeruser;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class EngagementUser implements Parcelable {


    private String firstName;
    private String lastName;
    private String language;
    private String latitude;
    private String longitude;
    private String companyKey;
    private String email;
    private String emailNotificationSubscription;
    private String country;
    private boolean isActive;
    private String phoneNumber;
    private String dateOfBirth;
    private String profileImageURL;
    private String userID;
    private String pushNotificationHeaderImage;
    private String imagesDirectoryName;
    private String pushNotificationHeaderBackgroundColor;
    private String pushNotificationHeaderTextColor;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public String getCompanyKey() {
        return companyKey;
    }

    public void setCompanyKey(String companyKey) {
        this.companyKey = companyKey;
    }



    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }


    public String getCountry() {
        return country;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }


    public String getEmailNotificationSubscription() {
        return emailNotificationSubscription;
    }

    public void setEmailNotificationSubscription(String emailNotificationSubscription) {
        this.emailNotificationSubscription = emailNotificationSubscription;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPushNotificationHeaderImage() {
        return pushNotificationHeaderImage;
    }

    public void setPushNotificationHeaderImage(String pushNotificationHeaderImage) {
        this.pushNotificationHeaderImage = pushNotificationHeaderImage;
    }

    public String getPushNotificationHeaderBackgroundColor() {
        return pushNotificationHeaderBackgroundColor;
    }

    public void setPushNotificationHeaderBackgroundColor(String pushNotificationHeaderBackgroundColor) {
        this.pushNotificationHeaderBackgroundColor = pushNotificationHeaderBackgroundColor;
    }

    public String getPushNotificationHeaderTextColor() {
        return pushNotificationHeaderTextColor;
    }

    public void setPushNotificationHeaderTextColor(String pushNotificationHeaderTextColor) {
        this.pushNotificationHeaderTextColor = pushNotificationHeaderTextColor;
    }

    public String getImagesDirectoryName() {
        return imagesDirectoryName;
    }

    public void setImagesDirectoryName(String imagesDirectoryName) {
        this.imagesDirectoryName = imagesDirectoryName;
    }


    @Override
    public String toString() {
        return "EngagementUser{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", language='" + language + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", companyKey='" + companyKey + '\'' +
                ", email='" + email + '\'' +
                ", emailNotificationSubscription='" + emailNotificationSubscription + '\'' +
                ", country='" + country + '\'' +
                ", isActive=" + isActive +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", profileImageURL='" + profileImageURL + '\'' +
                ", userID='" + userID + '\'' +
                ", pushNotificationHeaderImage='" + pushNotificationHeaderImage + '\'' +
                ", imagesDirectoryName='" + imagesDirectoryName + '\'' +
                ", pushNotificationHeaderBackgroundColor='" + pushNotificationHeaderBackgroundColor + '\'' +
                ", pushNotificationHeaderTextColor='" + pushNotificationHeaderTextColor + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.language);
        dest.writeString(this.latitude);
        dest.writeString(this.longitude);
        dest.writeString(this.companyKey);
        dest.writeString(this.email);
        dest.writeString(this.emailNotificationSubscription);
        dest.writeString(this.country);
        dest.writeByte(this.isActive ? (byte) 1 : (byte) 0);
        dest.writeString(this.phoneNumber);
        dest.writeString(this.dateOfBirth);
        dest.writeString(this.profileImageURL);
        dest.writeString(this.userID);
        dest.writeString(this.pushNotificationHeaderImage);
        dest.writeString(this.imagesDirectoryName);
        dest.writeString(this.pushNotificationHeaderBackgroundColor);
        dest.writeString(this.pushNotificationHeaderTextColor);
    }

    public EngagementUser() {
    }

    protected EngagementUser(Parcel in) {
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.language = in.readString();
        this.latitude = in.readString();
        this.longitude = in.readString();
        this.companyKey = in.readString();
        this.email = in.readString();
        this.emailNotificationSubscription = in.readString();
        this.country = in.readString();
        this.isActive = in.readByte() != 0;
        this.phoneNumber = in.readString();
        this.dateOfBirth = in.readString();
        this.profileImageURL = in.readString();
        this.userID = in.readString();
        this.pushNotificationHeaderImage = in.readString();
        this.imagesDirectoryName = in.readString();
        this.pushNotificationHeaderBackgroundColor = in.readString();
        this.pushNotificationHeaderTextColor = in.readString();
    }

    public static final Parcelable.Creator<EngagementUser> CREATOR = new Parcelable.Creator<EngagementUser>() {
        @Override
        public EngagementUser createFromParcel(Parcel source) {
            return new EngagementUser(source);
        }

        @Override
        public EngagementUser[] newArray(int size) {
            return new EngagementUser[size];
        }
    };
}
