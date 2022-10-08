package com.blackphoenixproductions.forumbackend;

import com.blackphoenixproductions.forumbackend.email.EmailSender;
import com.blackphoenixproductions.forumbackend.entity.Post;
import com.blackphoenixproductions.forumbackend.entity.Topic;
import com.blackphoenixproductions.forumbackend.entity.User;
import com.blackphoenixproductions.forumbackend.repository.PostRepository;
import com.blackphoenixproductions.forumbackend.repository.TopicRepository;
import com.blackphoenixproductions.forumbackend.repository.UserRepository;
import com.blackphoenixproductions.forumbackend.service.INotificationService;
import com.blackphoenixproductions.forumbackend.service.IPostService;
import com.blackphoenixproductions.forumbackend.service.ITopicService;
import com.blackphoenixproductions.forumbackend.service.IUserService;
import com.blackphoenixproductions.forumbackend.service.impl.NotificationService;
import com.blackphoenixproductions.forumbackend.service.impl.PostService;
import com.blackphoenixproductions.forumbackend.service.impl.TopicService;
import com.blackphoenixproductions.forumbackend.service.impl.UserService;
import com.blackphoenixproductions.forumbackend.sse.SsePushNotificationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class NotificationServiceTests {

    @TestConfiguration
    static class NotificationServiceTestsConfigurationContext{
        @MockBean
        TopicRepository topicRepository;

        @MockBean
        UserRepository userRepository;

        @MockBean
        PostRepository postRepository;

        @Bean
        public IUserService getUserService() {
            return new UserService(userRepository, "test-server",
                    "test-realm", "test-resource",
                    "test-username",
                    "test-user-password");
        }

        @Bean
        public ITopicService getTopicService(){
            return new TopicService(topicRepository,
                    getUserService());
        }

        @Bean
        public IPostService getPostService(){
            return new PostService(postRepository,
                    getUserService(),
                    topicRepository,
                    new EmailSender(null, "domain"));
        }

        @Bean
        public INotificationService getNotificationService(){
            return new NotificationService(new SsePushNotificationService(),
                    getPostService());
        }


        @BeforeAll
        void setUp(){
            List<Post> posts = new ArrayList<>();
            Post post = new Post();
            post.setId(1L);
            posts.add(post);
            postRepository.saveAll(posts);
        }

    }

    @Autowired
    INotificationService notificationService;

    @Test
    public void notifyTopicAuthor_differentAuthor(){
        User user = new User();
        user.setId(1L);
        User user_2 = new User();
        user_2.setId(2L);
        Topic topic = new Topic();
        topic.setId(1L);
        topic.setUser(user_2);
        Post post = new Post();
        post.setUser(user);
        post.setTopic(topic);
        post.setMessage("ciao mondo");
        notificationService.notifyTopicAuthor(post);
    }



    @Test
    public void notifyTopicAuthor_sameAuthor(){
        User user = new User();
        user.setId(1L);
        Topic topic = new Topic();
        topic.setId(1L);
        topic.setUser(user);
        Post post = new Post();
        post.setUser(user);
        post.setTopic(topic);
        post.setMessage("ciao mondo");
        notificationService.notifyTopicAuthor(post);
    }

}
