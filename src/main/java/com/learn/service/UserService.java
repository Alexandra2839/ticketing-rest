package com.learn.service;


import com.learn.dto.UserDTO;
import com.learn.exception.TicketingProjectException;

import java.util.List;

public interface UserService  {

    UserDTO findByUserName(String username);
    List<UserDTO> listAllUsers();
    void save(UserDTO user);
//    void deleteByUserName(String username);
    UserDTO update(UserDTO user);
    void delete(String username) throws TicketingProjectException;
    List<UserDTO> listAllByRole(String role);

}
