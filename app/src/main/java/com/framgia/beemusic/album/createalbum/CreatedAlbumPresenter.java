package com.framgia.beemusic.album.createalbum;

import com.framgia.beemusic.data.model.Album;
import com.framgia.beemusic.data.model.Song;
import com.framgia.beemusic.data.source.AlbumDataSource;
import com.framgia.beemusic.data.source.DataSourceRelationship;
import com.framgia.beemusic.data.source.SingerDataSource;
import com.framgia.beemusic.data.source.SongDataSource;
import java.util.ArrayList;
import java.util.List;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by beepi on 13/04/2017.
 */
public class CreatedAlbumPresenter implements CreatedAlbumContract.Presenter {
    private static final String NAME_ALBUM_UNKNOWN = "Unknown";
    private CreatedAlbumContract.View mView;
    private AlbumDataSource mAlbumRepository;
    private Album mAlbum = new Album();
    private CompositeSubscription mSubscription;
    private SongDataSource mSongRepository;
    private SingerDataSource mSingerRepository;
    private DataSourceRelationship mSongAlbumRepository;
    private DataSourceRelationship mSongSingerRepository;
    private List<Song> mSongs = new ArrayList<>();

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
    public void onCheckBox(CreatedAlbumAdapter.ViewHolder.BindingModel model) {
        mView.onCheckBox(model.getSong());
        if (!model.getIsCheck().get()) {
            addSong(model);
            mView.addSong();
            model.getIsCheck().set(true);
            return;
        }
        removeSong(model);
        model.getIsCheck().set(false);
        mView.removeSong();
    }

    private void removeSong(CreatedAlbumAdapter.ViewHolder.BindingModel model) {
        if (model.getSong() == null) return;
        mSongs.remove(model.getSong());
    }

    private void addSong(CreatedAlbumAdapter.ViewHolder.BindingModel model) {
        if (model.getSong() == null) return;
        mSongs.add(model.getSong());
    }

    @Override
    public void onCompletedCreation(String nameAlbum, int numberOfSong) {
        mAlbum.setName(nameAlbum.equals("") ? NAME_ALBUM_UNKNOWN : nameAlbum);
        mAlbum.setCount(numberOfSong);
        int albumId = mAlbumRepository.save(mAlbum);
        for (Song song : mSongs) {
            mSongAlbumRepository.save(song.getId(), albumId);
        }
        mView.onCompletedCreation();
    }

    @Override
    public void subcribe() {
        mSubscription.clear();
        final List<Song> songs = new ArrayList<>();
        final List<String> singers = new ArrayList<>();
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
