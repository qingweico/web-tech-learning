<template>
  <el-calendar>
    <template #date-cell="{ data }">
      <p
          class="el-calendar-one"
          style="height: 100%;"
          :class="checkDateAvailable(data.day) ? '' : 'disabled'"
          @click="doSelect(data.day)"
      >
        {{ formatTime(data.day, format) }} {{ checkSelect(data.day) ? "✔️" : "" }}
      </p>
    </template>
  </el-calendar>
</template>

<script>
import {ElMessage} from "element-plus";
import {computed, ref, watch} from "vue";
import dayjs from "dayjs";
export default {
  name: "SelectableCalendar",
  props: {
    modelValue: {
      type: [String, Array],
      default: "",
    },
    range: {
      type: Array,
      default: () => [],
    },
    outRangeTitle: {
      type: String,
      default: "",
    },
    format: {
      type: String,
      default: "MM-dd",
    },
  },
  emits: ["update:modelValue"],
  setup(props, {emit}) {
    const checkList = ref([]);

    watch(
        () => props.modelValue,
        (newV) => {
          if (typeof newV === 'string') {
            checkList.value = newV ? newV.split(',') : [];
          } else if (Array.isArray(newV)) {
            checkList.value = newV;
          } else {
            checkList.value = [];
          }
        },
        { immediate: true }
    );


    const checkSelect = computed(() => {
      return (date) => {
        return checkList.value.indexOf(date) > -1;
      };
    });

    const formatTime = (date, fmt = "MM-DD") => {
      return dayjs(date).format(fmt);
    };

    const checkDateAvailable = (date) => {
      if (props.range && props.range.length > 0) {
        return new Date(props.range[0] + " 00:00:00").getTime() < new Date(date + " 12:00:00").getTime() && new Date(props.range[1] + " 23:59:59").getTime() > new Date(date + " 12:00:00").getTime();
      }
      return true;
    };

    const doSelect = (date) => {
      if (checkDateAvailable(date)) {
        if (checkList.value.indexOf(date) > -1) {
          checkList.value.splice(checkList.value.indexOf(date), 1);
        } else {
          checkList.value.push(date);
        }
        if (Object.prototype.toString.call(props.modelValue) === "[object String]") {
          emit("update:modelValue", checkList.value.join(","));
        } else if (Object.prototype.toString.call(props.modelValue) === "[object Array]") {
          emit("update:modelValue", checkList.value);
        }
      } else {
        props.outRangeTitle && ElMessage.error(props.outRangeTitle);
      }
    };

    return {
      checkList,
      checkSelect,
      formatTime,
      checkDateAvailable,
      doSelect,
    };
  },
};
</script>

<style lang="scss" scoped>
.el-calendar-one {
  &.disabled {
    cursor: not-allowed;
  }
}
</style>
