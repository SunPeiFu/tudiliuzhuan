package com.tudiliuzhuan.service;


import com.tudiliuzhuan.common.result.Result;
import com.tudiliuzhuan.model.dto.Code2SessionDTO;

/**
 * 作者:  sunpeifu
 * 日期:  2020/5/24
 * 描述:  小程序service
 */
public interface MiniProgramService {


    /***
     *  根据code获取openid,暂时返回Object
     */
    Code2SessionDTO code2Session(String code);

//    /**
//     * 获取小程序token
//     */
//    String getToken();
//
//    /**
//     * 获取二维码地址
//     */
//    Result<String> getQRCode(String channelCode, Integer width);
//
//    /**
//     * 发送模板消息
//     */
//    boolean sendMsg(MessageDTO messageDTO);

}
