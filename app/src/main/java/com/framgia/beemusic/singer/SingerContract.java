package com.framgia.beemusic.singer;

import com.framgia.beemusic.BaseFragmentPresenter;
import com.framgia.beemusic.BaseFragmentView;
import com.framgia.beemusic.data.model.Singer;
import java.util.List;

/**
 * Created by beepi on 16/05/2017.
 */

public interface SingerContract {
    interface View extends BaseFragmentView<Presenter> {
        void initRecycleview(List<Singer> singers);
    }

    interface Presenter extends BaseFragmentPresenter {

    }
}
