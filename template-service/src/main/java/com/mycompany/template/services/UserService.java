package com.mycompany.template.services;

import com.mycompany.template.beans.Role;
import com.mycompany.template.beans.User;
import com.mycompany.template.repositories.UserRepository;
import com.mycompany.template.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: azee
  */
@Service
public class UserService {
    @Autowired
    private UserRepository usersRepository;

    @Autowired
    private StringUtils stringUtils;

    @Value("#{internal['token.timeout']}")
    private long TOKEN_TIMEOUT;

    @Value("#{internal['cookie.timeout']}")
    private long COOKIE_TIMEOUT;

    /**
     * Update a specific token expiration with default value
     * @param token
     * @throws Exception
     */
    private void updateTokenExpire(String token) throws Exception {
        updateTokenExpire(token, TOKEN_TIMEOUT);
    }

    /**
     * Update a specific token expiration
     * @param token
     * @throws Exception
     */
    private void updateTokenExpire(String token, long expire) throws Exception {
        User user = usersRepository.findByToken(token);
        user.setTokenExpire(user.getTokenExpire() + expire);
        usersRepository.save(user);

    }

    /**
     * Authenticate a user by pass and login
     * @param login
     * @param pass
     * @return
     * @throws Exception
     */
    public User authenticate(String login, String pass) throws Exception {
        if (pass == null || login == null){
            throw new RuntimeException("You must specify login and password");
        }

        //Encode a pass - we don't want to store passwords as is
        String encodedPass = stringUtils.getMd5String(pass);
        User user = getUserByName(login);
        if (user == null){
            throw new RuntimeException("Can't find user [" + login + "].");
        }
        if (!encodedPass.equals(user.getPassword())){
            throw new RuntimeException("Password is incorrect");
        }
        return user;
    }

    /**
     * Get a user with given token
     * @param token
     * @return
     * @throws Exception
     */
    public User getUserByToken(String token) throws Exception {
        User user = usersRepository.findByToken(token);
        if (user != null && user.getTokenExpire() > new Date().getTime()){
            return user;
        }
        else {
            throw new RuntimeException("Can't find user or token has expired.");
        }
    }

    /**
     * Get a user by name
     * @param name
     * @return
     * @throws Exception
     */
    public User getUserByName(String name) throws Exception {
        User user = usersRepository.findByName(name);
        if (user != null){
            return user;
        }
        else {
            throw new RuntimeException("Can't find user [" + name + "]");
        }
    }

    /**
     * Return a sorted by name list of all users
     * @return
     * @throws Exception
     */
    public List<User> getAllUsers() throws Exception {
        List<User> result = (List<User>) usersRepository.findAll();
        Collections.sort(result, new Comparator<User>() {
            public int compare(User user, User user1) {
                if (user.getName() == null) {
                    user.setName("");
                }
                if (user1.getName() == null) {
                    user1.setName("");
                }
                return user.getName().compareTo(user1.getName());
            }
        });
        return result;
    }

    /**
     * Removes user token
     * @param token
     * @throws Exception
     */
    public User removeUserToken(String token) throws Exception {
        User user = usersRepository.findByToken(token);
        if (user != null) {
            user.setToken(null);
            user.setTokenExpire(0);
            usersRepository.save(user);
        }
        return new User();
    }

    /**
     * Add a Role to a user
     * @param role
     * @param user
     */
    public void addRole(Role role, User user) {
        user.getRoles().add(role);
        usersRepository.save(user);
    }

    /**
     * Remove a role from the user
     * @param role
     * @param user
     */
    public void removeRole(String role, User user) {
        List<Role> newRoles = new ArrayList<Role>();
        for (Role presentedRole : user.getRoles()){
            if (!presentedRole.equals(role)) {
                newRoles.add(presentedRole);
            }
        }
        user.getRoles().clear();
        user.getRoles().addAll(newRoles);
        usersRepository.save(user);
    }
}
