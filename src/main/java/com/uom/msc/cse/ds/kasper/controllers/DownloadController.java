package com.uom.msc.cse.ds.kasper.controllers;

import com.uom.msc.cse.ds.kasper.entities.File;
import com.uom.msc.cse.ds.kasper.repositories.DownloadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class DownloadController {

    private final DownloadRepository downloadRepository;

    @Autowired
    public DownloadController(DownloadRepository downloadRepository){
        this.downloadRepository = downloadRepository;
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @PostMapping("/searchFile")
    public String SearchFile(@Valid File file, BindingResult result, Model model) {
        System.out.println(file.getName());
        return "index";

    }

}
