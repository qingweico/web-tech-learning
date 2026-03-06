<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="12">
          <a-col :md="6" :sm="12">
            <a-form-item label="剧名">
              <a-input v-model="queryParam.name" placeholder="请输入剧名" allowClear />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="12">
            <a-form-item label="观看状态">
              <JDictSelectTag
                v-model="queryParam.watchStatus"
                dict-code="watch_status"
                placeholder="请选择观看状态"
                allowClear
              />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="12">
            <a-form-item label="分类">
              <a-select v-model="queryParam.categoryId" placeholder="请选择分类" allowClear showSearch>
                <a-select-option v-for="item in categoryList" :key="item.id" :value="item.id">
                  {{ item.name }}
                </a-select-option>
              </a-select>
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
      <a-button @click="handleAdd" type="primary" icon="plus">添加记录</a-button>
      <a-button type="primary" icon="bar-chart" @click="handleStatistics">统计图表</a-button>
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
        <span slot="watchStatus" slot-scope="text">
          <JDictSelectTag :value="String(text)" dict-code="watch_status" type="tag" disabled use-preset-color  />
        </span>
        <span slot="rating" slot-scope="text">
          <span v-if="text">{{ text }}分</span>
          <span v-else>-</span>
        </span>
        <span slot="tags" slot-scope="text, record">
          <a-tag v-for="tagId in record.tagIds" :key="tagId">
            {{ getTagName(tagId) }}
          </a-tag>
        </span>
        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical" />
          <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
            <a>删除</a>
          </a-popconfirm>
        </span>
      </a-table>
    </div>

    <television-record-modal ref="modalForm" @ok="modalFormOk"></television-record-modal>
  </a-card>
</template>

<script>
import TelevisionRecordModal from './modules/TelevisionRecordModal'
import { getRecordList, deleteRecord, deleteBatchRecord, getAllCategories, getAllTags } from '@/api/television'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import JDictSelectTag from '@comp/dict/JDictSelectTag.vue'

export default {
  name: 'TelevisionRecordList',
  mixins: [JeecgListMixin],
  components: {
    JDictSelectTag,
    TelevisionRecordModal,
  },
  data() {
    return {
      description: '这是追剧记录管理页面',
      url: {
        list: '/television/record/list',
        delete: '/television/record/delete',
        deleteBatch: '/television/record/deleteBatch',
      },
      categoryList: [],
      tagList: [],
      columns: [
        {
          title: '剧名',
          align: 'center',
          dataIndex: 'name',
          width: 200,
        },
        {
          title: '集数',
          align: 'center',
          dataIndex: 'episode',
          width: 80,
        },
        {
          title: '观看状态',
          align: 'center',
          dataIndex: 'watchStatus',
          width: 120,
          scopedSlots: { customRender: 'watchStatus' },
        },
        {
          title: '评分',
          align: 'center',
          dataIndex: 'rating',
          width: 100,
          scopedSlots: { customRender: 'rating' },
        },
        {
          title: '分类',
          align: 'center',
          dataIndex: 'categoryName',
          width: 120,
          customRender: (text, record) => {
            const category = this.categoryList.find((item) => item.id === record.categoryId)
            return category ? category.name : '-'
          },
        },
        {
          title: '标签',
          align: 'center',
          dataIndex: 'tagIds',
          width: 200,
          scopedSlots: { customRender: 'tags' },
        },
        {
          title: '开始时间',
          align: 'center',
          dataIndex: 'startTime',
          width: 180,
        },
        {
          title: '结束时间',
          align: 'center',
          dataIndex: 'endTime',
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
  mounted() {
    this.loadCategoryList()
    this.loadTagList()
  },
  methods: {
    loadCategoryList() {
      getAllCategories().then((res) => {
        if (res.success && res.result) {
          this.categoryList = res.result
        }
      })
    },
    loadTagList() {
      getAllTags().then((res) => {
        if (res.success && res.result) {
          this.tagList = res.result
        }
      })
    },
    getTagName(tagId) {
      const tag = this.tagList.find((item) => item.id === tagId)
      return tag ? tag.name : '-'
    },
    handleMenuClick(e) {
      if (e.key === '1') {
        this.batchDel()
      }
    },
    handleStatistics() {
      this.$router.push({ path: '/television/statistics' })
    },
  },
}
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>


