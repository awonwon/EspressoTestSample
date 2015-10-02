package example.com.espressotest;

import android.support.test.espresso.PerformException;
import android.support.test.espresso.action.EspressoKey;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.KeyEvent;
import android.widget.TextView;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.equalToIgnoringWhiteSpace;
import static org.hamcrest.Matchers.instanceOf;

/**
 * Created by GTW on 2015/9/25.
 */
@RunWith(AndroidJUnit4.class)
public class ActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);

    @Test
    public void Basic() throws InterruptedException {
        onView(withId(R.id.btnSummit)).perform(click());

        rest();

        /* 輸入文字 & 關閉鍵盤 */
        onView(withId(R.id.edit)).perform(typeText("I'm awonwonwon;"),closeSoftKeyboard());
        rest();
        onView(withId(R.id.edit)).perform(clearText());

        rest();
        /* 利用 allOf 使用多個 filter */
        onView(allOf(hasSibling(withText(equalToIgnoringWhiteSpace("OH YA"))), instanceOf(TextView.class), withText("耶嘿～"))).perform(click());
    }

    @Test
    public void openMenu(){
         /* Open ActionBar Menu*/
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Settings")).perform(click());
    }

    /* 加上 expected 可以略過一個 Exception*/
    @Test(expected = PerformException.class)
    public void onDataInterAction() throws InterruptedException {
        onView(withId(R.id.btnList)).perform(click());

        /* wait open intent */
        rest();

        onData(anything())
                .inAdapterView(withId(R.id.list)) //如果 layout 上有多個 AdapterView，可以用 inAdapterView 指定
                .atPosition(3)
                .perform(click());

        rest();

        /* 單純的 scrollTo */
        onData(anything())
                .inAdapterView(withId(R.id.list))
                .atPosition(4)
                .perform(scrollTo());

        rest();

        /* 如果是對 Item Click 的話，會直接幫忙 Scroll + Click */
        onData(allOf(instanceOf(ListActivity.Item.class)))
                .atPosition(9)
                .onChildView(withId(R.id.switch1))
                .perform(click());

    }

//    @Test
    public void pressKeyTest() throws InterruptedException {
        onView(withId(R.id.btnList)).perform(click());

        rest();

        EspressoKey.Builder builder = new EspressoKey.Builder();
        builder.withKeyCode(KeyEvent.KEYCODE_VOLUME_UP);
        builder.withKeyCode(KeyEvent.KEYCODE_MUTE);
        builder.withKeyCode(KeyEvent.KEYCODE_POWER);
        builder.withKeyCode(KeyEvent.KEYCODE_BACK);
        pressKey(builder.build());

        pressKey(KeyEvent.KEYCODE_POWER);
        onView(withId(R.id.list)).perform(pressKey(KeyEvent.KEYCODE_POWER));

        click();

//        rest();
//        pressBack();
    }

    @After
    public void close() throws InterruptedException {
        rest();
    }

    public void rest() throws InterruptedException {
        Thread.sleep(2 * 1000);
    }
}
