import _ from 'lodash';

export const hasEvery = (obj, attrArr) => attrArr.every(key => _.has(obj, key));

export const isDefined = () => {
  const args = Array.from(arguments);
  return args.every((key => !_.isUndefined(key)));
};
