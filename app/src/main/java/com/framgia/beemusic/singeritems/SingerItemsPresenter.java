package com.framgia.beemusic.singeritems;

import com.framgia.beemusic.data.model.Song;
import com.framgia.beemusic.data.source.DataSourceRelationship;
import com.framgia.beemusic.data.source.SongDataSource;
import com.framgia.beemusic.data.source.local.song.SongSourceContract;
import java.util.ArrayList;
import java.util.List;
import ru.rambler.libs.swipe_layout.SwipeLayout;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by beepi on 17/05/2017.
 */

public class SingerItemsPresenter implements SingerItemsContract.Presenter {
    private SingerItemsContract.View mView;
    private SongDataSource mSongRepository;
    private CompositeSubscription mSubscriptions;
    private DataSourceRelationship mSongSingerRepository;

    public SingerItemsPresenter(SingerItemsContract.View view, SongDataSource songRepository,
            DataSourceRelationship songSingerRepository) {
        mView = view;
        mSongRepository = songRepository;
        mSongSingerRepository = songSingerRepository;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subcribe() {

    }

    @Override
    public void unsubcribe() {
        mSubscriptions.clear();
    }

    @Override
    public void subcribe(int idSinger) {
        mSubscriptions.clear();
        final List<Song> songs = new ArrayList<>();
        List<Integer> idSongs = mSongSingerRepository.getListIdSong(idSinger);
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
                                mView.initRecycleView(songs);
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(Song song) {
                                songs.add(song);
                            }
                        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void onAddToAlbum(Song song, SwipeLayout layout) {

    }

    @Override
    public void onAddToFavorite(Song song, SwipeLayout layout) {

    }

    @Override
    public void onRemoveFromFavorite(Song song, SwipeLayout layout) {

    }

    @Override
    public void subcribeFavorite(Song song) {

    }

    @Override
    public void onOpenPlayMusic(Song song, String singer) {

    }
}
