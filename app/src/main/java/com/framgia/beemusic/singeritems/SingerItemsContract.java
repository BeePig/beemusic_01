package com.framgia.beemusic.singeritems;

import com.framgia.beemusic.BasePresenter;
import com.framgia.beemusic.data.model.Song;
import java.util.List;
import ru.rambler.libs.swipe_layout.SwipeLayout;

/**
 * Created by beepi on 17/05/2017.
 */

public interface SingerItemsContract {
    interface View {
        void initRecycleView(List<Song> songs);

        void onOpenPlayMusic(Song song);
    }

    interface Presenter extends BasePresenter {
        void subcribe(int idSinger);

        void onAddToAlbum(Song song, SwipeLayout layout);

        void onAddToFavorite(Song song, SwipeLayout layout);

        void onRemoveFromFavorite(Song song, SwipeLayout layout);

        void subcribeFavorite(Song song);

        void onOpenPlayMusic(Song song);
    }
}
