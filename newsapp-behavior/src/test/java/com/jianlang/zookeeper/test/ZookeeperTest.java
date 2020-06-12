package com.jianlang.zookeeper.test;

import com.jianlang.behavior.BehaviorJarApplication;
import com.jianlang.common.zookeeper.sequence.Sequences;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = BehaviorJarApplication.class)
@RunWith(SpringRunner.class)
public class ZookeeperTest {
    @Autowired
    private Sequences sequences;

    @Test
    public void test(){
        Long id = null;
        for(int i = 0; i < 10; i++)
            id = sequences.sequenceApReadBehavior();
            System.out.println(id + "----------");
    }
}
