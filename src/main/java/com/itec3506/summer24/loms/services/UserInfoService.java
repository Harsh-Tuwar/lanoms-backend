package com.itec3506.summer24.loms.services;

import com.itec3506.summer24.loms.models.User;
import com.itec3506.summer24.loms.models.UserInfoDetails;
import com.itec3506.summer24.loms.models.UserListItem;
import com.itec3506.summer24.loms.repositories.UserInfoRepository;
import com.itec3506.summer24.loms.utils.LomsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserInfoService implements UserDetailsService {

    @Autowired
    private UserInfoRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userDetail = repository.getUserByEmail(email);

        // Converting userDetail to UserDetails
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + email));
    }

    public String addUser(User userInfo) {
        LomsUtils utils = new LomsUtils();
        userInfo.setUserId(utils.generateUuid());
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        repository.save(userInfo);
        return "User Added Successfully";
    }

    public List<UserListItem> getAllUsers() {
        List<UserListItem> users = new ArrayList<UserListItem>();

        try {
            for (UserInfoRepository.NameOnly userListItem : repository.getAllUsers()) {
                UserListItem user = new UserListItem();
                user.setName(userListItem.getName());
                user.setEmail(userListItem.getEmail());
                user.setUser_id(userListItem.getUserId());
                user.setRoles(userListItem.getRoles());

                users.add(user);
            }
        } catch (UnknownError error) {
            System.out.println(error.getMessage());
        }

        return users;
    }
}
