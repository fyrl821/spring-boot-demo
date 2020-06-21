package com.xkcoding.cache.redis.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Function Description: 测试myRedisTemplate中的Object通用模版的实体类 <br>
 * Writter: PL <br>
 * Creating Time: 2020-06-20 12:29 <br>
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {

    public  static final String Table = "t_user";

    private String name;
    private String address;
    private Integer age;

    public static class Builder {
        private String name;
        private String address;
        private Integer age;

        public Builder() {
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withAddr(String address) {
            this.address = address;
            return this;
        }

        public Builder withAge(Integer age) {
            this.age = age;
            return this;
        }

        public UserVo build() {
            return new UserVo(name, address, age);
        }
    }
}
