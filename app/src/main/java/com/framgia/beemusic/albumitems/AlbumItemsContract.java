package com.framgia.beemusic.albumitems;

import com.framgia.beemusic.BasePresenter;
import com.framgia.beemusic.data.model.Song;
import java.util.List;
import ru.rambler.libs.swipe_layout.SwipeLayout;

/**
 * Created by beepi on 17/05/2017.
 */

public interface AlbumItemsContract {
    interface View {
        void initRecycleView(List<Song> songs, List<String> singer);

        void notifyItemRemove(int pos);

        void onAddToAnotherAlbum(Song song);
    }

    interface Presenter extends BasePresenter {
        void subcribe(int idAlbum);

        void onDeleteSong(Song song, int pos);

        void onAddToAlbum(Song song, SwipeLayout layout);

        void onAddToFavorite(Song song, SwipeLayout layout);

        void onRemoveFromFavorite(Song song, SwipeLayout layout);

        void subcribeFavorite(Song song);

        void onOpenPlayMusic(Song song, String singer);
    }
}
