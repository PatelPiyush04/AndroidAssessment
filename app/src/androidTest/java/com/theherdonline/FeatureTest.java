package com.theherdonline;

import android.widget.DatePicker;

import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import com.theherdonline.db.entity.LivestockCategory;
import com.theherdonline.db.entity.LivestockSubCategory;
import com.theherdonline.ui.main.MainActivity;

import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */


@RunWith(AndroidJUnit4.class)
@LargeTest
public class FeatureTest {

    /* @Rule
     public ActivityScenarioRule<FlashActivity> activityFlash = new ActivityScenarioRule<>(FlashActivity.class);
 */
    @Rule
    public ActivityScenarioRule<MainActivity> activityMain = new ActivityScenarioRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule grantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

   // private String stringToBetyped;
   // private IdlingResource mIdlingResource;

   /* @Rule
    public ActivityScenarioRule<MainActivity> activityMain = new ActivityScenarioRule<>(MainActivity.class);*/

/*    @Before
    public void registerIdlingResource() {
        activityMain.getScenario().onActivity(new ActivityScenario.ActivityAction<MainActivity>() {
            @Override
            public void perform(MainActivity activity) {
                mIdlingResource = activity.getIdlingResource();
                //IdlingRegistry.getInstance().register(mIdlingResource);
            }
        });
    }*/



    @Before
    public void login() throws Exception
    {
        onView(withId(R.id.navigation_me)).perform(click());
        onView(withId(R.id.button_login)).perform(click());
        onView(withId(R.id.edit_email)).perform(typeText("agent@agent.com"),closeSoftKeyboard());
        onView(withId(R.id.edit_password)).perform(typeText("secret"),closeSoftKeyboard());
        onView(withId(R.id.button_login)).perform(click());
        mySleep(5000);
    }

    @Test
    public void postLivestock() throws Exception {
        onView(withId(R.id.navigation_myad)).perform(click());
        onView(withId(R.id.view_paddock)).perform(click());

        //waiting
        String title = new DateTime().toString();
        onView(withId(R.id.image_right_1)).perform(click());
        onView(withId(R.id.edit_title)).perform(typeText(title), closeSoftKeyboard());
        onView(withId(R.id.edit_description)).perform(typeText("Test"), closeSoftKeyboard());
        onView(withId(R.id.edit_price)).perform(typeText("100"), closeSoftKeyboard());
        onView(withId(R.id.tv_spinner_price_type)).perform(click());
        onView(withText("Per Head")).perform(click());


        onView(withId(R.id.tv_spinner_offer_type)).perform(click());
        onData(allOf(is(instanceOf(String.class)))).atPosition(0).perform(click());
        mySleep(1000);
        onView(withId(R.id.checkbox_ono)).check(matches(isDisplayed()));
        onView(withId(R.id.checkbox_ono)).perform(click());

        //onView(withId(R.id.tv_spinner_offer_type)).perform(click());
        /*onData(allOf(is(instanceOf(String.class)))).atPosition(1).perform(click());
        mySleep(1000);
        onView(withId(R.id.checkbox_ono)).check(matches(not(isDisplayed())));*/


        onView(withId(R.id.tv_available_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020,3,23));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.tv_spinner_animal)).perform(newScrollTo(),click());
        onData(allOf(is(instanceOf(LivestockCategory.class)))).atPosition(0).perform(click());
        onView(withId(R.id.tv_spinner_breed)).perform(newScrollTo(),click());
        onData(allOf(is(instanceOf(LivestockSubCategory.class)))).atPosition(0).perform(click());


        //mySleep(1000);
        //onView(withId(R.id.frameLayout_age_unit)).perform(scrollTo());
        onView(withId(R.id.tv_age_unit)).perform(newScrollTo(),  click());
        onData(allOf(is(instanceOf(String.class)))).atPosition(0).perform(click());
        onView(withId(R.id.edit_min_age)).perform(newScrollTo(), typeText("10"), closeSoftKeyboard());
        onView(withId(R.id.edit_max_age)).perform(newScrollTo(),typeText("10"), closeSoftKeyboard());
        onView(withId(R.id.edit_average_age)).perform(newScrollTo(),typeText("10"), closeSoftKeyboard());
        onView(withId(R.id.edit_quantity)).perform(newScrollTo(),typeText("10"), closeSoftKeyboard());
        onView(withId(R.id.edit_weight_min)).perform(newScrollTo(),typeText("10"), closeSoftKeyboard());
        onView(withId(R.id.edit_weight_max)).perform(newScrollTo(),typeText("100"), closeSoftKeyboard());
        onView(withId(R.id.edit_weight_average)).perform(newScrollTo(),typeText("50"), closeSoftKeyboard());
        onView(withId(R.id.checkbox_detition)).perform(newScrollTo(),click());
        onView(withId(R.id.checkbox_mouthed)).perform(newScrollTo(),click());
        onView(withId(R.id.checkbox_msa)).perform(newScrollTo(),click());
        onView(withId(R.id.checkbox_lpa)).perform(newScrollTo(),click());
        onView(withId(R.id.checkbox_organic)).perform(newScrollTo(),click());
        onView(withId(R.id.checkbox_pcas)).perform(newScrollTo(),click());
        onView(withId(R.id.checkbox_weighed)).perform(newScrollTo(),click());
        onView(withId(R.id.checkbox_eu)).perform(newScrollTo(),click());


        //onData(allOf(is(instanceOf(Media.class)))).atPosition(0).perform(newScrollTo(),click());
       // onData(allOf(is(instanceOf(String.class)))).atPosition(0).perform(newScrollTo(), click());


        onView(withId(R.id.button_confirm)).perform(newScrollTo(),click());
        onView(withText("Publish")).perform(click());
        mySleep(2000);
        onView(withText("OK")).perform(click());
        onView(withId(R.id.navigation_search)).perform(click());
        onView(withId(R.id.navigation_myad)).perform(click());
        onView(withId(R.id.view_paddock)).perform(click());
        mySleep(5000);

        onView(withText(title)).check(matches(isDisplayed()));




    }


    @After
    public void logout() throws Exception
    {
        onView(withId(R.id.navigation_me)).perform(click());
        onView(withId(R.id.image_right_1)).perform(click());
        onView(withText("Logout")).perform(click());
        onView(withText("LOGOUT")).perform(click());
        mySleep(5000);
    }






    public void mySleep(long l) throws InterruptedException
    {
        Thread.sleep(l);
    }

    public NestedScrollViewScrollToAction  newScrollTo()
    {
        return new  NestedScrollViewScrollToAction();
    }



   /* @After
    public void unregisterIdlingResource(){
        if (mIdlingResource != null)
        {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }*/
}
