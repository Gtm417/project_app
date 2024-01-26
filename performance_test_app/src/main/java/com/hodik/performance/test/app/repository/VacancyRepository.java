package com.hodik.performance.test.app.repository;

import com.hodik.performance.test.app.model.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VacancyRepository extends JpaRepository<Vacancy, Long> {
}
