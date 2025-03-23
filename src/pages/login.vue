<template>
    <div class="reds-modal login-modal" role="presentation">
      <div tabindex="-1" class="reds-mask" aria-label="弹窗遮罩"></div>
      <!---->
      <div class="login-container animate__animated animate__bounceIn">
        <!-- <div class="icon-btn-wrapper close-button" @click="close">
               <i class="el-icon-circle-close" style="width: 1em; height: 1em"></i>
             </div> -->
        <div class="left">
          <img src="@/assets/logo4.jpg" style="height: 100%; width: 100%" />
        </div>
        <div class="right">
          <div class="title" v-if="this.method === 'phone'">手机号登录</div>
          <div class="title" v-if="this.method === 'password'">密码登录</div>
          <!---->
          <div class="fixed-logo">
            <img src="@/assets/1333.png" alt="Logo" />
          </div>
          <div style="height: 10px"></div>
          <div class="input-container">
            <div class="comment-wrapper active comment-comp">
              <div class="input-wrapper">
                <input
                  class="comment-input"
                  v-model="formData.phone"
                  type="text"
                  :placeholder="phonePlaceholder"
                  @focus="clearPhonePlaceholder"
                  @blur="restorePhonePlaceholder"
                />
              </div>
            </div>
            <div
              class="comment-wrapper active comment-comp"
              v-if="this.method === 'phone'"
            >
              <div class="input-wrapper">
                <input
                  class="comment-input"
                  v-model="formData.code"
                  type="text"
                  :placeholder="codePlaceholder"
                  @focus="clearCodePlaceholder"
                  @blur="restoreCodePlaceholder"
                />
              </div>
              <button class="submit code" @click="getCode">{{ sms_text }}</button>
            </div>
            <div
              class="comment-wrapper active comment-comp"
              v-if="this.method === 'password'"
            >
              <div class="input-wrapper">
                <input
                  class="comment-input"
                  v-model="formData.password"
                  type="password"
                  :placeholder="passwordPlaceholder"
                  @focus="clearPasswordPlaceholder"
                  @blur="restorePasswordPlaceholder"
                />
              </div>
            </div>
            <button
              class="submit"
              v-loading="loginLoading"
              style="margin-top: 60px"
              type="button"
              @click.prevent="submit()"
            >
              登&nbsp;&nbsp;&nbsp;录
            </button>
          </div>
          <div class="oauth-tip"></div>
          <div class="login">
            <div
              class="login-common"
              @click="changePwd('password')"
              v-if="this.method === 'phone'"
            >
              密码登录
            </div>
            <div
              class="login-common"
              @click="changePwd('phone')"
              v-if="this.method === 'password'"
            >
              手机号登录
            </div>
            <div class="reg">忘记密码</div>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import Cookies from "js-cookie";
  import gdMapUtil from "@/utils/gdMapUtil";
  import { Notification } from "element-ui";
  
  export default {
    data() {
      return {
        method: "phone",
        formData: {
          phone: "",
          code: "",
          password: "",
          province: "",
          city: "",
        },
        codePlaceholder: "输入验证码",
        phonePlaceholder: "输入手机号",
        passwordPlaceholder: "输入密码",
        sms_time: 60,
        sms_text: "获取验证码",
        loginLoading: false,
      };
    },
    methods: {
      clearCodePlaceholder() {
        this.codePlaceholder = "";
      },
      restoreCodePlaceholder() {
        if (!this.formData.code) {
          this.codePlaceholder = "输入验证码";
        }
      },
  
      clearPhonePlaceholder() {
        this.phonePlaceholder = "";
      },
  
      clearPasswordPlaceholder() {
        this.passwordPlaceholder = "";
      },
  
      restorePhonePlaceholder() {
        if (!this.formData.phone) {
          this.phonePlaceholder = "输入手机号";
        }
      },
  
      restorePasswordPlaceholder() {
        if (!this.formData.password) {
          this.passwordPlaceholder = "输入密码";
        }
      },
  
      changePwd(val) {
        this.method = val;
      },
      submit() {
        this.loginLoading = true;
        gdMapUtil.getCity().then(
          (res) => {
            this.formData.province = res.province;
            this.formData.city = res.city;
            this.login();
          },
          (error) => {
            this.login();
          }
        );
      },
      login() {
        if (this.method === "phone") {
          this.$api.user.login(this.formData).then((res) => {
            this.loginLoading = false;
            Cookies.set("web-token", res.result.tokenValue);
            this.$store.dispatch("getUserInfo").then((res) => {
              this.$emit("click-child", true);
              this.$router.push("/");
            });
          });
        } else if (this.method === "password") {
          this.$api.user.loginPwd(this.formData).then((res) => {
            this.loginLoading = false;
            Cookies.set("web-token", res.result.tokenValue);
            this.$store.dispatch("getUserInfo").then((res) => {
              this.$emit("click-child", true);
              this.$router.push("/");
            });
          });
        }
      },
      getCode() {
        if (!this.formData.phone) return;
        this.$api.user.getCode(this.formData.phone).then((res) => {
          this.sms_time = 59;
          this.sendPhoneTime();
          Notification({
            type: "success",
            title: "原创力平台",
            message: "发送验证码成功",
            customClass: "http-message",
          });
        });
      },
      sendPhoneTime() {
        this.sms_text = this.sms_time + "秒后重试";
        this.sms_time--;
        let _this = this;
        if (this.sms_time < 0) {
          this.sms_time = 60;
          this.sms_text = "获取验证码";
        } else {
          setTimeout(function () {
            _this.sendPhoneTime();
          }, 1000);
        }
      },
      close() {
        this.$emit("click-child", false);
      },
    },
  };
  </script>
  
  <style lang="less" scoped>
  
  
  .fixed-logo {
    position: absolute;
    top: 65px;
    left: 700px;
    z-index: 1000003; /* 确保它在最上层 */
    width: 120px;
    height: auto;
    filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.3));
  }
  
  .fixed-logo img {
    width: 100%;
    height: 100%;
    object-fit: contain;
  }
  
  
  
  
  a {
    text-decoration: none;
    background-color: transparent;
  }
  
  :deep(.el-loading-mask) {
    background-color: #E6C170 !important;
    backdrop-filter: none;
    border-radius: 999px;
  }
  
  .login-modal {
    z-index: 100010;
    flex-direction: column;
  }
  
  .reds-modal {
    display: flex;
    align-items: center;
    justify-content: center;
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 1000002;
    box-sizing: border-box;
    visibility: visible;
    opacity: 1;
    transition: opacity 0.2s, visibility 0.2s;
    .reds-mask {
      position: absolute;
      top: 0;
      left: 0;
      width:100%;
      height:100%;
      background-color: rgba(252, 247, 247, 0.25);
  
      z-index: -1;
    }
  
    .login-container {
      display: flex;
      position: relative;
      width: 900px;
      height: 480px;
      background: #466591;
      border: 16px solid #466491;
      border-radius: 16px;
      box-shadow: 0 4px 32px 0 rgba(0, 0, 0, 0.8), 0 1px 4px 0 rgba(0, 0, 0, 0.04);
      transition: all 0.2s;
      .close-button {
        position: absolute;
        right: 20px;
        top: 20px;
        cursor: pointer;
        color: rgba(51, 51, 51, 0.8);
      }
  
      .left {
        display: flex;
        align-items: center;
        flex-direction: column;
        border-right: 13px solid #2a4492;
  
        img {
          border-style: none;
          width: 100%;
        }
  
        .logo {
          margin-top: 64px;
          width: 91.08px;
          height: 48px;
          -webkit-user-select: none;
          user-select: none;
          pointer-events: none;
        }
  
        .course-video {
          margin-top: 20px;
          width: 160px;
          height: 240px;
          overflow: hidden;
          margin-bottom: -1px;
  
          video {
            width: 100%;
            height: 100%;
            object-fit: cover;
          }
        }
  
        .qrcode {
          position: relative;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-top: 32px;
          width: 192px;
          height: 192px;
          border: 2px solid var(--color-shadow-border);
          border-radius: 12px;
          background: var(--color-white);
  
          .qrcode-img {
            width: 160px;
            height: 160px;
          }
  
          .status {
            display: flex;
            align-items: center;
            justify-content: center;
            position: absolute;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            flex-direction: column;
            background: #fff;
            opacity: 0.95;
            -webkit-backdrop-filter: blur(2.5px);
            backdrop-filter: blur(2.5px);
            border-radius: 10px;
          }
        }
  
        .tip {
          color: #333;
          font-weight: 600;
          font-size: 12px;
          margin-top: 16px;
          line-height: 120%;
        }
  
        .course {
          width: 64px;
          height: 28px;
          display: flex;
          align-items: center;
          justify-content: center;
          border-radius: 999px;
          margin-top: 43.5px;
          font-weight: 400;
          font-size: 14px;
          color: rgba(51, 51, 51, 0.8);
          cursor: pointer;
        }
      }
  
      .right {
        width: 400px;
        padding-top: 40px;
        display: flex;
        align-items: center;
        flex-direction: column;
        border: 8px solid #fcfcfc;
        .title {
          font-size: 27px;
          color: #ecc26e;
          font-weight: 600;
          line-height: 120%;
        }
  
        .input-container {
          margin-top: 24px;
          width: 304px;
          display: flex;
          flex-direction: column;
  
          .comment-wrapper {
            &.active {
              .input-wrapper {
                flex-shrink: 1;
              }
            }
          }
  
          .comment-wrapper .code {
            margin-left: 20px;
          }
  
          .comment-wrapper {
            display: flex;
            font-size: 16px;
            overflow: hidden;
  
            .input-wrapper {
              display: flex;
              position: relative;
              width: 100%;
              flex-shrink: 0;
              transition: flex 0.3s;
  
              .comment-input:placeholder-shown {
                background-image: none;
                padding: 12px 16px 12px 16px;
                background-repeat: no-repeat;
                background-size: 16px 16px;
                background-position: 16px 12px;
                color: #0f0d0d;
              }
  
              .comment-input::placeholder {
                color: #4f4f4f;
                font-weight: 550;
              }
  
              .comment-input {
                padding: 12px 16px;
                width: 100%;
                height: 40px;
                line-height: 16px;
                border-radius: 22px;
                border: 3px solid #29478e; /* 添加了边框 */
                border-color: #29478e;
                outline: none;
                resize: none;
                color: #0f0d0d;
                background-color: #fff;
              }
  
              .input-buttons {
                cursor: pointer;
                position: absolute;
                right: 0;
                top: 0;
                height: 40px;
                display: flex;
                align-items: center;
                justify-content: center;
                width: 40px;
                color: rgba(51, 51, 51, 0.3);
              }
            }
  
            .submit {
              width: 120px;
              height: 40px;
              display: flex;
              align-items: center;
              justify-content: center;
              color: #fff;
              font-weight: 600;
              cursor: pointer;
              flex-shrink: 0;
              background: #07192f;
              border-radius: 44px;
              box-shadow: #e2bd71;
              font-size: 16px;
              border: 4px solid #e2bd71;
            }
          }
  
          .comment-comp {
            margin-top: 20px;
          }
  
          .auth-code {
            justify-content: center;
          }
  
          .auth-code,
          .phone {
            display: flex;
            align-items: center;
            font-size: 16px;
            line-height: 22px;
            color: rgba(51, 51, 51, 0.8);
            height: 48px;
            background: #fff;
            border-radius: 999px;
            transition: border-color 0.2s;
            border-bottom: 0.5px solid rgba(0, 0, 0, 0.8);
  
            .country-code {
              display: flex;
              align-items: center;
              justify-content: center;
              position: relative;
              width: 29px;
              height: 100%;
              font-weight: 400;
              margin-left: 16px;
              margin-right: 20px;
              color: #333;
            }
  
            .country-code:after {
              position: absolute;
              left: 41px;
              content: "";
              height: 24px;
              width: 1px;
              background-color: #fff;
            }
          }
  
          .auth-code input,
          .phone input {
            font-size: 16px;
            width: 130px;
            height: 100%;
            caret-color: #ff2442;
            color: #333;
          }
  
          .code-button {
            font-size: 16px;
            color: #ff2442;
            cursor: pointer;
            opacity: 0.5;
          }
  
          .err-msg {
            margin-top: 9.5px;
            height: 10px;
            line-height: 10px;
            color: var(--color-red);
            font-size: 14px;
            font-weight: 400;
            display: flex;
            align-items: center;
            justify-content: center;
          }
  
          .submit {
            height: 48px;
            background: #e6c170;
            color: #0c0b0b;
            border: 4px solid #050505;
            border-radius: 999px;
            font-size: 24px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.2s;
            width: 100%;
          }
  
          form {
            display: block;
            margin-top: 0;
          }
        }
  
        .agreements {
          width: 280px;
          position: relative;
          margin-top: 16px;
          padding-left: 18px;
          font-size: 12px;
          color: var(--color-tertiary-label);
          line-height: 120%;
  
          .agree-icon {
            display: inline-block;
            width: 10px;
            height: 10px;
            border-radius: 100%;
            background-color: #fff;
            border: 1px solid rgba(0, 0, 0, 0.183);
          }
  
          a {
            color: #13386c;
          }
  
          .links {
            font-weight: 400;
          }
        }
  
        .oauth-tip {
          margin-top: 35px;
          display: flex;
          align-items: center;
          justify-content: center;
          width: 400px;
          height: 11px;
          content: "";
          background-color: #f8f9fb;
        }
  
        .login {
          display: flex;
          margin-top: 9px;
          align-items: center;
          justify-content: center;
          .login-common {
            width: 144px;
            height: 40px;
            background-color: #fff;
            border: 3px solid #2d4395;
            border-radius: 999px;
            color: #4b6691;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-right: 15px;
            cursor: pointer;
            font-weight: bold;
          }
  
          .reg {
            width: 144px;
            height: 40px;
            background-color: #fff;
            border: 3px solid #2d4395;
            border-radius: 999px;
            color: #4b6691;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-left: 15px;
            cursor: pointer;
            font-weight: bold;
          }
        }
      }
    }
  }
  </style>
  