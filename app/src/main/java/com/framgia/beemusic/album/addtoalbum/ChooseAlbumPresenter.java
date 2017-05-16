package com.framgia.beemusic.album.addtoalbum;

import android.support.annotation.NonNull;
import com.framgia.beemusic.data.model.Album;
import com.framgia.beemusic.data.model.Song;
import com.framgia.beemusic.data.source.AlbumDataSource;
import com.framgia.beemusic.data.source.DataSourceRelationship;
import java.util.ArrayList;
import java.util.List;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by beepi on 16/05/2017.
 */

public class ChooseAlbumPresenter implements ChooseAlbumContract.Presenter {
    private ChooseAlbumContract.View mView;
    private AlbumDataSource mAlbumHandler;
    private DataSourceRelationship mSongAlbumHandler;
    private CompositeSubscription mSubscription;

    public ChooseAlbumPresenter(@NonNull ChooseAlbumContract.View view,
            AlbumDataSource albumHandler, DataSourceRelationship songAlbumHandler) {
        mView = view;
        mAlbumHandler = albumHandler;
        mSongAlbumHandler = songAlbumHandler;
        mSubscription = new CompositeSubscription();
    }

    @Override
    public void subcribe() {
        mSubscription.clear();
        final List<Album> albums = new ArrayList<>();
        Subscription subscription =
                mAlbumHandler.getDataObservableByModels(mAlbumHandler.getModel(null, null))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Album>() {
                            @Override
                            public void onCompleted() {
                                mView.initRecycleview(albums);
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(Album album) {
                                albums.add(album);
                            }
                        });
        mSubscription.add(subscription);
    }

    @Override
    public void unsubcribe() {
        mSubscription.clear();
    }

    @Override
    public void onCompleteAddToAlbum(Song song, List<Album> albums) {
        if (albums == null || song == null) return;
        for (final Album album : albums) {
            Subscription subscription = rx.Observable.just(song)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Song>() {
                        @Override
                        public void onCompleted() {
                            album.setCount(album.getCount() + 1);
                            mAlbumHandler.update(album);
                            mView.onSuccessfullAddToAlbum();
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.getStackTrace();
                            mView.onErrorAddToAlbum();
                        }

                        @Override
                        public void onNext(Song song) {
                            mSongAlbumHandler.save(song.getId(), album.getId());
                        }
                    });
            mSubscription.add(subscription);
        }
    }

    @Override
    public void onAddToAlbum(Album album) {
        if (album == null) return;
        mView.onAddToAlbum(album);
    }
}
