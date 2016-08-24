import * as Ctrl from '../controllers/user';

/**
 *  格式:
 *  [方法名]: { url : controller }
 * */
export default {
  post: {
    '/api/signup': Ctrl.signup,
    '/api/signin': Ctrl.signin,
  },
};
