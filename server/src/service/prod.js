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

async function findAllProd(query) {
  const dbQuery = {
    issueBank: {
      $regex: query.bank,
    },
    timeLimit: {},
    highestRate: {},
    startAmount: {},
  };
  query.highestRateL = query.rateL;
  query.highestRateH = query.rateH;

  ['timeLimit', 'highestRate', 'startAmount'].forEach((field) => {
    if (query[field + 'L']) {
      dbQuery[field].$gte = query[field + 'L'];
    }

    if (query[field + 'H']) {
      dbQuery[field].$lte = query[field + 'H'];
    }
  });

  if (query.page) {
    dbQuery._id = {
      $gt: ObjectId(query.page),
    };
  }

  return await db.prod.find(dbQuery).limit(10);
}

export default {
  addComment,
  findAllProd,
};
