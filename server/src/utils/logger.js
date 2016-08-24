import log4js from 'log4js';
import config from '../config';

log4js.configure(config.log);

export default log4js;
