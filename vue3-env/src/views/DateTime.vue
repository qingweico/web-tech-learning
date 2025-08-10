<template>
  <div class="date-time">
    <p>{{ formattedDate() }}</p>
    <p>{{ id }}</p>
  </div>
</template>

<script>
export default {
  props: {
    id: {
      type: Number
    }
  },
  data() {
    return {
      now: new Date(),
      interval: null
    };
  },
  mounted() {
    this.interval = setInterval(() => {
      this.now = new Date();
    }, 1000)
  },
  methods: {
    formattedDate() {
      const year = this.now.getFullYear();
      const month = this.now.getMonth() + 1;
      const day = this.now.getDate();
      const week = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'][this.now.getDay()];
      const hour = this.now.getHours();
      const minute = this.now.getMinutes();
      const seconds = this.now.getSeconds() < 10 ? `0${this.now.getSeconds()}` : this.now.getSeconds();
      const ampm = hour >= 12 ? '下午' : '上午';
      const formattedHour = hour % 12 || 12;
      return `${year}年${month}月${day}日 ${week} ${ampm} ${formattedHour}点 ${minute}分${seconds}秒`;
    }
  },
};
</script>

<style scoped>
.date-time {
  font-size: 1.2em;
  text-align: center;
  margin-top: 50px;
}
</style>
