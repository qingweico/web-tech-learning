package cn.qingweico.common.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class AliSmsResult implements Serializable {
    private static  final String SUCCESS ="OK";
    private String action;
    private String domain;
    private String method;
    private String version;
    private String signName;
    private String phoneNumbers;
    private String templateParam;
    private String templateCode;
    private String responseStatus;
    private  String message;
    private String requestId;
    private String bizId;
    private String code;

    public boolean getSuccess(){
        return SUCCESS.equals(code);
    }
}
