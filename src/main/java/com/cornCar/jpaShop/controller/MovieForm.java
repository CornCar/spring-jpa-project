package com.cornCar.jpaShop.controller;

import com.cornCar.jpaShop.domain.Category;
import jakarta.persistence.ManyToMany;

import java.util.ArrayList;
import java.util.List;

public class MovieForm {
    private Long id;

    private String name;
    private int price;

    private String director;
    private String actor;

}
