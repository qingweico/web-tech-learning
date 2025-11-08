<template>
  <div>
    <el-button v-if="editable" :icon="Upload" type="primary" size="small" @click="openFileUpload">{{buttonName}}
    </el-button>

    <div v-if="componentFileDetails.length === 0 && !editable" class="rmk">暂无附件</div>

    <!-- 文件附件列表 -->
    <div v-if="showFileList" class="fileList">
      <div v-for="(item, index) in componentFileDetails" :key="item.fileId || index" class="one">
        <div class="info" :class="{ cursorPoint: isPreviewable(item) }" @click="handleInfoClick(item)">
          <el-icon>
            <component :is="getFileIcon(item.type)"/>
          </el-icon>
          <span class="name">
            {{ item.originalName }} ({{ formatSize(item.fileSize) }}M)
            <template v-if="showTime">&nbsp;<!-- 可扩展显示时间 --> </template>
          </span>
        </div>

        <div class="menu">
          <el-tooltip content="删除" placement="top">
            <div v-if="editable" class="close-icon" @click.stop="deleteFile(item, index)">
              <el-icon>
                <Close/>
              </el-icon>
            </div>
          </el-tooltip>

          <el-tooltip content="下载" placement="top">
            <div class="close-icon" @click.stop="downloadFile(item)">
              <el-icon>
                <Download/>
              </el-icon>
            </div>
          </el-tooltip>

          <el-tooltip content="播放" placement="top">
            <div v-if="item.type === 'audio' || item.type === 'video'" class="close-icon" @click.stop="audioPlay(item)">
              <el-icon>
                <CaretRight/>
              </el-icon>
            </div>
          </el-tooltip>

          <el-tooltip content="预览" placement="top">
            <div v-if="item.type === 'image'" class="close-icon" @click.stop="imagePreview(item)">
              <el-icon>
                <View/>
              </el-icon>
            </div>
            <div v-else-if="isPreviewable(item)" class="close-icon" @click.stop="filePreview(item)">
              <el-icon>
                <View/>
              </el-icon>
            </div>
          </el-tooltip>
        </div>
      </div>
    </div>

    <!-- 上传弹窗 -->
    <el-dialog v-model="fileUpload" :destroy-on-close="true" :append-to-body="isFalse" title="选择附件" center>
      <el-upload
          ref="fileUploadRef"
          :headers="myHeaders"
          :data="uploadData"
          :name="fileName"
          :auto-upload="autoUpload"
          :action="action"
          :list-type="listType"
          :accept="accept"
          :multiple="multiple"
          :limit="computedFileLimit"
          :file-list="fileList"
          :before-upload="beforeFileUpload"
          :on-change="changeFile"
          :on-success="handleFileSuccess"
          :on-error="handleFail"
          :on-exceed="handleExceed"
          drag
          class="upload"
      >
        <el-button :icon="Upload" type="text">点击或拖拽上传文件</el-button>
        <template #tip>
          <div class="el-upload__tip">
            <span v-if="acceptFile">只能上传{{ acceptFile }}文件</span>
            <span v-else-if="accept">只能上传{{ accept }}文件</span>
            (单个文件不能超过{{ sizeLimit }}M), 最多允许选择{{ multiple ? fileLimit : 1 }}个文件,
            还剩余可上传{{ computedFileLimit }}个
          </div>
        </template>
      </el-upload>
      <template #footer>
        <el-button class="pan-btn light-blue-btn" size="small" @click="submitFileUpload">确认上传</el-button>
      </template>
    </el-dialog>

    <!-- 音视频弹窗 -->
    <el-dialog v-model="dialogAudioVisible" :title="selectAudio.originalName" :append-to-body="isFalse" center>
      <div style="width: 100%;text-align: center;max-height: 100vh;">
        <audio v-if="selectAudio.type === 'audio'" :id="`audio-${selectAudio.fileId}`" :src="selectAudio.src" controls
               autoplay style="width: 80%;max-height: 60vh;">
          您的浏览器不支持audio标签
        </audio>
        <video v-if="selectAudio.type === 'video'" :id="`video-${selectAudio.fileId}`" :src="selectAudio.src" controls
               autoplay style="width: 80%;max-height: 60vh;">
          您的浏览器不支持video标签
        </video>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import {computed, nextTick, onBeforeUnmount, ref, watch} from "vue";
import {CaretRight, Close, Document, Download, Picture, Upload, View} from "@element-plus/icons-vue";
import Ajax from "axios";
import {ElMessage, ElMessageBox} from "element-plus";

// 组件名
defineOptions({name: "UploadFile"});

