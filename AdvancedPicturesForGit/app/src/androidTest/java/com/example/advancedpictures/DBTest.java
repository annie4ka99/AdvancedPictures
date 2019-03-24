package com.example.advancedpictures;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.advancedpictures.FavouritesDBHelper;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.TestObserver;


import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class DBTest {

    private FavouritesDBHelper helper;
    private CompositeDisposable compositeDisposable;


    @Before
    public void setUp() {
        getTargetContext().deleteDatabase(FavouritesDBHelper.getTableName());
        helper = FavouritesDBHelper.getInstance(getTargetContext());
        compositeDisposable = new CompositeDisposable();
        /*
        asserterFalse = new Consumer<Boolean>() {
            @Override
            public void accept(Boolean f) throws Exception {
                //if (f)
                //    Log.d("BOOLEAN ACCEPTED", "TRUE");
                assertFalse(f);
            }
        };
        asserterTrue = new Consumer<Boolean>() {
            @Override
            public void accept(Boolean f) throws Exception {
                assertTrue(f);
            }
        };
        */

    }

    @After
    public void finish() {
        compositeDisposable.clear();
        Log.d("TEST", " FINISHED SUCCESSFULLY");
        helper.close();
    }

    private void check(String description, String srcUrl, boolean result) throws InterruptedException {
        TestObserver<Boolean> testObserver = new TestObserver<>();
        compositeDisposable.add(helper.check(description, srcUrl).subscribeWith(testObserver));
        if (result) {
            testObserver.awaitTerminalEvent();
            testObserver.assertValue(true);
        } else {
            testObserver.awaitTerminalEvent();
            testObserver.assertValues(false);
        }
    }

    private void add(String description, String srcUrl, boolean result) throws InterruptedException {
        TestObserver<Boolean> testObserver = new TestObserver<>();
        compositeDisposable.add(helper.add(description, srcUrl).subscribeWith(testObserver));
        if (result) {
            testObserver.awaitTerminalEvent();
            testObserver.assertValue(true);
        } else {
            testObserver.awaitTerminalEvent();
            testObserver.assertValues(false);
        }
    }

    private void del(String description, String srcUrl, boolean result) throws InterruptedException {
        TestObserver<Boolean> testObserver = new TestObserver<>();
        compositeDisposable.add(helper.delete(description, srcUrl).subscribeWith(testObserver));
        if (result) {
            testObserver.awaitTerminalEvent();
            testObserver.assertValue(true);
        } else {
            testObserver.awaitTerminalEvent();
            testObserver.assertValues(false);
        }
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
