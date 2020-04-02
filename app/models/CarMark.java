package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

@Entity
public class CarMark {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long id;

    private String name;
    private String country;

}
