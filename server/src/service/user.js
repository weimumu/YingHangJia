import _ from 'lodash';
import bcrypt from 'bcryptjs';
import mongoose from 'mongoose';
import db from '../models';

const ObjectId = mongoose.Types.ObjectId;

async function getUser(id) {
  return await db.user.findOne({
    name: id,
  }, '-password');
}

async function createUser(user, requiredAttr) {
  const user_ = _.pick(user, requiredAttr);

  const salt = bcrypt.genSaltSync(10);
  user_.password = bcrypt.hashSync(user.password, salt);

  await db.user.create(user_);
}

async function checkUser(user) {
  const user_ = _.pick(user, ['name', 'password']);

  const res = await db.user.findOne({
    name: user_.name,
  });

  if (!res) {
    return Promise.reject('用户不存在');
  } else {
    const match = bcrypt.compareSync(user_.password, res.password);

    if (!match) {
      return Promise.reject('密码错误');
    }
  }
}

async function addNewsStar(userId, newsId) {
  await db.user.update({
    _id: ObjectId(userId),
  }, {
    $push: {
      starNews: newsId,
    },
  });
}

async function addProdStar(userId, prodId) {
  await db.user.update({
    _id: ObjectId(userId),
  }, {
    $push: {
      starProd: prodId,
    },
  });
}

async function getNewsStar(userId) {
  const stars = await db.user.findOne({
    _id: ObjectId(userId),
  }, '-_id starNews');

  if (!stars || stars.length === 0) return [];

  return db.news.find({
    _id: {
      $in: stars.starNews,
    }
  }, '-__v');
}

async function getProdStar(userId) {
  const stars =  await db.user.findOne({
    _id: ObjectId(userId),
  }, '-_id starProd');

  if (!stars || stars.length === 0) return [];

  return db.prod.find({
    _id: {
      $in: stars.starProd,
    }
  }, '-__v');
}

async function delNewsStar(userId, starId) {
  await db.user.update({
    _id: ObjectId(userId),
  }, {
    $pull: {
      starNews: starId,
    },
  });
}

async function delProdStar(userId, starId) {
  await db.user.update({
    _id: ObjectId(userId),
  }, {
    $pull: {
      starProd: starId,
    },
  });
}

async function modifyScore(userId, body) {
  const score = parseInt(body.score);
  const scoreAge = parseInt(body.scoreAge);
  const age = parseInt(body.age);

  if (!score || !scoreAge || !age) {
    return Promise.reject('无法解析成数字');
  }

  await db.user.update({
    _id: ObjectId(userId),
  }, {
    score: score,
    scoreAge: scoreAge,
    age: age,
  });
}

async function checkIn(userId) {
  await db.user.update({
    _id: ObjectId(userId),
  }, {
    checkIn: 10,
  });
}

async function modifyUser(userId, user) {
  const allField = [
    'name',
    'score',
    'scoreAge',
    'age',
    'phone',
    'email',
    'gender',
  ];
  const user_ = _.pick(user, allField);
  await db.user.update({
    _id: userId,
  }, user_);
}

async function modifyPassword(userId, password) {
  const salt = bcrypt.genSaltSync(10);
  password = bcrypt.hashSync(password, salt);

  await db.user.update({
    _id: ObjectId(userId),
  }, {
    password: password,
  });
}

export default {
  getUser,
  getNewsStar,
  getProdStar,
  createUser,
  checkUser,
  addNewsStar,
  addProdStar,
  delNewsStar,
  delProdStar,
  modifyScore,
  modifyUser,
  modifyPassword,
  checkIn,
};
