<template>
  <div>
    <h1>作品上链模拟</h1>

    <!-- 上链进度条 -->
    <div v-if="isProcessing" class="loader">
      <div class="loading-text">{{ progressMessage }}</div>
      <div class="loading-bar-background">
        <div class="loading-bar" :style="{ width: progress + '%' }">
          <div class="white-bars-container">
            <div class="white-bar"></div>
            <div class="white-bar"></div>
            <div class="white-bar"></div>
          </div>
        </div>
      </div>
      <div class="dots">
        <span class="dot"></span>
        <span class="dot"></span>
        <span class="dot"></span>
      </div>
    </div>

    <div v-if="isSuccess" class="certificate-container">
      <div class="certificate">
        <h2 class="certificate-title">原创力平台区块链存证证书</h2>
        <p style="margin-top: 30px">
          <strong>证书编号:</strong>{{ certificateId }}
          <button @click="copyid1" class="copy-button">复制证书编号</button>
        </p>
        <p>
          <strong
            >本证书由天平链区块链系统签发，证明文件（电子数据)自申请时间起已经存在目内
            容保持完整，未被篡改。</strong
          >
        </p>

        <p>
          <strong>区块链存证编号：</strong
          >{{ blockChainId }}
          <button @click="copyid2" class="copy-button2">复制区块链存证编号</button>
        </p>        
        <p>
          <strong>区块哈希值：</strong>
          <span
            >{{ blockHash }}</span
          >
          <button @click="copyHash" class="copy-button">复制哈希值</button>
        </p>
        <p><strong>作品名称：</strong>{{ workTitle }}</p>
        <p><strong>创作者：</strong>{{ creatorName }}</p>
        <p><strong>存证时间：</strong>{{ timestamp }}</p>

        <p><strong>存证方式：</strong>图片</p>
        <p><strong>文件格式：</strong>png</p>        
        <p>
          <strong style="font-size: 30px">虚拟签名：</strong>
          <div class="signature-box"></div>
        </p>
        <p >
          <strong
            >本证书可登陆杭州互联网法院平台官网www.netcourt.gov.cn查询</strong
          >
        </p>
        <div class="stamp"></div>

        <button @click="downloadCertificate()" class="download-button">
          下载证书
        </button>
        <button @click="changeBlock(productId)" class="return">
          返回作品界面
        </button>

      </div>
    </div>
  </div>
</template>

<script>
import pro from "@/utils/pro";
import html2canvas from "html2canvas";

