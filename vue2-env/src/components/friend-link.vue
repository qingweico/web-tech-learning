<template>
  <div>
    <h2 style="text-align: center">友情链接管理</h2>
    <el-dialog :close-on-click-modal="false" :before-close="closeDialog" :visible="showDialog" :title="cuStatus === 1 ? '新增' : '编辑'" width="800px">
      <el-form ref="form" :model="form" :rules="formRules" size="small" label-width="120px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="url" prop="url">
          <el-input v-model="form.url" />
        </el-form-item>
        <el-form-item label="可用状态" prop="avl">
          <el-select v-model="form.avl" clearable placeholder="可用状态" style="width:100%">
            <el-option value="1" label="可用"></el-option>
            <el-option value="0" label="不可用"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button sid="form_column_cancel" size="mini" type="text" @click="closeDialog">关闭</el-button>
        <el-button sid="form_column_submit" size="mini" type="primary" @click="handleClick" :loading="buttonLoading">确认</el-button>
      </div>
    </el-dialog>

    <el-dialog :close-on-click-modal="false" :before-close="randomAddDialogClose" :visible="showRandomAddDialog" :title="'随机添加'" width="400px">
      <el-form ref="random" :model="random" :rules="randomFormRules" size="small" label-width="60px">
        <el-form-item label="数量" prop="quantity">
          <el-input-number v-model.number="random.quantity" :min="1" :max="9999" :precision="0" style="width: 100%;" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button sid="form_column_cancel" size="mini" type="text" @click="randomAddDialogClose">关闭</el-button>
        <el-button sid="form_column_submit" size="mini" type="primary" @click="doRandomAdd" :loading="buttonLoading">确认</el-button>
      </div>
    </el-dialog>
    <div class="crud-opts">
            <span class="crud-opts-left">
              <span style="margin-right: 10px">
                 <el-button  size="mini" type="primary" icon="el-icon-thumb" :loading="buttonLoading"
                            @click="randomAdd">随机添加</el-button>
              </span>
                <span style="margin-right: 10px">
                <el-button  size="mini" type="primary" icon="el-icon-plus" :loading="buttonLoading"
                           @click="add">添加</el-button>
                </span>
               <span style="margin-right: 10px">
                <el-button  size="mini"  type="primary" icon="el-icon-edit"
                           :disabled="multipleSelection.length !== 1" :loading="buttonLoading"
                           @click="edit(multipleSelection[0])">编辑</el-button>
               </span>
              <span style="margin-right: 10px">
                <el-button  size="mini"  type="danger" icon="el-icon-delete"
                           :loading="buttonLoading" :disabled="multipleSelection.length === 0"
                           @click="del(multipleSelection)">删除</el-button>
              </span>
              <span style="margin-right: 10px">
                 <el-button  size="mini" type="success" icon="el-icon-refresh"
                            :loading="buttonLoading"
                            @click="refresh">刷新</el-button>
              </span>
            </span>
    </div>
    <el-table
        ref="table"
        border
        :data="links"
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
      <el-table-column prop="name" label="名称" align="center" show-overflow-tooltip min-width="200px">
      </el-table-column>
      <el-table-column prop="url" label="url" align="center" show-overflow-tooltip
                       min-width="200px"></el-table-column>
      <el-table-column prop="avl" label="可用状态" align="center" show-overflow-tooltip
                       min-width="200px">
        <template slot-scope="scope">
          {{ scope.row.avl === '1' ? '可用' : scope.row.avl === '0' ? '不可用' : ''}}
        </template>
      </el-table-column>
      <el-table-column prop="create" label="创建时间" align="center" show-overflow-tooltip
                       min-width="200px">
        <template slot-scope="scope">
          {{ scope.row.create }}
        </template>
      </el-table-column>
      <el-table-column prop="update" label="更新时间" align="center" show-overflow-tooltip
                       min-width="200px">
        <template slot-scope="scope">
          {{ scope.row.update }}
        </template>
      </el-table-column>
      <el-table-column width="200px" label="操作" align="center">
        <template slot-scope="scope">
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
  name: 'friend-link',
  components: {UdBtn},
  data() {
    return {
      links: [],
      currentPage: 1,
      pageSize: 10,
      records: 0,
      multipleSelection: [],
      // 1 新增 2  编辑
      cuStatus : null,
      visible: false,
      buttonLoading: false,
      tableLoading: false,
      form: {
        name: '',
        url: '',
        avl: '1'
      },
      random: {
        quantity: 0
      },
      showDialog: false,
      showRandomAddDialog: false,
      formRules: {
        name: [
          {
            required: true,
            message: '名称必填',
            trigger: 'blur'
          }
        ],
        url: [
          {
            required: true,
            message: 'url必填',
            trigger: 'blur'
          }
        ],
        avl: [
          {
            required: true,
            message: '可用状态必填',
            trigger: 'blur'
          }
        ],
      },
      randomFormRules: {
        quantity: [
          {
            required: true,
            message: '数量必填',
            trigger: 'blur'
          }
        ]
      }
    };
  },
  created() {
    this.searchList()
  },
  methods: {
    handleClick() {
      if (this.cuStatus === 1) {
        this.doAdd();
      } else if (this.cuStatus === 2){
        this.doEdit();
      }
    },
    add() {
      this.cuStatus = 1
      this.showDialog = true
    },
    randomAddDialogClose() {
      this.showRandomAddDialog = false;
      this.random.quantity = 0
      this.$refs.random.clearValidate()
    },
    randomAdd() {
      this.showRandomAddDialog = true;
    },
    doRandomAdd() {
      this.$refs.random.validate((valid) => {
        if (valid) {
          this.buttonLoading = true
          axios.post('http://localhost:8080/fl/randomAdd/' + this.random.quantity).then(res => {
            const {data} = res
            if (data.success) {
              this.$message.success('随机添加成功')
              this.searchList();
              this.randomAddDialogClose()
            }else {
              this.$message.success('随机添加失败')
            }
            this.buttonLoading = false
          }).catch(() => {
            this.buttonLoading = false
          })
        }
      })
    },
    doAdd(){
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.buttonLoading = true
          axios.post('http://localhost:8080/fl/save', this.form).then(res => {
            const {data} = res
            if (data.success) {
              this.$message.success('添加成功')
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
    doEdit(){
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.buttonLoading = true
          axios.post('http://localhost:8080/fl/update', this.form).then(res => {
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
      axios.post('http://localhost:8080/fl/delete/' + row.id).then(res => {
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
    doBatchDelete(rows) {
      axios.post('http://localhost:8080/fl/delete', rows.map(e => e.id)).then(res => {
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
      this.$message.success('刷新成功')
    },
    edit(row) {
      this.form.id = row.id
      this.form.name = row.name
      this.form.url = row.url
      this.form.avl = row.avl
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
      axios.post('http://localhost:8080/fl/searchList', params).then(res => {
        const {data} = res
        if (data.success) {
          const result = data.data
          this.links = result.rows
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
        name: '',
        url: '',
        avl: '1'
      };
      this.cuStatus = null
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
    }
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