const props = defineProps({
  showFileEdit: {type: Boolean, default: false},
  showFileView: {type: Boolean, default: true},
  showFileList: {type: Boolean, default: true},
  showCropper: {type: Boolean, default: false},
  cropperOptions: {type: Object, default: () => ({})},
  buttonName: {type: String, default: "添加附件"},
  isFalse: {type: Boolean, default: true},
  editable: {type: Boolean, default: true},
  public: {type: Boolean, default: false},
  watermark: {type: Boolean, default: false},
  fileName: {type: String, default: "file"},
  showTime: {type: Boolean, default: false},
  appId: {type: String, default: ""},
  files: {type: [String, Array], required: true},
  autoUpload: {type: Boolean, default: false},
  action: {type: String, default: process.env.BASE_API + "/file/upload"},
  downloadUrl: {type: String, default: ""},
  sizeLimit: {type: Number, default: 20},
  listType: {type: String, default: "text"},
  accept: {type: String, default: ""},
  acceptFile: {type: String, default: ""},
  multiple: {type: Boolean, default: false},
  fileLimit: {type: Number, default: 10},
});

const emit = defineEmits(["uploadSuccess"]);

// refs
const platform = ref(process.env.platform);
const componentFile = ref("");
const componentFileIds = ref([]);
const componentFileDetails = ref([]);
const fileList = ref([]);
const fileUpload = ref(false);
const fileUploadRef = ref(null);
const dialogAudioVisible = ref(false);
const selectAudio = ref({});
const fileIndex = ref(0);

// computed
const computedFileLimit = computed(() => {
  if (props.multiple === true) {
    const already = Array.isArray(props.files) ? props.files.length : (props.files ? 1 : 0);
    return Math.max(props.fileLimit - already, 0);
  }
  return 1;
});

const myHeaders = computed(() => ({
  // 需要时在这里添加token等头
}));

const uploadData = computed(() => ({public: props.public}));

// icon mapping
const getFileIcon = (type) => (type === "image" ? Picture : Document);

// helpers
const formatSize = (size) => {
  if (!size) return "0.000";
  const m = size / (1024 * 1024);
  return m.toFixed(3);
};

const isPreviewable = (item) => {
  if (!props.showFileView) return false;
  return (
      item.type === "image"
  );
};

watch(
    () => JSON.stringify(props.files || []),
    async (val) => {
      // 统一把 props.files 解析为 ids 数组
      if (!props.files) {
        componentFile.value = "";
        componentFileIds.value = [];
        componentFileDetails.value = [];
        return;
      }

      if (typeof props.files === "string") {
        componentFile.value = props.files;
        componentFileIds.value = props.files ? props.files.split(",") : [];
      } else if (Array.isArray(props.files)) {
        componentFileIds.value = [...props.files];
      }


      const needIds = componentFileIds.value.filter((id) => !componentFileDetails.value.find((d) => d.fileId === id));
      if (needIds.length > 0) {
        const details = await getFilesDetail(needIds);
        // 合并去重
        const merged = [...(componentFileDetails.value || [])];
        details.forEach((d) => {
          if (!merged.find((m) => m.fileId === d.fileId)) merged.push(d);
        });
        componentFileDetails.value = merged;
      }
    },
    {immediate: true}
);

const getFilesDetail = async (ids) => {
  if (!ids || ids.length === 0) return [];
  try {
    const resp = await Ajax.post("/file/info", {fileId: ids});
    const data = (resp && resp.data) || [];
    return data.map((value, i) => {
      const v = value || {fileMime: "image/jpeg", fileId: ids[i], originalName: ids[i]};
      v.type = v.fileMime ? v.fileMime.split("/")[0] : "file";
      v.src = props.downloadUrl ? `${props.downloadUrl}${v.fileId}?token=` : `${process.env.BASE_API}/file/download/${v.fileId}?token=`;
      return v;
    });
  } catch (err) {
    console.error("获取文件详情失败:", err);
    return [];
  }
};

const getFilesList = () => (props.multiple === false ? componentFileDetails.value[0] : componentFileDetails.value);

// 操作方法
const openFileUpload = () => {
  const already = componentFileIds.value.length || 0;
  if (props.multiple && props.fileLimit <= already) {
    ElMessage.error("超过最大文件上传限制!");
    return;
  }
  fileUpload.value = true;
  nextTick(() => fileUploadRef.value && fileUploadRef.value.clearFiles());
};

