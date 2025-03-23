package com.zyh.market.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyh.market.entity.ChatList;
import com.zyh.market.entity.Comment;
import com.zyh.market.model.dto.ChatListCreateDto;
import com.zyh.market.model.vo.ChatListVo;

import java.util.List;

public interface ChatListService extends IService<ChatList> {
  String create(ChatListCreateDto dto);
  
  List<ChatListVo> getMyChatList();
  
  
  int getNoReadTotal();
  
}
