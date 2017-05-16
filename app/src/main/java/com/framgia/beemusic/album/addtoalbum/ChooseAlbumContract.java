package com.framgia.beemusic.album.addtoalbum;

import com.framgia.beemusic.BasePresenter;
import com.framgia.beemusic.data.model.Album;
import com.framgia.beemusic.data.model.Song;
import java.util.List;

/**
 * Created by beepi on 16/05/2017.
 */

public interface ChooseAlbumContract {
    interface View {
        void onSuccessfullAddToAlbum();

        void onErrorAddToAlbum();

        void initRecycleview(List<Album> albums);

        /**
         * add to single album while choosing
         *
         * @param album: album choose
         */
        void onAddToAlbum(Album album);
    }

    interface Presenter extends BasePresenter {
        void onCompleteAddToAlbum(Song song, List<Album> albums);

        /**
         * add to single album while choosing
         *
         * @param album: album choose
         */
        void onAddToAlbum(Album album);
    }
}
