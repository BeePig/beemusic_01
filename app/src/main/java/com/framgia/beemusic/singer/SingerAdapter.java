package com.framgia.beemusic.singer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.framgia.beemusic.data.model.Singer;
import com.framgia.beemusic.databinding.ItemSingerAdapterBinding;
import java.util.List;

/**
 * Created by beepi on 16/05/2017.
 */

public class SingerAdapter extends RecyclerView.Adapter<SingerAdapter.ViewHolder> {
    private List<Singer> mSingers;
    private SingerContract.Presenter mPresenter;
    private LayoutInflater mLayoutInflater;

    public SingerAdapter(List<Singer> singers, SingerContract.Presenter presenter) {
        mSingers = singers;
        mPresenter = presenter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mLayoutInflater == null) mLayoutInflater = LayoutInflater.from(parent.getContext());
        ItemSingerAdapterBinding binding =
                ItemSingerAdapterBinding.inflate(mLayoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mSingers == null ? 0 : mSingers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private static final String BAI_HAT = " songs";
        private ItemSingerAdapterBinding mBinding;
        private Singer mSinger;
        private String mCount;

        public ViewHolder(ItemSingerAdapterBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        private void bind(int pos) {
            mSinger = mSingers.get(pos);
            if (mSinger == null) return;
            mCount = String.valueOf(mSinger.getCount()) + BAI_HAT;
            mBinding.setHolder(this);
            mBinding.setPresenter(mPresenter);
            mBinding.executePendingBindings();
        }

        public Singer getSinger() {
            return mSinger;
        }

        public String getCount() {
            return mCount;
        }
    }
}
