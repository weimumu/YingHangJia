import db from '../models';
import _ from 'lodash';
import bcrypt from 'bcryptjs';

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

export default {
  createUser,
  checkUser,
};
