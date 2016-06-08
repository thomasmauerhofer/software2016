package com.bitschupfa.sw16.yaq.ui;


import android.annotation.TargetApi;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Build;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.activities.Host;
import com.bitschupfa.sw16.yaq.activities.Join;
import com.bitschupfa.sw16.yaq.activities.MainMenu;
import com.robotium.solo.Solo;


public class MainMenuTests extends ActivityInstrumentationTestCase2<MainMenu> {

    private Solo solo;

    public MainMenuTests() {
        super(MainMenu.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }

    public void testMainMenuHost() {
        solo.clickOnButton(getActivity().getResources().getString(R.string.host));
        assertTrue("Wrong Activity!", solo.waitForActivity(Host.class));
    }

    public void testAbout() {
        solo.clickOnImageButton(0);
        solo.clickOnText(getActivity().getString(R.string.action_about));
        solo.waitForActivity(MainMenu.class);
        assertTrue(solo.searchText(getActivity().getString(R.string.about_dialog_message)));
    }

    public void testProfile() {
        String name = "Holger";
        solo.clickOnImageButton(0);
        solo.clickOnImage(1);
        solo.clearEditText(solo.getEditText(0));
        solo.typeText(solo.getEditText(0), name);
        solo.goBack();

        assertTrue(solo.searchText(name));
    }

    public void testMainMenuJoin() {
        solo.clickOnButton(getActivity().getResources().getString(R.string.join));
        assertTrue("Wrong Activity!", solo.waitForActivity(Join.class));
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void testThemeBlue() {
        solo.waitForActivity(MainMenu.class);
        testTheme(getActivity().getResources().getColor(R.color.blue500, getActivity().getTheme()), R.string.theme1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void testThemePink() {
        solo.waitForActivity(MainMenu.class);
        testTheme(getActivity().getResources().getColor(R.color.pink500, getActivity().getTheme()), R.string.theme3);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void testThemeTeal() {
        solo.waitForActivity(MainMenu.class);
        testTheme(getActivity().getResources().getColor(R.color.teal500, getActivity().getTheme()), R.string.theme4);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void testThemeGreen() {
        solo.waitForActivity(MainMenu.class);
        testTheme(getActivity().getResources().getColor(R.color.greenlight500, getActivity().getTheme()), R.string.theme2);
    }

    private void testTheme(int expectedColor, int buttonStringId) {
        solo.clickOnImageButton(0);
        solo.clickOnText(getActivity().getString(R.string.action_themes));
        solo.clickOnText(getActivity().getResources().getString(buttonStringId));
        solo.waitForActivity(MainMenu.class);
        int currentColor = getActivity().themeChooser.getThemeStorage().getPrimaryColor();
        assertEquals(expectedColor, currentColor);
    }
}
