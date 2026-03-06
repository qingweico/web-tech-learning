<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="12">
          <a-col :md="6" :sm="12">
            <a-form-item label="账号">
              <a-input placeholder="请输入账号查询" v-model="queryParam.username"></a-input>
            </a-form-item>
          </a-col>

          <a-col :md="6" :sm="12">
            <a-form-item label="姓名">
              <a-input placeholder="请输入姓名查询" v-model="queryParam.realname"></a-input>
            </a-form-item>
          </a-col>

           <a-col :md="6" :sm="8">
            <a-form-item label="状态">
              <a-select v-model="queryParam.status" placeholder="请选择用户状态查询">
                <a-select-option value>请选择用户状态</a-select-option>
                <a-select-option value="1">正常</a-select-option>
                <a-select-option value="2">解冻</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>


          <a-col :md="6" :sm="8">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button
                type="primary"
                @click="searchReset"
                icon="reload"
                style="margin-left: 8px"
              >重置</a-button>

            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator" style="border-top: 5px">
      <a-button @click="handleAdd" v-has="'user:add'" type="primary" icon="plus">添加用户</a-button>
      <a-button
        type="primary"
        v-has="'user:export'"
        icon="download"
        @click="handleExportXls('用户信息')"
      >导出
      </a-button>

      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay" @click="handleMenuClick">
          <a-menu-item key="1">
            <a-icon type="delete" @click="batchDel"/>
            删除
          </a-menu-item>
          <a-menu-item key="2">
            <a-icon type="lock" @click="batchFrozen('2')"/>
            冻结
          </a-menu-item>
          <a-menu-item key="3">
            <a-icon type="unlock" @click="batchFrozen('1')"/>
            解冻
          </a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px">
          批量操作
          <a-icon type="down"/>
        </a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i>已选择&nbsp;
        <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项&nbsp;&nbsp;
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        bordered
        size="middle"
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange"
      >
        <template slot="avatarslot" slot-scope="text, record, index">
          <div class="anty-img-wrap">
            <a-avatar shape="square" :src="getAvatarView(record.avatar)" icon="user"/>
          </div>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical"/>

          <a-dropdown>
            <a class="ant-dropdown-link">
              更多
              <a-icon type="down"/>
            </a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a href="javascript:;" @click="handleDetail(record)">详情</a>
              </a-menu-item>

              <a-menu-item>
                <a href="javascript:;" @click="handleChangePassword(record.id,record.username)">密码</a>
              </a-menu-item>

              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>

              <a-menu-item v-if="record.status==1">
                <a-popconfirm title="确定冻结吗?" @confirm="() => handleFrozen(record.id,2)">
                  <a>冻结</a>
                </a-popconfirm>
              </a-menu-item>

              <a-menu-item v-if="record.status==2">
                <a-popconfirm title="确定解冻吗?" @confirm="() => handleFrozen(record.id,1)">
                  <a>解冻</a>
                </a-popconfirm>
              </a-menu-item>

            </a-menu>
          </a-dropdown>
        </span>
      </a-table>
    </div>

    <user-modal ref="modalForm" @ok="modalFormOk"></user-modal>

    <password-modal ref="passwordmodal" @ok="passwordModalOk"></password-modal>

  </a-card>
</template>

<script>
import UserModal from './modules/UserModal'
import PasswordModal from './modules/PasswordModal'
import {putAction} from '@/api/manage'
import {frozenBatch} from '@/api/api'
import {JeecgListMixin} from '@/mixins/JeecgListMixin'

export default {
  name: 'UserList',
  mixins: [JeecgListMixin],
  components: {
    UserModal,
    PasswordModal
  },
  data() {
    return {
      description: '这是用户管理页面',
      queryParam: {},
      columns: [
        {
          title: '用户账号',
          align: 'center',
          dataIndex: 'username',
          width: 120
        },
        {
          title: '姓名',
          align: 'center',
          width: 100,
          dataIndex: 'realname'
        },
        {
          title: '头像',
          align: 'center',
          width: 120,
          dataIndex: 'avatar',
          scopedSlots: {customRender: 'avatarslot'}
        },

        {
          title: '性别',
          align: 'center',
          width: 80,
          dataIndex: 'sex',
          sorter: true,
          customRender: x => {
            if(1===x) {
              return "男"
            }else if(2===x){
              return "女"
            }else{
              return "";
            }


        }
        },
        {
          title: '生日',
          align: 'center',
          width: 180,
          dataIndex: 'birthday'
        },
        {
          title: '手机号码',
          align: 'center',
          width: 100,
          dataIndex: 'phone'
        },
        {
          title: '邮箱',
          align: 'center',
          dataIndex: 'email'
        },
        // {
        //   title: '手持端登录权限',
        //   align: 'center',
        //   width: 80,
        //   dataIndex: 'signMobile',
        //   customRender: x => (0 == x ? "否" : "是")
        // },
        {
          title: '状态',
          align: 'center',
          width: 80,
          dataIndex: 'status',
          customRender: x => (1 == x ? "正常" : "冻结")
        },
        {
          title: '操作',
          dataIndex: 'action',
          scopedSlots: {customRender: 'action'},
          align: 'center',
          width: 170
        }
      ],
      url: {
        imgerver: window._CONFIG['domianURL'] + '/sys/common/view',
        syncUser: '/process/extActProcess/doSyncUser',
        list: '/sys/user/list',
        delete: '/sys/user/delete',
        deleteBatch: '/sys/user/deleteBatch',
        exportXlsUrl: '/sys/user/exportXls',
        importExcelUrl: 'sys/user/importExcel'
      }
    }
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    }
  },
  methods: {
    getAvatarView: function (avatar) {
      return this.url.imgerver + '/' + avatar
    },

    batchFrozen: function (status) {
      if (this.selectedRowKeys.length <= 0) {
        this.$message.warning('请选择一条记录！')
        return false
      } else {
        let ids = ''
        let that = this
        that.selectedRowKeys.forEach(function (val) {
          ids += val + ','
        })
        that.$confirm({
          title: '确认操作',
          content: '是否' + (status == 1 ? '解冻' : '冻结') + '选中账号?',
          onOk: function () {
            frozenBatch({ids: ids, status: status}).then(res => {
              if (res.success) {
                that.$message.success(res.message)
                that.loadData()
                that.onClearSelected()
              } else {
                that.$message.warning(res.message)
              }
            })
          }
        })
      }
    },
    handleMenuClick(e) {
      if (e.key === '1') {
        this.batchDel()
      } else if (e.key === '2') {
        this.batchFrozen(2)
      } else if (e.key === '3') {
        this.batchFrozen(1)
      }
    },
    handleFrozen: function (id, status) {
      let that = this
      frozenBatch({ids: id, status: status}).then(res => {
        if (res.success) {
          that.$message.success(res.message)
          that.loadData()
        } else {
          that.$message.warning(res.message)
        }
      })
    },
    handleChangePassword(id,username) {
      this.$refs.passwordmodal.show(id,username)
    },
    handleAgentSettings(username) {
      this.$refs.sysUserAgentModal.agentSettings(username)
      this.$refs.sysUserAgentModal.title = '用户代理人设置'
    },
    passwordModalOk() {
      //TODO 密码修改完成 不需要刷新页面，可以把datasource中的数据更新一下
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>