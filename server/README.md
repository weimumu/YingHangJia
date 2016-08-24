# koa2 种子项目

## 功能

- 允许使用 ES6/7 的特性，如 async/await
- 代码更新后热重启
- 日志

## 使用说明

```bash
$ npm install # 安装依赖
$ npm run dev # 开发模式，使用 dev.env.js 的配置
$ npm run build # babel 编译 src 文件夹下的代码
$ npm run serve # 运行编译后的代码，使用 prod.env.js 的配置
```

### router

将路由表写在 router 目录下, 配置会被自动加载

```javascript
/**
 *  [方法名]: { url : controller }
 *  同一个方法下可以写多个路由
 * */
export default {
  get: {
    '/api/user': Ctrl.getName,
  },
};
```

### model

将数据库模块写在 model 目录下，模块会被自动加载

模块名为文件名，代码导出一个 schema

```javascript
import mongoose from 'mongoose';

const Schema = mongoose.Schema;

const userSchema = new Schema({
  name: String,
});

export default userSchema;
```

## 目录结构说明

```
.
├── README.md
├── .babelrc
├── .eslintrc
├── .gitignore
├── logs                    # 日志文件
│   ├── debug-xx-xx.log
│   └── error-xx-xx.log
├── package.json
├── server                  # 编译后的代码
│   └── *
├── src
│   ├── config              # 配置目录
│   │   ├── dev.env.js
│   │   ├── index.js
│   │   └── prod.env.js
│   ├── controllers         # 控制器，处理请求
│   │   └── user.js
│   ├── index.js            # 入口
│   ├── models              # 数据库模型
│   │   ├── index.js        # 载入模型，导出所有模型
│   │   └── user.js
│   ├── routers             # 路由目录
│   │   ├── index.js        # 载入路由
│   │   └── user.js         # 路由表
│   ├── service             # 数据库服务
│   │   └── user.js
│   └── utils               # 存放一些辅助模块
│       └── logger.js
└── test                    # mocha 测试
    └── test.js
```