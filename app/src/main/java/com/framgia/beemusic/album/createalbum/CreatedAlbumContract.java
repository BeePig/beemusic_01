package com.framgia.beemusic.album.createalbum;

import com.framgia.beemusic.BasePresenter;
import com.framgia.beemusic.data.model.Song;
import java.util.List;

/**
 * Created by beepi on 13/04/2017.
 */
public interface CreatedAlbumContract {
    interface View {
        void onPickImage();

        void initRecycleview(List<Song> songs, List<String> singers);

        void onCheckBox(Song song);

        void addSong();

        void removeSong();

        void onCompletedCreation();
    }

    interface Presenter extends BasePresenter {
        void onOpenChooseBackground();

        String getFilePathPickedImage(String uriIntent);

        void onCheckBox(CreatedAlbumAdapter.ViewHolder.BindingModel model);

        void onCompletedCreation(String nameAlbum, int numberOfSong);
    }
}
