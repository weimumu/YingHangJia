/**
 * Created by zhuangqh on 16/8/27.
 */

import newsService from '../service/news';

async function getNews(ctx) {
  let news = await newsService.getDailyNews()
    .catch((err) => {
      ctx.body = {
        err: err,
      };
    });

  ctx.body = {
    data: news,
  };
}

export default function newsCtrl(router) {
  router.get('/api/news', getNews);
};
