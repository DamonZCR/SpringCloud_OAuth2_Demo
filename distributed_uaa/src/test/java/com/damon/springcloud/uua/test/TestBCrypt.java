package com.damon.springcloud.uua.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class TestBCrypt {
    /** 测试 BCryptPasswordEncoder
     * 使用BCrypt提供的Hash加密，使用这个加密算法提供的随机生成盐，每一次计算出的Hash值都是不一样的；
     * 但是使用这不同的Hash值去校验却可以验证成功；
     * 猜测是因为这个Hash值中包含了每次生成的不同的盐，所以校验的时候使用这盐再去加密进行比对；
     */
    @Test
    public void testBCrypt(){
        //对密码进行加密,提供密码和一个盐，这个盐可以是随机生成，所以每一个计算出来的都不一样；
        String hashpw = BCrypt.hashpw("secret1", BCrypt.gensalt());
        System.out.println(hashpw);

        //校验密码
        boolean checkpw = BCrypt.checkpw("123", "$2a$10$aFsOFzujtPCnUCUKcozsHux0rQ/3faAHGFSVb9Y.B1ntpmEhjRtru");
        boolean checkpw2 = BCrypt.checkpw("123", "$2a$10$HuClcUqr/FSLmzSsp9SHqe7D51Keu1sAL7tUAAcb..FyILiLdFKYy");
        System.out.println(checkpw);
        System.out.println(checkpw2);
    }

}
