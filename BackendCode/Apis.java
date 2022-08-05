package microbi.organic.coworkuser.activity;

public class Apis {

    public String UserRegister = "http://65.0.139.141:3012/users";
    public String UserLogin = "http://65.0.139.141:3012/users/login";
    public String VenueList = "http://65.0.139.141:3012/venues?date=";
    public String FeatureList = "http://65.0.139.141:3012/features?venue_id=";
    public String Purchase = "http://65.0.139.141:3012/purchase";
    public String MyPurchase = "http://65.0.139.141:3012/purchase?user_id=";
    public String MyProfile = "http://65.0.139.141:3012/users/profile/";
    public String SubFeature = "http://65.0.139.141:3012/sub-features?venue_id=";

    public String getSubFeature() {
        return SubFeature;
    }

    public void setSubFeature(String subFeature) {
        SubFeature = subFeature;
    }

/// 604dc89164cdd09b1b412c01


    public String getMyProfile() {
        return MyProfile;
    }

    public void setMyProfile(String myProfile) {
        MyProfile = myProfile;
    }

    public String getMyPurchase() {
        return MyPurchase;
    }

    public void setMyPurchase(String myPurchase) {
        MyPurchase = myPurchase;
    }

    public String getUserRegister() {
        return UserRegister;
    }

    public void setUserRegister(String userRegister) {
        UserRegister = userRegister;
    }

    public String getUserLogin() {
        return UserLogin;
    }

    public void setUserLogin(String userLogin) {
        UserLogin = userLogin;
    }

    public String getVenueList() {
        return VenueList;
    }

    public void setVenueList(String venueList) {
        VenueList = venueList;
    }

    public String getFeatureList() {
        return FeatureList;
    }

    public void setFeatureList(String featureList) {
        FeatureList = featureList;
    }

    public String getPurchase() {
        return Purchase;
    }

    public void setPurchase(String purchase) {
        Purchase = purchase;
    }
}

