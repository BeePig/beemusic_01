package com.framgia.beemusic.singer;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.framgia.beemusic.R;
import com.framgia.beemusic.data.model.Singer;
import com.framgia.beemusic.databinding.FragmentSingerBinding;
import java.util.List;

public class SingerFragment extends Fragment implements SingerContract.View {
    private SingerContract.Presenter mPresenter;
    private SingerAdapter mAdapter;
    private FragmentSingerBinding mBinding;

    public SingerFragment() {
    }

    public static SingerFragment newInstance() {
        SingerFragment fragment = new SingerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_singer, container, false);
        mBinding = DataBindingUtil.bind(view);
        if (mPresenter != null) mPresenter.subcribe();
        return view;
    }

    @Override
    public void setPresenter(SingerContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onSearch(String keySearch) {
        if (mPresenter == null) return;
        mPresenter.onSearch(keySearch);
    }

    @Override
    public void initRecycleview(List<Singer> singers) {
        mAdapter = new SingerAdapter(singers, mPresenter);
        mBinding.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null) mPresenter.subcribe();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mPresenter != null) mPresenter.unsubcribe();
    }
}
