package com.sharkfeed.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.sharkfeed.R;
import com.sharkfeed.adapters.SharkItemAdapter;
import com.sharkfeed.apicommunicators.FlickrApiCommunicator;
import com.sharkfeed.asynctasks.ServerInteractionTask;
import com.sharkfeed.business.contracts.FlickrPhotoContract;
import com.sharkfeed.business.contracts.PhotoContract;
import com.sharkfeed.business.managers.FlickrManager;
import com.sharkfeed.interfaces.ServerResponseListener;
import com.sharkfeed.modelobjects.ServerResponseObject;
import com.sharkfeed.modelobjects.SharkItem;
import com.sharkfeed.uicontrols.RecyclerItemClickListener;
import com.sharkfeed.uicontrols.RecyclerOnScrollListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView sharkRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SharkItemAdapter sharkItemAdapter;
    private List<SharkItem> sharkItemList = new ArrayList<SharkItem>();
    private static int currentPage = 1;
    private GridLayoutManager layoutManager;

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
                removeScrollListener();
                addScrollListener();
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
        sharkRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getApplicationContext(), ImageDetailActivity.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putParcelable("image", sharkItemList.get(position));
                        intent.putExtras(mBundle);
                        startActivity(intent);
                    }
                })
        );
        addScrollListener();
    }

    /************************************************************************************************/
    private void refreshItems(final int page) {
        String endpoint = "?method=flickr.photos.search&api_key=" + FlickrApiCommunicator.API_KEY + "&tags=shark&format=json&nojsoncallback=1&page=" + page + "&extras=url_t,url_l";
        new ServerInteractionTask(new FlickrManager(FlickrPhotoContract.class, endpoint), new ServerResponseListener() {
            @Override
            public void onResponseReceived(ServerResponseObject responseObject) {
                onPhotoListResponse(responseObject);
            }
        }, FlickrManager.class).execute();
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
            Log.v(TAG, String.valueOf(flickrPhotoContract.stat));
        } else {
            showErrorDialog(getResources().getString(R.string.network_connect_issue));
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    /************************************************************************************************/
    private void removeScrollListener() {
        sharkRecyclerView.removeOnScrollListener(new RecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
            }
        });
    }

    /************************************************************************************************/
    private void addScrollListener() {
        sharkRecyclerView.addOnScrollListener(new RecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                refreshItems(current_page);
            }
        });
    }
    /************************************************************************************************/
}
