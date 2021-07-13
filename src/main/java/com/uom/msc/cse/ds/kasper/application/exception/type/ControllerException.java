
package com.uom.msc.cse.ds.kasper.application.exception.type;

public class ControllerException extends BaseException {
    public ControllerException(String message){
        super(message);
    }
    public ControllerException(String message, String code){
        super(message,code);
    }
}
