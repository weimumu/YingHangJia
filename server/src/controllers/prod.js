/**
 * Created by zhuangqh on 16/8/25.
 */

import prodService from '../service/prod';
import { isDefined, hasEvery } from '../utils/kit';

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

export default function prodCtrl(router) {
  router.post('/api/product/comment/:id', comment);
  router.get('/api/product', getAllProd);
};
