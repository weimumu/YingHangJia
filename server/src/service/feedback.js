/**
 * Created by zhuangqh on 16/8/29.
 */

import _ from 'lodash';
import mongoose from 'mongoose';
import db from '../models';
import { getDateStr } from '../utils/kit';

const ObjectId = mongoose.Types.ObjectId;

async function addFeedBack(feedback) {
  const feedback_ = _.pick(feedback, ['username', 'text']);
  feedback_.time = getDateStr(new Date());

  await db.feedback.create(feedback_);
}

async function getFeedBack(page, dir) {
  if (dir != '-1') dir = '1';

  if (page) {
    if (dir == "-1") {
      let ans = await db.feedback.find({
          _id: {
            $lt: ObjectId(page),
          },
        }).sort({
          _id: -1,
        })
        .limit(10);

      return _.sortBy(ans, '_id');
    } else {
      return await db.feedback.find({
        _id: {
          $gt: ObjectId(page),
        },
      }).limit(10);
    }
  } else {
    return await db.feedback.find().limit(10);
  }
}
export default {
  addFeedBack,
  getFeedBack,
};
