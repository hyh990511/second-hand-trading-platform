package com.shadouyou.backend.service.BaiduAI;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class CheckTextAPI {
    private static final String URL = "https://aip.baidubce.com/rest/2.0/solution/v1/text_censor/v2/user_defined";

    /**
     * @param content 需要审核的文本
     *
     * @return true 通过 false 失败
     */
    public String check(String content){

        //获取access_token
        String access_token = AuthService.getAuth();
        Map<String,String> parameters = new HashMap<>();
        //添加调用参数
        parameters.put("access_token",access_token);
        parameters.put("text",content);
        //调用文本审核接口
        String result = HttpUtil.doPost(URL, parameters);

        JSONObject job = JSONObject.fromObject(result);



        return job.get("conclusion").toString();
    }


}
