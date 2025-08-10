<template>
  <p>SuspensePage</p>
  <Suspense>
    <AsyncFetchPage/>
    <!-- 在 #fallback 插槽中显示 “正在加载中” -->
    <template #fallback>
      正在加载中...
    </template>
  </Suspense>
</template>
<script>
import {defineAsyncComponent, defineComponent} from "vue";

export default defineComponent({
  components: {
    AsyncFetchPage: defineAsyncComponent({
      timeout: 100,
      loader: () => new Promise((resolve) => {
        setTimeout(() => {
          resolve(import("@/components/AsyncFetchPage.vue"));
        }, 2000);
      }),
      //delay: 1000,
      errorComponent: () => import("@/components/ErrorComponent.vue")
    })
  }
})
</script>
