package com.example.renanfucci.beeradvisor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Renan Fucci on 21/10/2015.
 */
public class BeerExpert {
    List<String> getBrands(String color){

        List<String> brands =  new ArrayList<String>();
        if(color.contentEquals("amber")){
            brands.add("Sol");
            brands.add("Itaipava");
        }
        else{
            brands.add("Heineken");
            brands.add("Stella");

        }
        return brands;
    }
}
