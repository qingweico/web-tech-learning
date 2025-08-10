<template>
  <p>404 Not Found</p>
  {{ '不存在的 url'  }}
  <el-tag type="danger">{{ $route.params.pathMatch }}</el-tag>
</template>
<script setup></script>
