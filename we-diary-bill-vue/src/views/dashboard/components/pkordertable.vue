<template>
  <div class="tablebox" :data-down="isDown ? 'true' : 'false'">
    <a-spin :spinning="isDataLoad" size="large" />
    <table class="table" style="margin: 0">
      <tr class="title">
        <th v-for="(item, index) in th" :key="index">{{ item }}</th>
      </tr>
    </table>
<!-- <list-park-number-popover
              v-if="result.parkNumberMore"
              style="position: absolute; right: 55px; top: -15px; margin: 0"
              placement="bottom"
              :detail="result.parkNumberMore"
              title="绑定车位信息"
              trigger="click"
              content="更多"
            /> -->
    <div :id="`${id}${isDown ? 'down' : ''}`" class="td" style="overflow-x: hidden; overflow-y: scroll">
      <table class="table" v-if="td.length >= 1">
        <tr v-for="(tritem, trindex) in td" :key="trindex" :class="trindex % 2 == 0 ? '' : 'even'">
          <td v-for="(item, index) in tritem" :key="index">{{ item }}</td>
        </tr>
      </table>


    </div>

    <a-pagination
      v-if="!isDown"
      :show-total="(total, range) => `${range[0]}-${range[1]} 共 ${total} 条`"
      size="small"
      :total="result.total"
      show-quick-jumper
      show-size-changer
      v-model="inRequestParams.pageNo"
      :current="inRequestParams.pageNo"
      @change="pagination"
      @showSizeChange="pagination"
      :defaultPageSize="20"
    />
  </div>
</template>

