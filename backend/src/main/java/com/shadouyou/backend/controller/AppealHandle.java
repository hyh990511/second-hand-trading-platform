package com.shadouyou.backend.controller;

import com.shadouyou.backend.entity.Appeal;
import com.shadouyou.backend.entity.Message;
import com.shadouyou.backend.repository.AppealRepository;
import com.shadouyou.backend.repository.MessageRepository;
import com.shadouyou.backend.service.Token.AdmiLoginToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/complain")
public class AppealHandle {
    @Autowired
    private AppealRepository appealRepository;
    @Autowired
    private MessageRepository messageRepository;

    @AdmiLoginToken
    @GetMapping("/list/{page}/{size}/{type}")
    public Page<Appeal> findAppeal(@PathVariable("page") Integer page,
                                   @PathVariable("size") Integer size,
                                   @PathVariable("type") Integer type) {
        PageRequest pageRequest = PageRequest.of(page,size);
        return appealRepository.findAllByState(type,pageRequest);//0：未处理
    }

    @AdmiLoginToken
    @PutMapping("/disposing")
    public void dispoingAppeal(@RequestBody Appeal appeal) {
        Integer uid = appeal.getUserid();
        appeal.setState(1);//处理中
        String oid = appeal.getOrderid().toString();
        Message message = new Message();
        message.setReceiverid(uid);
        message.setMessage("订单编号"+oid+"的申诉已受理，我们将派专人与您联系");
        message.setType(0);//系统通知
        message.setIsread(0);//未读

        messageRepository.save(message);
        appealRepository.save(appeal);

    }

    @AdmiLoginToken
    @PutMapping("/disposed/{aid}")
    public void disposedAppeal(@RequestBody Message message,
                               @PathVariable("aid") Integer aid) {
        Appeal appeal = appealRepository.findById(aid).get();

        message.setReceiverid(appeal.getUserid());
        message.setType(0);
        message.setIsread(0);
        messageRepository.save(message);

        appeal.setState(2);//处理完毕
        appealRepository.save(appeal);

    }

}
