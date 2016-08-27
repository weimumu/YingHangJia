/**
 * Created by zhuangqh on 16/8/25.
 */

import Router from 'koa-router';
import compose from 'koa-compose';
import path from 'path';
import serve from 'koa-static';
import userCtrl from './user';
import prodCtrl from './prod';
import newsCtrl from './news';

const router = new Router();

userCtrl(router);
prodCtrl(router);
newsCtrl(router);

export default () => compose([
  serve(path.join(__dirname, '..', '..', '..', 'doc', 'api-doc')),
  router.routes(),
  router.allowedMethods(),
]);
