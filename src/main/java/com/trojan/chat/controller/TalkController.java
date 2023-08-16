package com.trojan.chat.controller;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.taobao.api.ApiException;
import com.trojan.chat.common.req.ChatWithTokenReq;
import com.trojan.chat.openai.ChatGPTUtil;
import com.trojan.chat.repository.entity.chat.ChatCompletionResponse;
import com.trojan.chat.repository.entity.chat.Message;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/talk")
@RestController
@Slf4j
@Api(tags = "会话")
public class TalkController {
  
  @Autowired
  private ChatGPTUtil chatGPTUtil;
  
  //  @RequestMapping("/receive")
  //  public String helloRobots(@RequestBody(required = false) JSONObject json) {
  //    System.out.println(JSONUtil.toJsonStr(json));
  //    String content = json.getJSONObject("text").get("content").toString().replaceAll(" ", "");
  //    String sessionWebhook = json.getStr("sessionWebhook");
  //    DingTalkClient client = new DefaultDingTalkClient(sessionWebhook);
  //    if ("text".equals(json.getStr("msgtype"))) {
  //      text(client, content);
  //    }
  //    return null;
  //  }
  
  @RequestMapping("/chat")
  @CrossOrigin(origins = "http://127.0.0.1:3000")
  public String chat(@RequestBody Message message) {
    log.info(message.getContent());
    Message retMsg = chatGPTUtil.chat(message.getContent(), "dingtalk");
    return retMsg.getContent();
  }
  
  @RequestMapping("/chatNative")
  @CrossOrigin(origins = "http://127.0.0.1:3000")
  public String chatNative(@RequestBody JSONObject json) {
    log.info(JSONUtil.toJsonStr(json));
    JSONArray jsonArray = JSONUtil.parseArray(json.get("content"));
    jsonArray.remove(jsonArray.size() - 1);
    ChatCompletionResponse response = chatGPTUtil.chatNative(jsonArray.toString(), "user");
    String content = response.getChoices().get(0).getMessage().getContent();
    log.info(content);
    if (content.startsWith("\\[")) {
      JSONArray responseArray = JSONUtil.parseArray(content.replaceAll("\\[b\\]", "<b>"));
      content = JSONUtil.parse(responseArray.get(responseArray.size() - 1)).toString();
    }
    log.info(content);
    //    JSONArray responseArray=JSONUtil.parseArray(content.replaceAll("\\[b\\]","<b>"));
    return content;
  }
  
  @RequestMapping("/chatWithToken")
  @CrossOrigin(origins = "http://127.0.0.1:3000")
  public ChatCompletionResponse chatWithToken(@RequestBody ChatWithTokenReq req) {
    String content = req.getContent();
    return chatGPTUtil.chatNative(content, "dingtalk");
  }
  
  private void text(DingTalkClient client, String content) {
    try {
      OapiRobotSendRequest request = new OapiRobotSendRequest();
      request.setMsgtype("text");
      OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
      Message message = chatGPTUtil.chat(content, "dingtalk");
      text.setContent(message.getContent());
      request.setText(text);
      OapiRobotSendResponse response = client.execute(request);
      System.out.println(response.getBody());
    } catch (ApiException e) {
      e.printStackTrace();
    }
  }
}
