/**
 * Created by zhuangqh on 16/8/25.
 */

import mongoose from 'mongoose';

const newsSchema = new mongoose.Schema({
  title: String,
  url: String,
  img: String,
});

export default newsSchema;
