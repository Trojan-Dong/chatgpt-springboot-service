package com.trojan.chat.common.req;

import com.trojan.chat.repository.entity.chat.Message;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "ChatWithTokenReq", description = "带会话的请求信息")
public class ChatWithTokenReq implements Serializable {
    
    @ApiModelProperty(value = "角色")
    private String role;
    
    @ApiModelProperty(value = "消息列表")
    private String content;
}
