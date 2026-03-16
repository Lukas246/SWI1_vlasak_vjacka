package cz.vlasak_vjacka.backend.repository;

import cz.vlasak_vjacka.backend.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, String> {
}
