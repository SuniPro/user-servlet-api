package com.taekang.userservletapi.repository.employee;

import com.taekang.userservletapi.entity.employee.Site;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteRepository extends JpaRepository<Site, Integer> {

    Optional<Site> findBySite(String site);
    
    String site(String site);
}

