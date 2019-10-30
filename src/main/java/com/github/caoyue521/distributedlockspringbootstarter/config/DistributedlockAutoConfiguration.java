package com.github.caoyue521.distributedlockspringbootstarter.config;

import com.github.caoyue521.distributedlockspringbootstarter.lock.DistributedLockTemplate;
import com.github.caoyue521.distributedlockspringbootstarter.lock.SingleDistributedLockTemplate;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author caoyue
 */
@Configuration
@ConditionalOnWebApplication
public class DistributedlockAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public RedissonClient redissonClient(@Value("${spring.redis.host}") String host, @Value("${spring.redis.port}") String port, @Value("${spring.redis.database}")int index, @Value("${spring.redis.password}")String password){
        Config config = new Config();

        config.setCodec(new org.redisson.client.codec.StringCodec());

        //指定使用单节点部署方式
        config.useSingleServer().setAddress("redis://"+host+":"+port);
        SingleServerConfig singleServerConfig = config.useSingleServer();
        //设置密码
        singleServerConfig.setPassword(password);
        //设置对于master节点的连接池中连接数最大为500
        singleServerConfig.setConnectionPoolSize(500);
        //如果当前连接池里的连接数量超过了最小空闲连接数，而同时有连接空闲时间超过了该数值，那么这些连接将会自动被关闭，并从连接池里去掉。时间单位是毫秒。
        singleServerConfig.setIdleConnectionTimeout(10000);
        //同任何节点建立连接时的等待超时。时间单位是毫秒。
        singleServerConfig.setConnectTimeout(30000);
        //等待节点回复命令的时间。该时间从命令发送成功时开始计时。
        singleServerConfig.setTimeout(3000);
        singleServerConfig.setPingTimeout(30000);
        singleServerConfig.setReconnectionTimeout(3000);
        singleServerConfig.setDatabase(index);
        singleServerConfig.setPassword(password);
        RedissonClient redissonClient = Redisson.create(config);
        return  redissonClient;
    }

    @Bean
    @ConditionalOnMissingBean
    public DistributedLockTemplate distributedLockTemplate(RedissonClient redissonClient){
        return new SingleDistributedLockTemplate(redissonClient);
    }


}
