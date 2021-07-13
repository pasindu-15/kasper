package com.uom.msc.cse.ds.kasper.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
@Component
public class RouteTable {

    private List<Node> neighbours;
}
