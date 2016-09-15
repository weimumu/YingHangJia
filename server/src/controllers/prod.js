/**
 * Created by zhuangqh on 16/8/25.
 */

import prodService from '../service/prod';
import { isDefined } from '../utils/kit';

async function comment(ctx) {
  const id = ctx.params.id;
  const body = ctx.request.body;

  if (isDefined(id, body.username, body.text)) {
    await prodService.addComment(id, body);

    ctx.response.status = 200;
  } else {
    ctx.response.status = 400;
  }
}

async function getAllProd(ctx) {
  const query = ctx.request.query;

  const prods = await prodService.findAllProd(query);

  ctx.body = {
    data: prods,
  };
}

async function getAProd(ctx) {
  const id = ctx.params.id;

  if (id) {
    const prod = await prodService.findAProd(id)
      .catch(() => {
        ctx.response.status = 404;
      });

    ctx.body = {
      data: prod,
    }
  } else {
    ctx.response.status = 400;
  }
}

async function recommend(ctx) {
  const id = ctx.params.id;

  if (id) {
    let prods = await prodService.getRecommend(id)
      .catch(() => {
        ctx.response.status = 404;
      });

    ctx.body = {
      data: prods,
    }
  } else {
    ctx.response.status = 400;
  }
}

export default function prodCtrl(router) {
  router.put('/api/product/comment/:id', comment);
  router.get('/api/product', getAllProd);
  router.get('/api/product/:id', getAProd);
  router.get('/api/product/recommend/:id', recommend);
};
