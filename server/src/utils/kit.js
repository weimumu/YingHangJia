import _ from 'lodash';

export const hasEvery = (obj, attrArr) => attrArr.every(key => _.has(obj, key));

export const isDefined = () => {
  const args = Array.from(arguments);
  return args.every((key => !_.isUndefined(key)));
};

export const getDateStr = (date) => {
  const mm = date.getMonth() + 1; // getMonth() is zero-based
  const dd = date.getDate();

  return [date.getFullYear(), (mm > 9 ? '' : '0') + String(mm),
    (dd > 9 ? '' : '0') + String(dd)].join('-'); // padding
};
