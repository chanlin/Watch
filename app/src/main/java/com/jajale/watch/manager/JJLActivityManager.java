package com.jajale.watch.manager;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by athena on 2015/11/12.
 * Email: lizhiqiang@bjjajale.com
 */
public class JJLActivityManager {

    private static JJLActivityManager instance = null;

    private JJLActivityManager() {
    }

    public static JJLActivityManager getInstance() {
        if (instance == null) {
            instance = new JJLActivityManager();
        }
        return instance;
    }

    private List<Activity> activityList = new LinkedList<Activity>();

    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    public void killExceptItem(Activity item) {
        int size = activityList.size();
        for (int i = 0; i < size; i++) {
            Activity activity = activityList.get(i);
            if (activity != null) {
                if (!activity.equals(item)) {
                    activityList.get(i).finish();
                }
            }
        }
        activityList.clear();
        activityList.add(item);
    }

    public void killAllActivity() {

        int size = activityList.size();
        for (int i = 0; i < size; i++) {
            if (activityList.get(i) != null) {
                activityList.get(i).finish();
            }
        }
        activityList.clear();
    }

    public boolean exist(String simpleName) {
        if (activityList.size() > 0) {
            for (Activity activity : activityList) {
                if (activity.getClass().getSimpleName().equals(simpleName)) {
                    return true;
                }
            }
        }
        return false;
    }

}