<script>
import { getAction, httpAction, postAction, putAction } from '@/api/manage'
import { welcome, isEmpty, timeFormatting, filterObj } from '@/utils/util'
let elementHeigth = 0
export default {
  name: 'pkordertable',
  components: {
  },
  props: {
    id: {
      type: String,
      default: '',
    },
    isDown: {
      type: Boolean,
      default: false,
    },
    inquireParams: {
      type: Object,
      default: {},
    },
  },
  data() {
    return {
      isDataLoad: true,
      url: {
        monthly: '/sentryBox/parkCarRecord/monthlyCardList',
        charge: '/sentryBox/parkCarRecord/parkCarChargeList',
        park: '/sentryBox/parkCarRecord/parkCarOrderList',
        out: '/sentryBox/parkCarRecord/parkCarOutLogList',
        entrance: '/sentryBox/parkCarRecord/parkCarEnterLogList', // 入场
      },
      // monthlyStatus	int	月租状态 (1、正常 2、报停 3、注销 4、过期)
      monthlyStatusText: ['', '正常', '报停', '注销', '过期'],
      //payType	string	支付类型：1微信，2支付宝,3无感支付，4,全免劵，5储值车，6线下收费
      payTypeText: ['', '微信', '支付宝', '无感支付', '全免劵', '储值车', '线下收费'],
      // carNature	string	车辆性质(0:临时车,1:月租车,2储值车，3军警车）
      carNatureText: ['临时车', '月租车', '储值车', '军警车' , '白名单'],
      // carType	string	出场车辆类型(0,无牌车，1,有牌车)
      carTypeText: ['无牌车', '有牌车'],
      // 入场类型：0：默认 1：重复入场 2：手动入场 3：补录入场
      enterTypeText: ['默认', '重复入场', '手动入场', '补录入场'],
      // outerType （1 : 正常出场 2：手动出场 3：补录场内）
      outerTypeText: ['', '正常出场', '手动出场', '补录场内'],
      // isOut （车辆状态(0,在场场 1，已出场)
      isOutText: ['在场', '出场'],
      // table 表名
      thWantText: {
        monthly: ['车牌号码', '月租套餐', '月租状态', '起始时间', '到期时间', '通行权限组', '车主姓名', '备注'],
        charge: [
          '车牌号码',
          '车辆类型',
          '计费开始',
          '计费截止',
          '应收金额',
          '实收金额',
          '优惠金额',
          '支付方式',
          '缴费地点',
          '收费操作员',
          '创建时间',
          '备注',
        ],
        park: [
          '车牌号码',
          '车辆类型',
          '入场地点',
          '入场时间',
          '出场地点',
          '出场时间',
          '停车时长',
          '应收金额',
          '实收金额',
          '优惠金额',
          '出场方式',
          '出场终端',
          '出场操作员',
          '备注',
        ],
        out: ['车牌号码', '车辆类型', '出场地点', '出场类型', '出场时间', '出场终端', '出场操作员', '备注'],
        entrance: [
          '车牌号码',
          '车辆类型',
          '入场地点',
          '入场类型',
          '停车状态',
          '入场时间',
          '入场终端',
          '入场操作员',
          '备注',
        ],
      },
      // table 表内容
      tdWantParams: {
        monthly: [
          'plateNo',
          'packageName',
          'monthlyStatus',
          'beginTime',
          'endTime',
          'throughAuthorityName',
          'carOwnerName',
          'remark',
        ],
        charge: [
          'plateNo',
          'carTypeName',
          'enterTime',
          'outTime',
          'chargeDue',
          'chargePaid',
          'couponAmt',
          'payType',
          'workName',
          'sysUserName',
          'created',
          'remark',
        ],
        park: [
          'plateNo',
          'carTypeName',
          'enterArmName',
          'enterTime',
          'armName',
          'outTime',
          'chargeDuration',
          'chargeDue',
          'chargePaid',
          'couponAmt',
          'outerType',
          'workName',
          'sysUserName',
          'remark',
        ],
        out: ['plateNo', 'carTypeName', 'armName', 'outerType', 'carOutTime', 'workName', 'sysUserName', 'remark'],
        entrance: [
          'plateNo',
          'carTypeName',
          'armName',
          'enterType',
          'isOut',
          'carEnterTime',
          'workName',
          'sysUserName',
          'remark',
        ],
      },

      th: Array(),

      td: Array(),

      inRequestParams: {
        pageNo: 1,
        pageSize: 20,
      },

      result: {}, // 翻页数据

      requestParamsPublic: ['plateNo', 'timeStart', 'timeEnd', 'pageNo', 'pageSize'],

      requestParamsThose: {
        monthly: ['monthlyStatus','packageName',], // 月租
        charge: ['sysUserName','payType'], // 缴费
        park: ['serialNo'], // 停车'armName',
        out: ['outerType', 'serialNo'], // 出场'armName',
        entrance: ['enterType', 'isOut', 'serialNo'], // 入场'armName',
      },
    }
  },
  created() {
    this.dataInit()
  },
  mounted() {
    let _self = this,
      element,
      down = _self.isDown ? 'down' : ''
    _self.isDown && (element = document.getElementById(`${_self.id}${down}`))
    element && isEmpty(elementHeigth) && (elementHeigth = element.offsetHeight)
    _self.isDown &&
      _self.elementScroll(element, function (res) {
        _self.isDataLoad = true
        _self.getTdListData()
      })
  },
  methods: {
    pagination(page, pageSize) {
      this.inRequestParams.pageNo = page
      this.inRequestParams.pageSize = pageSize
      this.getTdListData()
    },

    // 获取td列表数据
    getTdListData() {
      let _self = this,
        {
          url,
          id,
          inRequestParams,
          tdWantParams,
          enterTypeText,
          carTypeText,
          carNatureText,
          monthlyStatusText,
          payTypeText,
          outerTypeText,
          isDown,
          isOutText,
        } = _self

      _self.isDataLoad = true
      url[id] &&
        getAction(url[id], inRequestParams)
          .then((res) => {
            // console.log(res, '进入获取td列表数据')
            _self.isDataLoad = false
            if (isEmpty(res.result.records)) return
            let result = res.result,
              records = result.records,
              i = 0,
              r = 0,
              tdArr = Array(),
              tdArrContainer = Array(),
              tdWantParamstr = '',
              record
            while (records.length > i) {
              r = 0
              tdArr = Array()
              record = records[i]
              while (tdWantParams[id].length > r) {
                tdWantParamstr = tdWantParams[id][r]
                switch (tdWantParamstr) {
                  case 'enterType':
                    // 入场类型
                    tdArr.push(enterTypeText[record[tdWantParamstr]])
                    break
                  case 'carType':
                    // 出场类型
                    tdArr.push(carTypeText[record[tdWantParamstr]])
                    break
                  case 'carNature':
                    // 出场车辆类型
                    tdArr.push(carNatureText[record[tdWantParamstr]])
                    break
                  case 'monthlyStatus':
                    tdArr.push(monthlyStatusText[record[tdWantParamstr]])
                    break
                  case 'payType':
                    tdArr.push(payTypeText[record[tdWantParamstr]])
                    break
                  case 'chargeDue':
                    tdArr.push(record[tdWantParamstr] / 100)
                    break
                  case 'couponAmt':
                    tdArr.push(record[tdWantParamstr] / 100)
                    break
                  case 'chargePaid':
                    tdArr.push(record[tdWantParamstr] / 100)
                    break
                  case 'totalDiscAmt':
                    tdArr.push(record[tdWantParamstr] / 100)
                    break
                  case 'chargeDuration':
                    tdArr.push(timeFormatting(record[tdWantParamstr]))
                    break
                  case 'outerType':
                    tdArr.push(outerTypeText[record[tdWantParamstr]])
                    break
                  case 'isOut':
                    tdArr.push(isOutText[record[tdWantParamstr]])
                    break
                  case 'plateNo':
                    if(id == 'monthly' && !isEmpty(record[tdWantParamstr])){
                      tdArr.push(record[tdWantParamstr].split(',')[0])
                      break
                    }

                    tdArr.push(isEmpty(record[tdWantParamstr]) ? '无牌车' : record[tdWantParamstr])
                    break
                  default:
                    tdArr.push(record[tdWantParamstr])
                    break
                }
                r++
              }
              tdArrContainer.push(tdArr)
              i++
            }
            isDown && (_self.td = [..._self.td, ...tdArrContainer])

            isDown && _self.inRequestParams.pageNo++

            !isDown && (_self.td = tdArrContainer)

            !isDown && (_self.result = result)
          })
          .catch((e) => {})
          .finally((_) => {
            _self.isDataLoad
          })
    },

    dataInit() {
      let _self = this,
        { id, thWantText } = _self
      _self.th = thWantText[id]
      _self.getTdListData()
    },

    elementScroll(element, callback) {
      if (!element) return
      let _self = this
      // element.setAttribute('style', 'overflow-x: hidden;overflow-y: scroll;')
      var scrollFunc = function (e) {
        e = e || window.event
        if (e.wheelDelta) {
          //第一步：先判断浏览器IE，谷歌滑轮事件
          if (e.wheelDelta > 0) {
            //当滑轮向上滚动时
          }
          if (e.wheelDelta < 0) {
            if (_self.isDataLoad) return
            //当滑轮向下滚动时
            _self.scollPostion(element).then((res) => {
              let { top, height } = res
              // console.log(res, height, top, elementHeigth)
              height - top - elementHeigth < elementHeigth && callback(true)
            })
          }
        } else if (e.detail) {
          //Firefox滑轮事件
          if (e.detail > 0) {
            //当滑轮向上滚动时
          }
          if (e.detail < 0) {
            //当滑轮向下滚动时
          }
        }
      }
      if (element.addEventListener) {
        //firefox
        element.addEventListener('DOMMouseScroll', scrollFunc, false)
      }
      //滚动滑轮触发scrollFunc方法 //ie 谷歌
      element.onmousewheel = scrollFunc
    },

    scollPostion(element) {
      return new Promise((resolut, reject) => {
        var t, l, w, h
        if (element && element.scrollTop) {
          t = element.scrollTop
          l = element.scrollLeft
          w = element.scrollWidth
          h = element.scrollHeight
        } else if (document.body) {
          t = document.body.scrollTop
          l = document.body.scrollLeft
          w = document.body.scrollWidth
          h = document.body.scrollHeight
        } else {
          reject(false)
          return
        }
        resolut({
          top: t,
          left: l,
          width: w,
          height: h,
        })
      })
    },

    // inquiryMethod 搜索条件
    inquiryMethod() {
      let { requestParamsPublic, requestParamsThose, id } = this,
        fitrateParams = [...requestParamsPublic, ...requestParamsThose[id]],
        parentParams = Object.assign(this.inRequestParams, this.inquireParams),
        i = 0,
        inRequestParams = {}
      while (fitrateParams.length > i) {
        inRequestParams[fitrateParams[i]] = parentParams[fitrateParams[i]]
        i++
      }
      this.td = Array()
      this.current = 1
      this.inRequestParams = inRequestParams
      this.inRequestParams.pageNo = 1
      this.getTdListData()
    },
  },
  computed: {},
  watch: {},
}
</script>

