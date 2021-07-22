package com.uom.msc.cse.ds.kasper.application.controller;

import com.uom.msc.cse.ds.kasper.application.config.YAMLConfig;
import com.uom.msc.cse.ds.kasper.dto.FileSearchResponse;
import com.uom.msc.cse.ds.kasper.dto.SearchFileResponse;
import com.uom.msc.cse.ds.kasper.service.FileService;
import com.uom.msc.cse.ds.kasper.service.NodeHandlerService;
import com.uom.msc.cse.ds.kasper.dto.File;
import com.uom.msc.cse.ds.kasper.service.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;


@Controller
@RequestMapping("${base-url.context}")
public class UIController {

    @Autowired
    private FileService fileService;

    @Autowired
    NodeHandlerService nodeHandlerService;

    @Autowired
    YAMLConfig yamlConfig;

    @Autowired
    SearchResult searchResult;



    @GetMapping("/home")
    public String showSignUpForm(File file) {
        return "home";
    }

    @PostMapping("/leave")
    public String leaveClient(@Valid String msg, Model model) {
//    public String leaveClient() {
        nodeHandlerService.removeFromOwnRouteTable();

        System.exit(0);

        return "Successfully Leaved";
    }

    @PostMapping("/search")
    public String searchFile(@Valid File file, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "home";
        }
        nodeHandlerService.doSearch(file.getName(),yamlConfig.getHops());
        while (true){
            if(!searchResult.getFileSearchResponse().isEmpty()){
                break;
            }
        }
        FileSearchResponse fr = searchResult.getFileSearchResponse().get(0);
        if(fr != null){
            model.addAttribute("file-search-response", fr);
        }


        return "home";

    }

    @PostMapping("/download")
    public String downloadFile(@Valid File fileDown, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "home";
        }

        System.out.println(fileDown.toString());
        model.addAttribute("fileDownload", fileDown);
        return "home";

    }

}

