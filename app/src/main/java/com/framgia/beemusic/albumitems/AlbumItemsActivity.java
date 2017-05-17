package com.framgia.beemusic.albumitems;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;
import com.framgia.beemusic.BeeApplication;
import com.framgia.beemusic.R;
import com.framgia.beemusic.album.addtoalbum.ChooseAlbumActivity;
import com.framgia.beemusic.data.model.Album;
import com.framgia.beemusic.data.model.Song;
import com.framgia.beemusic.data.source.AlbumRepository;
import com.framgia.beemusic.data.source.SingerRepository;
import com.framgia.beemusic.data.source.SongAlbumRepository;
import com.framgia.beemusic.data.source.SongRepository;
import com.framgia.beemusic.data.source.SongSingerRepository;
import com.framgia.beemusic.databinding.ActivityAlbumItemsBinding;
import java.util.List;

public class AlbumItemsActivity extends AppCompatActivity
        implements AlbumItemsContract.View, SearchView.OnQueryTextListener {
    private static final String EXTRA_ALBUM = "EXTRA_ALBUM";
    private Album mAlbum;
    private AlbumItemsContract.Presenter mPresenter;
    private ObservableField<AlbumItemsAdapter> mAdapter = new ObservableField<>();
    private ActivityAlbumItemsBinding mBinding;
    private String mNumberOfSong;

    public static Intent getIntent(Album album) {
        Intent intent = new Intent(BeeApplication.getInstant(), AlbumItemsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_ALBUM, album);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_album_items);
        mPresenter = new AlbumItemsPresenter(this, SongRepository.getInstant(this),
                SongSingerRepository.getInstant(this), SongAlbumRepository.getInstant(this),
                SingerRepository.getInstant(this), AlbumRepository.getInstant(this));
        mBinding.setActivity(this);
        extraBundle();
        mBinding.searchview.setOnQueryTextListener(this);
        if (mPresenter != null && mAlbum != null) mPresenter.subcribe(mAlbum.getId());
    }

    @Override
    public void initRecycleView(List<Song> songs, List<String> singer) {
        mAdapter.set(new AlbumItemsAdapter(songs, singer, mPresenter));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void notifyItemRemove(int pos) {
        mAdapter.get().getSingers().remove(pos);
        mAdapter.get().getSongs().remove(pos);
        mAdapter.get().notifyItemRemoved(pos);
    }

    @Override
    public void onAddToAnotherAlbum(Song song) {
        startActivity(ChooseAlbumActivity.getIntent(song));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null && mAlbum != null) mPresenter.subcribe(mAlbum.getId());
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mPresenter != null) mPresenter.unsubcribe();
    }

    private void extraBundle() {
        Intent intent = getIntent();
        if (intent == null) ;
        mAlbum = (Album) intent.getSerializableExtra(EXTRA_ALBUM);
        if (mAlbum == null) return;
        mNumberOfSong = String.valueOf(mAlbum.getCount());
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mAdapter.get().onSearch(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mAdapter.get().onSearch(newText);
        return true;
    }

    public Album getAlbum() {
        return mAlbum;
    }

    public ObservableField<AlbumItemsAdapter> getAdapter() {
        return mAdapter;
    }

    public String getNumberOfSong() {
        return mNumberOfSong;
    }
}
