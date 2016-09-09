<template>
  <div>
    <h1>用户反馈</h1>
    <div class="main-content">
      <table class="table table-bordered table-hover">
        <thead>
          <tr>
            <th>用户名</th>
            <th>反馈信息</th>
            <th>反馈时间</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in feedbackList">
            <td>{{ item.username }}</td>
            <td>{{ item.text }}</td>
            <td>{{ item.time }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="op-btn-container">
      <button type="button" class="btn btn-primary btn-left" @click="prevPage">上一页</button>
      <button type="button" class="btn btn-primary btn-right" @click="nextPage">下一页</button>
    </div>
    <div v-show="errShow" id="err-warn">
      <p>没有更多了</p>
    </div>
  </div>
</template>

<script>
export default {
  data () {
    return {
      feedbackList: [],
      historyPage: '',
      errShow: false,
    }
  },
  ready() {
    this.getList();
  },
  methods: {
    getList(page, dir) {
      page = page || '';
      dir = dir || 1;
      this.$http.get(`/api/feedback?page=${page}&dir=${dir}`)
        .then((data) => {
          this.feedbackList = data.json().data;
        })
        .catch((err) => {
          this.errShow = true;
          setTimeout(() => {
            this.errShow = false;
          }, '1000');
        });
    },
    nextPage() {
      this.getList(this.feedbackList.slice(-1)[0]._id, 1);
    },
    prevPage() {
      this.getList(this.feedbackList[0]._id, -1);
    },
  },
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1 {
  text-align: center;
}

.main-content {
  min-width: 40%;
  max-width: 80%;
  margin: auto;
}

.op-btn-container {
  width: 200px;
  margin: 50px auto 0 auto;
}

.btn-right {
  float: right;
}

#err-warn {
  position: absolute;
  top: 30px;
  right: 100px;
}
</style>
