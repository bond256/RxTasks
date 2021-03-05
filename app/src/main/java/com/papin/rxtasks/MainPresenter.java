package com.papin.rxtasks;

import android.annotation.SuppressLint;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

class MainPresenter {

    private Network network;
    private View mView;

    public MainPresenter(View view) {
        network = new Network();
        this.mView=view;
    }

    @SuppressLint("CheckResult")
    void loadSmth() {
        network.loadSmth()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<Boolean>() {
                @Override
                public void accept(Boolean aBoolean) throws Exception {

                }
            });
    }

    @SuppressLint("CheckResult")
    void task1(){
        network.getFirstPage()
                .subscribeOn(Schedulers.io())
                .zipWith(network.getSecondPage(), new BiFunction<List<Story>, List<Story>, List<Story>>() {

                    @NonNull
                    @Override
                    public List<Story> apply(@NonNull List<Story> stories, @NonNull List<Story> stories2) throws Exception {
                        stories.addAll(stories2);
                        return stories;
                    }
                })
                .subscribe((Consumer<List<Story>>) stories -> mView.showResultFirst(stories));
    }

    @SuppressLint("CheckResult")
    void task2(){
        network.getFirstPage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .flatMapIterable(new Function<List<Story>, Iterable<Story>>() {
                    @Override
                    public Iterable<Story> apply(@NonNull List<Story> stories) throws Exception {
                        return stories;
                    }
                })
                .flatMap(it->network.getAuthor(it.getAuthor()).toObservable())
                .filter(it->it.getKarma()>3000)
                .toList()
                .subscribe(it->mView.showResultSecond(it));

    }

    @SuppressLint("CheckResult")
    void task3(){
        network.getFirstPage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Story>>() {
                    @Override
                    public void accept(List<Story> stories) throws Exception {
                        //mView.showResultSecond(stories);
                    }
                });
    }


}
