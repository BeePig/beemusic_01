package com.framgia.beemusic.singeritems;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;
import com.framgia.beemusic.BeeApplication;
import com.framgia.beemusic.R;
import com.framgia.beemusic.data.model.Singer;
import com.framgia.beemusic.data.model.Song;
import com.framgia.beemusic.data.source.SongRepository;
import com.framgia.beemusic.data.source.SongSingerRepository;
import com.framgia.beemusic.databinding.ActivitySingerItemsBinding;
import java.util.List;

public class SingerItemsActivity extends AppCompatActivity
        implements SingerItemsContract.View, SearchView.OnQueryTextListener {

    private static final String EXTRA_SINGER = "EXTRA_SINGER";
    private ActivitySingerItemsBinding mBinding;
    private Singer mSinger;
    private SingerItemsContract.Presenter mPresenter;
    private ObservableField<SingerItemsAdapter> mAdapter = new ObservableField<>();
    private String mNumberOfSong;

    public static Intent getIntent(Singer singer) {
        Intent intent = new Intent(BeeApplication.getInstant(), SingerItemsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_SINGER, singer);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_singer_items);
        mBinding.searchview.setOnQueryTextListener(this);
        mPresenter = new SingerItemsPresenter(this, SongRepository.getInstant(this),
                SongSingerRepository.getInstant(this));
        getExtra();
        mBinding.setActivity(this);
        if (mPresenter != null && mSinger != null) mPresenter.subcribe(mSinger.getId());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null && mSinger != null) mPresenter.subcribe(mSinger.getId());
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mPresenter != null) mPresenter.unsubcribe();
    }

    @Override
    public void initRecycleView(List<Song> songs) {
        mAdapter.set(new SingerItemsAdapter(songs, mPresenter));
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

    private void getExtra() {
        Intent intent = getIntent();
        if (intent == null) ;
        mSinger = (Singer) intent.getSerializableExtra(EXTRA_SINGER);
        if (mSinger == null) return;
        mNumberOfSong = String.valueOf(mSinger.getCount());
    }

    public ObservableField<SingerItemsAdapter> getAdapter() {
        return mAdapter;
    }

    public String getNumberOfSong() {
        return mNumberOfSong;
    }

    public Singer getSinger() {
        return mSinger;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (mAdapter.get() == null) return false;
        mAdapter.get().onSearch(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (mAdapter.get() == null) return false;
        mAdapter.get().onSearch(newText);
        return true;
    }
}
