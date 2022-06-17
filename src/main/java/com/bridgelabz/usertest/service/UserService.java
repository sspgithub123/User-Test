package com.bridgelabz.usertest.service;

import com.bridgelabz.usertest.dto.LoginDTO;
import com.bridgelabz.usertest.dto.UserDTO;
import com.bridgelabz.usertest.exception.PasswordException;
import com.bridgelabz.usertest.exception.UserException;
import com.bridgelabz.usertest.model.User;
import com.bridgelabz.usertest.repository.UserRepository;
import com.bridgelabz.usertest.util.EmailSenderService;
import com.bridgelabz.usertest.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements IUserService {

    private static final long EXPIRE_TOKEN_AFTER_MINUTES = 30;

    @Autowired
    UserRepository repo;

    @Autowired
    EmailSenderService sender;

    @Autowired
    TokenUtil tokenUtil;

    @Override
    //to register user
    public String registerUser(UserDTO dto) {
        Optional<User> matcher = repo.findByEmail(dto.getEmail());
        if (matcher.isPresent()) {
            throw new UserException("Email Already Registered");
        } else {
            User user = new User(dto);
            repo.save(user);
            String token = tokenUtil.createToken(user.getUserId());
            this.sender.sendEmail(user.getEmail(), "User successfully registered", "Hi, " + user.getFirstName()
                    + " " + user.getLastName() + " Welcome to user application \n"
                    + "\n click on following link to retrieve data : \n http://localhost:8082/user/getAll/"
                    + token);
            return token;
        }
    }

    //to retrieve list of all users
    @Override
    public List<User> getUser() {
        List<User> list = repo.findAll();
        if (list.isEmpty()) {
            throw new UserException("There are no users added");
        } else {
            return list;
        }
    }


    @Override
    public User loginUser(LoginDTO dto) {
        Optional<User> user = repo.findByEmail(dto.getEmail());
        if (user.get().equals(null)) {
            throw new UserException(" There are no user with given email id ");
        } else {
            if (!dto.getPassword().equals(user.get().getPassword())) {
                throw new PasswordException(" Invalid password ");
            } else {
                sender.sendEmail(user.get().getEmail(), "User successfully login", "Hi, " + user.get().getFirstName()
                        + "Welcome to user application \n"
                        + "to get account information : \n"
                        + "http://localhost:8082/user/getAll" + tokenUtil.createToken(user.get().getUserId()));
                return user.get();
            }
        }
    }

    public User getById(String token) {
        Integer userId = this.tokenUtil.decodeToken(token);
        Optional<User> user = repo.findById(userId);
        if (user.isEmpty()) {
            throw new UserException("There are no users with given id");
        } else {
            this.sender.sendEmail(((User) user.get()).getEmail(), "User successfully retrieved", "for User : \n"
                    + user + "\n click on following link to retrieve data : \n http://localhost:8082/user/findbyid/"
                    + token);
            return user.get();
        }
    }

    public Object forgotPassword(String email) {
        Optional<User> userOptional = repo.findByEmail(email);

        String token = userOptional.get().getToken();
        if (!userOptional.isPresent()) {
            return "Invalid email id.";
        } else {
            User user = userOptional.get();
            user.setToken(generateToken());
            user.setTokenCreationDate(LocalDateTime.now());

            user = repo.save(user);

            sender.sendEmail(userOptional.get().getEmail(), "Reset Password", "Hi " + userOptional.get().getFirstName()
                    + "\n" + "You're receiving this email because you requested a password reset \n"
                    + "Click the following link to change the password : " + "http://localhost:8082/user/reset-password?token="
                    + user.getToken());
        }

        return token;
    }

    @Override
    public String resetPassword(String token, String password) {
        Optional<User> userOptional = repo.findByToken(token);

        if (!userOptional.isPresent()) {
            return "Invalid token.";
        }

        LocalDateTime tokenCreationDate = userOptional.get().getTokenCreationDate();

        if (isTokenExpired(tokenCreationDate)) {
            return "Token Expired.";
        }
        User user = userOptional.get();

        user.setPassword(password);
        user.setToken(null);
        user.setTokenCreationDate(null);

        repo.save(user);
        return "Your password successfully updated.";
    }

    /**
     * Generate unique OTP.
     *
     * @return unique OTP
     */

    private String generateToken() {
        StringBuilder token = new StringBuilder();

        return token.append(UUID.randomUUID().toString())
                .append(UUID.randomUUID().toString()).toString();
    }

    /**
     * Check whether the created token expired or not.
     *
     * @param tokenCreationDate
     * @return true or false
     */
    private boolean isTokenExpired(final LocalDateTime tokenCreationDate) {

        LocalDateTime now = LocalDateTime.now();
        Duration diff = Duration.between(tokenCreationDate, now);

        return diff.toMinutes() >= EXPIRE_TOKEN_AFTER_MINUTES;
    }

    //to delete user by id
    public Object deleteById(String token) {
        Integer userId = tokenUtil.decodeToken(token);
        Optional<User> user = repo.findById(userId);
        if (user.isEmpty()) {
            throw new UserException("Invalid token..please input valid token");
        } else {
            sender.sendEmail(((User)user.get()).getEmail(), "User successfully deleted", "User : \n" + user.get());
            repo.deleteById(userId);
            return user.get();
        }
    }

}