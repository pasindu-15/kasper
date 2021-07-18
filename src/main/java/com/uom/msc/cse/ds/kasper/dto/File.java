package com.uom.msc.cse.ds.kasper.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

//@Entity
@Getter
@Setter
@ToString
public class File {

//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotBlank(message = "Name is mandatory")
    private String name;

    private int hopeCount;


    public File() {
    }

    public File(String name) {
        this.name = name;
    }



}
