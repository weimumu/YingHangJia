import Router from 'koa-router';
import compose from 'koa-compose';
import bulk from 'bulk-require';

const routeMap = bulk(__dirname, ['./!(*index).js']); // 路由配置文件的集合, 忽略 index.js
const router = new Router();
const allowMethods = ['get', 'post', 'del', 'put']; // router 可用的方法

// 自动加载该目录下的路由文件
Object.keys(routeMap).forEach((item) => {
  const maps = routeMap[item].default;
  if (!maps) return;

  const methods = Object.keys(maps); // 路由方法的集合

  methods.forEach((method) => {
    if (allowMethods.indexOf(method) === -1) return;

    const urls = Object.keys(maps[method]); // 路由路径的集合, 如 '/api/user'

    urls.forEach((url) => {
      const controller = maps[method][url];

      if (typeof controller !== 'function') return;
      // console.log(method, url, controller);
      router[method](url, controller);
    });
  });
});

export default () => compose([
  router.routes(),
  router.allowedMethods(),
]);
