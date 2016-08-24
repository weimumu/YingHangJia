import db from '../models';

async function createUser() {
  db.user.create({
    name: 'xiaoming',
  });
}

export default {
  createUser,
};
