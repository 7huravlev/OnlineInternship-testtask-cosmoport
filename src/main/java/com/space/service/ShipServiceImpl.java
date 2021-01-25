package com.space.service;

import com.space.controller.SearchCriteria;
import com.space.model.Ship;
import com.space.repository.ShipRepository;
import com.space.service.specs.ShipSpecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class ShipServiceImpl implements ShipService {
    @Autowired
    private ShipRepository shipRepository;

    @Override
    public Page<Ship> listAll(SearchCriteria searchCriteria) {
        return shipRepository.findAll(
                ShipSpecs.find(searchCriteria),
                PageRequest.of(searchCriteria.getPageNumber(),
                        searchCriteria.getPageSize(),
                        Sort.by(searchCriteria.getOrder().getFieldName()))
        );
    }

    @Override
    public long count(SearchCriteria searchCriteria) {
        return shipRepository.count(ShipSpecs.find(searchCriteria));
    }

    @Override
    public Optional<Ship> findById(Long id) {
        return shipRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        shipRepository.deleteById(id);
    }

    @Transactional
    @Override
    public <S extends Ship> S save(S entity) {
        return shipRepository.save(entity);
    }
}
