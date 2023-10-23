package com.lisovski.tgspringbot.services;

import com.lisovski.tgspringbot.models.User;
import com.lisovski.tgspringbot.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    public String getToken(long chatId){
        return userRepository.getTokenByChatId(chatId);
    }

    public User getStateAndToken(long chatId){
        return userRepository.getUser(chatId);
    }

    public boolean existsByChatId(long chatId){
        return userRepository.existsByChatId(chatId);
    }

    public int insert(User user){
        return userRepository.insert(user.getChatId(),user.getToken(),user.getState());
    }

    public void deleteById(int id){
        userRepository.deleteById(id);
    }

    public int updateStateByChatId(String state, long chatId){
        return userRepository.updateStateByChatId(state, chatId);
    }

}
