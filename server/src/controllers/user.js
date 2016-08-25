import userService from '../service/user';
import log4js from '../utils/logger';
import { hasEvery, isDefined } from '../utils/kit';

const logger = log4js.getLogger('controller user');

const requiredAttr = ['name', 'password', 'starProd', 'starNews', 'purchase'];

async function signup(ctx) {
  const body = ctx.request.body;

  if (hasEvery(body, requiredAttr)) {
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

  if (hasEvery(body, ['name', 'password'])) {
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

async function star(ctx) {
  const userId = ctx.params.id;
  const type = ctx.request.body.type;
  const starId = ctx.request.body.starid;

  if (isDefined(userId, type, starId)) {
    if (type === 'news') {
      await userService.addNewsStar(userId, starId);

      ctx.response.status = 200;
    } else if (type === 'prod') {
      await userService.addProdStar(userId, starId);

      ctx.response.status = 200;
    } else {
      ctx.response.status = 400;
    }
  } else {
    ctx.response.status = 400;
  }
}

export const userCtrl = (router) => {
  router.post('/api/signup', signup);
  router.post('/api/signin', signin);
  router.post('/api/star/:id', star);
};
