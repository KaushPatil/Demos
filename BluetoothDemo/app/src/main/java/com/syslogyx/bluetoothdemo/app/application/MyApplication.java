package com.syslogyx.bluetoothdemo.app.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.stetho.Stetho;
import com.syslogyx.bluetoothdemo.database.listener.BaseManagerInterface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kaushik on 22-Nov-16.
 * Application class for this Smart Factory Sirus application.
 * dfgdf
 */
public class MyApplication extends Application {

    public static MyApplication myApplication = null;
    public static final String TAG = MyApplication.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private final ArrayList<Object> registeredManagers;

    /**
     * Parameterless constructor.
     */
    public MyApplication() {
        managerInterfaces = new HashMap<Class<? extends BaseManagerInterface>, Collection<? extends BaseManagerInterface>>();
        registeredManagers = new ArrayList<Object>();
    }

    /**
     * Returns the singleton instance of this class.
     *
     * @return
     */
    public static MyApplication getInstance() {
        return myApplication;
    }

    /**
     * Unmodifiable collections of managers that implement some common
     * interface.
     */
    private Map<Class<? extends BaseManagerInterface>, Collection<? extends BaseManagerInterface>> managerInterfaces;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Logs Context", "My Application Oncreate:" + this);
        myApplication = this;
        MultiDex.install(this);
        initStetho();

    }

    /**
     * Register new manager.
     *
     * @param manager
     */
    public void addManager(Object manager) {
        registeredManagers.add(manager);
    }

    /**
     * @param cls Requested class of managers.
     * @return List of registered manager.
     */
    @SuppressWarnings("unchecked")
    public <T extends BaseManagerInterface> Collection<T> getManagers(
            Class<T> cls) {
        Collection<T> collection = (Collection<T>) managerInterfaces.get(cls);
        if (collection == null) {
            collection = new ArrayList<T>();
            for (Object manager : registeredManagers)
                if (cls.isInstance(manager))
                    collection.add((T) manager);
            collection = Collections.unmodifiableCollection(collection);
            managerInterfaces.put(cls, collection);
        }
        return collection;
    }

    /**
     * Initializes stetho for viewing the database into chrome inspect.
     */
    private void initStetho() {
        // Create an InitializerBuilder
        Stetho.InitializerBuilder initializerBuilder =
                Stetho.newInitializerBuilder(this);

        // Enable Chrome DevTools
        initializerBuilder.enableWebKitInspector(
                Stetho.defaultInspectorModulesProvider(this)
        );

        // Enable command line interface
        initializerBuilder.enableDumpapp(
                Stetho.defaultDumperPluginsProvider(getApplicationContext())
        );

        // Use the InitializerBuilder to generate an Initializer
        Stetho.Initializer initializer = initializerBuilder.build();

        // Initialize Stetho with the Initializer
        Stetho.initialize(initializer);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * Returns valley request queue.
     *
     * @return
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    /**
     * Add the request into valley request queue.
     *
     * @param req
     * @param tag
     * @param <T>
     */
    public <T> void addToRequestQueue(Request<T> req, int tag) {
        // set the default tag if tag is empty
        req.setTag(tag);
        getRequestQueue().add(req);
    }
}

