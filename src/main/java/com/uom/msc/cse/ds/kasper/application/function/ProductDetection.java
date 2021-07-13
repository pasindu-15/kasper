package com.uom.msc.cse.ds.kasper.application.function;//package com.uom.msc.cse.sdoncloud.detection.application.function;
//
////import BaseController;
//import ResponseEntityTransformer;
//import ProductDetectRequestEntity;
//import ProductDetectionResponseTransformer;
//import RequestEntityValidator;
//import com.uom.msc.cse.sdoncloud.detection.domain.entities.dto.ProdDetectionDomainRequestEntity;
//import ProductDetectDomainResponseEntity;
//import ProductManageService;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.Map;
//import java.util.function.Function;
//
//public class ProductDetection implements Function<ProductDetectRequestEntity,ResponseEntity> {
//
//    @Autowired
//    ProductManageService productManageService;
//
//    @Autowired
//    ResponseEntityTransformer responseEntityTransformer;
//
//    @Autowired
//    ProductDetectionResponseTransformer sampleResponseTransformer;
//
//    @Autowired
//    private RequestEntityValidator validator;
//
//
//    @Override
//    public ResponseEntity apply(ProductDetectRequestEntity productDetectRequestEntity){
////        return "Hello";
//
//
//////        TODO: set UUID
////        setLogIdentifier(request);
////        TODO: validate the request
//        validator.validate(productDetectRequestEntity);
////        logger.info("Request validation success");
//
////        TODO: request object map to domain entity object
//        ProdDetectionDomainRequestEntity prodDetectionDomainRequestEntity = new ModelMapper().map(productDetectRequestEntity, ProdDetectionDomainRequestEntity.class);
//
////        TODO: call domain business logic
//        ProductDetectDomainResponseEntity sampleDomainResponseEntity = productManageService.process(prodDetectionDomainRequestEntity);
//
////        TODO: transform domain response
//        Map trResponse = responseEntityTransformer.transform(sampleDomainResponseEntity,sampleResponseTransformer);
////        logger.info("Transformed response : "+trResponse.toString());
//
////        TODO: return response
//        return ResponseEntity.status(HttpStatus.OK).body(trResponse);
//
//
//    }
//
//
//}
