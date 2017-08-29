package com.returnlive.healthinspectioninstrument.utils;

import android.content.Context;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by 张梓彬 on 2017/6/16 0016.
 */

public class RxUtils {
    private static final String TAG = "RxUtils";
    public static String str = "";
    private Context context;
    private OkHttpClient okHttpClient;

    public RxUtils(Context context) {
        this.context = context;
        okHttpClient = new OkHttpClient();
    }

    public static void createObservable() {
        //定义被观察者
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext("我");
                    subscriber.onNext("是");
                    subscriber.onNext("张");
                    subscriber.onNext("梓");
                    subscriber.onNext("彬");
                    subscriber.onCompleted();
                }
            }
        });

        //定义观察者
        Subscriber<String> showsub = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.e(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onNext(String s) {
                Log.e(TAG, "onNext: " + s);
                str = s;
            }
        };
        observable.subscribe(showsub);//关联被观察者
    }


    public static void createObser() {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    for (int i = 0; i < 10; i++) {
                        subscriber.onNext(i);
                    }
                    subscriber.onCompleted();
                }
            }
        }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Log.e(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onNext(Integer integer) {
                Log.e(TAG, "onNext: " + integer);
            }
        });

    }

    //使用在被观察者，返回的对象一般都是数值类型
    public static void from() {
        Integer[] items = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        Observable observable = Observable.from(items);
        observable.subscribe(new Action1() {
            @Override
            public void call(Object o) {
                Log.e(TAG, "call: " + o.toString());
            }
        });
    }


    //每隔1秒发送一个数据
    public static void interval() {
        Integer[] items = {1, 2, 3, 4, 5};
        Observable observal = Observable.interval(1, 1, TimeUnit.SECONDS);
        observal.subscribe(new Action1() {
            @Override
            public void call(Object o) {
                Log.e(TAG, "call: " + o.toString());
            }
        });
    }


    //数组依次输出
    public static void just() {
        Integer[] items1 = {1, 3, 5, 7, 9};
        Integer[] items2 = {2, 4, 6, 8, 10};
        Observable observal = Observable.just(items1, items2);
        observal.subscribe(new Subscriber<Integer[]>() {
            @Override
            public void onCompleted() {
                Log.e(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onNext(Integer[] o) {
                for (int i = 0; i < o.length; i++) {
                    Log.e(TAG, "onNext: " + o[i]);

                }
            }
        });
    }

    //输出在某个范围中的值
    public static void range() {
        Observable observal = Observable.range(20, 10);
        observal.subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Log.e(TAG, "onCompleted: ");

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());

            }

            @Override
            public void onNext(Integer o) {
                Log.e(TAG, "onNext: " + o);
            }
        });
    }

    //过滤
    public static void filite() {
        Observable observable = Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9);
        observable.filter(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer o) {
                return o < 5;
            }
        }).observeOn(Schedulers.io()).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Log.e(TAG, "onCompleted: ");

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());

            }

            @Override
            public void onNext(Integer o) {
                Log.e(TAG, "onNext: " + o.toString());
            }
        });
    }


    /*public Observable<String> dwonLoadBaiDu(){
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                OkHttpUtils.get().url("http://192.168.0.100/wuliu.txt")
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        subscriber.onError(e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        subscriber.onNext(response);
                        subscriber.onNext(response+"张梓彬");
                        subscriber.onCompleted();
                    }
                });
            }
        });
    }*/

   /* public Observable<String> dwonLoadBaiDu() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                Request request = new Request.Builder().get().url("https://hao.360.cn/?z1002").build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        subscriber.onError(e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()){
                            subscriber.onNext(response.body().string());
                        }
                        subscriber.onCompleted();
                    }
                });
            }
        });
    }*/
}
