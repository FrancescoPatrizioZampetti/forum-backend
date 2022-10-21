package com.blackphoenixproductions.forumbackend;

import com.blackphoenixproductions.forumbackend.adapters.api.dto.Filter;
import com.blackphoenixproductions.forumbackend.domain.model.User;
import com.blackphoenixproductions.forumbackend.domain.model.VTopic;
import com.blackphoenixproductions.forumbackend.domain.enums.BooleanOperator;
import com.blackphoenixproductions.forumbackend.domain.enums.Pagination;
import com.blackphoenixproductions.forumbackend.domain.enums.QueryOperator;
import com.blackphoenixproductions.forumbackend.domain.model.VTopic_;
import com.blackphoenixproductions.forumbackend.domain.ports.repository.TopicRepository;
import com.blackphoenixproductions.forumbackend.domain.ports.repository.UserRepository;
import com.blackphoenixproductions.forumbackend.domain.ports.repository.VTopicRepository;
import com.blackphoenixproductions.forumbackend.adapters.specification.SpecificationBuilder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Integration Tests - Persistence Layer
 */
@Disabled
@SpringBootTest
@TestPropertySource(locations = "classpath:/application-test.properties")
public class RepositoryTest {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private VTopicRepository vtopicRepository;

    @Test
//    @Transactional(propagation = Propagation.REQUIRED)
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void test_session(){
        User user = userRepository.findByUsername("normal_user");
        assertNotNull(user);
    }

    @Test
    @Transactional(readOnly = true)
    public void test_simple_filter(){
        // esempio filtro semplificato proveniente da front-end
        List<Filter> listaFiltri = new ArrayList<>();
        Filter s1 = buildFilter(VTopic_.AUTHOR_USERNAME, "test-user-1", QueryOperator.EQUALS);
        Filter s2 = buildFilter(VTopic_.TITLE, "prova", QueryOperator.LIKE);
        listaFiltri.add(s1);
        listaFiltri.add(s2);

        // creo il filtro di base, ogni filtro sarà in AND
        Filter rootFilter = getRootFilter();
        List<Filter> filters = rootFilter.getFilters();
        // aggiungo tutti i filtri
        Filter filter = null;
        for (Filter simpleFilter : listaFiltri) {
            filter = buildFilter(simpleFilter.getField(), simpleFilter.getValue(), simpleFilter.getQueryOperator());
            filters.add(filter);
        }

        SpecificationBuilder specificationBuilder = new SpecificationBuilder();
        Specification<VTopic> spec = specificationBuilder.getSpecification(rootFilter);
        Page<VTopic> pagedTopics = vtopicRepository.findAll(spec, PageRequest.of(0, 10));
        assertNotNull(pagedTopics);
    }

    private Filter getRootFilter() {
        return Filter.builder()
                .booleanOperator(BooleanOperator.AND)
                .build();
    }

    public Filter buildFilter(String field, String value, QueryOperator queryOperator){
        return Filter.builder()
                .queryOperator(queryOperator)
                .field(field)
                .value(value)
                .build();
    }


    @Test
    @Transactional(readOnly = true)
    public void test_topic_specification(){
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<VTopic> vtopicQuery = builder.createQuery(VTopic.class);
            Root<VTopic> vTopicRoot = vtopicQuery.from(VTopic.class);

            // Predicate predicate = builder.equal(vTopicRoot.get("authorUsername"),"normal_user");
            // Specification<VTopic> spec1 = VTopicSpecification.topicAuthor("admin");

            // creo il filtro
            // 1 livello
            Filter filter1_= Filter.builder()
                    .booleanOperator(BooleanOperator.AND)
                    .build();
            List<Filter> filters_1 = new ArrayList<>();
            filter1_.setFilters(filters_1);
            Filter filter2 = Filter.builder()
                            .queryOperator(QueryOperator.EQUALS)
                            .field(VTopic_.AUTHOR_USERNAME)
                            .value("admin")
                            .build();
            filters_1.add(filter2);

            // 2 livello
            Filter filter2_= Filter.builder()
                    .booleanOperator(BooleanOperator.OR)
                    .build();
            List<Filter> filters_2 = new ArrayList<>();
            filter2_.setFilters(filters_2);
            Filter filter3 = Filter.builder()
                    .queryOperator(QueryOperator.EQUALS)
                    .field(VTopic_.ID)
                    .value("1")
                    .build();
            Filter filter4 = Filter.builder()
                    .queryOperator(QueryOperator.EQUALS)
                    .field(VTopic_.ID)
                    .value("2")
                    .build();
            filters_2.add(filter3);
            filters_2.add(filter4);

            filters_1.add(filter2_);

            Collections.reverse(filter1_.getFilters());

            // authorUsername equals admin AND (id equals 1 OR id equals 2)

            SpecificationBuilder specificationBuilder = new SpecificationBuilder();
            Specification<VTopic> spec2 = specificationBuilder.getSpecification(filter1_);

            List<VTopic> topics = vtopicRepository.findAll(spec2);
            Page<VTopic> pagedTopics = vtopicRepository.findAll(spec2, PageRequest.of(0, Math.toIntExact(Pagination.POST_PAGINATION.getValue())));
            assertNotNull(topics);
            assertNotNull(pagedTopics);
        } catch (Exception e){
            e.printStackTrace();
        }
    }



}
