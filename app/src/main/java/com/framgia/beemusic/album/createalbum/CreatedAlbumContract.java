package com.framgia.beemusic.album.createalbum;

import android.databinding.ObservableArrayList;
import com.framgia.beemusic.BasePresenter;
import com.framgia.beemusic.data.model.Song;

/**
 * Created by beepi on 13/04/2017.
 */
public interface CreatedAlbumContract {
    interface View {
        void onPickImage();

        void initRecycleview(ObservableArrayList<Song> songs, ObservableArrayList<String> singers);
    }

    interface Presenter extends BasePresenter {
        void onOpenChooseBackground();

        String getFilePathPickedImage(String uriIntent);

        void onCompletedCreation();
    }
}
