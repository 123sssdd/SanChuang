<template>
  <div class="container">
    <div class="push-container">
      <div class="header">
        <span class="header-icon"></span><span class="header-title">支付</span>
      </div>
      <div class="img-list">
        <div
          v-if="
            paymentOrderInfo.paymentStatus === 0 ||
            paymentOrderInfo.paymentStatus === 1
          "
          style="color: white; font-size: 20px; font-weight: bold"
        >
          支付金额：{{ $utils.convert.to_price(paymentOrderInfo.payPrice) }} 元
        </div>
        <div
          v-if="paymentOrderInfo.paymentStatus === 9"
          style="color: white; font-size: 20px; font-weight: bold"
        >
          正在跳转到支付界面……
        </div>
      </div>

      <div style="margin-left: 20px">
        <el-divider style="width: 576px" />
      </div>
      <div
        class="header-title"
        style="
          margin-top: 10px;
          margin-bottom: 15px;
          font-size: 18px;
          padding-left: 20px;
        "
      >
        订单信息
      </div>
      <div style="margin-left: 20px">
        <el-descriptions class="margin-top" :column="1" border>
          <el-descriptions-item>
            <template slot="label">
              <i class="el-icon-s-order"></i>
              订单编号
            </template>
            {{ paymentOrderInfo.orderNumber }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label">
              <i class="el-icon-s-release"></i>
              订单类型
            </template>
            {{ paymentOrderInfo.payTypeName }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
      <div class="submit">
        <div
          v-if="
            paymentOrderInfo.paymentStatus === 0 ||
            paymentOrderInfo.paymentStatus === 1
          "
        >
          <button class="publishBtn" type="submit" @click.prevent="pay()">
            <span class="btn-content">确认支付</span>
          </button>
          <button class="clearBtn" @click="$router.push('/')">
            <span class="btn-content">取消</span>
          </button>
        </div>
        <div v-else>
          <button
            class="publishBtn"
            type="submit"
            @click="$router.push('/user')"
          >
            <span class="btn-content">返回</span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import API from "@/utils/AliPay.js";
export default {
  data() {
    return {
      paymentOrderInfo: {},
      paymentPayId: "",
    };
  },
  created() {
    this.getPaymentOrder();
  },
  methods: {
    pay() {
      this.$api.paymentPay.payOrder(this.paymentPayId).then((res) => {
        this.paymentOrderInfo.paymentStatus = 9;
      });
      API({
        url: "/pay",
        method: "get",
      }).then((res) => {
        const div = document.createElement("div");
        div.innerHTML = res.data; // data就是接口返回的form 表单字符串
        document.body.appendChild(div);
        // 获取新添加的表单
        const form = div.querySelector("form");
        if (form) {
          form.setAttribute("target", "_blank"); // 新窗口打开
          form.submit();
        }
      });
    },
    getPaymentOrder() {
      this.$api.paymentOrder
        .getPaymentOrder(this.$route.query.paymentOrderId)
        .then((res) => {
          this.paymentOrderInfo = res.result;
          this.paymentPayId = res.result.paymentPayId;
          if (this.paymentOrderInfo.paymentStatus === 0) {
            this.createPaymentPay();
          }
        });
    },
    createPaymentPay() {
      this.$api.paymentPay
        .createPayOrder({ paymentOrderId: this.$route.query.paymentOrderId })
        .then((res) => {
          this.paymentPayId = res.result;
        });
    },
  },
};
</script>
<style lang="less" scoped>
.container {
  flex: 1;
  padding-top: 72px;

  .push-container {
    margin-left: 12vw;
    width: 600px;
    border-radius: 8px;
    box-sizing: border-box;
    box-shadow: var(--el-box-shadow-lighter);

    .header {
      padding: 15px 0;
      line-height: 16px;
      font-size: 16px;
      font-weight: 400;

      .header-icon {
        position: relative;
        top: 2px;
        display: inline-block;
        width: 6px;
        height: 19px;
        background: #3a64ff;
        border-radius: 3px;
        margin-right: 2px;
      }

      .header-title {
        line-height: 20px;
        font-size: 20px;
      }
    }

    .img-list {
      width: 576px;
      margin-left: 20px;
      padding: 0;
      height: 200px;
      background-color: #3a64ff;
      border-radius: 10px;
      display: flex;
      align-items: center;
      justify-content: center;
      flex-direction: column;
    }

    .push-content {
      position: relative;

      .scroll-tag-container {
        position: absolute;
        width: 98%;
        background-color: #fff;
        z-index: 99999;
        border: 1px solid #f4f4f4;
        height: 300px;
        overflow: auto;

        .scrollbar-tag-item {
          display: flex;
          align-items: center;
          height: 30px;
          margin: 10px;
          text-align: center;
          border-radius: 4px;
          padding-left: 2px;
          color: #484848;
          font-size: 14px;
        }

        .scrollbar-tag-item:hover {
          background-color: #f8f8f8;
        }
      }

      .input-title {
        margin-bottom: 5px;
        font-size: 12px;
      }

      .input-content {
        font-size: 12px;
      }
    }

    .btns {
      padding: 0 12px 0px 20px;

      button {
        min-width: 62px;
        width: 62px;
        margin: 0 6px 0 0;
        height: 18px;
      }

      .css-fm44j {
        -webkit-font-smoothing: antialiased;
        appearance: none;
        font-family: RedNum, RedZh, RedEn, -apple-system;
        vertical-align: middle;
        text-decoration: none;
        border: 1px solid rgb(217, 217, 217);
        outline: none;
        user-select: none;
        cursor: pointer;
        display: inline-flex;
        -webkit-box-pack: center;
        justify-content: center;
        -webkit-box-align: center;
        align-items: center;
        margin-right: 16px;
        border-radius: 4px;
        background-color: white;
        color: rgb(38, 38, 38);
        height: 24px;
        font-size: 12px;
      }
    }

    .categorys {
      padding: 0 12px 10px 12px;
    }

    .trend-container {
      padding-left: 15px;

      .trend-item {
        display: flex;
        flex-direction: row;
        padding-top: 10px;
        max-width: 100vw;

        .main {
          flex-grow: 1;
          flex-shrink: 1;
          display: flex;
          flex-direction: row;
          padding-bottom: 12px;
          border-bottom: 1px solid rgba(0, 0, 0, 0.08);

          .details-box {
            width: 25%;
          }

          .info {
            flex-grow: 1;
            flex-shrink: 1;
            margin-left: 10px;

            .user-info {
              display: flex;
              flex-direction: row;
              align-items: center;
              font-size: 16px;
              font-weight: 600;
              margin-bottom: 4px;

              a {
                color: #333;
              }
            }

            .interaction-hint {
              font-size: 14px;
              color: rgba(51, 51, 51, 0.6);
              margin-bottom: 8px;
            }

            .interaction-content {
              display: flex;
              font-size: 14px;
              color: #333;
              margin-bottom: 12px;
              line-height: 140%;

              display: -webkit-box;
              -webkit-box-orient: vertical;
              -webkit-line-clamp: 3;
              word-break: break-all;
              overflow: hidden;
            }

            .interaction-price {
              font-weight: bolder;
              font-size: 16px;
              color: red;
            }

            .interaction-footer {
              margin: 8px 12px 0 0;
              padding: 0 12px;
              display: flex;
              justify-content: space-between;
              align-items: center;

              .icon-item {
                display: flex;
                justify-content: left;
                align-items: center;
                color: rgba(51, 51, 51, 0.929);

                .count {
                  margin-left: 3px;
                }
              }
            }
          }
        }
      }
    }

    .submit {
      padding: 0 12px 10px 20px;
      margin-top: 30px;

      button {
        width: 80px;
        height: 32px;
        font-size: 14px;
      }

      .publishBtn {
        background-color: #ff2442;
        color: #fff;
        border-radius: 4px;
        cursor: pointer;
      }

      .clearBtn {
        cursor: pointer;
        border-radius: 4px;
        margin-left: 10px;
        border: 1px solid rgb(217, 217, 217);
      }
    }
  }
}
</style>
