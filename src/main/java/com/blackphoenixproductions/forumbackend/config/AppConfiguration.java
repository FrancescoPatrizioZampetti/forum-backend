package com.blackphoenixproductions.forumbackend.config;


import com.blackphoenixproductions.forumbackend.domain.MessagePublisher;
import com.blackphoenixproductions.forumbackend.infrastructure.messagebroker.RedisMessagePublisher;
import com.blackphoenixproductions.forumbackend.infrastructure.messagebroker.RedisMessageSubscriber;
import com.blackphoenixproductions.forumbackend.domain.ISSEPushNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final String redisHost;
    private final Integer redisPort;
    private final ISSEPushNotificationService ssePushNotificationService;

    private static final Logger logger = LoggerFactory.getLogger(AppConfiguration.class);


    @Autowired
    public AppConfiguration(@Value("${spring.redis.host}") String redisHost,
                            @Value("${spring.redis.port}") Integer redisPort,
                            ISSEPushNotificationService ssePushNotificationService) {
        this.redisHost = redisHost;
        this.redisPort = redisPort;
        this.ssePushNotificationService = ssePushNotificationService;
    }


    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        logger.info("-----------------------> redis host : {}", redisHost);
        logger.info("-----------------------> redis port : {}", redisPort);
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
