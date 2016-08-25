import _ from 'lodash';
import userService from '../service/user';
import log4js from '../utils/logger';

const logger = log4js.getLogger('controller user');

const requiredAttr = ['name', 'password', 'starProd', 'starNews', 'purchase'];

async function signup(ctx) {
  const body = ctx.request.body;

  if (requiredAttr.every(key => _.has(body, key))) {
    await userService.createUser(body, requiredAttr)
      .catch((err) => {
        logger.error(err);
        ctx.response.status = 403;
      });

    ctx.response.status = 200;
  } else {
    ctx.response.status = 400;
  }
}

async function signin(ctx) {
  const body = ctx.request.body;

  if (['name', 'password'].every(key => _.has(body, key))) {
    await userService.checkUser(body)
      .catch((err) => {
        logger.error(err);
        ctx.body ={
          err: err,
        }
      });

    ctx.response.status = 200;
  } else {
    ctx.response.status = 400;
  }
}

export const userCtrl = (router) => {
  router.post('/api/signup', signup);
  router.post('/api/signin', signin);
};
