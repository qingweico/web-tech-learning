<template>
  <h1>插件方式</h1>
  <p>{{ $translate('common.email') }}</p>
  <p>{{ $translate('common.delete') }}</p>

  <el-divider/>
  <h1>依赖注入</h1>
  <p>{{ i18n.common.email }}</p>
  <p>{{ i18n.common.delete }}</p>

  <el-divider/>
  <h1>i18n</h1>
  <p>{{ $t('message.Confirm') }}</p>
  <p>{{ $t('message.Cancel') }}</p>
  <el-button type="success" text
             bg @click="changeLang('en')">English</el-button>
  <el-button type="success" text
             bg @click="changeLang('zh')">中文</el-button>


</template>
<script setup>
import { useI18n } from 'vue-i18n';

const { locale } = useI18n();

import {inject} from 'vue'
const i18n = inject('i18n')
const changeLang = (lang) => {
  locale.value = lang;
};
</script>
