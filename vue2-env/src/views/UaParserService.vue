<template>
  <div>
    <h1>用户信息</h1>
    <p>浏览器: {{ browser.name }} {{ browser.version }}</p>
    <p>操作系统: {{ os.name }} {{ os.version }}</p>
    <p>设备: {{ device.type || "未知设备" }}</p>
  </div>
</template>

<script>
import UAParser from "ua-parser-js";

export default {
  name: "UaParserService",
  data() {
    return {
      browser: {},
      os: {},
      device: {}
    };
  },
  mounted() {
    this.getUserAgentInfo();
  },
  methods: {
    parseUserAgent(uaString) {
      const parser = new UAParser();
      parser.setUA(uaString);
      return parser.getResult();
    },
    getUserAgentInfo() {
      const uaString = navigator.userAgent;
      const result = this.parseUserAgent(uaString);
      this.browser = result.browser;
      this.os = result.os;
      this.device = result.device;
    }
  }
};
</script>
