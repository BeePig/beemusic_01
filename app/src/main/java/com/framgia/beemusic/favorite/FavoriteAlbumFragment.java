package com.framgia.beemusic.favorite;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.framgia.beemusic.R;
import com.framgia.beemusic.data.model.Song;
import com.framgia.beemusic.databinding.FragmentFavoriteAlbumBinding;
import java.util.List;

public class FavoriteAlbumFragment extends Fragment implements FavoriteAlbumContract.View {

    private FragmentFavoriteAlbumBinding mBinding;
    private FavoriteAlbumContract.Presenter mPresenter;
    private FavoriteAlbumAdapter mAdapter;
    public FavoriteAlbumFragment() {
    }

    public static FavoriteAlbumFragment newInstance() {
        FavoriteAlbumFragment fragment = new FavoriteAlbumFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_album, container, false);
        mBinding = DataBindingUtil.bind(view);
        if (mPresenter != null) mPresenter.subcribe();
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setPresenter(FavoriteAlbumContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onSearch(String keySearch) {

    }

    @Override
    public void initRecycleview(List<Song> songs, List<String> singer) {
        mAdapter = new FavoriteAlbumAdapter(songs, singer, mPresenter);
        mBinding.setAdapter(mAdapter);
    }

    @Override
    public void notifyItemRemove(int pos) {
        mAdapter.getSingers().remove(pos);
        mAdapter.getSongs().remove(pos);
        mAdapter.notifyItemRemoved(pos);
    }

    @Override
    public void onAddToAnotherAlbum(Song song) {

    }
}
