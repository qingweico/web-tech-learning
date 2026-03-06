<template>
  <a-card :bordered="false">

    <!--导航区域-->
    <div>
      <a-tabs defaultActiveKey="1" @change="callback">
        <a-tab-pane tab="登录日志" key="login"></a-tab-pane>
        <a-tab-pane tab="操作日志" key="operation"></a-tab-pane>
      </a-tabs>
    </div>

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="12">

          <a-col :md="4" :sm="8">
            <a-form-item label="操作人">
              <a-input placeholder="请输入操作人" v-model="queryParam.username"></a-input>
            </a-form-item>
          </a-col>

          <a-col :md="4" :sm="8">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel">
            <a-icon type="delete"/>
            删除
          </a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作
          <a-icon type="down"/>
        </a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a
          style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
          ref="table"
          size="middle"
          bordered
          rowKey="id"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="ipagination"
          :loading="loading"
          :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
          @change="handleTableChange">

         <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">详情</a>
        </span>

      </a-table>
    </div>
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <operationLog-modal ref="modalForm" @ok="modalFormOk"></operationLog-modal>
  </a-card>
</template>

<script>
import OperationLogModal from './modules/OperationLogModal'
import {JeecgListMixin} from '@/mixins/JeecgListMixin'
import {deleteAction, getAction} from '@/api/manage'
import {filterObj} from '@/utils/util';
import JEllipsis from '@/components/jeecg/JEllipsis'

export default {
  name: "OperationLogList",
  mixins: [JeecgListMixin],
  components: {
    OperationLogModal,
    JEllipsis
  },
  data() {
    let ellipsis = (v, l = 10) => (<j-ellipsis value={v} length={l}/>)
    return {
      description: '操作日志管理页面',
      // 表头
      columns: [
        {
          title: '序号',
          dataIndex: '',
          key: 'rowIndex',
          width: 50,
          align: "center",
          customRender: function (t, r, index) {
            return parseInt(index) + 1;
          }
        },
        {
          title: '等级',
          align: "center",
          dataIndex: 'level',
          width: 50
        },
        {
          title: '操作人',
          align: "center",
          dataIndex: 'username',
          width: 120
        },
        {
          title: 'IP',
          align: "center",
          dataIndex: 'ip',
          width: 100
        },
        {
          title: '日志描述',
          align: "center",
          dataIndex: 'describe',
          width: 150,
          customRender: (t) => ellipsis(t),
        },
        {
          title: '操作类型',
          align: "center",
          dataIndex: 'operationType',
          width: 110
        },
        {
          title: '被操作的对象',
          align: "center",
          dataIndex: 'operationUnit',
          width: 110
        },
        {
          title: '方法名称',
          align: "center",
          dataIndex: 'method',
          customRender: (t) => ellipsis(t),
          width: 150
        },
        {
          title: '运行时间',
          align: "center",
          dataIndex: 'runTime',
          width: 100
        },
        {
          title: '异常信息',
          align: "center",
          dataIndex: 'exceptionMsg',
          customRender: (t) => ellipsis(t),
        },
        {
          title: '异常代码',
          align: "center",
          dataIndex: 'exceptionCode',
          customRender: (t) => ellipsis(t),
        },
        {
          title: '创建时间',
          align: "center",
          dataIndex: 'created'
        },
        {
          title: '操作',
          dataIndex: 'action',
          scopedSlots: {customRender: 'action'},
          align: 'center',
          width: 60
        }
      ],
      url: {
        list: "/operationLog/operationLog/list",
        delete: "/operationLog/operationLog/delete",
        deleteBatch: "/operationLog/operationLog/deleteBatch",
        exportXlsUrl: "operationLog/operationLog/exportXls",
        importExcelUrl: "operationLog/operationLog/importExcel",
      },
      queryParam: {
        "opTime_begin": "",
        "opTime_end": "",
        "type": "-1",
        "operationType":"login",
      },
      isorter: {
        column: 'created',
        order: 'desc',
      },
    }
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
    }
  },
  created() {
    this.keyupEnter()
  },
  methods: {
    handleEdit: function (record) {
      this.$refs.modalForm.edit(record);
      this.$refs.modalForm.title = "详情";
      this.$refs.modalForm.disableSubmit = false;
    },
    onChangeStartTime(date, dateString) {
      this.queryParam.opTime_begin = dateString;
    },

    onChangeEndTime(date, dateString) {
      this.queryParam.opTime_end = dateString;
    },
    searchQuery() {
      this.loadData(1);
    },
    searchReset() {
      let that = this;
      var operationType = that.queryParam.operationType;
      this.queryParam = {}
      that.queryParam.operationType = operationType;
      this.loadData(1)
    },
    // 日志类型
    callback(key) {
      console.log(key)
      let that = this;
      that.queryParam.operationType = key;
      that.loadData();
    },
    getQueryParams() {
      var param = Object.assign({}, this.isorter, this.queryParam);
      param.field = this.getQueryField();
      param.pageNo = this.ipagination.current;
      param.pageSize = this.ipagination.pageSize;
      if (this.queryParam.opTime_begin != "" && this.queryParam.opTime_end != "") {
        param.opTime_begin = this.queryParam.opTime_begin + " 00:00:00";
        param.opTime_end = this.queryParam.opTime_end + " 23:59:59";
      }

      // delete param.CreatedRange; // 时间参数不传递后台
      return filterObj(param);
    },
    loadData(arg) {
      if (!this.url.list) {
        this.$message.error("请设置url.list属性!")
        return
      }
      //加载数据 若传入参数1则加载第一页的内容
      if (arg === 1) {
        this.ipagination.current = 1;
      }
      var params = this.getQueryParams();//查询条件
      this.loading = true;
      getAction(this.url.list, params).then((res) => {
        if (res.success) {
          this.dataSource = res.result.records;
          this.ipagination.total = res.result.total;
        }
        if (res.code === 510) {
          this.$message.warning(res.message)
        }
        this.loading = false;
      })
    },

    batchDel: function () {
      if (!this.url.deleteBatch) {
        this.$message.error("请设置url.deleteBatch属性!")
        return
      }
      if (this.selectedRowKeys.length <= 0) {
        this.$message.warning('请选择一条记录！');
        return;
      } else {
        var ids = "";
        for (var a = 0; a < this.selectionRows.length; a++) {
          ids += this.selectionRows[a].id + ",";
        }
        console.log(this.selectionRows)
        var that = this;
        this.$confirm({
          title: "确认删除",
          content: "是否删除选中数据?",
          onOk: function () {
            console.log(ids)
            deleteAction(that.url.deleteBatch, {ids: ids}).then((res) => {
              if (res.success) {
                that.$message.success(res.message);
                that.loadData();
                that.onClearSelected();
              } else {
                that.$message.warning(res.message);
              }
            });
          }
        });
      }
    },
    handleDelete: function (invateuserdetailId) {
      if (!this.url.delete) {
        this.$message.error("请设置url.delete属性!")
        return
      }
      var that = this;
      deleteAction(that.url.delete, {invateuserdetailId: invateuserdetailId}).then((res) => {
        if (res.success) {
          that.$message.success(res.message);
          that.loadData();
        } else {
          that.$message.warning(res.message);
        }
      });
    },
    keyupEnter() {
      document.onkeydown = e => {
        let body = document.getElementsByTagName('body')[0]
        if (e.keyCode === 13) {
          this.loadData(1);
        }
      }
    },

  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>
