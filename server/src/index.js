import Koa from 'koa';
import koaBody from 'koa-body';
import cron from 'cron';
import config from './config';
import controller from './controllers';
import log4js from './utils/logger';
import newsService from './service/newsProvider';

const logger = log4js.getLogger('server');
const app = new Koa();
const CronJob = cron.CronJob;

let serverStarted = false;
process.on('unhandledRejection', (reason, p) => {
  logger.error("Catch Rejection :", reason, p);
  if (!serverStarted) {
    // 如果服务器还没完成初始化则直接退出
    process.exit(1);
  }
});

async function init() {
  try {
    var job = new CronJob('01 00 00 * * *', function () {
        newsService();
      }, function (err) {
        /* This function is executed when the job stops */
        logger.error('job stop :', err);
      },
      true, /* Start the job right now */
      null /* Time zone of this job. */
    );
  } catch (e) {
    logger.error(e);
  }

  app.use(koaBody());
  app.use(controller());

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
