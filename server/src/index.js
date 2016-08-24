import Koa from 'koa';
import config from './config';
import router from './routers';
import log4js from './utils/logger';

const logger = log4js.getLogger('server');
const app = new Koa();

let serverStarted = false;
process.on('unhandledRejection', (reason, p) => {
  logger.error("Catch Rejection :", reason, p);
  if (!serverStarted) {
    // 如果服务器还没完成初始化则直接退出
    process.exit(1);
  }
});

async function init() {
  app.use(router());

  app.on('error', (err, ctx) => {
    logger.error(err);
    ctx.response.status = 500;
  });

  app.listen(config.port, () => {
    serverStarted = true;
    logger.info(`server started on port ${config.port}`);
  });
}

init();

export default app;
