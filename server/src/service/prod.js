/**
 * Created by zhuangqh on 16/8/25.
 */

import _ from 'lodash';
import mongoose from 'mongoose';
import db from '../models';
import { getDateStr } from '../utils/kit';

const ObjectId = mongoose.Types.ObjectId;

async function addComment(id, comment) {
  const comment_ = _.pick(comment, ['username', 'text']);
  comment_.time = getDateStr(new Date());

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
        let r1 = null;
        let r2 = null;
        if (field === 'highestRate') {
          r1 = parseFloat(r[0]);
          r2 = parseFloat(r[1]);
        } else {
          r1 = parseInt(r[0], 10);
          r2 = parseInt(r[1], 10);
        }
        if (r1) {
          subQ[field].$gte = r1;
        }
        if (r2) {
          subQ[field].$lte = r2;
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

async function getRecommend(id) {
  // an example
  let ids = [
    ObjectId("57d0c7396ddc242dbc061dd7"),
    ObjectId("57d0c8546ddc242dbc061dda"),
    ObjectId("57d0c7ff6ddc242dbc061dd9"),
    ObjectId("57d0c8a16ddc242dbc061ddb"),
    ObjectId("57d0c7b16ddc242dbc061dd8")
  ];

  return db.prod.find({
    _id: {
      $in: ids,
    }
  });
}

export default {
  addComment,
  findAllProd,
  findAProd,
  getRecommend,
};
