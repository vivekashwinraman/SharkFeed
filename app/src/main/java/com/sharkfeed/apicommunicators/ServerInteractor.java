package com.sharkfeed.apicommunicators;

import android.util.Log;

import com.sharkfeed.business.managers.BaseBusinessManager;
import com.sharkfeed.modelobjects.ServerResponseObject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by vraman on 10/22/16.
 */

public class ServerInteractor {
    private static final String TAG = "ServerInteractor";
    private Object requestManager;
    private Class<?> type;

    /************************************************************************************************/
    public ServerInteractor(Object requestManager, Class<?> type) {
        this.requestManager = requestManager;
        this.type = type;
    }

    /************************************************************************************************/
    public Subscription makeServerRequest(Subscriber<ServerResponseObject> subscriber) {

        return Observable.create(new Observable.OnSubscribe<ServerResponseObject>() {
            @Override
            public void call(Subscriber<? super ServerResponseObject> subscriber) {
                try {
                    Object object = Class.forName(type.getName()).cast(requestManager);
                    if (object instanceof BaseBusinessManager) {
                        Log.v(TAG, type.getName());
                        subscriber.onNext(((BaseBusinessManager) object).sendRequest());
                        subscriber.onCompleted();
                    }
                } catch (Exception e) {
                    Log.v(TAG, e.getLocalizedMessage());
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }
    /************************************************************************************************/
}
