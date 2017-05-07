package com.framgia.beemusic.album.createalbum;

import android.databinding.ObservableArrayList;
import com.framgia.beemusic.data.model.Album;
import com.framgia.beemusic.data.model.Song;
import com.framgia.beemusic.data.source.AlbumDataSource;
import com.framgia.beemusic.data.source.DataSourceRelationship;
import com.framgia.beemusic.data.source.SingerDataSource;
import com.framgia.beemusic.data.source.SongDataSource;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by beepi on 13/04/2017.
 */
public class CreatedAlbumPresenter implements CreatedAlbumContract.Presenter {
    private CreatedAlbumContract.View mView;
    private AlbumDataSource mAlbumRepository;
    private Album mAlbum = new Album();
    private CompositeSubscription mSubscription;
    private SongDataSource mSongRepository;
    private SingerDataSource mSingerRepository;
    private DataSourceRelationship mSongAlbumRepository;
    private DataSourceRelationship mSongSingerRepository;

    public CreatedAlbumPresenter(CreatedAlbumContract.View view, AlbumDataSource albumRepository,
            CompositeSubscription compositeSubscription, SongDataSource songRepository,
            SingerDataSource singerRepository, DataSourceRelationship songAlbumRepository,
            DataSourceRelationship songSingerRepository) {
        mView = view;
        mAlbumRepository = albumRepository;
        mSubscription = compositeSubscription;
        mSongRepository = songRepository;
        mSingerRepository = singerRepository;
        mSongAlbumRepository = songAlbumRepository;
        mSongSingerRepository = songSingerRepository;
    }

    @Override
    public void onOpenChooseBackground() {
        mView.onPickImage();
    }

    @Override
    public String getFilePathPickedImage(String uriIntent) {
        String path = mAlbumRepository.getFilePickedImageLocal(uriIntent);
        if (path == null) return null;
        mAlbum.setImageLink(path);
        return path;
    }

    @Override
    public void onCompletedCreation() {

    }

    @Override
    public void subcribe() {
        mSubscription.clear();
        final ObservableArrayList<Song> songs = new ObservableArrayList<>();
        final ObservableArrayList<String> singers = new ObservableArrayList<>();
        Subscription subscription =
                mSongRepository.getDataObservableByModels(mSongRepository.getModel(null, null))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Song>() {
                            @Override
                            public void onCompleted() {
                                mView.initRecycleview(songs, singers);
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(Song song) {
                                songs.add(song);
                                singers.add(mSingerRepository.getSingerNameByIds(
                                        mSongSingerRepository.getListId(song.getId())));
                            }
                        });
        mSubscription.add(subscription);
    }

    @Override
    public void unsubcribe() {
        mSubscription.clear();
    }
}