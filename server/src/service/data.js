/**
 * Created by zhuangqh on 16/8/30.
 */

import fs from 'fs';
import path from 'path';
import db from '../models';

function csv() {
  fs.readFile(path.join(__dirname, '../../public/data.csv'), 'utf8', (err, doc) => {
    if (err) throw err;
    const originTable = doc.split('\n');

    originTable.map((tableItem, idx) => {
      const row = tableItem.split('\t');

      if (row.length > 18) {
        const obj = {
          name: row[2].replace(/&quot;/g, '"').split('：')[1],
          issueBank: row[3].split('：')[1],
          issueDate: row[4].split('：')[1],
          earningMode: row[5].split('：')[1],
          currency: row[6].split('：')[1],
          effectDate: row[7].split('：')[1],
          type: row[8].split('：')[1],
          issueRegion: row[9].split('：')[1],
          maturity: row[10].split('：')[1],
          target: row[11].split('：')[1],
          timeLimit: parseInt(row[12].split('：')[1], 10),
          highestRate: parseFloat(row[13].split('：')[1]),
          realRate: parseFloat(row[14].split('：')[1]),
          interestPeriod: row[15].split('：')[1],
          startAmount: parseInt(row[16].split('：')[1], 10),
          incStep: row[17].split('：')[1],
          rateState: row[18].split('：')[1],
        };

        obj.logoUrl = `/static/img/${obj.issueBank}.png`;

        db.prod.create(obj);
        console.log(idx);
      }
    });
  });
}

export default csv;
