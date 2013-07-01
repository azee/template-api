package com.mycompany.template.scheduler;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ILock;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * User: azee
 * Date: 2/6/13
 * Time: 5:52 PM
 */

@Component
public class SchedulerJob {

    @Autowired
    HazelcastInstance instance;

    private final String LOCK_NAME = "scheduler";

    private final static Logger log = Logger.getLogger(SchedulerJob.class);

    @Scheduled(fixedRate = 30000)
    //Or by Cron @Scheduled(cron = "* 1 * * * ?")
    public void executeInternal() throws Exception {
        //Locking hazelcast
        ILock lock = instance.getLock(LOCK_NAME);
        if (lock.tryLock()){
            try{
                //Do scheduled job Something
            }
            finally {
                lock.unlock();
            }
        }
    }
}
