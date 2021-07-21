package com.uom.msc.cse.ds.kasper.application.controller;


//import com.uom.msc.cse.ds.kasper.application.service.NodeHandler;
import lombok.extern.log4j.Log4j2;
        import org.springframework.stereotype.Controller;
        import org.springframework.web.bind.annotation.*;

@Log4j2
@Controller
@RequestMapping(value = "${base-url.context}/search-file")
public class SearchFile extends BaseController{

   /* @Autowired
    DomainFileSearch domainFileSearch;

    @Autowired
    RouteTable routeTable;

    @Autowired
    NodeHandler nodeHandler;

    @PostMapping(value = "/direct-find", produces = MediaType.APPLICATION_JSON_VALUE)
    public void searchFileFromNetwork(@Validated @RequestBody InputDataForFileSearch serchFileInput) throws Exception {

        String searchFileName = serchFileInput.getFileName();
        log.info("Search File Name {}", searchFileName);
        boolean isFileAvailble = domainFileSearch.searchFileFromNetwork(searchFileName);
        List<Node> visitedNodes = serchFileInput.getVisitedNodes();
        if(visitedNodes.isEmpty()){
            serchFileInput.setRequester(nodeHandler.getMyNode());
        }
        visitedNodes.add(nodeHandler.getMyNode());

        log.info( "File Availability: {}", isFileAvailble);
        if(!isFileAvailble) {
            List<Node> neighbours = routeTable.getNeighbours();
            //Flood the call
            for (Node neighbourNode : neighbours) {
                if(!visitedNodes.contains(neighbourNode)) {

                }
            }
        }
    }*/

}
