package com.itec3506.summer24.loms.services;

import com.itec3506.summer24.loms.models.User;
import com.itec3506.summer24.loms.models.UserInfoDetails;
import com.itec3506.summer24.loms.models.UserListItem;
import com.itec3506.summer24.loms.repositories.UserInfoRepository;
import com.itec3506.summer24.loms.types.UserStatusEnum;
import com.itec3506.summer24.loms.utils.LomsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InsufficientAuthenticationException;
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
    public UserInfoDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userDetail = repository.getUserByEmail(email);

        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + email));
    }

    public String addUser(User userInfo) {
        LomsUtils utils = new LomsUtils();
        userInfo.setUserId(utils.generateUuid());
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        userInfo.setStatus(UserStatusEnum.AWAY);
        userInfo.setDeletedAt(null);
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
                user.setStatus(userListItem.getStatus());
                users.add(user);
            }
        } catch (UnknownError error) {
            System.out.println(error.getMessage());
        }

        return users;
    }

    public void deleteUser(String requesterId, String userIdToDelete) throws Exception {
        try {
            UserInfoRepository.UserRolesByUserId requesterInfo = repository.getRolesByUserId(requesterId);
            UserInfoRepository.UserRolesByUserId userToBeDeletedInfo = repository.getRolesByUserId(userIdToDelete);

            String userRoles = requesterInfo.getRoles();
            boolean canDelete = isCanDelete(userToBeDeletedInfo, userRoles);

            if (canDelete) {
                repository.deleteUser(userIdToDelete);
            } else {
                throw new InsufficientAuthenticationException("You are not allowed to perform this action");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    protected static boolean isCanDelete(UserInfoRepository.UserRolesByUserId userToBeDeletedInfo, String userRoles) {
        String tbdUserRoles = userToBeDeletedInfo.getRoles();

        boolean requesterIsAdmin = userRoles.contains("ROLE_ADMIN");
        boolean requesterIsSuperUser = userRoles.contains("ROLE_SUPER_USER");

        boolean userToDeleteIsSuperUser = tbdUserRoles.contains("ROLE_SUPER_USER");

        boolean canDelete = false;

        if (requesterIsSuperUser) {
            // Super user can delete anyone
            canDelete = true;
        } else if (requesterIsAdmin) {
            // Admin can delete other admins and regular users but not super users
            if (!userToDeleteIsSuperUser) {
                canDelete = true;
            }
        }
        return canDelete;
    }

    public UserListItem getMyData(String userId) throws Exception {
        try {
            UserInfoRepository.NameOnly dbResp = repository.getMyData(userId);

            UserListItem myData = new UserListItem();
            myData.setEmail(dbResp.getEmail());
            myData.setUser_id(dbResp.getUserId());
            myData.setStatus(dbResp.getStatus());
            myData.setRoles(dbResp.getRoles());
            myData.setName(dbResp.getName());

            return myData;
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void updateStatus(String requesterId, UserStatusEnum status) throws Exception {
        try {
            repository.updateStatus(requesterId, status);
        } catch (Exception e) {
            throw new Exception("Something went wrong!: " + e.getMessage());
        }
    }

    public Integer getTotalUserCount() throws Exception {
        Integer totalUserCount = -1;

        try {
            totalUserCount = repository.getTotalUserCount();
        } catch (Exception e) {
            throw new Exception("Something went wrong!: "+ e.getMessage());
        }

        return totalUserCount;
    }
}
