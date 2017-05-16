package com.framgia.beemusic.favorite;

import com.framgia.beemusic.BaseFragmentPresenter;
import com.framgia.beemusic.BaseFragmentView;
import com.framgia.beemusic.data.model.Song;
import java.util.List;
import ru.rambler.libs.swipe_layout.SwipeLayout;

/**
 * Created by beepi on 14/05/2017.
 */

public interface FavoriteAlbumContract {
    interface View extends BaseFragmentView<Presenter> {
        void initRecycleview(List<Song> songs, List<String> singer);

        void notifyItemRemove(Song song, int pos);

        void onAddToAnotherAlbum(Song song);
    }

    interface Presenter extends BaseFragmentPresenter {
        void onDeleteSong(Song song, FavoriteAlbumAdapter.ViewHolder holder);

        void onAddToAlbum(Song song, SwipeLayout layout);

        void onOpenPlayMusic(Song song, String singer);
    }
}
