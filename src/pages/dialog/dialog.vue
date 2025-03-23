<template>
  <el-dialog
    title="输入作品信息"
    :visible.sync="dialogVisible"
    @close="resetInput"
    width="400px"
    class="custom-dialog"
  >
    <el-form :model="form">
      <el-form-item label="作品名称">
        <el-input v-model="form.workTitle" placeholder="请输入作品名称" />
      </el-form-item>
      <el-form-item label="创作者名称">
        <el-input v-model="form.creatorName" placeholder="请输入创作者名称" />
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" @click="startBlockchainProcess(productId)"
        >开始上链</el-button
      >
    </span>
  </el-dialog>
</template>

<script>
import pro from "@/utils/pro";

export default {
  data() {
    return {
      productId: this.$route.query.id, // 声明 productId 并从路由中获取
      dialogVisible: true, // 默认弹出框显示
      form: {
        workTitle: "", // 作品名称
        creatorName: "", // 创作者名称
      },
    };
  },
  methods: {
    resetInput() {
      // 重置表单数据
      this.form.workTitle = "";
      this.form.creatorName = "";
    },

    startBlockchainProcess(productId) {
      // 验证输入
      if (this.form.workTitle && this.form.creatorName) {
        // 跳转到 /block 页面，并将输入的数据作为 query 传递
        this.$router.push({
          path: "/block",
          query: {
            workTitle: this.form.workTitle,
            creatorName: this.form.creatorName,
            productId: productId,
          },
        });
        this.dialogVisible = false; // 关闭弹出框
      } else {
        this.$message.error("请输入完整的作品信息");
      }
    },
  },
};
</script>

<style scoped>
/* 自定义弹出框样式 */
.custom-dialog .el-dialog {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 9999;
  width: 500px;
  background-color: yellow;
  border-radius: 10px;
  padding: 20px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

/* 自定义遮罩层 */
.custom-dialog .el-dialog__wrapper {
  z-index: 9998;
  background-color: rgba(0, 0, 0, 0.5); /* 半透明背景 */
}

/* 自定义按钮位置 */
.dialog-footer {
  display: flex;
  justify-content: space-between;
}
</style>
