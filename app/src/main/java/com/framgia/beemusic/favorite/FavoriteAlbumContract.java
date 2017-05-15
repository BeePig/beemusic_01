package com.framgia.beemusic.favorite;

import com.framgia.beemusic.BaseFragmentView;
import com.framgia.beemusic.data.model.Song;
import com.framgia.beemusic.song.SongContract;
import java.util.List;

/**
 * Created by beepi on 14/05/2017.
 */

public interface FavoriteAlbumContract {
    interface View extends BaseFragmentView<Presenter> {
        void initRecycleview(List<Song> songs, List<String> singer);

        void notifyItemRemove(int pos);

        void onAddToAnotherAlbum(Song song);
    }

    interface Presenter extends SongContract.Presenter {

    }
}
