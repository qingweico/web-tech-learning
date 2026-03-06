import { getAction, deleteAction, putAction, postAction } from '@/api/manage'

// 观影分类管理
const getCategoryList = (params) => getAction('/television/category/list', params)
const getAllCategories = (params) => getAction('/television/category/all', params)
const addCategory = (params) => postAction('/television/category/add', params)
const editCategory = (params) => putAction('/television/category/edit', params)
const deleteCategory = (params) => deleteAction('/television/category/delete', params)
const deleteBatchCategory = (params) => deleteAction('/television/category/deleteBatch', params)

// 观影标签管理
const getTagList = (params) => getAction('/television/tag/list', params)
const getAllTags = (params) => getAction('/television/tag/all', params)
const addTag = (params) => postAction('/television/tag/add', params)
const editTag = (params) => putAction('/television/tag/edit', params)
const deleteTag = (params) => deleteAction('/television/tag/delete', params)
const deleteBatchTag = (params) => deleteAction('/television/tag/deleteBatch', params)

// 追剧记录管理
const getRecordList = (params) => getAction('/television/record/list', params)
const addRecord = (params) => postAction('/television/record/add', params)
const editRecord = (params) => putAction('/television/record/edit', params)
const deleteRecord = (params) => deleteAction('/television/record/delete', params)
const deleteBatchRecord = (params) => deleteAction('/television/record/deleteBatch', params)

// 追剧统计
const getTop10ByRating = (params) => getAction('/television/statistics/top10Rating', params)
const getWatchStatusStatistics = (params) => getAction('/television/statistics/watchStatus', params)
const getTop10ByWatchDuration = (params) => getAction('/television/statistics/top10Duration', params)
const getTop10CategoryStatistics = (params) => getAction('/television/statistics/top10Category', params)
const getStatisticsByYear = (params) => getAction('/television/statistics/byYear', params)
const getStatisticsByQuarter = (params) => getAction('/television/statistics/byQuarter', params)
const getStatisticsByMonth = (params) => getAction('/television/statistics/byMonth', params)
const getStatisticsByWeek = (params) => getAction('/television/statistics/byWeek', params)

export {
  getCategoryList,
  getAllCategories,
  addCategory,
  editCategory,
  deleteCategory,
  deleteBatchCategory,
  getTagList,
  getAllTags,
  addTag,
  editTag,
  deleteTag,
  deleteBatchTag,
  getRecordList,
  addRecord,
  editRecord,
  deleteRecord,
  deleteBatchRecord,
  getTop10ByRating,
  getWatchStatusStatistics,
  getTop10ByWatchDuration,
  getTop10CategoryStatistics,
  getStatisticsByYear,
  getStatisticsByQuarter,
  getStatisticsByMonth,
  getStatisticsByWeek
}







