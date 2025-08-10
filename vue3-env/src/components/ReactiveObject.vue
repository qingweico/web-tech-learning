<template>
  <div v-for="(message, index) in messages" :key="index" class="message">
    <div>{{ message.content }}</div>
  </div>
  <el-button type="success" text
             bg @click="change2Qa">替换第二个回答</el-button>
</template>

<script>

export default {
  name: 'ReactiveObject',
  data() {
    return {
      count: 1,
      messages: [
        { id: 1, content: `这个是第1个回答` },
        { id: 2, content: `这个是第2个回答` },
        { id: 3, content: `这个是第3个回答` }
      ],
      intervalId: null
    }
  },
  methods: {
    change2Qa() {
      this.intervalId = setInterval(() => {
        this.messages = this.messages.map((message, index) => {
          if (index === 1) { // 确保更新第二个元素
            return {
              ...message,
              content: `测试替换第二个回答${Date.now()}`
            };
          }
          return message;
        });
      }, 100);

      // 2秒后清除定时器
      setTimeout(() => {
        clearInterval(this.intervalId);
      }, 2000);
    }
  }
}

</script>
