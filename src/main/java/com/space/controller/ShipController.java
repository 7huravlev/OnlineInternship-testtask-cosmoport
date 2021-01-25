package com.space.controller;

import com.space.model.Ship;
import com.space.service.ShipService;
import com.space.model.ShipType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@RestController
public class ShipController {
    @Autowired
    private final ShipService shipService;

    public ShipController(ShipService shipService) {
        this.shipService = shipService;
    }

    //Rest API
    //Get ships list
    @GetMapping("/rest/ships")
    public List<Ship> getAllNotes(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String planet,
            @RequestParam(required = false) ShipType shipType,
            @RequestParam(required = false) Long after,
            @RequestParam(required = false) Long before,
            @RequestParam(required = false) Boolean isUsed,
            @RequestParam(required = false) Double minSpeed,
            @RequestParam(required = false) Double maxSpeed,
            @RequestParam(required = false) Integer minCrewSize,
            @RequestParam(required = false) Integer maxCrewSize,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Double maxRating,
            @RequestParam(defaultValue = "ID") ShipOrder order,
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "3") Integer pageSize) {
        SearchCriteria searchCriteria = new SearchCriteria(name, planet, shipType, after,
                before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating,
                maxRating, order, pageNumber, pageSize);
        return shipService.listAll(searchCriteria).getContent();
    }

    //Get ships count
    @GetMapping("rest/ships/count")
    public long count(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String planet,
            @RequestParam(required = false) ShipType shipType,
            @RequestParam(required = false) Long after,
            @RequestParam(required = false) Long before,
            @RequestParam(required = false) Boolean isUsed,
            @RequestParam(required = false) Double minSpeed,
            @RequestParam(required = false) Double maxSpeed,
            @RequestParam(required = false) Integer minCrewSize,
            @RequestParam(required = false) Integer maxCrewSize,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Double maxRating) {
        SearchCriteria searchCriteria = new SearchCriteria(name, planet, shipType, after,
                before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating,
                maxRating);
        return shipService.count(searchCriteria);
    }

    //Create ship
    @PostMapping("rest/ships")
    public ResponseEntity<Ship> createShip(@RequestBody Ship ship) {
        try {
            if(ship == null) {
                throw new ValidationException("error ship");
            }
            if(ship.getName() == null || ship.getSpeed() == null || ship.getCrewSize() == null) {
                throw new ValidationException("error check required");
            }
            if(ship.getName().length() > 50 || ship.getName() == "") {
                throw new ValidationException("error name");
            }
            if(ship.getPlanet().length() > 50 || ship.getPlanet() == "") {
                throw new ValidationException("error planet");
            }
            Calendar calendar2800 = Calendar.getInstance();
            calendar2800.set(Calendar.YEAR, 2799);
            Calendar calendar3019 = Calendar.getInstance();
            calendar3019.set(Calendar.YEAR, 3019);
            if(ship.getProdDate().getTime() < 0) {
                throw new ValidationException("error prod date");
            }
            if(ship.getProdDate().before(calendar2800.getTime()) || ship.getProdDate().after(calendar3019.getTime())) {
                throw new ValidationException("error prod date");
            }
            if(ship.isUsed() == null) {
                ship.setUsed(false);
            }
            if(ship.getSpeed() < 0.01 || ship.getSpeed() > 0.99) {
                throw new ValidationException("error speed");
            }
            if(ship.getCrewSize() < 1 || ship.getCrewSize() > 9999) {
                throw new ValidationException("error crew size");
            }
        }
        catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Double k = ship.isUsed() ? 0.5 : 1;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(ship.getProdDate());
        Double rating = 80 * ship.getSpeed() * k / (3019 - calendar.get(Calendar.YEAR) + 1);
        ship.setRating(rating);

        shipService.save(ship);
        return ResponseEntity.ok(ship);
    }

    //Get ship
    @GetMapping("rest/ships/{id}")
    public ResponseEntity<Object> getShipById(@PathVariable("id") Long id) {
        if (id == null || id < 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Optional<Ship> optionalShip = shipService.findById(id);
        if (optionalShip.isPresent()) {
            return ResponseEntity.ok(optionalShip.get());
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    //Update ship
    @PostMapping("rest/ships/{id}")
    public ResponseEntity<Ship> updateShip(
            @PathVariable("id") Long id,
            @RequestBody Ship ship) {
        if(ship == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        if (id < 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Optional<Ship> optionalShip = shipService.findById(id);
        if (optionalShip.isPresent()) {
            Ship shipUpd = optionalShip.get();

            if(ship.getName() == null && ship.getSpeed() == null && ship.getCrewSize() == null
                    && ship.getProdDate() == null && ship.isUsed() == null && ship.getSpeed() == null && ship.getCrewSize() == null) {
                return ResponseEntity.ok(optionalShip.get());
            }

            try {
                if (ship.getName() != null) {
                    shipUpd.setName(ship.getName());
                }
                if (ship.getPlanet() != null) {
                    shipUpd.setPlanet(ship.getPlanet());
                }
                if (ship.getShipType() != null) {
                    shipUpd.setShipType(ship.getShipType());
                }
                if (ship.isUsed() != null) {
                    shipUpd.setUsed(ship.isUsed());
                }
                if (ship.getProdDate() != null || ship.getSpeed() != null) {
                    if (ship.getProdDate() != null) {
                        shipUpd.setProdDate(ship.getProdDate());
                    }
                    if (ship.getSpeed() != null) {
                        shipUpd.setSpeed(ship.getSpeed());
                    }

                }
                if (ship.getCrewSize() != null) {
                    shipUpd.setCrewSize(ship.getCrewSize());
                }
            }
            catch (ValidationException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            Double k = shipUpd.isUsed() ? 0.5 : 1;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(shipUpd.getProdDate());
            Double rating = 80 * shipUpd.getSpeed() * k / (3019 - calendar.get(Calendar.YEAR) + 1);
            shipUpd.setRating(Math.floor((rating + 0.005) * 100) / 100);

            shipService.save(shipUpd);
            return ResponseEntity.ok(optionalShip.get());
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    //Delete ship
    @DeleteMapping("rest/ships/{id}")
    public ResponseEntity<HttpStatus> deleteShipById(@PathVariable("id") Long id) {
        if (id == null || id < 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        try {
            shipService.deleteById(id);
            return ResponseEntity.ok(null);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
