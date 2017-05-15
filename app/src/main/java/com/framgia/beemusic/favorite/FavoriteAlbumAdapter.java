package com.framgia.beemusic.favorite;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.framgia.beemusic.data.model.Song;
import com.framgia.beemusic.databinding.ItemFavoriteAlbumAdapterBinding;
import java.util.List;

/**
 * Created by beepi on 14/05/2017.
 */

public class FavoriteAlbumAdapter extends RecyclerView.Adapter<FavoriteAlbumAdapter.ViewHolder> {
    private List<Song> mSongs;
    private List<String> mSingers;
    private LayoutInflater mLayoutInflater;
    private FavoriteAlbumContract.Presenter mFavoriteAlbumPresenter;

    public FavoriteAlbumAdapter(List<Song> songs, List<String> singers,
            FavoriteAlbumContract.Presenter favoriteAlbumPresenter) {
        mSongs = songs;
        mSingers = singers;
        mFavoriteAlbumPresenter = favoriteAlbumPresenter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mLayoutInflater == null) mLayoutInflater = LayoutInflater.from(parent.getContext());
        ItemFavoriteAlbumAdapterBinding binding =
                ItemFavoriteAlbumAdapterBinding.inflate(mLayoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mSongs == null ? 0 : mSongs.size();
    }

    public List<Song> getSongs() {
        return mSongs;
    }

    public List<String> getSingers() {
        return mSingers;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemFavoriteAlbumAdapterBinding mBinding;

        public ViewHolder(ItemFavoriteAlbumAdapterBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        private void bind(int pos) {
            Song song = mSongs.get(pos);
            if (song == null) return;
            mBinding.setModel(new BindingModel(song, mSingers.get(pos)));
            mBinding.executePendingBindings();
        }
    }

    public class BindingModel {
        private Song mSong;
        private String mSinger;

        public BindingModel(Song song, String singer) {
            mSong = song;
            mSinger = singer;
        }

        public Song getSong() {
            return mSong;
        }

        public String getSinger() {
            return mSinger;
        }
    }
}
