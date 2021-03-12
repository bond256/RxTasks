package com.papin.rxtasks;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;

import com.google.gson.internal.$Gson$Preconditions;

import java.net.BindException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
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
        this.mView = view;
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
    void task1() {
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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((Consumer<List<Story>>) stories -> mView.showResultFirst(stories));
    }

    @SuppressLint("CheckResult")
    void task2() {
        network.getFirstPage()
                .subscribeOn(Schedulers.io())
                .toObservable()
                .flatMapIterable(new Function<List<Story>, Iterable<Story>>() {
                    @Override
                    public Iterable<Story> apply(@NonNull List<Story> stories) throws Exception {
                        return stories;
                    }
                })
                .flatMap(it -> network.getAuthor(it.getAuthor()).toObservable())
                .filter(it -> it.getKarma() > 3000)
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(it -> mView.showResultSecond(it));

    }

    @SuppressLint("CheckResult")
    void task3() {
        Maybe<String> maybeSource = Maybe.create(emitter -> {
            boolean flag = new Random().nextBoolean();
            if (flag)
                emitter.onSuccess("Bang1");
            else emitter.onError(new IllegalArgumentException());
        });

        maybeSource
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(it1 -> {
                    Log.d("tag", "task3: " + it1);
                }, it2 -> Log.d("tag", "task3 error: " + it2));
    }

    @SuppressLint("CheckResult")
    void task4() {
        Maybe<String> maybeSource = Maybe.create(emitter -> {
            boolean flag = new Random().nextBoolean();
            if (flag) emitter.onSuccess("Bang1");
            else emitter.onComplete();
        });

        maybeSource
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(it1 -> {
                            Log.d("tag", "task4: " + it1);
                        }
                );


    }


    @SuppressLint("CheckResult")
    void task5() {
        Maybe<String> maybeSource = Maybe.create(emitter -> {
            boolean flag = new Random().nextBoolean();
            if (flag) emitter.onSuccess("Bang!");
            else emitter.onError(new Throwable("You're live"));
        });

        maybeSource
                .subscribeOn(Schedulers.io())
                .toSingle()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(it -> Log.d("tag", "task5: " + it.getLocalizedMessage()))
                .subscribe(it -> Log.d("tag", "task5: " + it));

    }

    @SuppressLint("CheckResult")
    void task7() {
        Observable<Long> observable = Observable.intervalRange(0, 10, 0, 1, TimeUnit.SECONDS);
        observable
                .subscribeOn(Schedulers.io()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d("tag", "onSubscribe: ");
                    }

                    @Override
                    public void onNext(@NonNull Long value) {
                        Log.d("tag", "onNext: " + value);
                    }


                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("tag", "onError: " + e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


//    @SuppressLint("CheckResult")
//    void task8(){
//        ExecutorService executorService1= Executors.newFixedThreadPool(1);
//        ExecutorService executorService2= Executors.newFixedThreadPool(1);
//        ExecutorService executorService3= Executors.newFixedThreadPool(1);
//        network.getFirstPage()
//                .subscribeOn(Schedulers.from(executorService1))
//                .flatMap(it->network.getSecondPage()
//                .subscribeOn(Schedulers.from(executorService2))
//                .subscribeOn(Schedulers.from(executorService3)))
//                .su
//
//
//    }

    @SuppressLint("CheckResult")
    void task9() {
        Observable<Long> observable = Observable.intervalRange(0, 10, 0, 1, TimeUnit.SECONDS);
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d("tag", "Subscribed");

                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        if (aLong == 7) {
                            onError(new Exception("Your error message"));
                        }
                        Log.d("tag", "onNext: " + aLong);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("tag", "onError: " + e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @SuppressLint("CheckResult")
    void task10() {
        BehaviorSubject<Integer> behaviorSubject = BehaviorSubject.create();
        behaviorSubject.onNext(1);
        behaviorSubject.onNext(2);
        behaviorSubject.onNext(3);
        behaviorSubject
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d("tag", "onSubscribe: ");
                    }

                    @Override
                    public void onNext(@NonNull Integer integer) {
                        Log.d("tag", "onNext: " + integer);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("tag", "onNext: " + e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d("tag", "onComplete");

                    }
                });
        behaviorSubject.onNext(4);
        behaviorSubject.onNext(5);
        behaviorSubject.onNext(6);
        //behaviorSubject.onComplete();
        Log.d("tag", "task4: " + behaviorSubject.getValue().toString());
    }
//    void task9(){
//        Observable observable=Observable.interval(1, TimeUnit.SECONDS).
//                flatMap(it->{
//                    return Observable.create(emitter -> )
//                        }
//                )
//    }

    @SuppressLint("CheckResult")
    void task11() {
        network.getFirstPage()
                .subscribeOn(Schedulers.io())
                .map(it -> it.get(2))
                .flatMap(it1 -> network.getAuthor(it1.getAuthor())
                        .map(it2 -> {
                            return new AuthorInfo(it2.getName(), it2.getKarma(), it1.getTitle());
                        })
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AuthorInfo>() {
                    @Override
                    public void accept(AuthorInfo authorInfo) throws Exception {
                        Log.d("tag", "task11: " + authorInfo.getName() + " " + authorInfo.getKarma() + " " + authorInfo.getTitle());
                    }
                });

    }


}
