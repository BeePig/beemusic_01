package com.framgia.beemusic.album.createalbum;

import android.content.Intent;

import com.framgia.beemusic.data.model.Album;
import com.framgia.beemusic.data.source.AlbumDataSource;

/**
 * Created by beepi on 13/04/2017.
 */
public class CreatedAlbumPresenter implements CreatedAlbumContract.Presenter {
    private CreatedAlbumContract.View mView;
    private AlbumDataSource mAlbumRepository;
    private Album mAlbum = new Album();

    public CreatedAlbumPresenter(CreatedAlbumContract.View view, AlbumDataSource albumRepository) {
        mView = view;
        mAlbumRepository = albumRepository;
    }

    @Override
    public void onOpenChooseBackground() {
        mView.onPickImage();
    }

    @Override
    public String getFilePathPickedImage(String uriIntent) {
        String path = mAlbumRepository.getFilePickedImageLocal(uriIntent);
        if(path == null) return null;
        mAlbum.setImageLink(path);
        return path;
    }

    @Override
    public void subcribe() {
    }

    @Override
    public void unsubcribe() {
    }
}
