<template>
  <div>
    <h2 style="text-align: center">人脸信息管理</h2>
    <el-dialog :close-on-click-modal="false" :before-close="closeDialog" :visible="showDialog" :title="cuStatus === 1 ? '新增' : '编辑'" width="800px">
      <el-form ref="form" :model="form" :rules="formRules" size="small" label-width="120px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" maxlength="32" show-word-limit/>
        </el-form-item>
        <el-form-item label="人脸信息" prop="faceId">
          <el-row>
            <el-col :span="24">
              <el-radio v-model="form.enableFace" :label=true @change="enableOrDisableFaceLogin">启用</el-radio>
              <el-radio v-model="form.enableFace" :label=false @change="enableOrDisableFaceLogin">未启用</el-radio>
              <el-button class="face-btn" @click="seeFace(form.faceId)" size="mini" style="margin-right: 10px"
                         v-show="form.enableFace && form.faceId">查看人脸
              </el-button>
              <el-button @click="takeFace" size="mini" style="margin-right: 10px"
                         v-show="form.enableFace && takeFaceStatus === 1">获取人脸
              </el-button>
              <el-button @click="refreshFace" size="mini"
                         v-show="form.enableFace && takeFaceStatus=== 2">重新获取
              </el-button>
              <el-button @click="startUpload" size="mini" style="margin-right: 10px"
                         v-show="form.enableFace && takeFaceStatus=== 2">上传人脸信息
              </el-button>
              <el-button v-show="form.enableFace" type="primary" size="mini"
                         @click="openOrCloseCamera()"
                         style="margin:0 0 25px 0;">开启/关闭摄像头
              </el-button>
              <el-link style="margin-left: 15px" v-show="form.enableFace && takeFaceStatus=== 2" type="danger" disabled>
                <el-popover placement="bottom" trigger="hover">
                  <div>
                    获取人脸后记得先点击上传人脸信息按钮上传人脸信息
                  </div>
                  <i slot="reference" class="el-icon-warning-outline" />
                </el-popover>
              </el-link>
            </el-col>
            <el-col :span="12">
              <div v-show="form.enableFace && takeFaceStatus === 1">
                <video id="video" width="140px" height="150px"></video>
              </div>
              <div v-show="form.enableFace && takeFaceStatus === 2">
                <canvas id="canvas" width="140px" height="150px"></canvas>
              </div>
            </el-col>
          </el-row>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button sid="form_column_cancel" size="mini" type="text" @click="closeDialog">关闭</el-button>
        <el-button sid="form_column_submit" size="mini" type="primary" @click="handleClick" :loading="buttonLoading">确认</el-button>
      </div>
    </el-dialog>
    <el-dialog
        title="人脸信息"
        align="center"
        :visible.sync="dialogVisible"
        :before-close="handleClose"
        width="20%">
        <div class="block">
          <el-image :src="base64Image" style="width: 200px;" fit="contain"></el-image>
        </div>
      <span slot="footer" class="dialog-footer"></span>
    </el-dialog>
    <div class="crud-opts">
            <span class="crud-opts-left">
                <span style="margin-right: 10px">
                <el-button  size="mini" type="primary" icon="el-icon-plus" :loading="buttonLoading"
                           @click="add" style="margin-left: 0">添加</el-button>
                </span>
                <span style="margin-right: 10px">
                <el-button  size="mini" style="margin-left: 0" type="primary" icon="el-icon-edit"
                           :disabled="multipleSelection.length !== 1" :loading="buttonLoading"
                           @click="edit(multipleSelection[0])">编辑</el-button>
                </span>
                <span style="margin-right: 10px">
                <el-button  size="mini" style="margin-left: 0" type="danger" icon="el-icon-delete"
                           :loading="buttonLoading" :disabled="multipleSelection.length === 0"
                           @click="del(multipleSelection)">删除</el-button>
                </span>
                <span style="margin-right: 10px">
                 <el-button  size="mini" style="margin-left: 0" type="success" icon="el-icon-refresh"
                            :loading="buttonLoading"
                            @click="refresh">刷新</el-button>
                </span>
            </span>
    </div>
    <el-table
        ref="table"
        border
        :data="users"
        size="small"
        v-loading="tableLoading"
        style="width: 100%;"
        @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column label="序号" width="60" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="username" label="用户名" align="center" show-overflow-tooltip min-width="200px">
      </el-table-column>
      <el-table-column prop="avl" label="是否启用人脸信息" align="center" show-overflow-tooltip
                       min-width="200px">
        <template slot-scope="scope">
          {{ scope.row.faceId ? '已启用' : '未启用' }}
        </template>
      </el-table-column>
      <el-table-column prop="create" label="创建时间" align="center" show-overflow-tooltip
                       min-width="200px">
        <template slot-scope="scope">
          {{ scope.row.createTime }}
        </template>
      </el-table-column>
      <el-table-column prop="update" label="更新时间" align="center" show-overflow-tooltip
                       min-width="200px">
        <template slot-scope="scope">
          {{ scope.row.updateTime }}
        </template>
      </el-table-column>
      <el-table-column width="200px" label="操作" align="center">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" icon="el-icon-view" :disabled="!scope.row.faceId"
                     @click="seeFace(scope.row.faceId)" style="float: left;"></el-button>
          <ud-btn :loading="buttonLoading" @doDelete="doDelete(scope.row)" @doEdit="edit(scope.row)"></ud-btn>
        </template>
      </el-table-column>
    </el-table>
    <!--分页-->
    <div class="block">
      <el-pagination
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
          :current-page.sync="currentPage"
          :page-size="pageSize"
          :page-sizes="[10, 30, 50, 100,200, 500, 1000]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="records"
      ></el-pagination>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import UdBtn from "@/components/ud-btn.vue";
