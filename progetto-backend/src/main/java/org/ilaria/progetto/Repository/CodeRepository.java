package org.ilaria.progetto.Repository;

import org.ilaria.progetto.Model.Entity.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeRepository extends JpaRepository<Code, Long> {

    @Query("SELECT c FROM Code c WHERE c.code=:teacherCode")
    Code findByCode(String teacherCode);
}

