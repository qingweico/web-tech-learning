<template>
  <a-modal
    :title="title"
    :width="800"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    :footer="null"
    cancelText="关闭">

    <table id="table_report" class="table table-striped table-bordered table-hover">

      <tr>
        <td style="width:90px;padding-top: 80px;">基本信息:</td>
        <td>
          日志等级：<span id="level">
           {{ result.level }}
                    </span><br>

          被操作的对象：<span id="operationUnit">
           {{ result.operationUnit }}
                    </span><br>


          方法名称：<span id="method">
           {{ result.method }}
                    </span><br>


          操作人：<span id="username">
           {{ result.username }}
                    </span><br>


          日志描述：<span id="describe">
           {{ result.describe }}
                    </span><br>


          操作类型：<span id="operationType">
           {{ result.operationType }}
                    </span><br>

          IP：<span id="ip">
           {{ result.ip }}
                    </span><br>


          方法运行时间：<span id="runTime">
           {{ result.runTime }}
                    </span><br>

          创建时间：<span id="created">
           {{ result.created }}
                    </span><br>
        </td>
      </tr>

      <tr>
        <td style="width:90px;padding-top: 60px;">详情:</td>
        <td style="word-break:break-all;">
          方法参数：<span id="args"
                     style="word-break:break-all;"> {{
            result.args
          }}</span><br>

          方法返回值：<span id="returnValue">
           {{ result.returnValue }}
                    </span><br>

          异常信息：<span id="exceptionMsg">
           {{ result.exceptionMsg }}
                    </span><br>

          异常代码：<span id="exceptionCode">
           {{ result.exceptionCode }}
                    </span><br>
        </td>
      </tr>

      <tr>
        <td style="text-align: center;" colspan="10">
          <a-button type="primary" @click="close">关闭</a-button>
        </td>
      </tr>
    </table>

  </a-modal>
</template>

<script>
import {httpAction} from '@/api/manage'

export default {
  name: "OperationLogModal",
  data() {
    return {
      title: "操作",
      visible: false,
      model: {},
      labelCol: {
        xs: {span: 24},
        sm: {span: 5},
      },
      wrapperCol: {
        xs: {span: 24},
        sm: {span: 16},
      },
      result: {},
      confirmLoading: false,
      form: this.$form.createForm(this),
      validatorRules: {},
      url: {
        add: "/operationLog/operationLog/add",
        edit: "/operationLog/operationLog/edit",
      },
    }
  },
  created() {
  },
  methods: {
    add() {
      this.edit({});
    },
    edit(record) {
      Object.assign(this.result, record);
      this.visible = true;
    },
    close() {
      this.$emit('close');
      this.visible = false;
    },
    handleOk() {
      const that = this;
      // 触发表单验证
      this.form.validateFields((err, values) => {
        if (!err) {
          that.confirmLoading = true;
          let httpurl = '';
          let method = '';
          if (!this.model.id) {
            httpurl += this.url.add;
            method = 'post';
          } else {
            httpurl += this.url.edit;
            method = 'put';
          }
          let formData = Object.assign(this.model, values);
          //时间格式化

          httpAction(httpurl, formData, method).then((res) => {
            if (res.success) {
              that.$message.success(res.message);
              that.$emit('ok');
            } else {
              that.$message.warning(res.message);
            }
          }).finally(() => {
            that.confirmLoading = false;
            that.close();
          })


        }
      })
    },
    handleCancel() {
      this.close()
    },


  }
}
</script>

<style lang="less" scoped>

</style>