export default {
  name: 'face-list',
  components: {UdBtn},
  data() {
    return {
      users: [],
      currentPage: 1,
      pageSize: 10,
      records: 0,
      multipleSelection: [],
      // 1 新增 2  编辑
      cuStatus : null,
      buttonLoading: false,
      tableLoading: false,
      form: {
        id: '',
        username: '',
        faceId: '',
        enableFace: false
      },
      showDialog: false,
      formRules: {
        username: [
          {
            required: true,
            message: '用户名必填',
            trigger: 'blur'
          }
        ]
      },
      enablingCamera: false,
      img64: "",
      base64Image: "",
      pop: false,
      dialogVisible: false,
      mediaStreamTrack: null,
      video: null,
      isCameraOpen: false,
      // 0: 不使用人脸,
      // 1: 使用人脸,并初始化,
      // 2: 获取人脸在网页展示
      takeFaceStatus: 0,
      // 人脸数据,用于数据层交互,入库
      faceData64: "",
    };
  },
  created() {
    this.searchList()
  },
  computed: {

  },
  methods: {
    handleClick() {
      if (this.cuStatus === 1) {
        this.doAdd();
      } else if (this.cuStatus === 2){
        this.doEdit();
      }
    },
    handleClose() {
      this.base64Image = "";
      this.dialogVisible = false;
    },
    doAdd(){
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.buttonLoading = true
          axios.post('http://localhost:8080/user/cu', this.form).then(res => {
            const {data} = res
            if (data.success) {
              this.$message.success('添加成功')
              this.searchList();
              this.closeDialog()
            }else {
              this.$message.error(data.msg)
            }
            this.buttonLoading = false
          }).catch(() => {
            this.buttonLoading = false
          })
        }
      })
    },
    doEdit(){
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.buttonLoading = true
          axios.post('http://localhost:8080/user/cu', this.form).then(res => {
            const {data} = res
            if (data.success) {
              this.$message.success('修改成功')
              this.searchList();
              this.closeDialog()
            }
            this.buttonLoading = false
          }).catch(() => {
            this.buttonLoading = false
          })
        }
      })
    },
    add() {
      this.cuStatus = 1
      this.showDialog = true
    },
    edit(row) {
      this.form.id = row.id
      this.form.username = row.username
      this.form.faceId = row.faceId
      this.form.enableFace = !!row.faceId
      this.cuStatus = 2
      this.showDialog = true;
    },
    searchList() {
      this.buttonLoading  = true;
      this.tableLoading = true;
      const params = {
        page: this.currentPage,
        pageSize: this.pageSize
      }
      axios.post('http://localhost:8080/user/searchList', params).then(res => {
        const {data} = res
        if (data.success) {
          const result = data.data
          this.users = result.rows
          this.currentPage = result.currentPage
          this.records = result.totalNumber
          this.tableLoading = false;
          this.buttonLoading  = false;
        }
      }).catch(() => {
        this.tableLoading = false;
        this.buttonLoading  = false;
      })
    },
    closeDialog() {
      this.showDialog = false;
      this.form =  {
        id: '',
        username: '',
        faceId: '',
        enableFace: false
      };
      this.cuStatus = null
      this.takeFaceStatus = 0
      this.faceData64 = ''
      this.$refs.form.clearValidate()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    handleCurrentChange(val) {
      this.currentPage = val;
      this.searchList()
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.searchList()
    },
    del(rows) {
      this.$confirm(`确认删除选中的${rows.length}条数据?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.buttonLoading = true
        this.doBatchDelete(rows)
      }).catch(() => {
      })
    },
    doDelete(row) {
      this.buttonLoading = true
      axios.post('http://localhost:8080/user/del', [row.id]).then(res => {
        const {data} = res
        if (data.success) {
          this.$message.success('删除成功')
          this.buttonLoading = false
          this.searchList()
          this.pop = false
        }
        this.buttonLoading = false
      }).catch(() => {
        this.buttonLoading = false
        this.pop = false
      })
    },
    doBatchDelete(rows) {
      axios.post('http://localhost:8080/user/del', rows.map(e => e.id)).then(res => {
        const {data} = res
        if (data.success) {
          this.$message.success('删除成功')
          this.buttonLoading = false
          this.searchList()
        }
        this.buttonLoading = false
      }).catch(() => {
        this.buttonLoading = false
      })
    },
    refresh() {
      this.searchList()
      this.$message.success('刷新成功')},
    // 查看人脸信息
    seeFace(faceId) {
      axios.get('http://localhost:8080/fs/readFace64InGridFS/' + faceId ).then((response) => {
        const { data } = response
        if(data.success) {
          this.base64Image = `data:image/png;base64, ${data.data}`;
          this.dialogVisible = true;
        } else {
           this.$message.error(data.msg)
        }
      })
    },
    startUpload() {
      if (this.form.enableFace) {
        let faceData64 = this.faceData64;
        if (faceData64 === "") {
          this.$message.error("\"请点击`获取人脸`按钮获得您的人脸信息\"");
        } else {
          this.img64 = faceData64.split("base64,")[1];
          let data = {};
          data.username = this.form.username;
          data.img64 = this.img64;
          // 上传人脸信息
          axios.post('http://localhost:8080/fs/uploadToGridFs', data).then((response) => {
            const { data } = response
            if(data.success) {
              this.form.faceId = data.data;
              this.$message.success('上传人脸信息成功')
            } else {
              this.$message.error('上传人脸信息失败')
            }
          })
        }
      }
    },
    // 获取人脸照片信息
    takeFace() {
      let isFaceLogin = this.form.enableFace;
      if (isFaceLogin) {
        this.video = document.getElementById('video');
        let canvas = document.getElementById('canvas');
        let ctx = canvas.getContext('2d');
        ctx.drawImage(this.video, 0, 0, 140, 150);
        // base64的图片
        this.faceData64 = document.getElementById('canvas').toDataURL();
        // 拍照,标记装为2,表示拍过了
        this.takeFaceStatus = 2;
      }
    },
    // 使用人脸登录,人脸入库
    enableOrDisableFaceLogin() {
      let isFaceLogin = this.form.enableFace;
      if (isFaceLogin) {
          this.initCameraStatus();
      } else {
          this.clearCameraStatus();

      }
    },
    openOrCloseCamera() {
      if (this.isCameraOpen) {
        this.clearCameraStatus();
      } else {
        this.initCameraStatus();
      }
    },
    // 重新刷新,重新获取人脸
    refreshFace() {
      // 之前存入的人脸信息清除
      this.faceData64 = "";
      // 重拍,回到状态1
      this.takeFaceStatus = 1;
    },

    initCameraStatus() {
      // 如果勾选人脸登录,则需要人脸入库
      this.openFace();
      // 使用人脸,初始化
      this.takeFaceStatus = 1;
    },
    clearCameraStatus() {
      this.closeFace();
      this.isCameraOpen = false;
      // 不使用人脸
      this.takeFaceStatus = 0;
    },
    // 打开人脸摄像头
    openFace() {
      const _this = this;
      // 如果摄像头已经打开了,就不再继续
      if (_this.isCameraOpen) {
        return;
      }

      let constraints = {
        video: {width: 140, height: 150},
        audio: false
      };
      // 获得video摄像头
      this.video = document.getElementById('video');
      let promise = navigator.mediaDevices.getUserMedia(constraints);
      promise.then((mediaStream) => {
        _this.mediaStreamTrack = mediaStream.getVideoTracks()
        _this.video.srcObject = mediaStream;
        _this.video.play();
      });
      _this.video.onloadedmetadata = function () {
        _this.isCameraOpen = true;
      };
    },
    // 关闭人脸摄像头
    closeFace() {
      let stream = document.getElementById('video').srcObject;
      if (stream == null) {
        return;
      }
      let tracks = stream.getTracks();

      tracks.forEach(function (track) {
        track.stop();
      });
      document.getElementById('video').srcObject = null;
    },
  }
}
</script>

<style lang="scss" scoped>
.crud-opts {
  padding: 4px 0 8px 0;
  display: flex;
  align-items: flex-end;
}
.crud-opts .crud-opts-left {
  flex: 1;
}
</style>
