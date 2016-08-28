/**
 * Created by zhuangqh on 16/8/27.
 */

import db from '../models';

async function getDailyNews() {
  return await db.news.find({
    created: new Date().toISOString().slice(0, 10),
  }, "-created -__v");
}

export default {
  getDailyNews,
};
