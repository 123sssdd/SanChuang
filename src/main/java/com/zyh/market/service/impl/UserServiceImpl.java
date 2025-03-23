package com.zyh.market.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyh.market.constants.AuthCode;
import com.zyh.market.constants.ResultCode;
import com.zyh.market.entity.User;
import com.zyh.market.exception.ServiceException;
import com.zyh.market.model.R;
import com.zyh.market.model.dto.UpdateUserInfoDto;
import com.zyh.market.model.dto.UserAdminPageDto;
import com.zyh.market.model.dto.UserLoginDto;
import com.zyh.market.service.UserService;
import com.zyh.market.mapper.UserMapper;
import com.zyh.market.utils.SendSmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangyihua
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2024-01-28 16:52:33
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
  @Autowired
  private StringRedisTemplate redisTemplate;
  
  @Override
  public SaTokenInfo login(UserLoginDto request) {
    String userCode = request.getCode();
    String phone = request.getPhone();
    String redis_code = redisTemplate.opsForValue().get("check:code:" + phone);
    if(StrUtil.isEmpty(redis_code) ) throw new ServiceException(ResultCode.Fail, "验证码错误");
    if ( !redis_code.equals(userCode)) throw new ServiceException(ResultCode.Fail, "验证码错误");
    //1.查询用户是否存在
    User user = lambdaQuery().eq(User::getPhone, request.getPhone()).one();
    //2.不存在 自动创建用户
    if (null == user) {
      user = new User();
      String numbers = RandomUtil.randomNumbers(9);
      user.setNumber(numbers);
      user.setPhone(Long.valueOf(request.getPhone()));
      user.setProvince(request.getProvince());
      user.setCity(request.getCity());
      user.setAvatar("https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png");
      user.setNickName("用户"+ numbers);
      user.setCreateTime(new Date().getTime());
      user.setUpdateTime(new Date().getTime());
      user.setStatus(9);
      boolean save = save(user);
      if (!save) throw new ServiceException(ResultCode.Fail);
    }else{
      if (user.getStatus() == 0) {
        throw new ServiceException(ResultCode.Fail,"账户被禁用");
      }
      user.setProvince(request.getProvince());
      user.setCity(request.getCity());
      user.setUpdateTime(new Date().getTime());
      boolean update = updateById(user);
      if (!update) throw new ServiceException(ResultCode.Fail);
    }
    StpUtil.login(user.getId());
    redisTemplate.delete("check:code:" + request.getPhone());
    //返回token
    return StpUtil.getTokenInfo();
  }
  
  @Override
  public SaTokenInfo loginPwd(UserLoginDto request) {
    User user = lambdaQuery().eq(User::getPhone, request.getPhone()).one();
    if(BeanUtil.isEmpty(user))throw new ServiceException(ResultCode.NotFindError,"第一次登陆，请选择手机号登录");
    String md5 = SaSecureUtil.md5(request.getPassword());
    if(!md5.equals(user.getPassword())) throw new ServiceException(ResultCode.Fail, "手机号或密码错误");
    user.setProvince(request.getProvince());
    user.setCity(request.getCity());
    user.setUpdateTime(new Date().getTime());
    boolean update = updateById(user);
    if (!update) throw new ServiceException(ResultCode.Fail);
    StpUtil.login(user.getId());
    return StpUtil.getTokenInfo();
  }
  
  @Override
  public void updateUserInfo(UpdateUserInfoDto dto) {
    String id = StpUtil.getLoginIdAsString();
    User user = getById(id);
    if (BeanUtil.isEmpty(user)) throw new ServiceException(ResultCode.NotFindError);
    user.setCheckNickName(dto.getNickName());
    user.setCheckIntro(dto.getIntro());
    user.setCheckAvatar(dto.getAvatar());
    user.setCheckStatus(0);
    boolean update = updateById(user);
    if (!update) throw new ServiceException(ResultCode.UpdateError);
  }
  
  @Override
  public Map getNum1UserStat() {
  return null;
  }
  
  @Override
  public List<Map> getUserStat() {
    LocalDate today = LocalDate.now();
    LocalDate monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    
    List<Map> weeklyUserData = new ArrayList<>();
    String[] weekDays = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    
    for (int i = 0; i < 7; i++) {
      LocalDate currentDate = monday.plusDays(i);
      
      long newUsers = __getNewUsersCount(currentDate);
      long activeUsers = __getActiveUsersCount(currentDate);
      
      Map<String, Object> userData = new HashMap<>();
      userData.put("date", weekDays[i]);
      userData.put("new", newUsers);
      userData.put("active", activeUsers);
      
      weeklyUserData.add(userData);
    }
    
    return weeklyUserData;
  }
  
  private long __getActiveUsersCount(LocalDate currentDate) {
    //根据currentDate 获取当日0点到24点的时间戳
    long start = currentDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC) * 1000;
    long end = currentDate.atStartOfDay().plusDays(1).toEpochSecond(ZoneOffset.UTC) * 1000;
    Long count = lambdaQuery().between(User::getUpdateTime, start, end).count();
    return count;
  }
  
  private long __getNewUsersCount(LocalDate currentDate) {
    //根据currentDate 获取当日0点到24点的时间戳
    long start = currentDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC) * 1000;
    long end = currentDate.atStartOfDay().plusDays(1).toEpochSecond(ZoneOffset.UTC) * 1000;
    Long count = lambdaQuery().between(User::getCreateTime, start, end).count();
    return count;
  }
  
  
  @Override
  public R<User> getUserInfo() {
    Object loginId = StpUtil.getLoginId();
    User user = lambdaQuery().eq(User::getId, loginId).one();
    if (user == null) {
      throw new ServiceException(ResultCode.NotFindError, "用户不存在");
    }
    return R.ok(user);
  }
  
  @Override
  public void getLoginCode(String phone) {
// 1. 校验手机号格式（可选）
    if (phone == null || !phone.matches("^1[3-9]\\d{9}$")) {
      throw new IllegalArgumentException("手机号格式不正确");
    }

    // 2. 生成一个 6 位数的随机验证码
    String code = String.format("%06d", new Random().nextInt(999999));

    // 3. 设置有效期（例如 5 分钟）
    String validityPeriod = "5";  // 假设有效期为 5 分钟

    // 3. 构建短信发送请求对象
    SendSmsUtil.MySendSmsRequest request = new SendSmsUtil.MySendSmsRequest();
    request.setPhone(phone);
    request.setSmsSdkAppId("1400971744"); // 替换为你的应用ID
    request.setSecretId("AKIDHQM4bv3xxqWyDH9qKhuxnRyKSN6ey5yf");         // 替换为你的 SecretId
    request.setSecretKey("Zu1vfDYgaIm36wa1ED0ax73nJchZnupV");       // 替换为你的 SecretKey
    request.setSignName("杭州夏品家居设计有限公司");         // 替换为你的签名
    request.setTemplateId("2386377");     // 替换为你的模板ID
    request.setTemplateParamSet(new String[]{code,validityPeriod}); // 设置模板参数（验证码）

    // 4. 调用腾讯云 API 发送短信
    Boolean success = SendSmsUtil.sendSms(request);

    // 5. 如果发送成功，将验证码存入 Redis
    if (Boolean.TRUE.equals(success)) {
      redisTemplate.opsForValue().set("check:code:" + phone, code, 120, TimeUnit.SECONDS);  // 设置过期时间 120 秒
    } else {
      throw new RuntimeException("短信发送失败，请稍后再试");
    }
  }
  
  @Override
  public R<User> getUserInfo(String userId) {
    User user = lambdaQuery().eq(User::getId, userId).one();
    if (user == null) {
      throw new ServiceException(ResultCode.NotFindError, "用户不存在");
    }
    return R.ok(user);
  }
  
  @Override
  public void updateUserInfoDetail(UpdateUserInfoDto dto) {
    String id = StpUtil.getLoginIdAsString();
    User user = getById(id);
    if(BeanUtil.isEmpty(user))  throw new ServiceException(ResultCode.NotFindError);
    if(StrUtil.isNotEmpty(dto.getPassword())){
      if (!dto.getPassword().equals(dto.getPasswordCheck())) throw new ServiceException(ResultCode.ValidateError,"输入密码不一致");
      String md5 = SaSecureUtil.md5(dto.getPassword());
      user.setPassword(md5);
    }
    boolean update = updateById(user);
    if(!update) throw new ServiceException(ResultCode.UpdateError);
  }
  
  @Override
  public Page getUserList(UserAdminPageDto dto) {
    Page<User> page = lambdaQuery()
      .like(StrUtil.isNotEmpty(dto.getKey()), User::getNickName, dto.getKey()).or()
      .like(StrUtil.isNotEmpty(dto.getKey()), User::getNumber, dto.getKey())
      .eq(null != dto.getCheckStatus(), User::getCheckStatus, dto.getCheckStatus())
      .orderByDesc(User::getCreateTime)
      .page(new Page<>(dto.getPageNumber(), dto.getPageSize()));
    return page;
  }
  

}




