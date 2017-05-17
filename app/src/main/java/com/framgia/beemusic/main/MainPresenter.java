package com.framgia.beemusic.main;

import android.database.Cursor;
import com.framgia.beemusic.data.source.SynchronizeRepository;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by beepi on 20/02/2017.
 */
public class MainPresenter implements MainContract.Presenter {
    private SynchronizeRepository mSynchronizeRepository;
    private MainContract.View mView;
    private CompositeSubscription mSubscription;

    public MainPresenter(MainContract.View view, CompositeSubscription subscription,
            SynchronizeRepository synchronizeRepository) {
        mView = view;
        mSubscription = subscription;
        mSynchronizeRepository = synchronizeRepository;
    }

    @Override
    public void subcribe() {
        mSubscription.clear();
        Subscription subscription = mSynchronizeRepository.getCursorObservable(
                mSynchronizeRepository.getCursorFromMediastore())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Cursor>() {
                    @Override
                    public void onCompleted() {
                        mView.startObserverService();
                        mView.initSongFragment();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Cursor cursor) {
                        mSynchronizeRepository.synchronizeByAddModel(cursor);
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public void unsubcribe() {
        mSubscription.clear();
    }
}
