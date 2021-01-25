package com.space.service;

import com.space.controller.SearchCriteria;
import com.space.model.Ship;
import org.springframework.data.domain.Page;
import java.util.Optional;

public interface ShipService {

    Page<Ship> listAll(SearchCriteria searchCriteria);

    long count(SearchCriteria searchCriteria);

    Optional<Ship> findById(Long id);

    void deleteById(Long id);

    public <S extends Ship> S save(S entity);
}
