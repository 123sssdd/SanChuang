<template>
  <div class="container">
    <div class="push-container">
      <el-form label-position="left" class="check-form" :model="formData" :rules="rules" ref="form" label-width="80px" :show-message="false" status-icon :hide-required-asterisk="true" size="small">
        <div class="header"><span class="header-icon"></span><span class="header-title">订单评价</span></div>
        <div class="header-title" style="margin-top: 10px;margin-bottom: 15px;font-size: 18px;padding-left: 20px"> 商品信息</div>
        <div class="trend-container">
          <div class="trend-item">
            <div class="main">
              <div class="details-box">
                <el-image style="border-radius: 10px;" :src="productInfo.image" fit="cover"></el-image>
              </div>
              <div class="info">
                <div>
                  <div class="user-info">
                    <a>{{ productInfo.productTitle }}</a>
                  </div>
                  <!--                <div class="interaction-hint"><span>1天前</span></div>-->
                  <div class="interaction-content">{{ productInfo.productInfo }}</div>
                </div>
                <div class="interaction-price">￥{{ $utils.convert.to_price(productInfo.buyMoneyAll) }}</div>
              </div>
            </div>
          </div>
        </div>
        <div class="header-title" style="margin-top: 15px;margin-bottom: 15px;font-size: 18px;padding-left: 20px"> 完成评价</div>
        <div class="trend-container">
          <div class="trend-item">
            <div class="main">
              <div class="info">
                <div>
                  <div class="user-info">
                    <el-rate v-model="formData.score" :texts="['极差', '失望', '一般', '满意', '极好']" show-text></el-rate>
                  </div>
                  <div class="interaction-content" style="margin-top: 25px">
                    <el-input type="textarea" placeholder="你的评价可以帮到其他买家哦!" maxlength="150" :autosize="{minRows: 5}" show-word-limit v-model="formData.content"></el-input>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="submit">
          <button class="publishBtn" type="submit" @click.prevent="evaluate()">
            <span class="btn-content">完成评价</span>
          </button>
          <button class="clearBtn" @click="$router.push('/user')">
            <span class="btn-content">取消</span>
          </button>
        </div>
      </el-form>
    </div>
  </div>
</template>
<script>
import gdMapUtil from "@/utils/gdMapUtil";
import Address_edit from "@/pages/publish/address_edit.vue";
import {Notification} from "element-ui";

export default {
  data() {
    return {
      productInfo: {
        image: ''
      },
      formData: {
        id:'',
        score:'',
        content:'',
      },
    }
  },
  created() {
    this.formData.id = this.$route.query.orderId
    this.getInfo()
  },
  methods: {
    getInfo() {
      this.$api.productOrder.getProductOrderInfo(this.formData.id).then(res=>{
        this.productInfo = res.result
        this.productInfo.image = JSON.parse(res.result.productImg)[0]
      })
    },
    evaluate(){
      this.$api.productOrder.evaluateOrder(this.formData).then(res=>{
        this.$router.push("/orderDetail?orderId="+ this.$route.query.orderId)
        Notification({type: 'success', title: '原创力交易平台', message: '评价成功'})
      })
    },
  },
  computed: {
    rules() {
      return {
        score: [{required: true, message: '请输入分数~', trigger: 'blur'}],
        content: [{required: true, message: '请输入评价内容~', trigger: 'blur'}],
      }
    }
  }
}
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
      width: 540px;
      margin-left: 20px;
      padding: 0;
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
            display: flex;
            flex-direction: column;
            justify-content: space-between;

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
      margin-top: 20px;

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
