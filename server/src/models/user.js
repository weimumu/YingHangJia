import mongoose from 'mongoose';

const Schema = mongoose.Schema;

const userSchema = new Schema({
  name: String,
  password: String,
  starProd: Array,
  starNews: Array,
  purchase: Array,
});

export default userSchema;
