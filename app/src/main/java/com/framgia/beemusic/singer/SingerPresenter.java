package com.framgia.beemusic.singer;

import com.framgia.beemusic.BeeApplication;
import com.framgia.beemusic.data.model.Singer;
import com.framgia.beemusic.data.source.SingerDataSource;
import com.framgia.beemusic.data.source.local.singer.SingerSourceContract;
import com.framgia.beemusic.singeritems.SingerItemsActivity;
import java.util.ArrayList;
import java.util.List;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by beepi on 16/05/2017.
 */

public class SingerPresenter implements SingerContract.Presenter {
    private SingerContract.View mView;
    private CompositeSubscription mSubscription;
    private SingerDataSource mSingerRepository;

    public SingerPresenter(SingerContract.View view, SingerDataSource singerRepository) {
        mSingerRepository = singerRepository;
        mView = view;
        mSubscription = new CompositeSubscription();
        mView.setPresenter(this);
    }

    @Override
    public void subcribe() {
        mSubscription.clear();
        final List<Singer> singers = new ArrayList<>();
        Subscription subscription =
                mSingerRepository.getDataObservableByModels(mSingerRepository.getModel(null, null))
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.newThread())
                        .subscribe(new Subscriber<Singer>() {
                            @Override
                            public void onCompleted() {
                                mView.initRecycleview(singers);
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(Singer singer) {
                                singers.add(singer);
                            }
                        });
        mSubscription.add(subscription);
    }

    @Override
    public void unsubcribe() {
        mSubscription.clear();
    }

    @Override
    public void onSearch(String keySearch) {
        mSubscription.clear();
        final List<Singer> singers = new ArrayList<>();
        String selection =
                SingerSourceContract.SingerEntry.COLUMN_NAME + " like '%" + keySearch + "%'";
        Subscription subscription = mSingerRepository.getDataObservableByModels(
                mSingerRepository.getModel(selection, null))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Singer>() {
                    @Override
                    public void onCompleted() {
                        mView.initRecycleview(singers);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.initRecycleview(singers);
                    }

                    @Override
                    public void onNext(Singer singer) {
                        singers.add(singer);
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public void onOpenSingerItems(Singer singer) {
        BeeApplication.getInstant().startActivity(SingerItemsActivity.getIntent(singer));
    }
}
