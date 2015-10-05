# Espresso Test Sample
練習 Espresso 的範例。

Espresso 是一個自動化測試工具，針對單一 app 模擬使用者的互動。提供同步化測試，在使用者手動測試 UI時，Espresso 會自動去偵測 main thread 是否為閒置狀態，來決定何時運行測試的程式碼。
Espresso 是一個以工具基礎的 API ，並利用 AndroidJunitRunner 來執行。

# Set Up Espresso 建置
### Step 1 設定 build.gradle
設定所需的 libraries。

```
dependencies {
    // App's dependencies, including test
    compile 'com.android.support:support-annotations:22.2.0'    
    androidTestCompile 'com.android.support.test:runner:0.3'
    androidTestCompile 'com.android.support.test:rules:0.3'
    androidTestCompile 'com.android.support.test.espresso:espresso-core:2.2'
    androidTestCompile 'com.android.support.test.espresso:espresso-intents:2.2.1'
    androidTestCompile 'org.testinfected.hamcrest-matchers:hamcrest-matchers:1.8'
}
```

在 config 上加入 TestRunner
```
android {
    defaultConfig {
        testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
    }
}
```


### Step 2 關閉 animation (Optional)
關閉 android 測試裝置上的開發 animation 選項

「設定 > 開發者工具 > 視窗動畫縮放、轉換動畫縮放、動畫影像時間伸縮效果」


### Step 3 初始化 Test
在 androidTest/java 下增加一個繼承 ActivityInstrumentationTestCase2<你要測試的Activity> 的子class
如果有需要做初始化的設定，可以寫一個方法並使用 @Before 加註在方法前 （這是 JUnit 的寫法）

``` java
@RunWith(AndroidJUnit4.class) // run test runner，並使用 JUnit 4
@LargeTest // 只是代表這個 Test 會跑比較久
public class ExpressoTestEx{
    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule(LoginActivity.class);
    
    @Before
    public void custom() throws Exception {
        //do something…
    }

}
```


### Step 4 撰寫測試
開始在 class 中寫測試，在自定義的 test method 前加上 annotation @Test
每個 @Test 都是獨立不牴觸的一次測試
如果有要撇除特定 Exception 的話，可以在 @Test 後方加上 (expected=Exception.class)
```
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


### Step 5 結束前的動作
如果在結束測試時，需要額外做什麼動作，可以自己寫一個方法並且加上 annotation @After
```
@After
public void custom() throws Exception {
	// do something
}
```
This is important to not leak any of the any objects from your tests


### Step 6 設定 Run Test Configuration

「Run > Edit Configuration 」