package com.cyl.ctrbt.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.cyl.ctrbt.openai.ChatGPTUtil;
import com.cyl.ctrbt.openai.entity.chat.ChatCompletionResponse;
import com.cyl.ctrbt.openai.entity.chat.Message;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/ding")
@RestController
@Slf4j
public class DingTalkController {

  @Autowired
  private ChatGPTUtil chatGPTUtil;

  @RequestMapping("/receive")
  public String helloRobots(@RequestBody(required = false) JSONObject json) {
    System.out.println(JSONUtil.toJsonStr(json));
    String content = json.getJSONObject("text").get("content").toString().replaceAll(" ", "");
    String sessionWebhook = json.getStr("sessionWebhook");
    DingTalkClient client = new DefaultDingTalkClient(sessionWebhook);
    if ("text".equals(json.getStr("msgtype"))) {
      text(client, content);
    }
    return null;
  }
  
  @RequestMapping("/chat")
  @CrossOrigin(origins = "http://127.0.0.1:3000")
  public String chat(@RequestBody Message message) {
    log.info(message.getContent());
    Message retMsg = chatGPTUtil.chat(message.getContent(), "dingtalk");
    return retMsg.getContent();
  }
  
  @RequestMapping("/chatNative")
  @CrossOrigin(origins = "http://127.0.0.1:3000")
  public ChatCompletionResponse chatNative(@RequestBody JSONObject json) {
    log.info(JSONUtil.toJsonStr(json));
    String content = json.getJSONObject("content").get("content").toString().replaceAll(" ", "");
    String sessionWebhook = json.getStr("sessionWebhook");
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
