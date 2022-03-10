package org.example.projectapp.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Embeddable
public class ProjectUserCompositeKey implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "project_id")
    private Long projectId;

}
