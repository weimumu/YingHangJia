/**
 * Created by zhuangqh on 16/8/25.
 */

import mongoose from 'mongoose';

export default {
  userId: mongoose.Schema.Types.ObjectId,
  text: String,
};
