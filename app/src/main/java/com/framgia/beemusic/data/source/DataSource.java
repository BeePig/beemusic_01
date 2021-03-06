package com.framgia.beemusic.data.source;

import android.database.Cursor;

import java.util.List;

import rx.Observable;

/**
 * Created by beepi on 17/02/2017.
 */
public interface DataSource<T> {
    List<T> getModel(String selection, String[] args);
    Cursor getCursor(String selection, String[] args);
    T getModel(int id);
    int save(T model);
    int update(T model);
    int delete(int id);
    void deleteAlls();
    Observable<T> getDataObservableByModels(List<T> models);
}
