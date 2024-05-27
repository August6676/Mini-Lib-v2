package com.zhouxiaoxuan.service;

import com.zhouxiaoxuan.entity.User;
import com.zhouxiaoxuan.mapper.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private AdminMapper adminMapper;

    public List<User> getAllAccounts() {
        return adminMapper.getAllAccounts();
    }

    public boolean createUser(String username, String email,String password) {
        if (adminMapper.createUser(username, email,password)) return true;
        else return false;
    }

    public void deleteUser(int id) {
        adminMapper.deleteUser(id);
    }

    public void updateUser(int id, String username, String email) {
        adminMapper.updateUser(id, username, email);
    }

    public int getIdByUsername(String username) {
        return adminMapper.getIdByUsername(username);
    }
}
