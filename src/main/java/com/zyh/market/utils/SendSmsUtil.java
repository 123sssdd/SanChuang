package com.zyh.market.utils;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class SendSmsUtil {

    public static Boolean sendSms(MySendSmsRequest request) {
        // 初始化腾讯云的身份验证信息
        Credential cred = new Credential(request.getSecretId(), request.getSecretKey());

        // 初始化短信客户端
        SmsClient client = new SmsClient(cred, "ap-nanjing");  // 设置为你所在区域

        // 创建发送短信的请求对象
        final SendSmsRequest req = new SendSmsRequest();
        req.setPhoneNumberSet(new String[]{"+86" + request.getPhone()}); // 设置手机号，拼接上国家代码 +86
        req.setSmsSdkAppId(request.getSmsSdkAppId());  // 应用 ID
        req.setSignName(request.getSignName());        // 短信签名
        req.setTemplateId(request.getTemplateId());    // 模板 ID
        req.setTemplateParamSet(request.getTemplateParamSet()); // 模板参数

        // 发送短信
        SendSmsResponse res = null;
        try {
            res = client.SendSms(req);  // 调用腾讯云 API 发送短信
        } catch (TencentCloudSDKException e) {
            log.error("发送短信出错：", e);
            return Boolean.FALSE;
        }

        log.info( SendSmsResponse.toJsonString(res));

        // 检查返回的状态和错误信息
        if (Objects.nonNull(res.getSendStatusSet()) && res.getSendStatusSet().length > 0) {
            String statusCode = res.getSendStatusSet()[0].getCode();
            if ("Ok".equals(statusCode)) {
                return Boolean.TRUE;  // 成功
            } else {
                // 打印详细的错误信息
                log.error("短信发送失败，错误码：{}, 错误信息：{}", statusCode, res.getSendStatusSet()[0].getMessage());
            }
        }
        return Boolean.FALSE;  // 失败
    }

    // 参数类，用户传递信息的类
    @Data
    public static class MySendSmsRequest {
        private String phone;           // 手机号
        private String signName;        // 短信签名
        private String templateId;      // 模板 ID
        private String[] templateParamSet;  // 模板参数
        private String smsSdkAppId;     // 应用 ID
        private String secretId;        // SecretId
        private String secretKey;       // SecretKey
    }
}
