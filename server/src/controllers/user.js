import userService from '../service/user';
import { error } from '../utils/logger';
import { hasEvery, isDefined } from '../utils/kit';

const requiredAttr = ['name', 'password'];

async function signup(ctx) {
  const body = ctx.request.body;

  if (hasEvery(body, requiredAttr)) {
    await userService.createUser(body, requiredAttr)
      .catch((err) => {
        error.error(err);
        ctx.response.status = 403;
      });

    ctx.response.status = 200;
  } else {
    ctx.response.status = 400;
  }
}

async function signin(ctx) {
  const body = ctx.request.body;

  if (hasEvery(body, requiredAttr)) {
    await userService.checkUser(body)
      .catch((err) => {
        error.error(err);
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
  const starId = ctx.request.body.starId;

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

async function getStar(ctx) {
  const userId = ctx.params.id;
  const type = ctx.request.query.type;

  let stars = null;
  if (isDefined(userId, type)) {
    if (type === 'news') {
      stars = await userService.getNewsStar(userId);

      ctx.body = {
        data: stars,
      }
    } else if (type === 'prod') {
      stars = await userService.getProdStar(userId);

      ctx.body = {
        data: stars,
      }
    } else {
      ctx.response.status = 400;
    }
  } else {
    ctx.response.status = 400;
  }
}

async function delStar(ctx) {
  const userId = ctx.params.id;
  const type = ctx.request.query.type;
  const starId = ctx.request.query.starId;

  if (isDefined(userId, type, starId)) {
    if (type === 'news') {
      await userService.delNewsStar(userId, starId)
        .catch((err) => {
          error.error(err);
          ctx.response.status = 403;
        });

      ctx.response.status = 200;
    } else if (type === 'prod') {
      await userService.delProdStar(userId, starId)
        .catch((err) => {
          error.error(err);
          ctx.response.status = 403;
        });

      ctx.response.status = 200;
    } else {
      ctx.response.status = 400;
    }
  } else {
    ctx.response.status = 400;
  }
}

async function getUser(ctx) {
  const userId = ctx.params.id;

  if (isDefined(userId)) {
    const userInfo = await userService.getUser(userId);

    if (userInfo) {
      ctx.body = {
        data: userInfo,
      };
    } else {
      ctx.body = {
        err: '用户不存在',
      };
    }

  } else {
    ctx.response.status = 400;
  }
}

async function postScore(ctx) {
  const userId = ctx.params.id;
  const score = parseInt(ctx.request.query.score);

  if (userId && score) {
    await userService.modifyScore(userId, score)
      .catch((err) => {
        error.error(err);
        ctx.response.status = 403;
      });

    ctx.response.status = 200;
  } else {
    ctx.response.status = 400;
  }
}

export default function userCtrl(router) {
  router.get('/api/user/:id', getUser);
  router.post('/api/signup', signup);
  router.post('/api/signin', signin);
  router.put('/api/star/:id', star);
  router.get('/api/star/:id', getStar);
  router.delete('/api/star/:id', delStar);
  router.post('/api/user/score/:id', postScore);
};
