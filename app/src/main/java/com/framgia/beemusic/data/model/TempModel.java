package com.framgia.beemusic.data.model;

import android.support.annotation.NonNull;

/**
 * Created by beepi on 24/02/2017.
 */
public class TempModel {
    private int mIdSong;
    private int mIdAlbum;
    private int mIdSinger;
    private String mTitleSong;
    private String mLinkSong;
    private boolean mIsFavorite;
    private int mType;
    private String mGenre;
    private int mDuration;
    private int mIdServer;
    private String mNameAlbum;
    private String mNameSinger;

    public TempModel(@NonNull int idSong, @NonNull int idAlbum, @NonNull int idSinger, @NonNull int
        idServer, String titleSong, String linkSong, String genre, String nameAlbum, String
                         nameSinger, boolean isFavorite, int type, int duration) {
        mIdSong = idSong;
        mIdAlbum = idAlbum;
        mIdSinger = idSinger;
        mIdServer = idServer;
        mIsFavorite = isFavorite;
        mType = type;
        mTitleSong = titleSong;
        mLinkSong = linkSong;
        mGenre = genre;
        mNameAlbum = nameAlbum;
        mNameSinger = nameSinger;
        mDuration = duration;
    }

    public int getIdSong() {
        return mIdSong;
    }

    public void setIdSong(int idSong) {
        mIdSong = idSong;
    }

    public int getIdAlbum() {
        return mIdAlbum;
    }

    public void setIdAlbum(int idAlbum) {
        mIdAlbum = idAlbum;
    }

    public int getIdSinger() {
        return mIdSinger;
    }

    public void setIdSinger(int idSinger) {
        mIdSinger = idSinger;
    }

    public String getTitleSong() {
        return mTitleSong;
    }

    public void setTitleSong(String titleSong) {
        mTitleSong = titleSong;
    }

    public String getLinkSong() {
        return mLinkSong;
    }

    public void setLinkSong(String linkSong) {
        mLinkSong = linkSong;
    }

    public boolean isFavorite() {
        return mIsFavorite;
    }

    public void setFavorite(boolean favorite) {
        mIsFavorite = favorite;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public String getGenre() {
        return mGenre;
    }

    public void setGenre(String genre) {
        mGenre = genre;
    }

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public int getIdServer() {
        return mIdServer;
    }

    public void setIdServer(int idServer) {
        mIdServer = idServer;
    }

    public String getNameAlbum() {
        return mNameAlbum;
    }

    public void setNameAlbum(String nameAlbum) {
        mNameAlbum = nameAlbum;
    }

    public String getNameSinger() {
        return mNameSinger;
    }

    public void setNameSinger(String nameSinger) {
        mNameSinger = nameSinger;
    }
}
