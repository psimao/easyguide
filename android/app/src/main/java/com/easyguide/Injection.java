package com.easyguide;

import com.easyguide.data.repository.user.UserRemoteDataSource;
import com.easyguide.data.repository.user.UserRepository;
import com.easyguide.util.schedulers.SchedulerProvider;
import com.google.firebase.auth.FirebaseAuth;

public class Injection {

    public static UserRepository provideUserRepository() {
        return UserRepository.getInstance(
                new UserRemoteDataSource(
                    FirebaseAuth.getInstance()
                )
        );
    }

    public static SchedulerProvider provideSchedulerProvider() {
        return SchedulerProvider.getInstance();
    }

}
