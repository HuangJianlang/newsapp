package com.jianlang.model.websocket.dtos;

import com.jianlang.model.annotation.IdEncrypt;
import lombok.Data;

@Data
public class WebSocketDto {
    // 设备ID
    Integer equipmentId;
    // 文章ID
    String token;
}
