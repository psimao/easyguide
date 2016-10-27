package com.easyguide.data.entity.mapper;

import com.easyguide.data.entity.UserEntity;
import com.google.firebase.auth.FirebaseUser;

public class UserEntityMapper {

    public static UserEntity transform(FirebaseUser firebaseUser) {
        UserEntity user = new UserEntity();
        user.setUid(firebaseUser.getUid());
        user.setName(firebaseUser.getDisplayName());
        user.setEmail(firebaseUser.getEmail());
        if(firebaseUser.getPhotoUrl() != null){
            user.setPhotoUrl(firebaseUser.getPhotoUrl().toString());
        }
        return user;
    }

}
