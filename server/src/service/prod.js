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
    $and: [],
  };
  query.highestRate = query.rate;

  ['timeLimit', 'highestRate', 'startAmount'].forEach((field) => {
    if (!query[field]) return;

    let q = {};
    q.$or = [];

    let rangeArr = query[field].split(';');

    rangeArr.forEach((range) => {
      let subQ = {};
      subQ[field] = {};

      let r = range.split(',');

      if (r) {
        if (r[0]) {
          subQ[field].$gte = r[0];
        }
        if (r[1]) {
          subQ[field].$lte = r[1];
        }
        q.$or.push(subQ);
      }
    });

    if (q.$or.length > 0) {
      dbQuery.$and.push(q);
    }
  });

  if (query.page) {
    dbQuery._id = {
      $gt: ObjectId(query.page),
    };
  }

  if (query.bank) {
    let banks = query.bank.split(',');
    let bankQ = {};

    if (banks.length > 0) {
      bankQ.$or = [];
      banks.forEach((bank) => {
        let subQ = {};

        subQ.issueBank = {
          $regex: bank,
        };
        bankQ.$or.push(subQ);
      });
      dbQuery.$and.push(bankQ);
    }
  }

  if (query.name) {
    dbQuery.name = {
      $regex: query.name,
    };
  }

  if (dbQuery.$and.length === 0) {
    delete dbQuery.$and;
  }
  
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
