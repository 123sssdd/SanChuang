<template>
  <div
    class="container"
    style="transition: background-color 0.4s ease 0s;hsla(0,0%,100%,0.98);"
  >
    <div class="chat-container">
      <header class="chat-header">
        <div class="header-left"></div>
        <div class="header-user">
          <!-- <el-avatar :src="toUserInfo.avatar"/> -->
          <span style="margin-left: 5px">{{ toUserInfo.nickName }}</span>
        </div>
        <div class="header-tool">
          <!-- <i class="el-icon-more icon-item"></i> -->
        </div>
      </header>
      <div class="order-detail">
        <div class="details-box" @click="toMain()">
          <el-image
            style="width: 100%; height: 100%; border-radius: 5px"
            :src="productInfo.image"
            fit="cover"
          ></el-image>
        </div>
        <div class="info">
          <div class="product-info" @click="toMain()">
            <div class="interaction-price">
              ￥ {{ $utils.convert.to_price(productInfo.price) }}
            </div>
            <div class="address">
              {{ productInfo.province }} {{ productInfo.city }}
            </div>
            <div class="postType" v-if="productInfo.postType === 0">
              电子邮箱
            </div>
            <div class="postType" v-if="productInfo.postType === 1">
              网盘链接
            </div>
          </div>
          <div class="pay-btn" @click="purchase">
            <img
              v-if="
                productInfo.status === 9 && productInfo.userId !== userInfo.id
              "
              src="@/assets/pay.png"
              alt="购买"
              class="pay-icon"
            />
          </div>
        </div>
      </div>
      <main class="chat-main">
        <div class="chat-record" ref="ChatRef">
          <div v-for="(item, index) in chatMessageList" :key="index">
            <div>
              <div
                class="message-my-item"
                v-if="item.fromUserId === userInfo.id"
              >
                <div class="message-my-item">
                  <div class="message-my-conent">{{ item.content }}</div>
                  <div class="user-avatar">
                    <el-image
                      class="avatar-item"
                      style="width: 40px; height: 40px; border-radius: 100%"
                      :src="userInfo.avatar"
                    ></el-image>
                  </div>
                </div>
              </div>
              <div class="message-item" v-else>
                <div class="message-item">
                  <div class="user-avatar">
                    <el-image
                      class="avatar-item"
                      style="width: 40px; height: 40px; border-radius: 100%"
                      :src="toUserInfo.avatar"
                    ></el-image>
                  </div>
                  <div class="message-conent">{{ item.content }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <hr color="#f4f4f4" />
        <div class="chat-input">
          <!-- <div class="input-tool">-->
          <!--            <div class="tool-left">-->
          <!--              &lt;!&ndash;              <PieChart class="icon-item"></PieChart>&ndash;&gt;-->
          <!--              <Picture class="icon-item"></Picture>-->
          <!--            </div>-->
          <!--            <div class="tool-history">-->
          <!--              &lt;!&ndash;              <Clock class="icon-item"></Clock>&ndash;&gt;-->
          <!--            </div>-->
          <!--          </div> -->
          <div class="comment-wrapper active comment-comp">
            <div class="input-wrapper">
              <input
                class="comment-input"
                v-model="content"
                type="text"
                placeholder="想跟TA说点什么..."
                @keyup.enter="submit"
              />
              <div class="input-buttons" @click="removeContent">
                <i class="el-icon-circle-close"></i>
              </div>
            </div>
            <button class="submit" @click="submit">发送</button>
          </div>
        </div>
      </main>
    </div>
  </div>
</template>
<script>
import websocket from "@/utils/websocket";
import Main from "@/pages/main/main.vue";

export default {
  components: { Main },
  props: {
    chatListItem: {},
    productId: "",
  },
  watch: {
    chatMessageList() {
      this.$nextTick(() => {
        this.$refs.ChatRef.scrollTop = this.$refs.ChatRef.scrollHeight;
      });
    },
  },
  data() {
    return {
      userInfo: {},
      toUserInfo: {
        avatar: "",
      },
      content: "",
      toUserId: "",
      chatMessageList: [],
      productInfo: {
        image: "",
      },
    };
  },
  mounted() {
    this.getProductInfo();
    this.userInfo = this.$store.state.user.userInfo;
    this.getUserInfo();
    websocket.Init(this.chatListItem.id, this.userInfo.id);
    this.getMessage();
    this.receiveMessage();
  },
  destroyed() {
    this.close();
  },
  methods: {
    getMessage() {
      this.$api.chatMessage
        .getMessageList({ chatListId: this.chatListItem.id })
        .then((res) => {
          this.chatMessageList = res.result;
        });
    },
    purchase() {
      this.close();
      this.$router.push("/orderCreate?productId=" + this.productInfo.id);
    },
    submit() {
      let message = {
        fromUserId: this.userInfo.id,
        content: this.content,
        toUserId: this.toUserId,
        chatListId: this.chatListItem.id,
        sendTime: String(new Date().getTime()),
      };
      websocket.Send(message);
      this.chatMessageList.push(message);
      this.content = "";
    },
    removeContent() {
      this.content = "";
    },
    toMain() {
      this.$emit("open_main", this.productInfo.id);
      this.close();
    },
    getProductInfo() {
      this.$api.product
        .getProductInfo({ productId: this.productId })
        .then((res) => {
          this.productInfo = res.result;
          this.productInfo.image = JSON.parse(this.productInfo.image)[0];
        });
    },
    getUserInfo() {
      this.toUserId =
        this.userInfo.id === this.chatListItem.fromUserId
          ? this.chatListItem.toUserId
          : this.chatListItem.fromUserId;
      this.$api.user.getUserInfoById(this.toUserId).then((res) => {
        this.toUserInfo = res.result;
      });
    },
    close() {
      websocket.Close().then((res) => {
        this.$emit("close");
      });
    },
    receiveMessage() {
      let ws = websocket.getwebsocket();
      ws.onmessage = (e) => {
        //心跳消息不做处理
        if (e.data === "ok") {
          return;
        }
        let message = JSON.parse(e.data);
        this.chatMessageList.push(message);
      };
    },
  },
};
</script>
<style lang="less" scoped>
.pay-icon {
  width: 60px;
  height: 60px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-left: -80px;
  margin-top: 10px;
}

.icon-item {
  width: 1.2em;
  height: 1.2em;
  margin-right: 5px;
  color: rgba(51, 51, 51, 0.8);
}

.container {
  width: 100%;
  height: 100%;

  .chat-container {
    width: 100%;
    height: 100%;

    transition: transform 0.4s ease 0s, width 0.4s ease 0s;
    background-color: #fff;

    .chat-header {
      height: 45px;
      width: 100%;
      background-color: #fff;
      display: flex;
      justify-content: space-between;
      align-items: center;

      .header-user {
        display: flex;
        justify-content: space-between;
        align-items: center;
      }
    }

    .order-detail {
      height: 13%;
      width: 100%;
      border-bottom: 1px solid rgba(0, 0, 0, 0.08);
      flex-grow: 1;
      flex-shrink: 1;
      display: flex;
      flex-direction: row;
      padding: 10px;

      .details-box {
        width: 25%;
        cursor: pointer;
      }

      .info {
        margin-left: 10px;
        display: flex;
        justify-content: space-between;
        width: 75%;

        .product-info {
          cursor: pointer;
          padding: 10px 0;
          display: flex;
          flex-direction: column;
          justify-content: space-between;
          height: 100%; /* 让子项充满高度，以便垂直对齐 */
          .interaction-price {
            font-weight: bolder;
            font-size: 16px;
            color: red;
          }
          .address {
            font-size: 13px;
          }
          .postType {
            font-size: 13px;
          }
        }
        .buy {
          margin-top: 13%;
        }
      }
    }

    .chat-main {
      height: 80%;
      padding: 0 15px;

      .chat-record {
        height: 85%;
        overflow-y: scroll;
        .message-item {
          display: flex;
          justify-content: left;
          align-items: center;
          margin: 2px 0;

          .message-conent {
            margin-left: 5px;
            padding: 4px 10px;
            border: 1px solid #f4f4f4;
            background-color: #fff;
            border-radius: 8px;
            font-size: 16px;
          }
        }

        .message-my-item {
          display: flex;
          justify-content: right;
          align-items: center;
          margin: 2px 0;

          .message-my-conent {
            margin-right: 5px;
            padding: 4px 10px;
            color: #fff;
            background-color: rgb(0, 170, 255);
            border-radius: 8px;
            font-size: 16px;
          }
        }
      }

      .chat-input {
        height: 10%;
        .input-tool {
          display: flex;
          justify-content: space-between;
          height: 30px;
          padding: 0 5px;
          background-color: #9e9e9e;
        }

        .comment-wrapper {
          &.active {
            .input-wrapper {
              flex-shrink: 1;
            }
          }
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
              padding: 12px 92px 12px 16px;
              background-repeat: no-repeat;
              background-size: 16px 16px;
              background-position: 16px 12px;
              color: rgba(51, 51, 51, 0.3);
            }

            .comment-input {
              padding: 12px 92px 12px 16px;
              width: 100%;
              height: 40px;
              line-height: 16px;
              background: rgba(0, 0, 0, 0.03);
              caret-color: rgba(51, 51, 51, 0.3);
              border-radius: 22px;
              border: none;
              outline: none;
              resize: none;
              color: #333;
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
            margin-left: 8px;
            width: 60px;
            height: 40px;
            display: flex;
            align-items: center;
            justify-content: center;
            color: #fff;
            font-weight: 600;
            cursor: pointer;
            flex-shrink: 0;
            background: #3d8af5;
            border-radius: 44px;
            font-size: 16px;
          }
        }

        .comment-comp {
          margin-top: 20px;
        }
        .input-content {
          width: 100%;
          padding: 0 10px;
          resize: none;
          border: 0;
          outline: none;
        }
      }
    }
  }

  .close-cricle {
    left: 18vw;
    top: 1.3vw;
    position: fixed;
    display: flex;
    z-index: 100;
    cursor: pointer;

    .close-mask-white {
      box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.04),
        0 1px 2px 0 rgba(0, 0, 0, 0.02);
      border: 1px solid rgba(0, 0, 0, 0.08);
    }

    .close {
      display: flex;
      justify-content: center;
      align-items: center;
      border-radius: 100%;
      width: 40px;
      height: 40px;
      border-radius: 40px;
      cursor: pointer;
      transition: all 0.3s;
      background-color: #fff;
    }
  }
}
</style>
