package com.framgia.beemusic.albumitems;

import com.framgia.beemusic.BeeApplication;
import com.framgia.beemusic.data.model.Song;
import com.framgia.beemusic.data.source.AlbumDataSource;
import com.framgia.beemusic.data.source.DataSourceRelationship;
import com.framgia.beemusic.data.source.SingerDataSource;
import com.framgia.beemusic.data.source.SongDataSource;
import com.framgia.beemusic.data.source.local.song.SongSourceContract;
import com.framgia.beemusic.displaysong.DisplaySongActivity;
import java.util.ArrayList;
import java.util.List;
import ru.rambler.libs.swipe_layout.SwipeLayout;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by beepi on 17/05/2017.
 */

public class AlbumItemsPresenter implements AlbumItemsContract.Presenter {
    private AlbumItemsContract.View mView;
    private SongDataSource mSongRepository;
    private DataSourceRelationship mSongSingerRepository;
    private DataSourceRelationship mSongAlbumRepository;
    private CompositeSubscription mSubscription;
    private SingerDataSource mSingerRepository;
    private AlbumDataSource mAlbumRepository;

    public AlbumItemsPresenter(AlbumItemsContract.View view, SongDataSource songRepository,
            DataSourceRelationship songSingerRepository, DataSourceRelationship songAlbumRepository,
            SingerDataSource singerRepository, AlbumDataSource albumRepository) {
        mView = view;
        mSongRepository = songRepository;
        mSongSingerRepository = songSingerRepository;
        mSongAlbumRepository = songAlbumRepository;
        mSubscription = new CompositeSubscription();
        mSingerRepository = singerRepository;
        mAlbumRepository = albumRepository;
    }

    @Override
    public void subcribe() {

    }

    @Override
    public void unsubcribe() {
        mSubscription.clear();
    }

    @Override
    public void subcribe(int idAlbum) {
        mSubscription.clear();
        final List<Song> songs = new ArrayList<>();
        final List<String> singers = new ArrayList<>();
        List<Integer> idSongs = mSongAlbumRepository.getListIdSong(idAlbum);
        if (idSongs == null) return;
        String allSong = new String();
        for (Integer id : idSongs) {
            allSong += id.toString().concat(",");
        }
        allSong = allSong.substring(0, allSong.length() - 1);
        String selection = SongSourceContract.SongEntry.COLUMN_ID_SONG + " IN (" + allSong + ")";
        Subscription subscription =
                mSongRepository.getDataObservableByModels(mSongRepository.getModel(selection, null))
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.newThread())
                        .subscribe(new Subscriber<Song>() {
                            @Override
                            public void onCompleted() {
                                mView.initRecycleView(songs, singers);
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
    public void onDeleteSong(Song song, int pos) {
        mView.notifyItemRemove(pos);
        if (song == null) return;
        int idSong = song.getId();
        mAlbumRepository.updateCountForDelSong(mSongAlbumRepository.getListId(idSong));
        mSongAlbumRepository.delete(idSong);
    }

    @Override
    public void onAddToAlbum(Song song, SwipeLayout layout) {
        layout.animateReset();
        mView.onAddToAnotherAlbum(song);
    }

    @Override
    public void onAddToFavorite(Song song, SwipeLayout layout) {
        layout.animateReset();
        song.setIsFavorite(Song.IS_FAVORITE);
        subcribeFavorite(song);
    }

    @Override
    public void onRemoveFromFavorite(Song song, SwipeLayout layout) {
        layout.animateReset();
        song.setIsFavorite(Song.IS_NON_FAVORITE);
        subcribeFavorite(song);
    }

    @Override
    public void subcribeFavorite(Song song) {
        Subscription subscription = Observable.just(song)
                .observeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Song>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Song song) {
                        mSongRepository.update(song);
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public void onOpenPlayMusic(Song song, String singer) {
        BeeApplication.getInstant().startActivity(DisplaySongActivity.getIntent(song, singer));
    }
}
