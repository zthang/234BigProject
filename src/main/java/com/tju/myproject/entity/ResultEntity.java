package com.tju.myproject.entity;

import lombok.Data;

@Data
public class ResultEntity
{
    private Integer code;
    private String msg;
    private Object data;
    public ResultEntity(){}
    public ResultEntity(Integer state,String message,Object data)
    {
        this.code=state;
        this.msg=message;
        this.data=data;
    }
}
