package com.shadouyou.backend.controller;

import com.shadouyou.backend.JsonResult;
import com.shadouyou.backend.entity.Goods;
import com.shadouyou.backend.entity.Notice;
import com.shadouyou.backend.repository.NoticeRepository;
import com.shadouyou.backend.service.Token.AdmiLoginToken;
import com.shadouyou.backend.service.Token.PassToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/notice")
public class NoticeHandle {
    @Autowired
    private NoticeRepository noticeRepository;

    //显示公告信息
    @PassToken
    @GetMapping("/info/{nid}")
    public Notice info (@PathVariable("nid") Integer id){

        return noticeRepository.findByNoticeid(id);
    }

    @PassToken
    @GetMapping("/list/{page}/{size}")
    public Page<Notice> findAll(@PathVariable("page") Integer page,@PathVariable("size") Integer size) {
        PageRequest pageRequest =PageRequest.of(page,size, Sort.by("releasetime").descending());

        return noticeRepository.findAll(pageRequest);
    }

    @AdmiLoginToken
    @DeleteMapping("/delete/{id}")
    public void deleteNotice (@PathVariable("id") Integer id) {
        noticeRepository.deleteById(id);
    }

    @AdmiLoginToken
    @PutMapping("/update")
    public String updateNotice(@RequestBody Notice notice)
    {
        Notice _notice=noticeRepository.findByNoticeid(notice.getNoticeid());
        _notice.setText(notice.getText());
        _notice.setTitle(notice.getTitle());
        Notice result = noticeRepository.save(_notice);
        if (result != null) {
            return "success";
        } else {
            return "error";
        }
    }

    @AdmiLoginToken
    @PostMapping("/issuer")
    public String saveNotice(@RequestBody Notice notice){
        Date dd = new Date();
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sim.format(dd);
        notice.setReleasetime(time);
        Notice result = noticeRepository.save(notice);
        if (result != null) {
            return "success";
        } else {
            return "error";
        }
    }
}
