package com.blackphoenixproductions.forumbackend.config;


import com.blackphoenixproductions.forumbackend.domain.MessagePublisher;
import com.blackphoenixproductions.forumbackend.infrastructure.messagebroker.RedisMessagePublisher;
import com.blackphoenixproductions.forumbackend.infrastructure.messagebroker.RedisMessageSubscriber;
import com.blackphoenixproductions.forumbackend.domain.ISSEPushNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.web.client.RestTemplate;


@Configuration
public class AppConfiguration {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private Integer redisPort;


    private final ISSEPushNotificationService ssePushNotificationService;

    @Autowired
    public AppConfiguration(ISSEPushNotificationService ssePushNotificationService) {
        this.ssePushNotificationService = ssePushNotificationService;
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        final RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
        return template;
    }

    @Bean
    public MessageListenerAdapter messageListener() {
        return new MessageListenerAdapter(new RedisMessageSubscriber(ssePushNotificationService));
    }

    @Bean
    RedisMessageListenerContainer redisContainer() {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(messageListener(), topic());
        return container;
    }

    @Bean
    public MessagePublisher redisPublisher() {
        return new RedisMessagePublisher(redisTemplate(), topic());
    }

    @Bean
    public ChannelTopic topic() {
        return new ChannelTopic("pubsub:queue");
    }


}
