<template>
  <div style="display: flex; flex-direction: column;">
    <el-switch
        v-model="mode"
        class="mb-2"
        :active-value="'ComponentMode'"
        :inactive-value="'RouterMode'"
        style="--el-switch-on-color: #13ce66; --el-switch-off-color: #409EFF"
        active-text="组件模式"
        inactive-text="路由模式"
    />
  </div>
  <div class="container" v-if="mode === 'ComponentMode'">
    <div class="left-panel">
      <el-select clearable filterable v-model="selected" placeholder="选择组件">
        <el-option
            v-for="item in componentList"
            :key="item.label"
            :label="item.label"
            :value="item.label"
        />
      </el-select>
    </div>
    <div class="right-panel">
      <component :is="selectedComponent" v-if="selectedComponent"/>
    </div>
  </div>
  <keep-alive v-else>
    <router-view/>
  </keep-alive>

</template>
<script setup>
import {ref, watch} from 'vue';
import {componentList, componentMap} from './components/ComponentList';

const selectedComponent = ref(null);
const selected = ref(null);
import {onMounted} from 'vue'
import {markRaw} from "@vue/runtime-core";
// 组件模式 / 路由模式
// ComponentMode / RouterMode
const mode = ref('')
onMounted(() => {
})
watch(() => selected.value, (newValue) => {
  if (newValue) {
    selectedComponent.value = markRaw(componentMap.get(newValue))
  } else {
    selectedComponent.value = null
  }
})
</script>

<style scoped>
.container {
  display: flex;
  height: calc(100vh - 52px);
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
