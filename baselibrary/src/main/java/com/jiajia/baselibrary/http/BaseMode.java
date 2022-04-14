package com.jiajia.baselibrary.http;

/**
 * Created by Numen_fan on 2022/4/13
 * Desc: 后台返回的格式
 * {
 *     errorCode: int
 *     errorMsg: String
 *     data:{};
 * }
 * data由具体的业务去实现相应的类， 扩充data
 */
public class BaseMode {

    public int errorCode;

    public String errorMsg;

    public boolean isSuccess() {
        return errorCode == 0;
    }

}
