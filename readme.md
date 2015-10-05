# Espresso Test Sample

練習 Espresso 的範例。

Espresso 是一個自動化測試工具，針對單一 app 模擬使用者的互動。提供同步化測試，在使用者手動測試 UI時，Espresso 會自動去偵測 main thread 是否為閒置狀態，來決定何時運行測試的程式碼。
Espresso 是一個以工具基礎的 API ，並利用 AndroidJunitRunner 來執行。

> **Note**: 本篇使用 JUnit 4 的版本撰寫為主。


## Set Up Espresso 建置

### Step 1 設定 build.gradle
* 設定所需的 libraries

```
dependencies {
    // App's dependencies, including test
    compile 'com.android.support:support-annotations:22.2.0'
    androidTestCompile 'com.android.support.test:runner:0.3'
    androidTestCompile 'com.android.support.test:rules:0.3'
    androidTestCompile 'com.android.support.test.espresso:espresso-core:2.2'

    // Optional : 測 Intent 時會使用到
    androidTestCompile 'com.android.support.test.espresso:espresso-intents:2.2.1'

    // Optional : ViewMatcher 的額外 Support Library
    androidTestCompile 'org.testinfected.hamcrest-matchers:hamcrest-matchers:1.8'
}
```

* 在 config 上加入 TestRunner

```
android {
    defaultConfig {
        testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
    }
}
```

<br /><br />
### Step 2 關閉 animation (Optional)
avoid flakiness.
關閉 android 測試裝置上的開發 animation 選項

「**設定 > 開發者工具 > 視窗動畫縮放、轉換動畫縮放、動畫影像時間伸縮效果**」

<br /><br />
### Step 3 初始化 Test
在 androidTest/java 資料夾下增加一個 Java Class 並繼承 ActivityInstrumentationTestCase2<要測試的Activity Name>

如果有需要做初始化的設定，可以寫一個方法並使用 **@Before** 加註在方法前 （這是 JUnit 的寫法）

``` java
@RunWith(AndroidJUnit4.class) // run test runner，並使用 JUnit 4
@LargeTest // 代表這個 Test 會跑比較久
public class ExpressoTestEx{
    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule(LoginActivity.class);

    @Before
    public void custom() throws Exception {
        //do something…
    }
}
```

<br /><br />
### Step 4 撰寫測試
開始在 class 中寫測試，在自定義的 test method 前加上 **annotation @Test**

每個 @Test 都是獨立互不牴觸的一次測試

如果有要撇除特定 Exception 的話，可以在 @Test 後方加上 **(expected=Exception.class)**


