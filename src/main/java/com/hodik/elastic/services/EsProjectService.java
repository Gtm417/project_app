package com.hodik.elastic.services;

import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.dto.SearchFilter;
import com.hodik.elastic.exceptions.EntityAlreadyExitsException;
import com.hodik.elastic.model.Project;
import com.hodik.elastic.repositories.ProjectRepository;
import com.hodik.elastic.util.Operations;
import com.hodik.elastic.util.SearchColumn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EsProjectService {
    //        private final static String INDEX_NAME = "projects";
    //    private final ObjectMapper mapper = new ObjectMapper();
    //    private final RestHighLevelClient esClient;
    //
    //
    //    public EsProjectService(RestHighLevelClient esClient) {
    //        this.esClient = esClient;
    //    }
    private final ProjectRepository projectRepository;
    private final ElasticsearchOperations elasticsearchTemplate; // autowired bean


    @Autowired
    public EsProjectService(ProjectRepository projectRepository, ElasticsearchOperations elasticsearchTemplate) {
        this.projectRepository = projectRepository;
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    public void createProject(Project project) throws EntityAlreadyExitsException {
        long id = project.getId();
        if (projectRepository.findById(id).isPresent()) {
            throw new EntityAlreadyExitsException("Project already exits id= " + id);
        }
        projectRepository.save(project);
    }

    public void updateProject(Project project) {
        projectRepository.save(project);
    }

    public void deleteProject(long id) {
        projectRepository.deleteById(id);
    }

    public Iterable<Project> findAll() {
        return projectRepository.findAll();
    }

    public Iterable<Project> findAllWithFilters(SearchCriteriaDto searchCriteriaDto, Pageable pageable) {
        Criteria criteria = new Criteria();
        List<SearchFilter> filters = searchCriteriaDto.getFilters();
        if (filters != null){
            for (SearchFilter filter: filters){
                SearchColumn column = filter.getColumn();
                Operations operation = filter.getOperations();
                List<?> values = filter.getValues();
                String value=values.toString();

                switch (operation){
                    case LIKE -> criteria.and(new Criteria(String.valueOf(column)).contains(value));
                    case EQUAL -> criteria.and(new Criteria(String.valueOf(column)).matches(value));
                    case MORE_THEN -> criteria.and(new Criteria(String.valueOf(column)).greaterThanEqual(value));
                    case LESS_THEN -> criteria.and(new Criteria(String.valueOf(column)).lessThanEqual(value));
                    case FULL_TEXT -> criteria.and(new Criteria(String.valueOf(column)).fuzzy(value));

                }
            }
        }


            return projectRepository.findAll(criteria, pageable);
    }


//        public void search() {
//            Criteria criteria = new Criteria();
//            criteria.and(new Criteria("foo").is(foo));
//            criteria.
//            criteria.and(new Criteria("bar").in(bars));
//            CriteriaQuery criteriaQuery = new CriteriaQuery(criteria);
//            elasticsearchOperations.search(criteriaQuery,
//                            FooElasticEntity.class).stream()
//                    .map(SearchHit::getContent)
//                    .collect(Collectors.toList())
//        }
}