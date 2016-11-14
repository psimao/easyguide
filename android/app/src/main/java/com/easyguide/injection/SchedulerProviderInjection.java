package com.easyguide.injection;

import com.easyguide.util.schedulers.SchedulerProvider;

public class SchedulerProviderInjection {

    public static SchedulerProvider provideSchedulerProvider() {
        return SchedulerProvider.getInstance();
    }

}
