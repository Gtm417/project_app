package org.example.projectapp.repository.specification;

import org.example.projectapp.model.Project;
import org.springframework.data.jpa.domain.Specification;


public class ProjectSpecs {

    public static Specification<Project> getProjectPrivateSpec() {
        return (root, query, builder) -> builder.isFalse(root.get("isPrivate"));
    }
}
