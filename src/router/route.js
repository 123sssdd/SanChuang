import User from "@/pages/user/user.vue";
import Message from "@/pages/message/message.vue";
import FollowTrend from "@/pages/follow-trend/follow-trend.vue";
import Dashboard from "@/pages/dashboard/dashboard.vue";
import Login from "@/pages/logine.vue";
import Index from "@/pages/index.vue";
import Main from "@/pages/main/main.vue";
import publish from "@/pages/publish/publish.vue";
import Chat from "@/components/Chat.vue";
import Order_create from "@/pages/order/order_create.vue";
import order_create from "@/pages/order/order_create.vue";

import payment_pay from "@/pages/order/payment_pay.vue";
import Order_detail from "@/pages/order/order_detail.vue";
import order_evaluate from "@/pages/order/order_evaluate.vue";
import Block from "@/pages/block/block.vue";
import Dialog from "@/pages/dialog/dialog.vue";
export default [
  {
    path: "/",
    redirect: "/index",
  },
  {
    name: "chat",
    path: "/chat",
    component: Chat,
  },
  {
    name: "main",
    path: "/main",
    component: Main,
  },
  {
    name: "index",
    path: "/index",
    component: Index,
    redirect: "/dashboard",
    children: [
      {
        path: "/dashboard",
        component: Dashboard,
        name: "dashboard",
      },
      {
        name: "login",
        path: "/login",
        component: Login,
      },
      {
        path: "/followTrend",
        component: FollowTrend,
        name: "followTrend",
      },
      {
        path: "/notice",
        component: Message,
        name: "notice",
      },
      {
        path: "/user",
        component: User,
        name: "user",
      },
      {
        path: "/publish",
        component: publish,
        name: "push",
      },
      {
        path: "/orderDetail",
        component: Order_detail,
        name: "orderDetail",
      },
      {
        path: "/orderCreate",
        component: order_create,
        name: "orderCreate",
      },
      {
        path: "/paymentPay",
        component: payment_pay,
        name: "paymentPay",
      },
      {
        path: "/orderEvaluate",
        component: order_evaluate,
        name: "orderEvaluate",
      },
      {
        path: "/block",
        name: "Block",
        component: Block,
      },
      {
        path: "/dialog",
        name: "Dialog",
        component: Dialog, // 跳转到 dialog 页面
      },
    ],
  },
];
