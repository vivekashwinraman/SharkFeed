package com.sharkfeed.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.sharkfeed.R;
import com.sharkfeed.adapters.SharkItemAdapter;
import com.sharkfeed.apicommunicators.FlickrApiCommunicator;
import com.sharkfeed.apicommunicators.ServerInteractor;
import com.sharkfeed.business.contracts.FlickrPhotoContract;
import com.sharkfeed.business.contracts.PhotoContract;
import com.sharkfeed.business.managers.FlickrManager;
import com.sharkfeed.modelobjects.ServerResponseObject;
import com.sharkfeed.modelobjects.SharkItem;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView sharkRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SharkItemAdapter sharkItemAdapter;
    private List<SharkItem> sharkItemList = new ArrayList<SharkItem>();
    private static int currentPage = 1;
    private GridLayoutManager layoutManager;
    private int itemCount, lastVisibleItem;
    private int visibleThreshold = 5;
    private boolean loading;

    /************************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                layoutManager.scrollToPosition(0);
                sharkItemList.clear();
                refreshItems(currentPage);

            }
        });
        refreshItems(currentPage);
        layoutManager = new GridLayoutManager(this, 3);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        sharkRecyclerView = (RecyclerView) findViewById(R.id.sharkRecyclerView);
        sharkRecyclerView.setLayoutManager(layoutManager);
        sharkItemAdapter = new SharkItemAdapter(sharkItemList, this);
        sharkRecyclerView.setAdapter(sharkItemAdapter);
        sharkRecyclerView.setItemAnimator(new DefaultItemAnimator());
        addScrollListener();
    }
    /************************************************************************************************/
    private void refreshItems(final int page) {
        String endpoint = "?method=flickr.photos.search&api_key=" + FlickrApiCommunicator.API_KEY + "&tags=shark&format=json&nojsoncallback=1&page=" + page + "&extras=url_t,url_l";
        Subscriber<ServerResponseObject> subscriber = new Subscriber<ServerResponseObject>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ServerResponseObject serverResponseObject) {
                onPhotoListResponse(serverResponseObject);
            }
        };
        compositeSubscription.add(new ServerInteractor(new FlickrManager(FlickrPhotoContract.class,endpoint), FlickrManager.class).makeServerRequest(subscriber));
    }

    /************************************************************************************************/
    private void onPhotoListResponse(ServerResponseObject serverResponseObject) {
        if (serverResponseObject.isSuccess()) {
            FlickrPhotoContract flickrPhotoContract = (FlickrPhotoContract) (serverResponseObject.getResponseObject());
            for (int i = 0; i < flickrPhotoContract.photoListContract.photoContractList.size(); i++) {
                PhotoContract photoContract = flickrPhotoContract.photoListContract.photoContractList.get(i);
                sharkItemList.add(new SharkItem(photoContract));
            }
            sharkItemAdapter.notifyDataSetChanged();
            loading = false;
            Log.v(TAG, String.valueOf(flickrPhotoContract.stat));
        } else {
            showErrorDialog(getResources().getString(R.string.network_connect_issue));
        }
        swipeRefreshLayout.setRefreshing(false);
    }
    /************************************************************************************************/
    private void addScrollListener() {
        sharkRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                itemCount = layoutManager.getItemCount();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                if (!loading && itemCount <= (lastVisibleItem + visibleThreshold)) {
                    loading = true;
                    currentPage++;
                    refreshItems(currentPage);
                }
            }
        });
    }
    /************************************************************************************************/
}
