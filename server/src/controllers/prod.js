/**
 * Created by zhuangqh on 16/8/25.
 */

import prodService from '../service/prod';
import { isDefined } from '../utils/kit';

async function comment(ctx) {
  const id = ctx.params.id;
  const body = ctx.request.body;

  if (isDefined(id, body.userId, body.text)) {
    await prodService.addComment(id, body);

    ctx.response.status = 200;
  } else {
    ctx.response.status = 400;
  }
}

async function getAllProd(ctx) {
  const prods = await prodService.findAllProd();

  ctx.body = {
    data: prods,
  };
}

export prodCtrl = (router) => {
  router.post('/api/product/comment/:id', comment);
  router.get('/api/product', getAllProd);
};
