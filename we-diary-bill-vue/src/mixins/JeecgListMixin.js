/**
 * 新增修改完成调用 modalFormOk方法 编辑弹框组件ref定义为modalForm
 * 高级查询按钮调用 superQuery方法  高级查询组件ref定义为superQueryModal
 * data中url定义 list为查询列表  delete为删除单条记录  deleteBatch为批量删除
 */
import {filterObj} from '@/utils/util';
import {deleteAction, postAction, getAction, downFile} from '@/api/manage'
import Vue from 'vue'
import {ACCESS_TOKEN} from "@/store/mutation-types"
import JDate from '@/components/jeecg/JDate.vue'


export const JeecgListMixin = {

  components: {
    JDate
  },
  filters: {
    booleanTypeFilter(status) {
      return booleanTypeKeyValue[status]
    },
  },
  data() {
    return {
      //token header
      tokenHeader: {
        'X-Access-Token': Vue.ls.get(ACCESS_TOKEN)
      },
      /* 查询条件-请不要在queryParam中声明非字符串值的属性 */
      queryParam: {},
      /* 数据源 */
      dataSource: [],
      timer:null,
      /* 分页参数 */
      ipagination: {
        current: 1,
        pageSize: 10,
        // pageSizeOptions: ['10', '20', '30'],
        pageSizeOptions: ['10', '20', '50', '100'],
        showTotal: (total, range) => {
          return range[0] + "-" + range[1] + " 共" + total + "条"
        },
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0
      },
      /* 排序参数 */
      isorter: {
        column: 'created',
        order: 'desc',
      },
      /* 筛选参数 */
      filters: {},
      /* table加载状态 */
      loading: false,
      /* table选中keys*/
      selectedRowKeys: [],
      /* table选中records*/
      selectionRows: [],
      /* 查询折叠 */
      toggleSearchStatus: false,
      /* 高级查询条件生效状态 */
      superQueryFlag: false,
      /* 高级查询条件 */
      superQueryParams: "",
      disableSubmit: false,
      booleanTypeOptions: [{
        key: 0,
        display_name: '否'
      }, {
        key: 1,
        display_name: '是'
      }],

      payTypeOptions: [{
        key: 1,
        display_name: '支付宝支付'
      }, {
        key: 2,
        display_name: '微信支付'
      }, {
        key: 3,
        display_name: '银联支付'
      }],

      /**
       * 1 卡券类 2服务类 3实物类
       */
      activityTypeOptions: [{
        key: 1,
        display_name: '卡券类'
      }, {
        key: 2,
        display_name: '服务类'
      }, {
        key: 3,
        display_name: '实物类'
      }],

      /**
       * 1秒杀，2限时抢购'
       */
      instanceTypeOptions: [{
        key: 1,
        display_name: '秒杀'
      }, {
        key: 2,
        display_name: '限时抢购'
      }],

      orderStatusOptions: [{
          key: 0,
          display_name: '未支付'
        },
        {
          key: 1,
          display_name: '待使用'
        },
        {
          key: 2,
          display_name: '已使用'
        },
        {
          key: 3,
          display_name: '待发货'
        },
        {
          key: 4,
          display_name: '待收货'
        },
        {
          key: 5,
          display_name: '已完成'
        },
        {
          key: 21,
          display_name: '退款中'
        },
        {
          key: 22,
          display_name: '已退款'
        }
      ]
    }
  },
  created() {
    //全局键盘enter事件
    this.initKeyUpEnter();
  },
  mounted() {

    this.loadData();
    //初始化字典配置 在自己页面定义
    this.initDictConfig();
  },
  computed: {
    // payTypeKeyValue: () => {
    //   return this.payTypeOptions.reduce((acc, cur) => {
    //     acc[cur.key] = cur.display_name
    //     return acc
    //   }, {})
    // },
    payTypeKeyValue: function () {
      return this.payTypeOptions.reduce((acc, cur) => {
        acc[cur.key] = cur.display_name
        return acc
      }, {});
    },
    booleanTypeKeyValue: function () {
      return this.booleanTypeOptions.reduce((acc, cur) => {
        acc[cur.key] = cur.display_name
        return acc
      }, {});
    },
    activityTypeKeyValue: function () {
      return this.activityTypeOptions.reduce((acc, cur) => {
        acc[cur.key] = cur.display_name
        return acc
      }, {});
    },
    orderStatusKeyValue: function () {
      return this.orderStatusOptions.reduce((acc, cur) => {
        acc[cur.key] = cur.display_name
        return acc
      }, {});
    }
  },
  methods: {
    filterObj: filterObj,
    getAction: getAction,
    deleteAction: deleteAction,
    postAction: postAction,
    loadData: function (arg) {
      if (!this.url.list) {
        // this.$message.error("请设置url.list属性!")
        return
      }
      //加载数据 若传入参数1则加载第一页的内容
      if (arg === 1) {
        this.ipagination.current = 1;
      }
      var params = this.getSearchQueryParams(); //查询条件
      this.loading = true;
      getAction(this.url.list, params).then((res) => {
        if (res.success || res.code == 200) {
          if (res.data) {
            this.dataSource = res.data.records || res.data;
            this.ipagination.total = res.data.total;
          } else if (res.result) {
            this.dataSource = res.result.records || res.result;
            this.ipagination.total = res.result.total;
          }
        }
        if (res.code === 510) {
          this.$message.error(res.message || res.msg)
        }
        this.loading = false;
      })
    },
    initDictConfig: function () {
      // console.log("--这是一个假的方法!")
    },
    handleSuperQuery: function (arg) {
      //高级查询方法
      if (!arg) {
        this.superQueryParams = ''
        this.superQueryFlag = false
      } else {
        this.superQueryFlag = true
        this.superQueryParams = JSON.stringify(arg)
      }
      this.loadData()
    },
    getSearchQueryParams: function () {
      //获取查询条件
      let sqp = {}
      if (this.superQueryParams) {
        sqp['superQueryParams'] = encodeURI(this.superQueryParams)
      }
      var param = this.getQueryParams(sqp);
      param.field = this.getQueryField();
      param.pageNo = this.ipagination.current;
      param.pageSize = this.ipagination.pageSize;
      return filterObj(param);
    },
    getQueryParams: function (sqp = {}) {
      return Object.assign(sqp, this.queryParam, this.isorter, this.filters);
    },
    //查看当前Vue文件里 Data里设置的Columns属性的dataIndex
    getQueryField: function () {
      //TODO 字段权限控制
      var str = "id";
      this.columns.forEach(function (value) {
        if (value.dataIndex) {
          str += "," + value.dataIndex;
        }
      });
      return str;
    },

    onSelectChange: function (selectedRowKeys, selectionRows) {
      if(selectedRowKeys.length>200)
      {
        return
      }
      this.selectedRowKeys = selectedRowKeys;
      this.selectionRows = selectionRows;
    },
    onClearSelected: function () {
      this.selectedRowKeys = [];
      this.selectionRows = [];
    },
    searchQuery: function () {
      if(this.queryParam.Created_begin!=null&&this.queryParam.Created_end!=null){
        if(this.queryParam.Created_begin>this.queryParam.Created_end){
          this.$message.warning("开始时间不能大于结束时间");
          return;
        }
      }
      this.loadData(1);
    },
    superQuery: function () {
      this.$refs.superQueryModal.show();
    },
    searchReset: function () {
      this.queryParam = {}
      this.loadData(1);
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
        for (var a = 0; a < this.selectedRowKeys.length; a++) {
          ids += this.selectedRowKeys[a] + ",";
        }
        var that = this;
        this.$confirm({
          title: "确认删除",
          content: "是否删除选中数据?",
          onOk: function () {
            deleteAction(that.url.deleteBatch, {
              ids: ids
            }).then((res) => {
              if (res.success || res.code == 200) {
                that.$message.success(res.message || res.msg);
                that.loadData();
                that.onClearSelected();
              } else {
                that.$message.warning(res.message || res.msg);
              }
            });
          }
        });
      }
    },
    handleDelete: function (id) {
      if (!this.url.delete) {
        this.$message.error("请设置url.delete属性!")
        return
      }
      var that = this;
      deleteAction(that.url.delete, {
        id: id
      }).then((res) => {
        if (res.success || res.code == 200) {
          that.$message.success(res.message);
          that.loadData();
        } else {
          that.$message.warning(res.message);
        }
      });
    },
    handleEdit: function (record) {
      this.$refs.modalForm.edit(record);
      this.$refs.modalForm.title = "编辑";
      this.$refs.modalForm.disableSubmit = false;
    },
    handleAdd: function () {
      this.$refs.modalForm.add();
      this.$refs.modalForm.title = "新增";
      this.$refs.modalForm.disableSubmit = false;
    },
    handleTableChange(pagination, filters, sorter) {
      //分页、排序、筛选变化时触发
      //TODO 筛选
      if (Object.keys(sorter).length > 0) {
        this.isorter.column = sorter.field;
        this.isorter.order = "ascend" == sorter.order ? "asc" : "desc"
      }
      this.ipagination = pagination;
      this.loadData();
    },
    handleToggleSearch() {
      this.toggleSearchStatus = !this.toggleSearchStatus;
    },
    modalFormOk() {
      // 新增/修改 成功时，重载列表
      this.loadData();
    },
    handleDetail: function (record) {
      this.$refs.modalForm.edit(record);
      this.$refs.modalForm.title = "详情";
      this.$refs.modalForm.disableSubmit = true;
    },
    /* 导出 */
    handleExportXls2() {
      let paramsStr = encodeURI(JSON.stringify(this.getSearchQueryParams()));
      let url = `${window._CONFIG['domianURL']}/${this.url.exportXlsUrl}?paramsStr=${paramsStr}`;
      window.location.href = url;
    },
    beforeExportExcel() {
      return {}
    },
    handleExportXls(fileName) {
      if (!fileName || typeof fileName != "string") {
        fileName = "导出文件"
      }
      let param = this.getSearchQueryParams();
      if (this.selectedRowKeys && this.selectedRowKeys.length > 0) {
        param['selections'] = this.selectedRowKeys.join(",")
      }
      return new Promise((resolve, reject) => {
        let result = this.beforeExportExcel();
        let {
          error,
          values,
          startTime
        } = this.beforeExportExcel();
        if (!!error && this.selectedRowKeys.length == 0) {
          this.$message.error(error)
          reject(error)
        } else {
          if (startTime) {
            fileName = startTime + '_' + fileName
          }
          downFile(this.url.exportXlsUrl, {
            ...param,
            targetFileName: fileName
          }).then((data, v, i) => {
            if (!data) {
              this.$message.warning("文件下载失败")
              return
            }
            if (typeof window.navigator.msSaveBlob !== 'undefined') {
              window.navigator.msSaveBlob(new Blob([data]), fileName + '.xls')
            } else {
              let url = window.URL.createObjectURL(new Blob([data]))
              let link = document.createElement('a')
              link.style.display = 'none'
              link.href = url
              link.setAttribute('download', fileName + '.xls')
              document.body.appendChild(link)
              link.click()
              document.body.removeChild(link); //下载完成移除元素
              window.URL.revokeObjectURL(url); //释放掉blob对象
            }
          })
          resolve(values)
        }
      })

    },
    handleExportXlsTmpl(fileName) {
      if (!fileName || typeof fileName != "string") {
        fileName = "导出模板文件"
      }
      if (fileName.indexOf("模板") === -1) {
        fileName += "模板"
      }
      let param = {}
      param["field"] = this.getQueryField()
      downFile(this.url.exportXlsTmplUrl, {
        ...param,
        targetFileName: fileName
      }).then((data, v, i) => {
        if (!data) {
          this.$message.warning("模板文件下载失败")
          return
        }
        if (typeof window.navigator.msSaveBlob !== 'undefined') {
          window.navigator.msSaveBlob(new Blob([data]), fileName + '.xls')
        } else {
          let url = window.URL.createObjectURL(new Blob([data]))
          let link = document.createElement('a')
          link.style.display = 'none'
          link.href = url
          link.setAttribute('download', fileName + '.xls')
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link); //下载完成移除元素
          window.URL.revokeObjectURL(url); //释放掉blob对象
        }
      })
    },
    /* 导入 */
    handleImportExcel(info) {
      if (info.status !== 'uploading') {
      }
      if (info.file.status === 'done') {
        let response = info.file.response
        if (response.success || response.code === 200) {
          this.$message.success(`${info.file.name} 文件上传成功`);
          this.loadData();
        }
        if (info.file.response.code === 201) {
          let {
            message,
            result: {
              msg,
              fileUrl,
              fileName
            }
          } = info.file.response
          let href = window._CONFIG['domianURL'] + fileUrl
          this.$warning({
            title: message,
            content: (
              <div>
                <span>{msg}</span><br/>
                <span>具体详情请 <a href={href} target="_blank" download={fileName}>点击下载</a> </span>
              </div>
            )
          })
        }
      } else if (info.file.status === 'error') {
        this.$message.error(`文件上传失败: ${info.file.msg} `);
      }
    },
    /* 图片预览 */
    getImgView(text) {
      if (text && text.indexOf(",") > 0) {
        text = text.substring(0, text.indexOf(","))
      }
      return window._CONFIG['imgDomainURL'] + "/" + text
    },
    beforeUploadExcel(file) {
      const isJPG = file.type === 'application/vnd.ms-excel'
      if (!isJPG) {
        this.$message.error('请选择excle文件!')
      }
      const isLt20M = file.size / 1024 / 1024 < 20
      if (!isLt20M) {
        this.$message.error('文件大小不能大于20M')
      }
      return isJPG && isLt20M
    },
    /* 文件下载 */
    uploadFile(text) {
      if (!text) {
        this.$message.warning("未知的文件")
        return;
      }
      if (text.indexOf(",") > 0) {
        text = text.substring(0, text.indexOf(","))
      }
      window.open(window._CONFIG['domianURL'] + "/sys/common/download/" + text);
    },
    initKeyUpEnter() {
      var _this = this;
      document.onkeydown = function (e) {
        let key = window.event.keyCode;
        if (key === 13) {
          if(this.timer)
          {
            clearTimeout(this.timer)
          }
          this.timer = window.setTimeout(() => {
            _this.loadData();
            clearTimeout(this.timer)
          }, 1000)
        }
      };
    },

  }
}
