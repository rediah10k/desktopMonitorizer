package org.proy.monitorizerdesktop.auth.repos;

import org.proy.monitorizerdesktop.entities.Sesion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SesionRepository extends JpaRepository<Sesion, Long> {
}
