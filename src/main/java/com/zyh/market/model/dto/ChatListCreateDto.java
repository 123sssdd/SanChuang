package com.zyh.market.model.dto;

import lombok.Data;

@Data
public class ChatListCreateDto {
  private String fromUserId;
  private String toUserId;
  private String productId;
  private String productImage;
}