const handleFileSuccess = async (res, file, fl) => {
  if (res && res.success) {
    if (!props.multiple) {
      componentFile.value = res.data && res.data[0];
      const detail = await getFilesDetail([componentFile.value]);
      componentFileDetails.value = detail;
      emit("uploadSuccess", componentFile.value, detail[0] || {});
      fileUpload.value = false;
    } else {
      componentFileIds.value.push((res.data && res.data[0]) || "");
      fileIndex.value++;
      if (fileIndex.value === fl.length && fl.length > 0) {
        const details = await getFilesDetail(componentFileIds.value);
        componentFileDetails.value = details;
        emit("uploadSuccess", componentFileIds.value, details);
        fileUpload.value = false;
      }
    }
  } else {
    ElMessage.error(`${file.name} 上传失败,${(res && res.message) || ""}`);
    fileUploadRef.value && fileUploadRef.value.clearFiles();
    fileUpload.value = false;
  }
};

const handleFail = (err, file) => {
  ElMessage.error(`${file.name} 上传失败`);
  fileUploadRef.value && fileUploadRef.value.clearFiles();
};

const handleExceed = () => ElMessage.error("超出可上传最大文件数");

const beforeFileUpload = (file) => {
  let allowed = true;
  if (props.acceptFile) {
    const acceptList = props.acceptFile.includes(",") ? props.acceptFile.split(",") : props.acceptFile.split(";");
    allowed = acceptList.some((ext) => file.name.toLowerCase().endsWith(ext.toLowerCase()));
  }
  if (!allowed) {
    ElMessageBox.alert(`上传附件只能是 ${props.acceptFile || props.accept} 格式!`, "提示", {type: "warning"});
  }
  const sizeOK = file.size / 1024 / 1024 < props.sizeLimit;
  if (!sizeOK) {
    ElMessageBox.alert(`上传附件大小不得超过 ${props.sizeLimit}M!`, "提示", {type: "warning"});
  }
  return allowed && sizeOK;
};

const changeFile = (file, fl) => {
  // 如果需要可以在这里做文件校验/预览生成等
};

const submitFileUpload = () => {
  fileIndex.value = 0;
  fileUploadRef.value && fileUploadRef.value.submit();
  fileUpload.value = false
};

const deleteFile = (file, index) => {
  ElMessageBox.confirm("确定要删除该附件吗?(保存之后彻底删除)", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  })
      .then(() => {
        const fileId = componentFileDetails.value[index].fileId;
        componentFileDetails.value.splice(index, 1);
        if (!props.multiple) {
          componentFile.value = "";
          emit("uploadSuccess", componentFile.value, null);
        } else {
          const idx = componentFileIds.value.findIndex((one) => one === fileId);
          if (idx > -1) componentFileIds.value.splice(idx, 1);
          emit("uploadSuccess", componentFileIds.value, componentFileDetails.value);
        }
      })
      .catch(() => {
      });
};

const filePreview = async (item) => {
  try {
    const res = await Ajax.post("/file/preview", {fileId: item.fileId, mobile: platform.value === "pc" ? "0" : "1"});
    if (res && res.data && res.data.url) window.open(res.data.url, "_blank");
  } catch (err) {
    console.error(err);
  }
};

const imagePreview = (file) => {
  window.open(file.src, "_blank");
};

const audioPlay = (file) => {
  selectAudio.value = file;
  dialogAudioVisible.value = true;
};

const downloadFile = (file) => {
  window.location.href = file.src;
};

const handleInfoClick = (item) => {
  if (item.type === "image") return imagePreview(item);
  if (item.type === "audio" || item.type === "video") return audioPlay(item);
  if (isPreviewable(item)) return filePreview(item);
};

// 组件卸载时停止播放
onBeforeUnmount(() => {
  if (selectAudio.value && selectAudio.value.fileId) {
    const audio = document.getElementById(`audio-${selectAudio.value.fileId}`);
    const video = document.getElementById(`video-${selectAudio.value.fileId}`);
    audio && audio.pause();
    video && video.pause();
  }
});
</script>

<style scoped lang="scss">
.rmk {
  color: #999;
  font-size: 12px;
}

.fileList {
  margin-top: 10px;

  .one {
    display: flex;
    align-items: center;
    margin-bottom: 10px;

    .info {
      flex: 1;
      display: flex;
      align-items: center;
      padding: 5px 10px;
      background: #f5f7fa;
      border-radius: 4px;

      i {
        color: #909399;
        margin-right: 5px;
      }

      .name {
        color: #606266;
        font-size: 12px;
        margin-left: 5px;
      }

      &.cursorPoint {
        cursor: pointer;
      }
    }

    .menu {
      display: flex;
      align-items: center;

      .close-icon {
        margin-left: 5px;
        color: #909399;
        cursor: pointer;

        &:hover {
          color: #409eff;
        }
      }
    }
  }
}

.upload {
  text-align: center;

  .el-upload {
    width: 100%;

    .el-upload-dragger {
      width: 100%;
    }
  }
}
</style>
