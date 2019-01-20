package com.san4o.just4fun.smsparser.app.dagger



import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import dagger.android.AndroidInjection

class DaggerActivityLifecycleCallback : Application.ActivityLifecycleCallbacks {


    override fun onActivityCreated(activity: Activity, p1: Bundle?) {

        if (activity is AppScopeMember) {
            AndroidInjection.inject(activity)
        }

    }


    override fun onActivityPaused(p0: Activity?) {// TODO not implemented
    }

    override fun onActivityResumed(p0: Activity?) {// TODO not implemented
    }

    override fun onActivityStarted(p0: Activity?) {// TODO not implemented
    }

    override fun onActivityDestroyed(p0: Activity?) {// TODO not implemented
    }

    override fun onActivitySaveInstanceState(p0: Activity?, p1: Bundle?) {// TODO not implemented
    }

    override fun onActivityStopped(p0: Activity?) {// TODO not implemented
    }
}
