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
  scoreAge: {
    type: Number,
    default: -1,
  },
  age: {
    type: Number,
    default: 18,
  },
  checkIn: {
    type: Number,
    default: 0,
  }
};