export default {
  data() {
    return {
      isProcessing: false,
      progress: 0,
      progressMessage: "正在上链...",
      isSuccess: false,
      blockHash: "",
      workTitle: this.$route.query.workTitle || "",
      creatorName: this.$route.query.creatorName || "",
      timestamp: new Date().toLocaleString(),
      productId: this.$route.query.productId || "", // 声明 productId 并从路由中获取
      certificateId: "",
      blockChainId: '',
    };
  },
  methods: {
    // 生成证书ID的函数
    generateCertificateId() {
      // 生成4个随机大写字母
      const letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
      let randomLetters = "";
      for (let i = 0; i < 4; i++) {
        randomLetters += letters.charAt(
          Math.floor(Math.random() * letters.length)
        );
      }

      // 获取当前日期，格式为YYYYMMDD
      const now = new Date();
      const year = now.getFullYear();
      const month = String(now.getMonth() + 1).padStart(2, "0");
      const day = String(now.getDate()).padStart(2, "0");
      const dateString = `${year}${month}${day}`;

      // 生成6位随机数字
      const randomDigits = Math.floor(100000 + Math.random() * 900000);

      // 拼接成完整的证书ID
      return `${randomLetters}-${dateString}-${randomDigits}`;
    },
    downloadCertificate() {
      // 显示加载提示
      this.$message.info("正在生成证书，请稍候...");

      // 获取证书元素
      const certificateElement = document.querySelector(".certificate-container");



      // 隐藏按钮 (保留原有代码)
      const buttons = certificateElement.querySelectorAll("button");
      const buttonStates = [];
      buttons.forEach((button) => {
        buttonStates.push(button.style.display);
        button.style.display = "none";
      });

      // 临时移除缩放
      const originalTransform = certificateElement.style.transform;
      certificateElement.style.transform = "none";

      // 其他配置保持不变...
      const options = {
        scale: 2,
        useCORS: true,
        allowTaint: true,
        backgroundColor: null,
        logging: false,
      };

      // 执行截图
      html2canvas(certificateElement, options)
        .then((canvas) => {
          const imgURL = canvas.toDataURL("image/png", 1.0);
          const link = document.createElement("a");
          link.href = imgURL;
          link.download = `证书_${this.workTitle}_${new Date().getTime()}.png`;
          link.click();
          this.$message.success("证书已成功下载");
        })
        .catch((error) => {
          console.error("截图失败:", error);
          this.$message.error("证书生成失败，请重试");
        })
        .finally(() => {

          // 恢复缩放和按钮
          certificateElement.style.transform = originalTransform;
          buttons.forEach((button, index) => {
            button.style.display = buttonStates[index];
          });
        });
    },

    startBlockchainProcess() {
      this.isProcessing = true;
      this.progress = 0;
      this.progressMessage = "正在上链...";
      this.isSuccess = false;

      // 进度条逐步增加并更改文本
      const interval = setInterval(() => {
        if (this.progress < 80) {
          this.progress += 10;
          this.updateProgressMessage(this.progress);
        } else if (this.progress >= 80 && this.progress < 100) {
          this.progress += 5;
          this.updateProgressMessage(this.progress);
        } else {
          clearInterval(interval);
          this.generateBlockHash();
        }
      }, 1300); // 每 0.4 秒增加 10%
    },

    updateProgressMessage(progressValue) {
      if (progressValue < 10) {
        this.progressMessage = "上传文件到时间戳服务...";
      } else if (progressValue < 20) {
        this.progressMessage = "生成时间戳证书...";
      } else if (progressValue < 30) {
        this.progressMessage = "等待区块链确认...";
      } else if (progressValue < 40) {
        this.progressMessage = "计算文件哈希值...";
      } else if (progressValue < 50) {
        this.progressMessage = "保存文件哈希值...";
      } else if (progressValue < 60) {
        this.progressMessage = "创建智能合约...";
      } else if (progressValue < 70) {
        this.progressMessage = "上传哈希到智能合约...";
      } else if (progressValue < 80) {
        this.progressMessage = "记录作品信息...";
      } else if (progressValue < 85) {
        this.progressMessage = "生成验证报告...";
      } else if (progressValue < 90) {
        this.progressMessage = "正在进行验证...";
      } else if (progressValue < 95) {
        this.progressMessage = "注册作品版权...";
      } else if (progressValue < 100) {
        this.progressMessage = "管理版权...";
      } else if (progressValue === 100) {
        this.progressMessage = "作品上链成功！";
      }
    },

    generateBlockHash() {
      // 获取当前时间戳，确保哈希的唯一性
      const timestamp = new Date().getTime();

      // 使用 Math.random 生成更长的随机字符串，并使用 slice() 提取
      const randomString = Math.random().toString(36).slice(2, 66); // 生成64位的随机字符串

      // 组合时间戳和随机字符串来生成伪区块链哈希
      this.blockHash = `0x${timestamp.toString(
        16
      )}${randomString}`.toUpperCase(); // 转换为大写

      this.isSuccess = true;

      // 弹出成功提示框
      this.$message.success("作品已成功上链！");
    },

    generateBlockChainId() {
    // 创建一个包含所有可能的十六进制字符的数组
    const hexChars = '0123456789abcdef';
    let result = '';
    
    // 生成64位十六进制字符
    for (let i = 0; i < 64; i++) {
      result += hexChars.charAt(Math.floor(Math.random() * hexChars.length));
    }
    
    // 加入区块链特征前缀
    return `0x${result}`;
  },
    copyHash() {
      // 将区块哈希值存到剪贴板
      navigator.clipboard
        .writeText(this.blockHash)
        .then(() => {
          // 提示成功复制
          this.$message.success("哈希值已存到剪贴板");
        })
        .catch((err) => {
          // 错误处理
          this.$message.error("复制哈希值失败");
        });
    },

    copyid1() {
      // 将证书ID存到剪贴板
      navigator.clipboard
        .writeText(this.certificateId)
        .then(() => {
          // 提示成功复制
          this.$message.success("证书编号已存到剪贴板");
        })
        .catch((err) => {
          // 错误处理
          this.$message.error("复制证书编号失败");
        });
    },

    copyid2() {
      // 将证书ID存到剪贴板
      navigator.clipboard
        .writeText(this.blockChainId)
        .then(() => {
          // 提示成功复制
          this.$message.success("证书编号已存到剪贴板");
        })
        .catch((err) => {
          // 错误处理
          this.$message.error("复制证书编号失败");
        })
    },

    

    changeBlock(productId) {
      // 往 changeBlockStatus(productId);发请求，成功后，跳转到/main，并携带参数productId
      this.$api.product.changeBlockStatus(productId).then((res) => {
        this.$router.push({ path: "/dashboard" }); // 跳转到/main页面，并携带productId参数
      });
    },
  },

  mounted() {
    // 在组件加载时开始上链过程
    this.startBlockchainProcess();
    this.certificateId = this.generateCertificateId(); // 设置证书ID
    // 生成区块链存证编号
    this.blockChainId = this.generateBlockChainId();
  },
};
</script>

<style scoped>

.stamp {
  position: absolute;
  bottom: 160px;        /* 距离底部距离 */
  left:1200px;         /* 距离右侧距离 */
  width: 247px;         /* 印章宽度 */
  height: 252px;        /* 印章高度 */
  background-image: url("@/assets/stamp.png");  /* 印章图片路径 */
  background-size: contain;  /* 保持图片比例 */
  background-repeat: no-repeat;
  background-position: center;
  opacity: 0.9;         /* 设置透明度 */
  z-index: 10;          /* 确保印章显示在上层 */
  transform: rotate(-15deg);  /* 可选：稍微旋转一个角度 */
}


