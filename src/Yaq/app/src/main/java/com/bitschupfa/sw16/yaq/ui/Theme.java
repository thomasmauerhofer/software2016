package com.bitschupfa.sw16.yaq.ui;

/**
 * Created by Andrej on 04.06.2016.
 */
public class Theme {

    private int id;
    private String backgroundImageName;
    private String logoImageName;
    private String navigationDrawerImage;

    private int primaryColorId;
    private int primaryColorDarkId;
    private int primaryColor600Id;

    public Theme(int id, int primaryColorId, int primaryColorDarkId, int primaryColor600Id, String backgroundImageName, String logoImageName, String navigationDrawerImage) {
        this.setId(id);
        this.setBackgroundImageName(backgroundImageName);
        this.setLogoImageName(logoImageName);
        this.setNavigationDrawerImage1(navigationDrawerImage);
        this.setPrimaryColorId(primaryColorId);
        this.setPrimaryColor600Id(primaryColor600Id);
        this.setPrimaryColorDarkId(primaryColorDarkId);
    }

    public String getBackgroundImageName() {
        return backgroundImageName;
    }

    public void setBackgroundImageName(String backgroundImageName) {
        this.backgroundImageName = backgroundImageName;
    }

    public String getLogoImageName() {
        return logoImageName;
    }

    public void setLogoImageName(String logoImageName) {
        this.logoImageName = logoImageName;
    }

    public String getNavigationDrawerImage1() {
        return navigationDrawerImage;
    }

    public void setNavigationDrawerImage1(String navigationDrawerImage1) {
        this.navigationDrawerImage = navigationDrawerImage1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrimaryColorId() {
        return primaryColorId;
    }

    public void setPrimaryColorId(int primaryColorId) {
        this.primaryColorId = primaryColorId;
    }

    public int getPrimaryColorDarkId() {
        return primaryColorDarkId;
    }

    public void setPrimaryColorDarkId(int primaryColorDarkId) {
        this.primaryColorDarkId = primaryColorDarkId;
    }

    public int getPrimaryColor600Id() {
        return primaryColor600Id;
    }

    public void setPrimaryColor600Id(int primaryColor600Id) {
        this.primaryColor600Id = primaryColor600Id;
    }
}
