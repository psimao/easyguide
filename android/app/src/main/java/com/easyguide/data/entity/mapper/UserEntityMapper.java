package com.easyguide.data.entity.mapper;

import com.easyguide.data.entity.UserEntity;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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

    public static UserEntity transform(GoogleSignInAccount googleSignInAccount) {
        UserEntity user = new UserEntity();
        user.setUid(googleSignInAccount.getId());
        user.setName(googleSignInAccount.getDisplayName());
        user.setEmail(googleSignInAccount.getEmail());
        if(googleSignInAccount.getPhotoUrl() != null){
            user.setPhotoUrl(googleSignInAccount.getPhotoUrl().toString());
        }
        if(googleSignInAccount.getIdToken() != null){
            user.setIdToken(googleSignInAccount.getIdToken());
        }
        return user;
    }

}
