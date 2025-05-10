package com.ssi.ms.common.service;

import com.ssi.ms.common.database.mapper.UserDetailsMapper;
import com.ssi.ms.common.database.repository.UserRepository;
import com.ssi.ms.common.dto.UserDetailsDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Praveenraja Paramsivam
 * UserService  provides services to userId and userName.
 */
@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsMapper userDetailsMapper;

    /**
     * Retrieve user details based on the provided user ID.
     *
     * @param userId {@link Long} The ID of the user to retrieve details for.
     * @return {@link UserDetailsDTO} The UserDetailsDTO containing the details of the user.
     */
    public UserDetailsDTO getUserById(Long userId) {
        return userDetailsMapper.daoToDto(userRepository.findById(userId).get());
    }

    /**
     * Retrieve the user name associated with the provided user ID.
     *
     * @param userIdIn {@link Long} The user ID for which to retrieve the user name.
     * @return {@link String} The user name associated with the provided user ID.
     */
    public String getUserName(Long userIdIn) {
        return Optional.ofNullable(userIdIn)
                .filter(userId -> null != userId)
                .flatMap(userId -> userRepository.findById(userId))
                .filter(userDAO -> null != userDAO)
                .map(userDAO -> userDAO.getUserFirstName() + " " + userDAO.getUserLastName())
                .orElseGet(() -> "");
    }

}
