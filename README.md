# 原央创设 - 数字智护创意设计交易平台

<div align="center">

![项目状态](https://img.shields.io/badge/status-active-brightgreen)
![Vue版本](https://img.shields.io/badge/Vue-2.6.14-green)
![Element UI](https://img.shields.io/badge/Element_UI-2.15.14-blue)
![许可证](https://img.shields.io/badge/license-MIT-blue)

**🏆 全国大学生电子商务"创新、创意及创业"挑战赛（2025）省金奖项目**

*设计交易 | 资源共享 | 版权保护 | 社群互动*

</div>

## 📋 项目简介

原央创设是一个基于区块链技术的数字智护创意设计交易平台，致力于为设计师提供"纯粹、交汇、可靠"的支持与服务。平台聚焦解决设计师版权易遭盗用的困境，打造了坚不可摧的版权保护体系。

### 🎯 首要目标
针对设计师版权易遭盗用的困境，打造坚不可摧的版权保护体系。

### 🎨 应用场景
- 平面设计
- 产品设计
- 动画设计  
- 数字游戏等设计行业

### ⭐ 核心特色
- **智能匹配机制** - 实现供需精准对接
- **区块链技术** - 赋能版权保护
- **AI多模态智能问答** - DeepSeek R1 驱动
- **实时通信** - WebSocket即时聊天系统

## 🚀 技术栈

### 前端技术
- **框架**: Vue.js 2.6.14
- **UI组件库**: Element UI 2.15.14
- **状态管理**: Vuex 3.6.2
- **路由管理**: Vue Router 2.8.0
- **动画效果**: Animate.css 4.1.1
- **瀑布流布局**: Vue Waterfall 1.0.6

### 开发工具与构建
- **构建工具**: Vue CLI 5.0.0
- **CSS预处理器**: Less 3.5.0
- **代码编译**: Babel
- **模板编译**: Vue Template Compiler

### 网络通信
- **HTTP客户端**: Axios 1.6.7
- **实时通信**: WebSocket
- **身份认证**: JWT Token + js-cookie

### 地图服务
- **地图API**: 高德地图 (@amap/amap-jsapi-loader 1.0.1)

### 其他功能库
- **Canvas截图**: html2canvas 1.4.1
- **Cookie管理**: js-cookie 2.2.0

## 🌟 核心功能

### 🏠 首页 - 免登录浏览
- 无需注册/登录即可查看核心内容
- 自由滑动、缩放作品细节
- 快速定位兴趣领域
- 轻量级"即用即走"体验

### 🔍 发现 - 每日原创设计精选
- 个性化设计作品推荐
- 涵盖工业、UI、插画等多领域
- 设计风格解读与创作故事
- 区块链存证标识展示
- 灵感专辑创建与管理

### 📈 动态 - 创作里程碑记录
- 实时记录作品完整轨迹
- 时间轴展示创作历程
- 平台审核机制保障质量
- 交易数据可视化呈现

### 💬 消息 - 作品查看动态
- 实时追踪作品浏览情况
- 即时推送互动通知
- 识别潜在客户
- 量化价值产出分析

### 📝 发布 - 区块链版权保护
- 作品信息加密上链
- 生成唯一数字指纹
- 不可篡改的"出生证明"
- 司法场景电子证据支持

### 👤 个人中心 - 创作价值中枢
- 专属创作管理中心
- 全生命周期数据掌控
- 收入统计与订单管理
- 版权状态透明化

### 🤖 AI智能助手 - DeepSeek R1多模态
- 文本/图像/语音输入支持
- 专业建议与灵感激发
- 方案优化与知识检索
- 基于用户行为定制内容

## 💻 技术环境

### 开发环境要求
- **Node.js**: >= 14.0.0
- **npm**: >= 6.0.0 或 **yarn**: >= 1.0.0
- **Vue CLI**: >= 5.0.0

### 浏览器支持
- Chrome >= 88
- Firefox >= 85
- Safari >= 14
- Edge >= 88

### 后端服务依赖
- **API服务器**: 需要配套的后端API服务
- **WebSocket服务**: 实时通信功能
- **区块链节点**: 版权存证功能
- **文件存储**: 作品图片存储服务

## 🛠️ 安装与运行

### 1. 克隆项目
```bash
git clone https://github.com/123sssdd/SanChuang.git
cd SanChuang
```

### 2. 安装依赖
```bash
npm install
# 或
yarn install
```

### 3. 环境配置
创建 `.env.local` 文件：
```env
VUE_APP_BASE_URL=http://your-api-server:port
VUE_APP_WEBSOCKET_URL=ws://your-websocket-server:port
```

### 4. 启动开发服务器
```bash
npm run serve
# 或
yarn serve
```

### 5. 构建生产版本
```bash
npm run build
# 或
yarn build
```

## 📁 项目结构

```
SanChuang/
├── public/                 # 静态资源
├── src/
│   ├── api/               # API接口模块
│   ├── components/        # 可复用组件
│   ├── pages/            # 页面组件
│   ├── router/           # 路由配置
│   ├── store/            # Vuex状态管理
│   ├── utils/            # 工具函数
│   ├── App.vue           # 根组件
│   ├── main.js           # 入口文件
│   └── style.css         # 全局样式
├── package.json          # 项目配置
└── README.md            # 项目说明
```

## 🔧 主要配置

### API接口模块
项目采用模块化API设计，包含以下模块：
- `user` - 用户管理
- `product` - 作品管理
- `comment` - 评论系统
- `chatList` - 聊天列表
- `chatMessage` - 聊天消息
- `productOrder` - 订单管理
- `paymentPay` - 支付功能

### WebSocket实时通信
- 支持聊天室功能
- 自动重连机制
- 心跳检测保持连接

### 状态管理
使用Vuex进行全局状态管理：
- 用户信息存储
- 登录状态管理
- 组件间数据共享

## 🤝 贡献指南

欢迎参与项目贡献！请遵循以下步骤：

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 👥 开发团队

- **项目负责人**: [@123sssdd](https://github.com/123sssdd)
- **技术栈**: Vue.js + Element UI + WebSocket + 区块链
- **获奖情况**: 全国大学生电子商务"创新、创意及创业"挑战赛（2025）省金

## 📞 联系我们

如有疑问或建议，请通过以下方式联系：

- 项目仓库: [https://github.com/123sssdd/SanChuang](https://github.com/123sssdd/SanChuang)
- Issue反馈: [提交Issue](https://github.com/123sssdd/SanChuang/issues)

---

<div align="center">

**让创意设计回归纯粹，让版权保护成为可能**

*原央创设 - 为创作者而生的数字智护平台*

</div>
