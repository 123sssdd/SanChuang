package com.zyh.market.model.dto;

import lombok.Data;

@Data
public class CreateCommentDto {
  private String productId;
  private String parentId;
  private String content;
}
