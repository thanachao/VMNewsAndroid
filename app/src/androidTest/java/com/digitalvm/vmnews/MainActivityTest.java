package com.digitalvm.vmnews;

/**
 * Created by TL on 5/18/17.
 */
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.digitalvm.vmnews.adapter.NewsAdapterRecycle;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> rule  = new  ActivityTestRule<>(MainActivity.class);

    @Test
    public void ensureListViewIsPresent() throws Exception {
        MainActivity activity = rule.getActivity();

        View viewById = activity.findViewById(R.id.swipeRefreshLayout);
        assertThat(viewById,notNullValue());
        assertThat(viewById, instanceOf(SwipeRefreshLayout.class));

        viewById = activity.findViewById(R.id.lv_news);
        assertThat(viewById,notNullValue());
        assertThat(viewById, instanceOf(RecyclerView.class));

        RecyclerView listView = (RecyclerView) viewById;
        RecyclerView.Adapter adapter = listView.getAdapter() ;
        assertThat(adapter, instanceOf(NewsAdapterRecycle.class));

    }
}
