import path from 'path';

export default {
  port: 8000,
  static: path.resolve(__dirname, 'public'),
  db: {
    host: 'localhost',
    username: 'root',
    password: '',
    database: 'example',
  },
  log: {
    appenders: [
      {
        type: 'console',
      },
      {
        type: 'dateFile',
        filename: 'logs/error',
        pattern: '-MM-dd.log',
        alwaysIncludePattern: true,
        category: 'error',
      },
      {
        type: 'dateFile',
        filename: 'logs/debug',
        pattern: '-MM-dd.log',
        alwaysIncludePattern: true,
        category: 'debug',
      },
    ],
    levels: {
      error: 'ALL',
    },
  },
};
