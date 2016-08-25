/**
 * Created by zhuangqh on 16/8/25.
 */

import mongoose from 'mongoose';

const prodSchema = new mongoose.Schema({
  name: String,
  password: String,
  starProd: Array,
  starNews: Array,
  purchase: Array,
});

export default prodSchema;
