package com.space.service.specs;

import com.space.controller.SearchCriteria;
import com.space.model.Ship;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;

public class ShipSpecs {

    public static Specification<Ship> find(SearchCriteria searchCriteria) {
        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();
                if (searchCriteria.getName() != null) {
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("name"), "%" + searchCriteria.getName() + "%"));
                }
                if (searchCriteria.getPlanet() != null) {
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("planet"), "%" + searchCriteria.getPlanet() + "%"));
                }
                if (searchCriteria.getShipType() != null) {
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("shipType"), searchCriteria.getShipType()));
                }
                if (searchCriteria.getAfter() != null) {
                    Date dateAfter = new Date();
                    dateAfter.setTime(searchCriteria.getAfter());
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("prodDate"), dateAfter));
                }
                if (searchCriteria.getBefore() != null) {
                    Date dateBefore = new Date();
                    dateBefore.setTime(searchCriteria.getBefore());
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("prodDate"), dateBefore));
                }
                if (searchCriteria.isUsed() != null) {
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("isUsed"), searchCriteria.isUsed()));
                }
                if (searchCriteria.getMinSpeed() != null) {
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("speed"), searchCriteria.getMinSpeed()));
                }
                if (searchCriteria.getMaxSpeed() != null) {
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("speed"), searchCriteria.getMaxSpeed()));
                }
                if (searchCriteria.getMinCrewSize() != null) {
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("crewSize"), searchCriteria.getMinCrewSize()));
                }
                if (searchCriteria.getMaxCrewSize() != null) {
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("crewSize"), searchCriteria.getMaxCrewSize()));
                }
                if (searchCriteria.getMinRating() != null) {
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("rating"), searchCriteria.getMinRating()));
                }
                if (searchCriteria.getMaxRating() != null) {
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("rating"), searchCriteria.getMaxRating()));
                }
                return predicate;
            }
        };
    }

}
