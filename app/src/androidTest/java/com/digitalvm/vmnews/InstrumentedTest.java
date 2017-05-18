package com.digitalvm.vmnews;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.digitalvm.vmnews.MockRetrofit.MockFailNewsService;
import com.digitalvm.vmnews.MockRetrofit.MockNewsService;
import com.digitalvm.vmnews.controller.ApiClient;
import com.digitalvm.vmnews.controller.ApiInterface;
import com.digitalvm.vmnews.model.NewsObject;
import com.digitalvm.vmnews.model.NewsObjectResponse;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class InstrumentedTest extends InstrumentationTestCase {
    private static MockRetrofit mockRetrofit;
    private static Retrofit retrofit;
    private final static String TAG  = InstrumentedTest.class.getSimpleName();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        //do your setUp
        retrofit = new Retrofit.Builder().baseUrl("http://test.com")
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NetworkBehavior behavior = NetworkBehavior.create();

        mockRetrofit = new MockRetrofit.Builder(retrofit)
                .networkBehavior(behavior)
                .build();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        //do your tearDown
        Log.d(TAG, "tearDown()");
    }

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.digitalvm.vmnews", appContext.getPackageName());
    }

    @Test
    public void testWebServiceConnectivity() throws IOException {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        String source = "techcrunch" ;

        Call<NewsObjectResponse> call = apiService.getNews(source, VMNewsApp.WEBSERVICE_KEY) ;
        assertTrue(call.execute() != null);
    }

    @Test
    public void testGsonParserSuccess() throws Exception {
        BehaviorDelegate<ApiInterface> delegate = mockRetrofit.create(ApiInterface.class);
        MockNewsService mockNewsService = new MockNewsService(delegate);

        //Actual Test
        Call<NewsObjectResponse> quote = mockNewsService.getNews("cnn", VMNewsApp.WEBSERVICE_KEY) ;
        Response<NewsObjectResponse> newsObjectResponse = quote.execute();

        //Asserting response
        Assert.assertTrue(newsObjectResponse.isSuccessful());
        Assert.assertEquals("Everything Google announced at its Google I/O keynote", newsObjectResponse.body().getArticles().get(0).getTitle());
        Assert.assertEquals("Romain Dillet", newsObjectResponse.body().getArticles().get(0).getAuthor());
    }

    @Test
    public void testGsonParserFail() throws Exception {
        BehaviorDelegate<ApiInterface> delegate = mockRetrofit.create(ApiInterface.class);
        MockFailNewsService mockFailNewsService = new MockFailNewsService(delegate);

        //Actual Test
        Call<NewsObjectResponse> quote = mockFailNewsService.getNews("cnn", VMNewsApp.WEBSERVICE_KEY) ;
        Response<NewsObjectResponse> newsObjectResponse = quote.execute();

        //Asserting response
        Assert.assertTrue(newsObjectResponse.isSuccessful());
        Assert.assertEquals("Fail", newsObjectResponse.body().getStatus());
    }


}



