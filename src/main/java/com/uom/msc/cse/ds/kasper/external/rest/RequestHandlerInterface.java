package com.uom.msc.cse.ds.kasper.external.rest;

import com.uom.msc.cse.ds.kasper.dto.Node;

public interface RequestHandlerInterface {
    public void ping(Node myNode, Node neighbourNode);
}
