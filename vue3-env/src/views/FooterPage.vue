<template>
  <footer class="site-footer">
    <div class="footer-content">
      <p>&copy; {{ currentYear }} 你的公司或网站名称. 保留所有权利. </p>
      <nav class="footer-nav">
        <router-link to="/about">关于我们</router-link> |
        <router-link to="/contact">联系我们</router-link> |
        <router-link to="/privacy">隐私政策</router-link>
      </nav>
    </div>
  </footer>
</template>

<script>
export default {
  name: 'FooterPage',
  data() {
    return {
      currentYear: new Date().getFullYear()
    };
  }
};
</script>

<style scoped>
.site-footer {
  background-color: #2c3e50;
  color: white;
  padding: 20px 0;
  text-align: center;
  font-size: 14px;
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
}

.footer-content {
  max-width: 100vw;
  margin: 0 auto;
  padding: 0 20px;
}

.footer-nav {
  margin-top: 10px;
}

.footer-nav a {
  color: #ecf0f1;
  text-decoration: none;
  margin: 0 10px;
}

.footer-nav a:hover {
  text-decoration: underline;
}
</style>
