<template>
  <!-- 普通文本显示模式 -->
  <span v-if="tagType === 'text'" :title="displayText" class="j-dict-text">
    {{ displayText }}
  </span>

  <!-- 标签显示模式 -->
  <a-tag v-else-if="tagType === 'tag'" :title="displayText" :color="tagColor" :class="textClass" :disabled="disabled">
    {{ displayText }}
  </a-tag>

  <!-- 单选框模式 -->
  <a-radio-group v-else-if="tagType === 'radio'" @change="handleInput" :value="value" :disabled="disabled">
    <a-radio v-for="(item, key) in dictOptions" :key="key" :value="item.value">{{ item.text }}</a-radio>
  </a-radio-group>

  <!-- 下拉选择模式 -->
  <a-select
    v-else-if="tagType === 'select'"
    :placeholder="placeholder"
    :disabled="disabled"
    :value="value"
    @change="handleInput"
  >
    <a-select-option v-for="(item, key) in dictOptions" :key="key" :value="item.value">
      <span style="display: inline-block; width: 100%" :title="item.text || item.label">
        {{ item.text || item.label }}
      </span>
    </a-select-option>
  </a-select>
</template>

<script>
import { ajaxGetDictItems } from '@/api/api'

export default {
  name: 'JDictSelectTag',
  props: {
    dictCode: {
      type: String,
      default: '',
    },
    placeholder: {
      type: String,
      default: '',
    },
    triggerChange: Boolean,
    disabled: Boolean,
    value: {
      type: String,
      default: undefined,
    },
    type: {
      type: String,
      default: '',
    },
    // 新增：文本显示的类名
    textClass: {
      type: String,
      default: '',
    },
    // 新增：是否显示未找到时的占位符
    showEmptyText: {
      type: Boolean,
      default: true,
    },
    // 新增：未找到时的占位符文本
    emptyText: {
      type: String,
      default: '-',
    },
    // 新增：标签颜色（直接传入颜色值）
    tagColor: {
      type: String,
      default: '',
    },
    // 新增：颜色映射（根据值自动匹配颜色）
    colorMap: {
      type: Object,
      default: () => ({}),
    },
    // 新增：是否使用预设颜色
    usePresetColor: {
      type: Boolean,
      default: false,
    },
    // 新增：标签大小
    tagSize: {
      type: String,
      default: 'default',
      validator: (value) => ['default', 'small', 'large'].includes(value),
    },
  },
  data() {
    return {
      dictOptions: [],
      tagType: '',
      // 预设颜色方案
      presetColors: {
        '0': '#f50',
        '1': '#2db7f5',
        '2': '#87d068',
        '3': '#108ee9',
        '4': '#f5222d',
        '5': '#fa8c16',
        '6': '#52c41a',
        '7': '#1890ff',
        '8': '#722ed1',
        '9': '#eb2f96',
        success: '#52c41a',
        processing: '#1890ff',
        error: '#f5222d',
        warning: '#faad14',
        default: '#d9d9d9',
      },
    }
  },
  computed: {
    // 计算显示的文本
    displayText() {
      if (!this.value && this.value !== 0) {
        return this.emptyText
      }

      const option = this.dictOptions.find((item) => {
        return item.value === this.value || item.value === this.value
      })

      if (option) {
        return option.text || option.label || this.value
      }

      // 如果没找到对应的字典项
      if (this.showEmptyText) {
        return this.emptyText
      }

      return this.value
    },

    // 计算标签颜色
    computedTagColor() {
      // 1. 优先使用直接传入的颜色
      if (this.tagColor) {
        return this.tagColor
      }

      // 2. 使用颜色映射
      if (this.colorMap && this.colorMap[this.value]) {
        return this.colorMap[this.value]
      }

      // 3. 使用预设颜色方案
      if (this.usePresetColor) {
        // 尝试从字典项中获取颜色
        const option = this.dictOptions.find((item) => {
          return item.value === this.value || item.value === this.value
        })

        if (option && option.color) {
          return option.color
        }

        // 根据值使用预设颜色
        if (this.presetColors[this.value]) {
          return this.presetColors[this.value]
        }

        // 根据文本长度或内容生成固定颜色
        if (this.displayText) {
          const colors = [
            '#f50',
            '#2db7f5',
            '#87d068',
            '#108ee9',
            '#f5222d',
            '#fa8c16',
            '#52c41a',
            '#1890ff',
            '#722ed1',
            '#eb2f96',
          ]
          const index = Math.abs(this.hashCode(this.displayText)) % colors.length
          return colors[index]
        }
      }

      // 4. 默认返回空，使用 Ant Design 的默认标签样式
      return ''
    },
  },
  created() {
    if (!this.type || this.type === 'list') {
      this.tagType = 'select'
    } else {
      this.tagType = this.type
    }
    //获取字典数据
    this.initDictData()
  },
  methods: {
    // 字符串哈希函数，用于生成一致的随机颜色
    hashCode(str) {
      let hash = 0
      for (let i = 0; i < str.length; i++) {
        hash = str.charCodeAt(i) + ((hash << 5) - hash)
      }
      return hash
    },

    initDictData() {
      //根据字典Code, 初始化字典数组
      ajaxGetDictItems(this.dictCode, null).then((res) => {
        if (res.success) {
          this.dictOptions = res.result
        }
      })
    },

    handleInput(e) {
      let val
      if (this.tagType === 'radio') {
        val = e.target.value
      } else {
        val = e
      }
      if (this.triggerChange) {
        this.$emit('change', val)
      } else {
        this.$emit('input', val)
      }
    },
  },
}
</script>

<style scoped>
/* 文本显示样式 */
.j-dict-text {
  display: inline-block;
  word-break: break-word;
  vertical-align: middle;
}

/* 可以添加一些常用样式类 */
.j-dict-text.ellipsis {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 100%;
}

.j-dict-text.bold {
  font-weight: bold;
}

.j-dict-text.primary {
  color: #1890ff;
}

.j-dict-text.success {
  color: #52c41a;
}

.j-dict-text.warning {
  color: #faad14;
}

.j-dict-text.error {
  color: #f5222d;
}
</style>
