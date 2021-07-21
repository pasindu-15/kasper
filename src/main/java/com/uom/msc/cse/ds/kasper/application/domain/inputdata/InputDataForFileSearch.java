package com.uom.msc.cse.ds.kasper.application.domain.inputdata;

import com.uom.msc.cse.ds.kasper.dto.Node;

import java.util.List;
import java.util.Set;

public class InputDataForFileSearch {
    String fileName;
    Node   requester;
    List<Node> VisitedNodes;

    public InputDataForFileSearch(String fileName, Node requester, List<Node> visitedNodes) {
        this.fileName = fileName;
        this.requester = requester;
        this.VisitedNodes = visitedNodes;
    }

    public List<Node> getVisitedNodes() {
        return VisitedNodes;
    }

    public void setVisitedNodes(List<Node> visitedNodes) {
        VisitedNodes = visitedNodes;
    }


    public Node getRequester() {
        return requester;
    }

    public void setRequester(Node requester) {
        this.requester = requester;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
