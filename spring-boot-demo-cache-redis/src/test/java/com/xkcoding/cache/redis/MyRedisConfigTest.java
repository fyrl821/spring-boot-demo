package com.xkcoding.cache.redis;

import com.xkcoding.cache.redis.entity.User;
import com.xkcoding.cache.redis.entity.UserVo;
import com.xkcoding.cache.redis.utils.RedisKeyUtil;
import com.xkcoding.cache.redis.utils.RedisTempalteUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Function Description:  <br>
 * Writter: PL <br>
 * Creating Time: 2020-06-20 12:30 <br>
 **/

@RunWith(SpringRunner.class)
@SpringBootTest
public class MyRedisConfigTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    RedisTemplate<String, User> userRedisTemplate;

    @Resource
    private RedisTempalteUtils redisTempalteUtils;


    @Test
    public void testObj() throws Exception {
        UserVo userVo = new UserVo();
        userVo.setAddress("上海");
        userVo.setName("阿伦");
        userVo.setAge(28);
        ValueOperations<String, Object>  operations = redisTemplate.opsForValue();
        redisTempalteUtils.expireKey("name", 20, TimeUnit.SECONDS);
        String key = RedisKeyUtil.getKey(UserVo.Table, "name", userVo.getName());
        UserVo vo = (UserVo) operations.get(key);
        System.out.println(vo);
    }

    // 测试forValue
    @Test
    public void testValueOption() throws Exception {
        UserVo.Builder builder = new UserVo.Builder();
        UserVo userVo = builder.withName("阿伦").withAddr("武汉").withAge(28).build();
        redisTemplate.opsForValue().set("test", userVo);
        System.out.println(redisTemplate.opsForValue().get("test"));
    }

    // 测试Set
    @Test
    public void testSetOperation() throws Exception {
        // 测试SetOperation
        UserVo.Builder builder = new UserVo.Builder();
        UserVo userVo1 = builder.withName("阿伦").withAddr("武汉东").withAge(18).build();
        UserVo userVo2 = builder.withName("小何").withAddr("武汉西").withAge(16).build();
        UserVo userVo3 = builder.withName("小彤").withAddr("武汉南").withAge(1).build();
        UserVo userVo4 = builder.withName("小彤").withAddr("武汉南").withAge(1).build();
        redisTemplate.opsForSet().add("user:test", userVo1, userVo2, userVo4);

        Set<Object> set = redisTemplate.opsForSet().members("user:test");
        System.out.println(set);
    }

    // 测试Hash
    @Test
    public void testHashOperation() throws Exception {
        // 测试SetOperation
        UserVo.Builder builder = new UserVo.Builder();
        UserVo userVo1 = builder.withName("阿伦").withAddr("武汉东").withAge(18).build();

        redisTemplate.opsForHash().put("hash:user", userVo1.hashCode()+"", userVo1);
        System.out.println(redisTemplate.opsForHash().get("hash:user",userVo1.hashCode()+""));

        UserVo userVo2 = builder.withName("小何").withAddr("武汉西").withAge(16).build();
        UserVo userVo3 = builder.withName("小彤").withAddr("武汉南").withAge(1).build();
        UserVo userVo4 = builder.withName("小彤").withAddr("武汉南").withAge(1).build();
        Map<String, UserVo> map = new HashMap<>();
        map.put(userVo2.hashCode()+"", userVo2);
        map.put(userVo3.hashCode()+"", userVo3);
        map.put(userVo4.hashCode()+"", userVo4);
        redisTemplate.opsForHash().putAll("hash:userAll", map);
    }

    // 测试List
    @Test
    public void testListOperation() throws Exception {
        // 测试SetOperation
        UserVo.Builder builder = new UserVo.Builder();
        UserVo userVo1 = builder.withName("阿伦").withAddr("武汉东").withAge(18).build();
        UserVo userVo2 = builder.withName("小何").withAddr("武汉西").withAge(16).build();
        UserVo userVo3 = builder.withName("小彤").withAddr("武汉南").withAge(1).build();
        UserVo userVo4 = builder.withName("小彤").withAddr("武汉南").withAge(1).build();
        redisTemplate.opsForList().leftPushAll("list:user", userVo1, userVo2, userVo3, userVo4);
        while (redisTemplate.opsForList().size("list:user") > 0) {
            redisTemplate.opsForList().rightPop("list:user");
        }
//        UserVo rUserVo = (UserVo) redisTemplate.opsForList().rightPop("list:user");
//        System.out.println(rUserVo);
    }
}

