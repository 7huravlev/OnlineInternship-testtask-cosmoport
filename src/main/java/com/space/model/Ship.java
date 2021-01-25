package com.space.model;

import javax.persistence.*;
import javax.xml.bind.ValidationException;
import java.util.Calendar;
import java.util.Date;

@Entity
public class Ship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String planet;

    @Enumerated(EnumType.STRING)
    private ShipType shipType;

    private Date prodDate;
    private Boolean isUsed;
    private Double speed;
    private Integer crewSize;
    private Double rating;

    protected Ship() {

    }

    public Ship(String name, String planet, ShipType shipType, Date prodDate, Double speed, Integer crewSize) {
        this.name = name;
        this.planet = planet;
        this.shipType = shipType;
        this.prodDate = prodDate;
        this.isUsed = false;
        this.speed = speed;
        this.crewSize = crewSize;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws ValidationException {
        if(name.length() > 50 || name == null || name == "") {
            throw new ValidationException("error name");
        }
        this.name = name;
    }

    public String getPlanet() {
        return planet;
    }

    public void setPlanet(String planet) throws ValidationException {
        if(planet.length() > 50 || planet == null || planet == "") {
            throw new ValidationException("error planet");
        }
        this.planet = planet;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public void setShipType(ShipType shipType) {
        this.shipType = shipType;
    }

    public Date getProdDate() {
        return prodDate;
    }

    public void setProdDate(Date prodDate) throws ValidationException {
        Calendar calendar2800 = Calendar.getInstance();
        calendar2800.set(Calendar.YEAR, 2800);
        Calendar calendar3019 = Calendar.getInstance();
        calendar3019.set(Calendar.YEAR, 3019);
        if(prodDate.getTime() < 0) {
            throw new ValidationException("error prod date");
        }
        if(prodDate.before(calendar2800.getTime()) || prodDate.after(calendar3019.getTime())) {
            throw new ValidationException("error prod date");
        }
        this.prodDate = prodDate;
    }

    public Boolean isUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) throws ValidationException {
        if(speed < 0.01 || speed > 0.99) {
            throw new ValidationException("error speed");
        }
        this.speed = speed;
    }

    public Integer getCrewSize() {
        return crewSize;
    }

    public void setCrewSize(Integer crewSize) throws ValidationException {
        if(crewSize < 1 || crewSize > 9999) {
            throw new ValidationException("error crew size");
        }
        this.crewSize = crewSize;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

}
