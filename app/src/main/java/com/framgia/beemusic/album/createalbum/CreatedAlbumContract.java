package com.framgia.beemusic.album.createalbum;

import com.framgia.beemusic.BasePresenter;

/**
 * Created by beepi on 13/04/2017.
 */
public interface CreatedAlbumContract {
    interface View {
        void onPickImage();
    }

    interface Presenter extends BasePresenter {
        void onOpenChooseBackground();
        String getFilePathPickedImage(String uriIntent);
    }
}
