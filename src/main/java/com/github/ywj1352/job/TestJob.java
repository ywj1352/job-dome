package com.github.ywj1352.job;

import com.github.ywj1352.dao.UserDao;
import com.github.ywj1352.distributed.DistributedScheduled;
import com.github.ywj1352.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class TestJob {

    @Autowired
    private UserDao userDao;


    @PostConstruct
    public void init() {
        System.out.println("init");
    }

    @DistributedScheduled(cron = "0/2 * * * * ?")
    public void testJob() {
        System.out.println("testJob+++++++++++++++++++++++++++++++++2");
        User allUser = userDao.findAllUser();
        System.out.println(allUser);
    }

    @DistributedScheduled(cron = "0/10 * * * * ?")
    public void testSayHaha() {
        System.out.println("testSayHaha ++++++++++++++++++++++++++++++++++++10");
    }



}
