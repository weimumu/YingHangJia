import _ from 'lodash';
import userService from '../service/user';


const requiredAttr = ['name', 'password', 'starProd', 'starNews', 'purchase'];

export async function signup(ctx) {
  const body = ctx.request.body;

  if (_.has(body, requiredAttr)) {
    await userService.createUser()
      .then(() => {
        ctx.response.status = 200;
      })
      .catch((err) => {

      })
  } else {
    ctx.response.status = 400;
  }
}
