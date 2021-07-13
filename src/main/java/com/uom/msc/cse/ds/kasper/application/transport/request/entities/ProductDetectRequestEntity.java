
package com.uom.msc.cse.ds.kasper.application.transport.request.entities;

import com.uom.msc.cse.ds.kasper.application.validator.RequestEntityInterface;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductDetectRequestEntity implements RequestEntityInterface {

    private List<String> imagesBase64;

}