```java
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
...
    @Test
    public void checkLogin() {
      // Test Code
        onView(withId(R.id.btn_login)).perform(click());
    }
```
Espresso 用法在下方 [參考](https://github.com/awonwon/EspressoTestSample#espresso-%E4%BD%BF%E7%94%A8%E6%96%B9%E6%B3%95)

<br /><br />
### Step 5 結束前的動作

如果在結束測試時，需要額外做什麼動作，可以自己寫一個方法並且加上 **annotation @After**
```java
@After
public void custom() throws Exception {
  // do something
}
```
This is important to not leak any of the any objects from your tests

<br /><br />
### Step 6 設定 Run Test Configuration

* 選擇「**Run > Edit Configuration** 」

  打開視窗後，點選左上角的的「**+**」新增一個 **Android Tests**，並命名你的 Run Configuration

* 在 **General Tab ** 下的 Module 選擇 「**App**」


<br /><br />
### Step 7 奔跑吧！Test
開始 Run Test 時，記得要把手機解鎖讓他成功執行。


<br /><br /><br /><br /><br />
## Espresso 使用方法
###  **基本使用**
```java
onView(/*ViewMatcher*/))  // onView will reutrn ViewInteration Object
  .perform(/*ViewAction*/)
  .check(/*ViewAssertion*/)
```
主要分成四部分：

1. **ViewMatcher**：找到 View
2. **ViewInteration**：取得對應 View 的物件
3. **ViewAction**：對 View 做某件事
4. **ViewAssertion**：Assert 驗證 View 狀態

> **Note**:  Espresso Cheat Sheet 可參考[這裡](https://github.com/googlesamples/android-testing/blob/master/downloads/espresso-cheat-sheet-2.1.0.png?raw=true)。


範例：
```java
onView(withId(R.id.test_view)) // Matcher
  .perform(click()) // Action
  .check(matched(isDisplayed())); //Assert
```
執行的流程為：
1. Espresso 跟 View 進行互動，須指定一個 view，可透過 onView 取得 [ViewInteration](https://developer.android.com/reference/android/support/test/espresso/ViewInteraction.html) 物件，進行後續操作

2. Matcher 根據 R.id.test_view 找到 View

3. 對這個 View 做 Click 的動作

4. 最後 Assert 這個 View 是否為顯示狀態，若為 false 就會跳出 Assert 並結束 Test

> **Note**: onView() 方法並不會檢查指定的 View 是否存在，ViewMatcher 會在現有的 View hierarchy 尋找，如果沒有符合條件的 View， onView 則會拋出 NoMatchingViewException 。

<br /><br />

####  **[ViewMatcher](https://developer.android.com/reference/android/support/test/espresso/matcher/ViewMatchers.html)** : 負責找View
ViewMatcher 也支持 [Hamcrest matchers](http://hamcrest.org/JavaHamcrest/javadoc/1.3/org/hamcrest/Matchers.html) 類別來處理，常用的方法有以下幾種：

1. 根據 ID 找 View
`withId(R.id.test_view) `

2. 根據 View 的 Text 內容找到 View
`withText("Test")`
withText 內還可以使用其他過濾字串的方法，如： equalToIgnoringWhiteSpace、startsWith、endWith 等，詳情可以參考[此篇](http://qathread.blogspot.tw/2014/01/discovering-espresso-for-android.html)。

3. 利用 allOf() 加入多個 filter 條件，可參考 [Hamcrest matchers](http://hamcrest.org/JavaHamcrest/javadoc/1.3/org/hamcrest/Matchers.html) 類別用法。
`allOf(hasSibling(withText(equalToIgnoringWhiteSpace("OH YA"))), instanceOf(TextView.class), withText("耶嘿～"))`
在 filter 當中，也可以用 [hasSibling()](https://developer.android.com/reference/android/support/test/espresso/matcher/ViewMatchers.html#hasSibling%28org.hamcrest.Matcher%3Candroid.view.View%3E%29) 、 [is()](http://hamcrest.org/JavaHamcrest/javadoc/1.3/org/hamcrest/Matchers.html#is%28java.lang.Class%29) 、[instanceOf()](http://hamcrest.org/JavaHamcrest/javadoc/1.3/org/hamcrest/Matchers.html#instanceOf%28java.lang.Class%29) 等等來判斷條件。
 > **Notes**: hasSibling() ：為了沒辦法使用 withId 或 withText 搜尋的狀況，可以用此方法找到同階層隔壁的 View 來指定你想要的 View。

<br />
#### **[ViewAction](https://developer.android.com/reference/android/support/test/espresso/action/ViewActions.html)**  : 對 View 做動作
取得 [ViewInteration](https://developer.android.com/reference/android/support/test/espresso/ViewInteraction.html) 後，可以使用 perform() 執行想要的 ViewAction，參數可帶多個，常用的 ViewAction 有：

1. click()
`perform(click())`

2. typeText() 、clearText() 與對 KeyBoard 開關的操作
`perform(typeText("I'm awonwonwon;"),closeSoftKeyboard())`
`perform(clearText())`

3. scrollTo()
Scroll View 時，此 View 必須是繼承 ScrollView 與 Visibilty 為 True 的物件，如果為 ListView 等的 AdapterView 則有另外用法，可參考 Adapter 情境。

4. swiftLeft()、swiftRight()

5. pressKey() 與鍵盤事件操作
`pressBack();`可獨立使用，或是用 EspressoKey Builder 製作 pressKey 順序。
```java
EspressoKey.Builder builder = new EspressoKey.Builder();
        builder.withKeyCode(KeyEvent.KEYCODE_VOLUME_UP);
        builder.withKeyCode(KeyEvent.KEYCODE_MUTE);
        builder.withKeyCode(KeyEvent.KEYCODE_POWER);
        builder.withKeyCode(KeyEvent.KEYCODE_BACK);
pressKey(builder.build());
```

<br />
#### **[ViewAssertion](https://developer.android.com/reference/android/support/test/espresso/assertion/ViewAssertions.html)** : 驗證 View
取得 [ViewInteration](https://developer.android.com/reference/android/support/test/espresso/ViewInteraction.html) 後，可以利用 check() 來驗證 View 的狀態，主要有三種方法：

1. matches()：指定 View 為存在

2. doesNotExist ()：指定 View 為不存在

3. selectedDescendantsMatch()：指定 view 的 parent view 為存在
方法內依然都是帶入 ViewMatcher 來找到指定的 View，若 Assert 失敗，則會跳出 Exception 並結束 Test。

範例：
```java
onView(withId(R.id.test_view))
  .check(matches(withText("hello")));
```

<br /><br /><br />
### **情境系列**
#### **開啟 Action Bar Menu**
```java
openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
onView(withText("Settings")).perform(click());
```

<br /><br />
#### **Adapter Based Views**
若有 ListView, GridView, Spinner 等等以 Adapter 為基礎的 View，則改成透過 onData() 取得 [DataInteration](https://developer.android.com/reference/android/support/test/espresso/DataInteraction.html)，並且不需要 ScrollTo 到指定位置點選 item ，只要呼叫 atPosition 即會自動處理 Scroll 到指定 item。
主要常用方法：

1. inAdapterView( **ViewMatcher** )用以指定 Adapter View

2. atPosition( **Index** )：指定哪個 Index 的 Item

3. onChildView( **ViewMatcher** )：找到 Item 當中的 View，如果像是有 Custom Layout，就可以找到 Layout 當中自定義的 view

範例一
```java
onData(anything())
        .inAdapterView(withId(R.id.list))
        .atPosition(3)
        .perform(click());
```
範例二
```java
onData(allOf(instanceOf(ListActivity.Item.class)))
        .atPosition(9)
        .onChildView(withId(R.id.switch1))
        .perform(click());

//範例的 Adapter Item 是寫在 ListActivity 的 Inner Class
```


<br /><br />
#### **Intent**
如果要測試 Intent 是否正確的話，會跟上述 Acitivy 測試方法些微不同，需要額外新增一個測試，並做以下設定：

* 在 build.gradle 加上
```java
androidTestCompile 'com.android.support.test.espresso:espresso-intents:2.2.1
```
* 新增一個 Test Java Class

* 將 Rule 從基本的 ActivityTestRule 改為 IntentsTestRule
```java
    @Rule
    public IntentsTestRule<MainActivity> mActivityRule = new IntentsTestRule(MainActivity.class);
```
> **Note**: 如果 ActivityTestRule 改的話，Intent 測試是不會跑的。

* 如果只想要跑 Intent 的Test，也可以到 Test Configuration 中 General Tab 下的「**Specific instrumentation runner**」指定某個 Test Class

<br />
在 Intent Test中，會改用 [IntentMatcher](https://developer.android.com/reference/android/support/test/espresso/intent/matcher/IntentMatchers.html) 來捕捉 Intent 內容，像有 hasAction()、hasComponent()、hasData() 與 toPackage() 等用法。

<br />
主要分為兩種測試 Intent 方式：

1. Intended：捕捉發送出去的 Intent 是否跟測試中寫的一樣
```java
String PACKAGE_NAME = "com.android.mms";
Uri INTENT_DATA_PHONE_NUMBER = Uri.parse("sms:" + "0123456");

onView(withId(R.id.btnSMS)).perform(click());

intended(allOf(
        hasAction(Intent.ACTION_VIEW),
        hasData(INTENT_DATA_PHONE_NUMBER),
        toPackage(PACKAGE_NAME)));
```


2. Intending：準備好測試的 Intent 看結果是否達到預期，通常會用在 ActivityResult 的狀況上，如：利用其他相機 App 拍照

> production 程式的動作：
>
> 1. 從 A Activiy 點選 Button 到 B Activiy
> 2. 在 B Activiy 上輸入文字，按下 Submit
> 3. 把輸入的文字回傳到 A Activity ，並顯示

範例
```java
// 準備 Result Acttiviy 回來的 Intent
Intent resultData = new Intent();
resultData.putExtra("msg","IntendingResult測試用");
nstrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);

intending(hasComponent("example.com.espressotest.ResultActivity"))
  .respondWith(result);

onView(withId(R.id.btnResult)).perform(click());
```

<br /><br /><br /><br /><br />
# Exception Solution

> android.support.test.espresso.NoActivityResumedException: No activities in stage RESUMED. Did you forget to launch the activity. (test.getActivity() or similar)?

記得手機要打開 Lock 畫面

-------
> android.support.test.espresso.NoMatchingViewException: No views in hierarchy found matching: with id: tw.com.schroders.veeda:id/btn_login_common

View 找不到，有可能執行 Test 時， View 還沒產生
可以用 Thread.sleep() 方式，等待 View 產生 ( Exception 可以搭配 throw )

-------
> java.lang.RuntimeException: Failed to get key events for string 我是阿旺旺旺旺旺嗨 (i.e. current IME does not understand how to translate the string into key events). As a workaround, you can use replaceText action to set the text directly in the EditText field.

typeText 使用英文比較保險
中文可能會因為鍵盤跳出來不是中文鍵盤就失敗了

-------
> android.support.test.espresso.PerformException: Error performing 'single click' on view 'with id: example.com.espressotest:id/btnDial'.

在 Click View 時，要確保 SoftKeyBoard 是關閉的


<br /><br /><br /><br /><br />
# Reference
> 官方文件

* [Android Testing Support Library](https://google.github.io/android-testing-support-library/)
* [Android Developer - Testing UI for a Single App](https://developer.android.com/training/testing/ui-testing/espresso-testing.html#specifying-view-matcher)
* [Github - googlesamples/android-testing](https://github.com/googlesamples/android-testing)

> 部落格文章

* [Androie Research Blog - Introduction to Android Espresso](https://androidresearch.wordpress.com/2015/04/04/an-introduction-to-espresso/)
* [科科 科技人 - Espresso UI test API ](btsken.blogspot.tw/2015/08/android-espresso-ui-test-api.html)
* [QA thread - Discovering Espresso for Android: matching and asserting view with text.](http://qathread.blogspot.tw/2014/01/discovering-espresso-for-android.html)
