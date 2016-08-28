/**
 * Created by zhuangqh on 16/8/25.
 */

import Router from 'koa-router';
import compose from 'koa-compose';
import userCtrl from './user';
import prodCtrl from './prod';
import newsCtrl from './news';

const router = new Router();

userCtrl(router);
prodCtrl(router);
newsCtrl(router);

export default () => compose([
  router.routes(),
  router.allowedMethods(),
]);
