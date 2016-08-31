export default {
  name: String,
  password: String,
  starProd: {
    type: Array,
    default: [],
  },
  starNews: {
    type: Array,
    default: [],
  },
  purchased: {
    type: Array,
    default: [],
  },
  score: {
    type: Number,
    default: -1,
  },
};
