<template>
  <div
    class="container"
  >
    <div class="left-panel">
      <el-select
        v-model="selected"
        clearable
        filterable
        placeholder="选择组件"
      >
        <el-option
          v-for="item in componentList"
          :key="item.label"
          :label="item.label"
          :value="item.label"
        />
      </el-select>
    </div>
    <div class="right-panel">
      <component
        :is="selectedComponent"
        v-if="selectedComponent"
      />
    </div>
  </div>
</template>

<script>
import { componentList, componentMap } from './components';

export default {
  name: 'App',
  data() {
    return {
      selected: null,
      selectedComponent: null,
      componentList: componentList
    };
  },
  watch: {
    selected(newValue) {
      if (newValue) {
        this.selectedComponent = componentMap.get(newValue);
      } else {
        this.selectedComponent = null;
      }
    }
  }
};
</script>

<style scoped>
.container {
  display: flex;
  height: 100vh;
}

.left-panel {
  width: 200px;
  padding: 20px;
}

.right-panel {
  flex: 1;
  padding: 20px;
  border-left: 1px solid #ccc;
}
</style>
