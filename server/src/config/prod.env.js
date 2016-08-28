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
        category: 'status',
      },
      {
        type: 'dateFile',
        filename: 'logs/error',
        pattern: '-MM-dd.log',
        alwaysIncludePattern: true,
        category: 'error',
      },
    ],
    levels: {
      error: 'ALL',
      status: 'ALL',
    },
  },
};
