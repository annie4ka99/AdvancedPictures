package com.example.advancedpictures;

import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class DBTest {
    private FavouritesDBHelper helper;
    private Observer<Boolean> observerTrue, observerFalse;

    @Before
    public void setUp() {
        getTargetContext().deleteDatabase(FavouritesDBHelper.getTableName());
        helper = FavouritesDBHelper.getInstance(getTargetContext());

        observerTrue = new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }
            @Override
            public void onNext(Boolean o) {
                assertTrue(o);
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onComplete() {
            }
        };

        observerFalse = new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }
            @Override
            public void onNext(Boolean o) {
                assertFalse(o);
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onComplete() {
            }
        };


    }

    @After
    public void finish() {
        //Log.d("TEST", " FINISHED SUCCESSFULLY");
        helper.close();
    }

    private void check(String description, String url, boolean result) throws InterruptedException {
        if (result) helper.check(description, url).blockingSubscribe(observerTrue);
        else helper.check(description, url).blockingSubscribe(observerFalse);
    }

    private void add(String description, String url, boolean result) throws InterruptedException {
        if (result) helper.add(description, url).blockingSubscribe(observerTrue);
        else helper.add(description, url).blockingSubscribe(observerFalse);
    }

    private void del(String description, String url, boolean result) throws InterruptedException {
        if (result) helper.delete(description, url).blockingSubscribe(observerTrue);
        else helper.delete(description, url).blockingSubscribe(observerFalse);
    }


    public void simpleTest(String description, String url) throws InterruptedException {
        check(url, description, false);
        add(description, url, true);
        check(description,url,true);
        add(description, url, false);
        del(description, url, true);
        del(description, url, false);
    }
    public void simpleTest2(String description, String url) throws InterruptedException {
        add(description, url, true);
        check(description,url,true);
        add(description, url, false);

        add(url, description, true);
        add(url, description, false);
        check(url, description, true);

        del(url, description, true);
        check(url, description, false);
        del(url, description, false);

        del(description, url, true);
        check(description, url, false);
        del(description, url, false);
    }
    @Test
    public void checkDB() throws InterruptedException {
        simpleTest("TEST", "SOME");
        simpleTest("TEST2", "SOME2");

    }
    @Test
    public void checkDB2() throws InterruptedException {
        simpleTest2("TEST3", "SOME3");
        simpleTest2("TEST4", "SOME4");
    }

}
