import * as Ctrl from '../controllers/user';

/**
 *  格式:
 *  [方法名]: { url : controller }
 * */
export default {
  get: {
    '/api/user': Ctrl.getName,
  },
};
