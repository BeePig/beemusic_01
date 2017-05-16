package com.framgia.beemusic.favorite;

import android.databinding.ObservableArrayList;
import com.framgia.beemusic.BeeApplication;
import com.framgia.beemusic.data.model.Song;
import com.framgia.beemusic.data.source.AlbumDataSource;
import com.framgia.beemusic.data.source.DataSourceRelationship;
import com.framgia.beemusic.data.source.SingerDataSource;
import com.framgia.beemusic.data.source.SongDataSource;
import com.framgia.beemusic.data.source.local.song.SongSourceContract;
import com.framgia.beemusic.displaysong.DisplaySongActivity;
import ru.rambler.libs.swipe_layout.SwipeLayout;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by beepi on 14/05/2017.
 */

public class FavoriteAlbumPresenter implements FavoriteAlbumContract.Presenter {
    private static final String IS_FAVORITE = "1";
    private FavoriteAlbumContract.View mView;
    private AlbumDataSource mAlbumRepository;
    private CompositeSubscription mSubscription;
    private DataSourceRelationship mSongAlbumRepository;
    private SongDataSource mSongRepository;
    private DataSourceRelationship mSongSingerRepository;
    private SingerDataSource mSingerRepository;

    public FavoriteAlbumPresenter(FavoriteAlbumContract.View view, AlbumDataSource albumRepository,
            DataSourceRelationship songAlbumRepository, SongDataSource songRepository,
            DataSourceRelationship songSingerRepository, SingerDataSource singerRepository) {
        mView = view;
        mAlbumRepository = albumRepository;
        mSongAlbumRepository = songAlbumRepository;
        mSongRepository = songRepository;
        mSongSingerRepository = songSingerRepository;
        mSingerRepository = singerRepository;
        mView.setPresenter(this);
        mSubscription = new CompositeSubscription();
    }

    @Override
    public void subcribe() {
        mSubscription.clear();
        final ObservableArrayList<Song> songs = new ObservableArrayList<>();
        final ObservableArrayList<String> singers = new ObservableArrayList<>();
        String selection = SongSourceContract.SongEntry.COLUMN_IS_FAVORITE + " = ?";
        Subscription subscription = mSongRepository.getDataObservableByModels(
                mSongRepository.getModel(selection, new String[] { IS_FAVORITE }))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
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

    @Override
    public void onSearch(String keySearch) {

    }

    @Override
    public void onDeleteSong(Song song, FavoriteAlbumAdapter.ViewHolder holder) {
        mView.notifyItemRemove(song, holder.getAdapterPosition());
        if (song == null) return;
        song.setIsFavorite(false);
        mSongRepository.update(song);
    }

    @Override
    public void onAddToAlbum(Song song, SwipeLayout layout) {
        mView.onAddToAnotherAlbum(song);
        layout.animateReset();
    }

    @Override
    public void onOpenPlayMusic(Song song, String singer) {
        BeeApplication.getInstant().startActivity(DisplaySongActivity.getIntent(song, singer));
    }
}
