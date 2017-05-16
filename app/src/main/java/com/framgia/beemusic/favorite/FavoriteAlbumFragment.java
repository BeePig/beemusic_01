package com.framgia.beemusic.favorite;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.framgia.beemusic.BeeApplication;
import com.framgia.beemusic.R;
import com.framgia.beemusic.album.addtoalbum.ChooseAlbumActivity;
import com.framgia.beemusic.data.model.Song;
import com.framgia.beemusic.databinding.FragmentFavoriteAlbumBinding;

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
    public void initRecycleview(ObservableArrayList<Song> songs,
            ObservableArrayList<String> singer) {
        mAdapter = new FavoriteAlbumAdapter(songs, singer, mPresenter);
        mBinding.setAdapter(mAdapter);
    }

    @Override
    public void notifyItemRemove(Song song, int pos) {
        mAdapter.getSingers().remove(pos);
        mAdapter.getSongs().remove(song);
        mAdapter.notifyItemRemoved(pos);
        Snackbar.make(getActivity().findViewById(android.R.id.content),
                R.string.action_remove_succesfully, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onAddToAnotherAlbum(Song song) {
        BeeApplication.getInstant().startActivity(ChooseAlbumActivity.getIntent(song));
    }
}
