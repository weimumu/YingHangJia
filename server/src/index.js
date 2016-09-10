import Koa from 'koa';
import koaBody from 'koa-body';
import cron from 'cron';
import config from './config';
import controller from './controllers';
import { error, status } from './utils/logger';
import newsService from './service/newsProvider';
import csv from './service/data';

const app = new Koa();
const CronJob = cron.CronJob;

let serverStarted = false;
process.on('unhandledRejection', (reason, p) => {
  error.error("Catch Rejection :", reason, p);
  if (!serverStarted) {
    // 如果服务器还没完成初始化则直接退出
    process.exit(1);
  }
});

async function init() {
  // csv();
  var job = new CronJob('01 00 00 * * *', function () {
      newsService();
    }, function (err) {
      /* This function is executed when the job stops */
      error.error('job stop :', err);
    },
    true, /* Start the job right now */
    null /* Time zone of this job. */
  );

  app.use(koaBody());
  app.use(controller());

  app.on('error', (err, ctx) => {
    error.error(err);
    ctx.response.status = 500;
  });

  app.listen(config.port, () => {
    serverStarted = true;
    status.info(`server started on port ${config.port}`);
  });
}

init();

export default app;
