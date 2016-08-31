/**
 * Created by zhuangqh on 16/8/25.
 */

import _ from 'lodash';
import mongoose from 'mongoose';
import db from '../models';

const ObjectId = mongoose.Types.ObjectId;

async function addComment(id, comment) {
  const comment_ = _.pick(comment, ['username', 'text']);

  await db.prod.update({
    _id: ObjectId(id),
  }, {
    $push: {
      comments: comment_,
    }
  });
}

async function findAllProd() {
  return await db.prod.find({}).limit(5);
}

export default {
  addComment,
  findAllProd,
};
