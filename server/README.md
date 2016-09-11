# 赢行家后端

## 使用说明

```bash
$ npm install # 安装依赖
$ npm run dev # 开发模式，使用 dev.env.js 的配置
$ npm run build # babel 编译 src 文件夹下的代码
$ npm run serve # 运行编译后的代码，使用 prod.env.js 的配置
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
├── pm2.json                # 服务器部署配置文件
├── public                  # 静态文件目录
|   └── *
├── server                  # 编译后的代码
│   └── *
└── src
    ├── config              # 配置目录
    │   ├── dev.env.js
    │   ├── index.js
    │   └── prod.env.js
    ├── controllers         # 控制器，处理请求
    │   └── *
    ├── index.js            # 入口
    ├── models              # 数据库模型
    │   ├── index.js        # 载入模型，导出所有模型
    │   └── user.js
    ├── service             # 数据库服务
    │   └── user.js
    └── utils               # 存放一些辅助模块
        └── logger.js
```
