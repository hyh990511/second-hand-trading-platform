package com.shadouyou.backend.controller;

import com.shadouyou.backend.entity.Message;
import com.shadouyou.backend.entity.User;
import com.shadouyou.backend.repository.MessageRepository;
import com.shadouyou.backend.repository.UserRepository;
import com.shadouyou.backend.service.Token.UserLoginToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageHandle {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;

    @UserLoginToken
    @GetMapping("/list/{accountnumber}/{page}/{size}")
    public Page<Message> findMessage(@PathVariable("page") Integer page,
                                     @PathVariable("size") Integer size,
                                     @PathVariable("accountnumber")  String accountnumber) {
        User user = userRepository.findUserByAccountnumber(accountnumber);
        Integer id = user.getUserid();
        PageRequest pageRequest = PageRequest.of(page,size);
        return messageRepository.findAllByReceiverid(id,pageRequest);

    }

    @UserLoginToken
    @PutMapping("/read/{mid}")
    public void readMessage(@PathVariable("mid")  Integer id) {
        Message message = messageRepository.findById(id).get();
        message.setIsread(1);
        messageRepository.save(message);
    }

    @UserLoginToken
    @GetMapping("/listnum/{accountnumber}")
    public String numberOfMessage(@PathVariable("accountnumber")  String accountnumber) {
        User user = userRepository.findUserByAccountnumber(accountnumber);
        Integer id = user.getUserid();
        String num = messageRepository.countByReceiveridAndIsread(id,0).toString();

        return num;
    }

    @UserLoginToken
    @DeleteMapping("/delete/{mid}")
    public void deleteMessage(@PathVariable("mid")  Integer id) {
        messageRepository.deleteById(id);
    }

    @UserLoginToken
    @DeleteMapping("/deleteAllByAccount/{accountnumber}")
    public void deleteAllMessage(@PathVariable("accountnumber")  String accountnumber) {
        User user = userRepository.findUserByAccountnumber(accountnumber);
        Integer uid = user.getUserid();
        List<Message> messageList = messageRepository.findAllByReceiverid(uid);
        messageRepository.deleteInBatch(messageList);
    }

}
