import { getAction, deleteAction, putAction, postAction } from '@/api/manage'

// 账单分类管理
const getCategoryList = (params) => getAction('/bill/category/list', params)
const getCategoryListByType = (params) => getAction('/bill/category/listByType', params)
const addCategory = (params) => postAction('/bill/category/add', params)
const editCategory = (params) => putAction('/bill/category/edit', params)
const deleteCategory = (params) => deleteAction('/bill/category/delete', params)
const deleteBatchCategory = (params) => deleteAction('/bill/category/deleteBatch', params)


// 账单记录管理
const getRecordList = (params) => getAction('/bill/record/list', params)
const addRecord = (params) => postAction('/bill/record/add', params)
const editRecord = (params) => putAction('/bill/record/edit', params)
const deleteRecord = (params) => deleteAction('/bill/record/delete', params)
const deleteBatchRecord = (params) => deleteAction('/bill/record/deleteBatch', params)

// 账单统计
const getTrendStatistics = (params) => getAction('/bill/statistics/trend', params)
const getCategoryStatistics = (params) => getAction('/bill/statistics/category', params)

export {
  getCategoryList,
  getCategoryListByType,
  addCategory,
  editCategory,
  deleteCategory,
  deleteBatchCategory,
  getRecordList,
  addRecord,
  editRecord,
  deleteRecord,
  deleteBatchRecord,
  getTrendStatistics,
  getCategoryStatistics
}


