package com.lfpo.msuser.services;

import com.lfpo.msuser.models.UserModel;

public interface UserService {
    void save(UserModel userModel);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
