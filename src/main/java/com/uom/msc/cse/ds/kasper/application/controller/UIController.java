package com.uom.msc.cse.ds.kasper.application.controller;

import com.uom.msc.cse.ds.kasper.application.config.YAMLConfig;
import com.uom.msc.cse.ds.kasper.dto.FileSearchResponse;
import com.uom.msc.cse.ds.kasper.service.FileService;
import com.uom.msc.cse.ds.kasper.service.NodeHandlerService;
import com.uom.msc.cse.ds.kasper.dto.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;


@Controller
public class UIController {

    @Autowired
    private FileService fileService;

    @Autowired
    NodeHandlerService nodeHandlerService;

    @Autowired
    YAMLConfig yamlConfig;


    @GetMapping("/home")
    public String showSignUpForm(File file) {
        return "home";
    }

    @PostMapping("/leave")
    public String leaveClient(@Valid String msg, Model model) {

        nodeHandlerService.removeFromOwnRouteTable();

        System.exit(0);

        return null;
    }

    @PostMapping("/search")
    public String searchFile(@Valid File file, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "home";
        }

        FileSearchResponse fr = nodeHandlerService.doSearch(file.getName(),yamlConfig.getHops());
        if(fr != null){
            model.addAttribute("file-search-response", fr);
        }

//        System.out.println(file.toString());
//
//
//        List<File> files = fileService.getFileList();
//        System.out.println(files.toString());
//        model.addAttribute("files", files);

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

//    @GetMapping("/edit/{id}")
//    public String showUpdateForm(@PathVariable("id") long id, Model model) {
////        File file = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid file Id:" + id));
////        model.addAttribute("file", file);
//
//        return "update-file";
//    }
//
//    @PostMapping("/update/{id}")
//    public String updateUser(@PathVariable("id") long id, @Valid File file, BindingResult result, Model model) {
//        if (result.hasErrors()) {
//            file.setId(id);
//            return "update-file";
//        }
//
////        userRepository.save(file);
//
//        return "redirect:/index";
//    }
//
//    @GetMapping("/delete/{id}")
//    public String deleteUser(@PathVariable("id") long id, Model model) {
////        File file = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid file Id:" + id));
////        userRepository.delete(file);
//
//        return "redirect:/index";
//    }
}

