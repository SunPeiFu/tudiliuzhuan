package com.tudiliuzhuan.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.tudiliuzhuan.common.result.Result;
import com.tudiliuzhuan.common.util.OKHttpUtils;
import com.tudiliuzhuan.config.MiniProgramConfig;
import com.tudiliuzhuan.model.dto.Code2SessionDTO;
import com.tudiliuzhuan.service.MiniProgramService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 作者:  sunpeifu
 * 日期:  2020/5/25
 * 描述:
 */
@Service
@Slf4j
public class MiniProgramServiceImpl implements MiniProgramService {


    private static final String CODE_2_SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session";

    private static final String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";

    private static final String SEND_MSG_URL = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=";

    private static final String CREATE_QR_CODE = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=";

    // OSS文件存储桶名称
    private static final String BUCKET_NAME = "auth-file";

    @Resource
    MiniProgramConfig miniProgramConfig;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    @Override
    public Code2SessionDTO code2Session(String code) {
        Map<String, String> param = new HashMap<>();
        param.put("appid", miniProgramConfig.getAppId());
        param.put("secret", miniProgramConfig.getSecret());
        param.put("js_code", code);
        param.put("grant_type", "authorization_code");
        String result = OKHttpUtils.get(CODE_2_SESSION_URL, param);
        log.info("【小程序接口:code2Session】响应信息:{}", result);
        // 解析响应信息,判断db中是否存在对应id,如不存在,则入库信息
        Code2SessionDTO code2SessionDTO = JSON.parseObject(result, Code2SessionDTO.class);
        return code2SessionDTO;
    }

//    @Override
//    public String getToken() {
//        String token = redisTemplate.opsForValue().get(RedisPrefixKey.MINI_PROGRAM_TOKEN);
//        if (StringUtils.isNotEmpty(token)) {
//            return token;
//        }
//        String url = new StringBuilder().append(TOKEN_URL).append("&appid=").append(miniProgramConfig.getAppId()).append("&secret=").append(miniProgramConfig.getSecret()).toString();
//        Result<String> result = HttpClient.httpGetUrl(url);
//        if (!result.isSuccess()) {
//            ExceptionUtils.be("调用小程序获取token异常");
//        }
//        String data = result.getData();
//        AccessTokenDTO accessTokenDTO = JsonUtils.json2Object(data, AccessTokenDTO.class);
//        if (StringUtils.isNotEmpty(accessTokenDTO.getAccess_token())) {
//            redisTemplate.opsForValue().set(RedisPrefixKey.MINI_PROGRAM_TOKEN, accessTokenDTO.getAccess_token(), Integer.valueOf(accessTokenDTO.getExpires_in()), TimeUnit.SECONDS);
//            return accessTokenDTO.getAccess_token();
//        }
//        throw new BizException(("获取小程序token异常:" + JsonUtils.object2Json(accessTokenDTO)));
//        return null;
//    }


//    /**
//     * 获取小程序二维码地址
//     *
//     * @param channelCode 渠道编码
//     * @return oss图片地址
//     */
//    @Override
//    public Result<String> getQRCode(String channelCode, Integer width) {
//        try {
//            Assert.notEmpty(channelCode, "渠道编码不能为空");
//            // 获取二维码
//            String token = getToken();
//            String url = CREATE_QR_CODE + token;
//            Map<String, Object> dataMap = new HashMap<>(2);
//            dataMap.put("page", "pages/index/index");
//            if (width != null) {
//                dataMap.put("width", width);
//            }
//            dataMap.put("scene", "channel=" + channelCode);
//            String postJson = JsonUtils.object2Json(dataMap);
//            RestTemplate restTemplate = new RestTemplate();
//            ResponseEntity<byte[]> post = restTemplate.postForEntity(url, postJson, byte[].class);
//            byte[] bytes = post.getBody();
//
//            // 生成OSS地址
//            //OssGetOtherContentToOssQry qry = OssFileUploadUtil.uploadFileByBytes(bytes, "jpg");
//            Assert.notNull(qry, "上传OSS返回对象OssGetOtherContentToOssQry为空");
//            return Result.wrapSuccessfulResult(qry.getUrl());
//        } catch (Exception e) {
//            log.error("获取小程序二维码地址异常，渠道编码[{}],errmsg[{}]", channelCode, e.getMessage(), e);
//            return Result.wrapError(0, e.getMessage());
//        }
//    }


//    @Override
//    public boolean sendMsg(MessageDTO messageDTO) {
////        String url = SEND_MSG_URL + getToken();
////        MiniProgramMessageDTO miniProgramMessageDTO = buildTemplateDTO(messageDTO);
////        String postJson = JsonUtils.object2Json(miniProgramMessageDTO);
////        Result<String> result = HttpClient.httpPostJson(url, postJson, "0", 0);
////        log.info("微信消息发送:{}", JsonUtils.object2Json(result));
////        if (result.isSuccess()) {
////            MessageReponseDTO messageReponseDTO = JsonUtils.json2Object(result.getData(), MessageReponseDTO.class);
////            MessageDO messageDO = buildMessageDO(messageDTO.getOpenId(), postJson);
////            if ("ok".equals(messageReponseDTO.getErrmsg())) {
////                messageDO.setStatus(YesOrNoEnum.YES.getCode());
////                messageMapper.insert(messageDO);
////                return true;
////            }
////            messageDO.setStatus(YesOrNoEnum.NO.getCode());
////            messageDO.setFailureReason(messageReponseDTO.getErrcode() + " " + messageReponseDTO.getErrmsg());
////            messageMapper.insert(messageDO);
////        }
//        return false;
//    }
//
//    private MiniProgramMessageDTO buildTemplateDTO(MessageDTO messageDTO) {
//        MiniProgramMessageDTO miniProgramMessageDTO = new MiniProgramMessageDTO();
//        miniProgramMessageDTO.setTouser(messageDTO.getOpenId());
//        miniProgramMessageDTO.setTemplate_id(messageDTO.getTemplateId());
//        Map<String, MiniProgramMessageDTO.Data> data = new HashMap<>();
//        for (MiniProgramMessageDTO.Param param : messageDTO.getParams()) {
//            data.put(param.getName(), new MiniProgramMessageDTO.Data(param.getValue()));
//        }
//        miniProgramMessageDTO.setData(data);
//        if (StringUtils.isNotEmpty(messageDTO.getPage())) {
//            miniProgramMessageDTO.setPage(messageDTO.getPage());
//        }
//        return miniProgramMessageDTO;
//    }
//
//    private MessageDO buildMessageDO(String openId, String postJson) {
//        MessageDO messageDO = new MessageDO();
//        messageDO.setMessage(postJson);
//        messageDO.setOpenId(openId);
//        messageDO.setDeleted(0);
//        messageDO.setCreateTime(LocalDateTime.now());
//        messageDO.setUpdateTime(LocalDateTime.now());
//        return messageDO;
//    }

}
