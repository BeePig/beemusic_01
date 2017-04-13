package com.framgia.beemusic.album.createalbum;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.framgia.beemusic.BR;
import com.framgia.beemusic.data.model.Song;
import com.framgia.beemusic.databinding.ItemCreatedAlbumAdapterBinding;

/**
 * Created by beepi on 04/05/2017.
 */

public class CreatedAlbumAdapter extends RecyclerView.Adapter<CreatedAlbumAdapter.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private ObservableArrayList<Song> mSongList;
    private ObservableArrayList<String> mSingerList;
    private CreatedAlbumContract.Presenter mPresenter;

    CreatedAlbumAdapter(ObservableArrayList<Song> songs, ObservableArrayList<String> singers,
            CreatedAlbumContract.Presenter presenter) {
        mSongList = songs;
        mSingerList = singers;
        mPresenter = presenter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mLayoutInflater == null) mLayoutInflater = LayoutInflater.from(parent.getContext());
        ItemCreatedAlbumAdapterBinding binding =
                ItemCreatedAlbumAdapterBinding.inflate(mLayoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mSongList == null ? 0 : mSongList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemCreatedAlbumAdapterBinding mBinding;
        private BindingModel mModel;

        public ViewHolder(ItemCreatedAlbumAdapterBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(int pos) {
            Song song = mSongList.get(pos);
            if (song == null) return;
            mModel = new BindingModel(song, mSingerList.get(pos), mPresenter);
            mBinding.setModel(mModel);
            mBinding.executePendingBindings();
        }

        public class BindingModel extends BaseObservable {
            private Song mSong;
            private String mSinger;
            private CreatedAlbumContract.Presenter mBindPresenter;

            public BindingModel(Song song, String singer,
                    CreatedAlbumContract.Presenter presenter) {
                mSong = song;
                mSinger = singer;
                mBindPresenter = presenter;
            }

            @Bindable
            public Song getSong() {
                return mSong;
            }

            public void setSong(Song song) {
                mSong = song;
                notifyPropertyChanged(BR.song);
            }

            @Bindable
            public String getSinger() {
                return mSinger;
            }

            public void setSinger(String singer) {
                mSinger = singer;
                notifyPropertyChanged(BR.singer);
            }

            public CreatedAlbumContract.Presenter getBindPresenter() {
                return mBindPresenter;
            }

            public void setBindPresenter(CreatedAlbumContract.Presenter bindPresenter) {
                mBindPresenter = bindPresenter;
            }
        }
    }
}
