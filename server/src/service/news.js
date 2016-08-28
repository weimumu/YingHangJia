/**
 * Created by zhuangqh on 16/8/27.
 */

import db from '../models';
import { getDateStr } from '../utils/kit';

async function getDailyNews() {
  return await db.news.find({
    created: getDateStr(new Date()),
  }, "-created -__v");
}

export default {
  getDailyNews,
};
