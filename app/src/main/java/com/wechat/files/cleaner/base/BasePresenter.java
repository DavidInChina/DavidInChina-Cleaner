package com.wechat.files.cleaner.base;


public interface BasePresenter<T extends BaseView> {
    void attachView(T view);
    void detachView();
}
