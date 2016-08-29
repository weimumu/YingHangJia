/**
 * Created by zhuangqh on 16/8/25.
 */

import Router from 'koa-router';
import compose from 'koa-compose';
import serve from 'koa-static';
import path from 'path';
import userCtrl from './user';
import prodCtrl from './prod';
import newsCtrl from './news';
import feedBackCtrl from './feedback';

const router = new Router();

userCtrl(router);
prodCtrl(router);
newsCtrl(router);
feedBackCtrl(router);

export default () => compose([
  serve(path.join(__dirname, '..', '..', 'public')),
  router.routes(),
  router.allowedMethods(),
]);
