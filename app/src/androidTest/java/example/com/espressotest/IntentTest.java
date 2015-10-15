package example.com.espressotest;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

//import static android.support.test.espresso.Espresso.pressBack;

/**
 * Created by GTW on 2015/9/25.
 */
@RunWith(AndroidJUnit4.class)
public class IntentTest {
    /* intent 要記得加 androidTestCompile 'com.android.support.test.espresso:espresso-intents:2.2.1' */

    @Rule
    public IntentsTestRule<MainActivity> mActivityRule = new IntentsTestRule(MainActivity.class);

    @Test
    public void intendingResultTest() throws InterruptedException {
        Intent resultData = new Intent();
        resultData.putExtra("msg","IntendingResult測試用");
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);

        intending(hasComponent("example.com.espressotest.ResultActivity")).respondWith(result);

        onView(withId(R.id.btnResult)).perform(click());

        rest();

    }

    @Test
    public void intentDialerTest() throws InterruptedException {
        String PACKAGE_NAME = "com.android.mms";
        String number = "0123456";
        Uri INTENT_DATA_PHONE_NUMBER = Uri.parse("sms:" + number);

        onView(withId(R.id.btnSMS)).perform(click());

        rest();

        /* 捕捉發出去的 intent 是否與下面測試的 intent 一樣正確*/
        intended(allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData(INTENT_DATA_PHONE_NUMBER),
                toPackage(PACKAGE_NAME)));

        rest();

        pressBack();

    }



    @After
    public void close() throws InterruptedException {
        rest();
    }

    /* rest 僅是將每一個操措測試變為慢動作，瞭解測試在做什麼 */
    public void rest() throws InterruptedException {
        Thread.sleep(3 * 1000);
    }
}
