package cn.qingweico.afr.utils;

import cn.qingweico.afr.enums.FaceVerifyType;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.facebody.model.v20191230.CompareFaceRequest;
import com.aliyuncs.facebody.model.v20191230.CompareFaceResponse;
import com.aliyuncs.profile.DefaultProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author zqw
 * @date 2022/4/9
 */
@Slf4j
@Component
public class CompareFace {
    @Resource
    private AliResource aliResource;

    public boolean faceVerify(int type, String face1, String face2, float targetConfidence) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-shanghai", aliResource.getAccessKeyId(),
                aliResource.getAccessKeySecret());
        IAcsClient client = new DefaultAcsClient(profile);

        CompareFaceRequest request = new CompareFaceRequest();
        if (type == FaceVerifyType.BASE64.getVal()) {
            request.setImageDataA(face1);
            request.setImageDataB(face2);
        }

        try {
            CompareFaceResponse response = client.getAcsResponse(request);
            float responseConfidence = response.getData().getConfidence();
            log.info("responseConfidence: {}, 人脸对比结果: {}", responseConfidence,
                    responseConfidence > targetConfidence ? "通过" : "未通过");

            return responseConfidence > targetConfidence;

        } catch (ServerException e) {
            log.error(e.getMessage());
        } catch (ClientException e) {
            log.error("ErrCode:{}", e.getErrCode());
            log.error("ErrMsg:{}", e.getErrMsg());
            log.error("RequestId:{}", e.getRequestId());
        }
        return false;
    }
}
