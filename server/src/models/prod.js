/**
 * Created by zhuangqh on 16/8/25.
 */

export default {
  name: String,
  issueBank: String,
  logoUrl: String,
  issueDate: String,
  earningMode: String,
  currency: String,
  effectDate: String,
  type: String,
  issueRegion: String,
  maturity: String,
  target: String,
  timeLimit: Number,
  highestRate: Number,
  realRate: Number,
  interestPeriod: String,
  startAmount: Number,
  incStep: String,
  rateState: String,

  comments: {
    type: Array,
    default: [],
  },
};
