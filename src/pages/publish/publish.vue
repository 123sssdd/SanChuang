<template>
  <div class="container">
    <div class="push-container">
      <el-form label-position="left" class="check-form" :model="formData" :rules="rules" ref="form" label-width="80px" :show-message="false" status-icon :hide-required-asterisk="true" size="mini">
        <div class="header"><span class="header-icon"></span><span class="header-title">发布作品</span></div>
        <div class="img-list">
          <div class="header-title" style="margin-top: 10px;margin-bottom: 15px;font-size: 18px">图片编辑</div>

          <el-upload
              v-model:file-list="fileList"
              action="http://124.222.121.87:5049/upload/image"
              list-type="picture-card"
              :on-preview="handlePreview"
              :on-remove="handleRemove"
              :on-success="handleUploadSuccess"
          >
            <div style="line-height: 80px;font-size: 12px;color: #9999b3">+添加优质图</div>
          </el-upload>

          <el-dialog :visible.sync="dialog.visible">
            <img width="100%" :src="dialog.imageUrl" alt="预览图像">
          </el-dialog>
        </div>
        <div class="push-content">
          <el-form-item label-width="0" prop="title">
            <el-input
                style="margin-left: 15px;"
                v-model="formData.title"
                maxlength="100"
                show-word-limit
                type="text"
                placeholder="填写作品标题，可能会更容易卖出哦~"
                class="input-title"
            />
          </el-form-item>
          <el-form-item label-width="0" prop="intro">
            <el-input
                style="margin-left: 15px;"
                v-model="formData.intro"
                :rows="5"
                maxlength="500"
                show-word-limit
                type="textarea"
                placeholder="描述下作品的品牌型号、货品来源..."
                class="input-content"
            />
          </el-form-item>
        </div>
        <div class="btns">
          <el-form-item label="作品分类" prop="type" style="width:200px ">
            <el-select v-model="formData.type" placeholder="选择作品分类" >
              <el-option v-for="(item,index) in menuList" :key="item.id" :label="item.typeName" :value="item.typeCode"></el-option>
            </el-select>
          </el-form-item>
        </div>
        <div style="margin-left: 20px">
          <el-divider style="width: 576px"/>
        </div>
        <div>
          <div class="header-title" style="margin-top: 10px;margin-bottom: 15px;font-size: 18px;padding-left: 20px"> 发布设置</div>
          <div style="padding-left: 20px">
            <el-form-item label="所处位置">
              <div  style="cursor: pointer;margin-left:0;border-radius: 7px;color: #857679;" @click="selectAdd">  <i class="el-icon-location-information"></i> {{ formData.province + " " + formData.city }} </div>
            </el-form-item>
            <el-form-item label="发货方式" prop="postType">
              <el-radio-group v-model="formData.postType" required>
                <el-radio label="0">电子邮箱</el-radio>
                <el-radio label="1">网盘链接</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="价格">
              <el-input-number required controls-position="right" v-model="formData.price" :precision="2" :step="1" :min="0"></el-input-number>
            </el-form-item>
            <el-form-item label="原价">
              <el-input-number required controls-position="right" v-model="formData.originalPrice" :precision="2" :step="1" :min="0"></el-input-number>
            </el-form-item>

          </div>
        </div>
        <div class="submit">
          <button class="publishBtn" type="submit" @click.prevent="publish()">
            <span class="btn-content">发布</span>
          </button>
          <button class="clearBtn" @click="$router.push('/')">
            <span class="btn-content">取消</span>
          </button>
        </div>
      </el-form>
    </div>

    <!--抽屉 地址编辑-->
    <!-- <div>
      <el-drawer @close-drawer="closeDrawer" :show-close="false" :visible.sync="drawer" direction="rtl">
        <address_edit @close-drawer="closeDrawer"></address_edit>
      </el-drawer>
    </div> -->
  </div>
</template>
<script>
import gdMapUtil from "@/utils/gdMapUtil";
import Address_edit from "@/pages/publish/address_edit.vue";
import {Notification} from "element-ui";

export default {
  components: {Address_edit},
  data() {
    return {
      drawer: false,
      dialog: {
        visible: false,
        imageUrl: '',
      },
      location: "",
      fileList: [],
      menuList:[],
      formData: {
        id:'',
        type:'',
        title: '',
        intro: '',
        image: '',
        price: '',
        originalPrice: '',
        postType: '1',
        province: '',
        city: '',
        adcode: '',
      },

    }
  },
  created() {
    this.getCity()
    this.getMenuList()
  },
  methods: {
    publish() {
      //图片处理
      if (this.fileList.length > 0) {
        let _fileList = this.fileList.map(file => file.response.result.url);
        this.formData.image = JSON.stringify(_fileList)
      }
      this.$refs.form.validate(valid => {
        if (valid) {
          this.$api.product.createProductInfo(this.formData).then(res => {
            this.$router.push("/user")
            Notification({type: 'success', title: '原创力平台', message: '请等待审核通过'})
          })
        }
      })
    },
    getMenuList() {
      this.$api.productType.getTypeList().then(res => {
        this.menuList = res.result
      })
    },
    selectAdd() {
      this.drawer = true;
    },
    getCity() {

      gdMapUtil.getCity().then(res=>{
        this.formData.province = res.province
        this.formData.city = res.city
        this.formData.adcode = res.adcode
      });
    },

    handlePreview(file) {
      this.dialog.imageUrl = file.url;
      this.dialog.visible = true;
    },
    handleRemove(file, fileList) {
      this.$api.image.removeImage(file.response.result.name).then(res => {
        this.fileList = fileList
      })
    },
    handleUploadSuccess(response, file, fileList) {
      this.fileList = fileList
    },
    closeDrawer(changeAddress){
      if(changeAddress){
        gdMapUtil.GeocoderGetAddress(changeAddress.location.lng, changeAddress.location.lat).then(res=>{
          this.formData.adcode = res.regeocode.addressComponent.adcode
          this.formData.province = res.regeocode.addressComponent.province
          this.formData.city = res.regeocode.addressComponent.city
          this.formData.district = res.regeocode.addressComponent.district
        })
      }
    },
  },
  computed: {
    rules() {
      return {
        postType: [{required: true, message: '请选择发货方式~', trigger: 'blur'}],
        intro: [{required: true, message: '有作品描述才能发布哦~', trigger: 'blur'}],
        title: [{required: true, message: '有作品标题才能发布哦~', trigger: 'blur'}],
        type: [{required: true, message: '选择作品分类才能发布哦~', trigger: 'blur'}],
      }
    }
  }
}
</script>
<style lang="less" scoped>
/deep/ .push-content .el-input--mini .el-input__inner{
  height: 40px;
}
.check-form .el-form-item__content {
  margin-left: 0 !important;
}
/deep/ .el-drawer__header {
  display: none;
}
/deep/ .el-upload-list--picture-card .el-upload-list__item {
  width: 80px;
  height: 80px;
}
/deep/ .el-upload-list__item.is-success .el-upload-list__item-status-label {
  display: none;
}

/deep/ .el-upload--picture-card {
  width: 80px;
  height: 80px;
}

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
      padding: 10px 6px 10px 6px;
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

    .submit {
      padding: 0 12px 10px 20px;
      margin-top: 10px;

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
