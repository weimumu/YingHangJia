/**
 * Created by zhuangqh on 16/8/27.
 */

import request from 'request';
import cheerio from 'cheerio';
import iconv from 'iconv-lite';
import { error } from '../utils/logger';
import db from '../models';
import { getDateStr } from '../utils/kit';


const news = [];

function getBasicNews() {
  return new Promise((resolve, reject) => {
    request.get({
      url: 'http://money.qq.com/',
      headers: {
        'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36',
      },
      encoding: null,
    }, (err, response, body) => {
      if (err) {
        reject(err);
      }

      if (response.statusCode === 200) {
        body = iconv.decode(body, 'GBK');
        const $ = cheerio.load(body);
        const main = $('.sst.second .Q-tpList');

        for (let i = 0; i < 5; i = i + 1) {
          const a = main.eq(i).find('a');

          const newsItem = {};
          newsItem.img = a.eq(0).find('img').eq(0).attr('src');
          newsItem.page = a.eq(1).attr('href');
          newsItem.title = a.eq(1).text();

          news.push(newsItem);
        }

        for (let i = 5; i < main.length; i = i + 1) {
          const a = main.eq(i).find('a');

          const newsItem = {};
          newsItem.img = a.eq(0).find('img')[0].attribs._src;
          newsItem.page = a.eq(1).attr('href');
          newsItem.title = a.eq(1).text();

          news.push(newsItem);
        }

        resolve();
      } else {
        reject('请求失败');
      }
    });
  });
}

function getTime(newsItem) {
  return new Promise((resolve, reject) => {
    request.get({
      url: newsItem.page,
      headers: {
        'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36',
      },
      encoding: null,
    }, (err, response, body) => {
      if (err) {
        error.error(err);
        resolve();
      }

      if (response.statusCode === 200) {
        const $ = cheerio.load(body);

        let time = $('.a_time');

        if (time.length === 0) {
          time = $('.article-time');
        }

        newsItem.time = time.eq(0).text();
        newsItem.created = getDateStr(new Date());
        resolve();
      } else {
        resolve('请求失败');
      }
    });
  });
}

export default function () {
  getBasicNews()
    .then(() => Promise.all(news.map(item => getTime(item))))
    .then(() => Promise.all(news.map(item => db.news.create(item))))
    .catch((err) => {
      error.error(err);
    });
}
