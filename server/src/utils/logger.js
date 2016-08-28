import log4js from 'log4js';
import config from '../config';

log4js.configure(config.log);

const debug = log4js.getLogger('debug');
const error = log4js.getLogger('error');
const status = log4js.getLogger('status');

export { debug, error, status };
