/**
 * Created by zhuangqh on 16/8/25.
 */

import _ from 'lodash';
import mongoose from 'mongoose';
import db from '../models';

const ObjectId = mongoose.Types.ObjectId;

async function addComment(id, comment) {
  const comment_ = _.pick(comment, ['username', 'text', 'time']);

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

  if (query.bank) {
    dbQuery.issueBank = {
      $regex: query.bank,
    };
  }

  if (query.name) {
    dbQuery.name = {
      $regex: query.name,
    };
  }

  Object.keys(dbQuery).forEach((attr) => {
    if (Object.keys(dbQuery[attr]).length === 0) {
      delete dbQuery[attr];
    }
  });

  return await db.prod.find(dbQuery).limit(10);
}


async function findAProd(id) {
  return db.prod.findOne({
    _id: ObjectId(id),
  });
}


export default {
  addComment,
  findAllProd,
  findAProd,
};
