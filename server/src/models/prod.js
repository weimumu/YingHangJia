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
  timeLimit: String,
  highestRate: String,
  realRate: String,
  interestPeriod: String,
  startAmount: String,
  incStep: String,
  rateState: String,

  comments: {
    type: Array,
    default: [],
  },
};
