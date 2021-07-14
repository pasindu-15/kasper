package com.uom.msc.cse.ds.kasper.external.rest;

import com.uom.msc.cse.ds.kasper.dto.Node;

public interface RequestHandlerInterface {
    public void join(Node myNode, Node neighbourNode);
    public void leave(Node myNode, Node neighbourNode);
}
