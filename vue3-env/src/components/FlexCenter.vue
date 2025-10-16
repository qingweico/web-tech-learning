<template>
  <div style="display: flex;">
    <div class="container">
      <div class="chunk">
        <div class="icon-container">
          <el-icon size="32"><Download /></el-icon>
        </div>
        <div class="text-description">
          下载
        </div>
      </div>

      <div class="chunk">
        <div class="icon-container">
          <el-icon size="32"><Microphone /></el-icon>
        </div>
        <div class="text-description">
          打开麦克风
        </div>
      </div>


      <div class="chunk">
        <div class="icon-container">
          <el-icon  size="32"><Connection /></el-icon>
        </div>
        <div class="text-description">
          复制
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>

</script>

<style scoped lang="scss">
.container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 800px;
  height: 500px;
  box-shadow: 0 0 24px rgba(0, 0, 0, .12);
  border-radius: 4px;

  .chunk {
    padding: 20px;
    flex-direction: column;
    .icon-container {
      width: 70px;
      height: 70px;
      display: flex;
      justify-content: center;
      align-items: center;
      border: 2px solid #ffffff;
      border-radius: 12px;
      background: #f5f5f5;
      box-sizing: border-box;
      cursor: pointer;
    }
    .icon-container:hover {
      background-color: #dde0e6;
    }
    .text-description {
      font-size: 16px;
      height: 12px;
      display: flex;
      justify-content: center;
      align-items: center;
      margin-top: 12px;
    }
  }
}
</style>
