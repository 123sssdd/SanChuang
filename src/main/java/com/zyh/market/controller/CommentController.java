package com.zyh.market.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.core.util.StrUtil;
import com.zyh.market.constants.ResultCode;
import com.zyh.market.entity.Comment;
import com.zyh.market.exception.ServiceException;
import com.zyh.market.model.R;
import com.zyh.market.model.dto.CreateCommentDto;
import com.zyh.market.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/comment")
@SaCheckLogin
public class CommentController {
  @Autowired
  private CommentService commentService;
  
  @PostMapping
  public R saveComment(@RequestBody CreateCommentDto dto) {
    commentService.saveComment(dto.getProductId(), dto.getParentId(), dto.getContent());
    return R.ok();
  }
  
  @GetMapping("/byProduct")
  public R<Map> getCommentList(@RequestParam("productId") String productId) {
    HashMap map = commentService.getCommentList(productId);
    return R.ok(map);
  }
  
  @DeleteMapping("/{id}")
  public R delComment(@PathVariable("id") String id) {
    boolean update = commentService.removeById(id);
    if (!update) throw new ServiceException(ResultCode.DeleteError);
    return R.ok();
  }
}
