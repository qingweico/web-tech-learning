<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="12">
          <a-col :md="6" :sm="12">
            <a-form-item label="标签名称">
              <a-input v-model="queryParam.name" placeholder="请输入标签名称" allowClear />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">添加标签</a-button>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay" @click="handleMenuClick">
          <a-menu-item key="1">
            <a-icon type="delete" @click="batchDel" />
            删除
          </a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px">
          批量操作
          <a-icon type="down" />
        </a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px" v-if="selectedRowKeys.length > 0">
        <i class="anticon anticon-info-circle ant-alert-icon"></i>已选择&nbsp;
        <a style="font-weight: 600">{{ selectedRowKeys.length }}</a
        >项&nbsp;&nbsp;
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
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        @change="handleTableChange"
      >
        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical" />
          <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
            <a>删除</a>
          </a-popconfirm>
        </span>
      </a-table>
    </div>

    <television-tag-modal ref="modalForm" @ok="modalFormOk"></television-tag-modal>
  </a-card>
</template>

<script>
import TelevisionTagModal from './modules/TelevisionTagModal'
import { getTagList, deleteTag, deleteBatchTag } from '@/api/television'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'

export default {
  name: 'TelevisionTagList',
  mixins: [JeecgListMixin],
  components: {
    TelevisionTagModal,
  },
  data() {
    return {
      description: '这是观影标签管理页面',
      url: {
        list: '/television/tag/list',
        delete: '/television/tag/delete',
        deleteBatch: '/television/tag/deleteBatch',
      },
      columns: [
        {
          title: '标签名称',
          align: 'center',
          dataIndex: 'name',
          width: 150,
        },
        {
          title: '排序',
          align: 'center',
          dataIndex: 'sort',
          width: 80,
        },
        {
          title: '备注',
          align: 'center',
          dataIndex: 'remark',
          ellipsis: true,
        },
        {
          title: '创建时间',
          align: 'center',
          dataIndex: 'created',
          width: 180,
        },
        {
          title: '操作',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' },
          align: 'center',
          width: 150,
        },
      ],
    }
  },
  methods: {
    handleMenuClick(e) {
      if (e.key === '1') {
        this.batchDel()
      }
    },
  },
}
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>

