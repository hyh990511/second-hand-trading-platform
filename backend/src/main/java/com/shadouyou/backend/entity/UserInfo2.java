package com.shadouyou.backend.entity;

import lombok.Data;

@Data
public class UserInfo2 {
        private String accountnumber;
        private String nickname;
        private String gender;
        private String introduction;
        private String email;
        private String qqnumber;
        private Double creditrating;
        private String images;

        public UserInfo2 (User user) {
            this.accountnumber = user.getAccountnumber();
            this.nickname = user.getNickname();
            this.gender = user.getGender();
            this.introduction = user.getIntroduction();
            this.email = user.getEmail();
            this.qqnumber = user.getQqnumber();
            this.creditrating = user.getCreditrating();
            this.images = user.getImages();
        }
    }

