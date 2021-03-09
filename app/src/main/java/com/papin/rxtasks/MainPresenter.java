package com.papin.rxtasks;

import android.annotation.SuppressLint;
import android.util.Log;

import java.net.BindException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

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
        Observable.just("Bung");
    }
    @SuppressLint("CheckResult")
    void task4(){
        BehaviorSubject<Integer> behaviorSubject=BehaviorSubject.create();
        behaviorSubject.onNext(1);
        behaviorSubject.onNext(2);
        behaviorSubject.onNext(3);
        behaviorSubject
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Integer integer) {
                        Log.d("tag", "onNext: "+integer);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
    });
        behaviorSubject.onNext(4);
        behaviorSubject.onNext(5);
        behaviorSubject.onNext(6);
        behaviorSubject.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d("tag", "onNext: "+integer);
            }
        });

        Log.d("tag", "task4: "+behaviorSubject.getValue().toString());
    }

    void task11(){
        network.getFirstPage()
                .flatMap(it->network.getAuthor(it.get(2).getAuthor()))
                .map(it->)
                .subscribeOn(Schedulers.io())
    }


}