<style lang="less" scoped>
.content {
  .tablebox {
    height: calc(100% - 55px);
    .table {
      margin-bottom: 0px;
      color: #fff;
      border: none;
      .title,
      .even {
        background: #162d5a;
      }
      .title {
        font-size: 16px;
        color: #1990ff;

        th {
          padding: 10px 0;
        }
      }
      tr {
        border: none;
        th,
        td {
          vertical-align: middle;
          text-align: center;
          border: none;
          width: 6.6%;
          pointer-events: none;
          padding: 16px 0;
          .borderb {
            border-bottom: 1px solid #fff;
            cursor: pointer;
            pointer-events: auto;
          }
          .ant-popover-arrow {
            display: none;
          }
        }

        .picture {
          width: 68px;
          height: 29px;
          background-color: #fff;
        }
      }
    }

    .td {
      height: calc(100% - 40px);
      /* ::-webkit-scrollbar ,兼容chrome和safari浏览器 */
      &::-webkit-scrollbar {
        display: none;
      }
      /* 兼容火狐 */
      scrollbar-width: none;
      /* 兼容IE10+ */
      -ms-overflow-style: none;
    }

    .ant-spin-spinning {
      position: absolute;
      left: 50%;
      top: 50%;
      transform: translate(-50%, -50%);
    }
  }
  .tablebox[data-down='true'] {
    height: 100%;
  }
}
</style>