.certificate-container {
  display: flex;
  justify-content: center;
  margin-top: -140px;
  padding: 0px;
  /* 插入手画的边框图像 */
  background-image: url("/src/assets/123.png");
  background-size: 1400px 1500px;
  background-repeat: no-repeat; /* 防止图像重复 */
  background-position: center;
  transform: scale(0.75); /* 缩小证书 */
}

.certificate {
  width: 1900px; /* 你可以调整证书宽度 */
  height: 1500px;
  padding: 20px;
  position: relative;
  right:50px;
  transform: scale(0.7); /* 缩小证书 */

}

.certificate-title {
  color: #e5bc52;
  text-align: center;
  font-size: 65px;
  font-weight: bold;
  font-family: "Times New Roman", Times, serif;
  margin-top: 30px;
  margin-bottom: 30px;
  margin-left: 190px;
}

.certificate p {
  font-size: 35px;
  margin: 40px 60px;
  color: #f8f7f5;
}

.certificate p strong {
  font-size: 35px;
  margin: 30px 0;
  color: #e5bc52;
  letter-spacing: 5px;
}

.download-button {
  margin-left: 55px;
  padding: 15px 20px;
  font-size: 25px;
  font-weight:bolder;
  background-color: #FFFFFF;
  color: #091327;
  border: 9px solid #e5bc52;
  border-radius: 5px;
  cursor: pointer;
}

.return {
  margin-left: 60px;
  margin-top:10px;
  padding: 15px 20px;
  font-weight: bolder;
  background-color: #FFFFFF;
  font-size: 25px;
  color: #091327;
  border: 9px solid #e5bc52;
  border-radius: 5px;
  cursor: pointer;
}

.return:hover {
  background-color: #f9e4b0;
}


.download-button:hover {
  background-color: #f9e4b0;
}


.signature-box {
  width: 300px; /* 设置盒子的宽度 */
  height: 150px; /* 设置盒子的高度 */
  background-color: #f8f7f5;
  position: relative;
  left:61px;
  border: 8px solid #e5bc52;
  border-radius: 8px;
  background-image: url("@/assets/name.png"); /* 设置图片为背景 */
  background-size: cover; /* 确保图片按比例缩放 */
  background-repeat: no-repeat; /* 防止背景图像重复 */
}

.copy-button {
  margin-left: 40px;
  padding: 5px 10px;
  font-weight: bolder;
  background-color: #FFFFFF;
  font-size: 25px;
  color: #091327;
  border: 9px solid #e5bc52;
  border-radius: 5px;
  cursor: pointer;
}

.copy-button2 {
  margin-left: 0cqb;
  margin-top: 30px;
  padding: 5px 10px;
  font-weight: bolder;
  background-color: #FFFFFF;
  font-size: 25px;
  color: #091327;
  border: 9px solid #e5bc52;
  border-radius: 5px;
  cursor: pointer;
}


.copy-button:hover {
  background-color: #f9e4b0;
}

.copy-button2:hover {
  background-color: #f9e4b0;
}



.loader {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  gap: 5px;
}

.loading-text {
  color: rgb(31, 26, 26);
  font-size: 30px;
  font-weight: 600;
  margin-left: 10px;
}

.dot {
  margin-left: 3px;
  animation: blink 1.5s infinite;
}
.dot:nth-child(2) {
  animation-delay: 0.3s;
}

.dot:nth-child(3) {
  animation-delay: 0.6s;
}

.loading-bar-background {
  --height: 30px;
  display: flex;
  align-items: center;
  box-sizing: border-box;
  padding: 5px;
  width: 1000px;
  height: var(--height);
  background-color: #070707; /* background color */
  box-shadow: #302f2f -2px 2px 4px 0px inset;
  border-radius: calc(var(--height) / 2);
}

.loading-bar {
  position: relative;
  display: flex;
  justify-content: center;
  flex-direction: column;
  --height: 20px;
  width: 0%;
  height: var(--height);
  overflow: hidden;
  background: rgb(19, 229, 244);
  background: linear-gradient(
    0deg,
    rgb(15, 167, 222) 0%,
    rgb(79, 249, 150) 100%
  );
  border-radius: calc(var(--height) / 2);
  animation: loading 21s ease-out forwards; /* 只播放一次 */
}

.white-bars-container {
  position: absolute;
  display: flex;
  align-items: center;
  gap: 18px;
}

.white-bar {
  background: rgb(255, 255, 255);
  background: linear-gradient(
    -45deg,
    rgba(255, 255, 255, 1) 0%,
    rgba(255, 255, 255, 0) 70%
  );
  width: 10px;
  height: 45px;
  opacity: 0.3;
  transform: rotate(45deg);
}

@keyframes loading {
  0% {
    width: 0;
  }
  80% {
    width: 100%;
  }
  100% {
    width: 100%;
  }
}

@keyframes blink {
  0%,
  100% {
    opacity: 0;
  }
  50% {
    opacity: 1;
  }
}
</style>
