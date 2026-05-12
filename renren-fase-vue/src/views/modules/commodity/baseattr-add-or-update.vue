<template>
  <el-dialog :title="!dataForm.attrId ? '新增' : '修改'" :close-on-click-modal="false" :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="180px">
      <el-form-item label="属性名" prop="attrName">
        <el-input v-model="dataForm.attrName" placeholder="属性名"></el-input>
      </el-form-item>
      <el-form-item label="是否需要检索" prop="searchType">
        <el-input v-model="dataForm.searchType" placeholder="是否需要检索[0-不需要，1-需要]"></el-input>
      </el-form-item>
      <el-form-item label="图标" prop="icon">
        <el-input v-model="dataForm.icon" placeholder="图标"></el-input>
      </el-form-item>
      <el-form-item label="可选值列表" prop="valueSelect">
        <el-input v-model="dataForm.valueSelect" placeholder="可选值列表[用逗号分隔]"></el-input>
      </el-form-item>
      <el-form-item label="属性类型" prop="attrType">
        <el-select v-model="dataForm.attrType" placeholder="请选择">
          <el-option label="销售属性" :value="0"></el-option>
          <el-option label="基本属性" :value="1"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="启用状态" prop="enable">
        <el-input v-model="dataForm.enable" placeholder="启用状态[0 - 禁用，1 - 启用]"></el-input>
      </el-form-item>
      <el-form-item label="所属分类ID" prop="categoryId">
        <!--
        级联选择器；v-model存放父层到子层的分类ID；options存放各层的基本属性；props指定options的哪些属性与v-model的哪些层绑定
        filterable placeholder="搜索: " 增加一个搜索功能，可以直接定位到指定分层，就不需要一级级的点击
        -->
        <el-cascader v-model="cascadedCategoryId" :options="categorys" :props="props">
        </el-cascader>
      </el-form-item>

      <el-form-item label="所属分组">
        <el-select ref="groupSelect" v-model="dataForm.attrGroupId" placeholder="请选择">
          <el-option v-for="item in attrGroups" :key="item.id" :label="item.name" :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="快速展示" prop="showDesc">
        <el-input v-model="dataForm.showDesc" placeholder="快速展示【是否展示在介绍上；0-否 1-是】"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  data() {
    return {
      attrGroups: [],
      cascadedCategoryId: [],
      categorys: [],
      props: {
        value: "id",
        label: "name",
        children: "childrenCategories"
      },
      visible: false,
      dataForm: {
        attrGroupId: '', //可以和用户选择的属性组id关联
        attrId: 0,
        attrName: '',
        searchType: '',
        icon: '',
        valueSelect: '',
        attrType: '',
        enable: '',
        categoryId: 0, //给默认值通过表单校验
        showDesc: ''
      },
      dataRule: {
        attrName: [
          {required: true, message: '属性名不能为空', trigger: 'blur'}
        ],
        searchType: [
          {required: true, message: '是否需要检索[0-不需要，1-需要]不能为空', trigger: 'blur'}
        ],
        icon: [
          {required: true, message: '图标不能为空', trigger: 'blur'}
        ],
        valueSelect: [
          {required: true, message: '可选值列表[用逗号分隔]不能为空', trigger: 'blur'}
        ],
        attrType: [
          {required: true, message: '属性类型[0-销售属性，1-基本属性]不能为空', trigger: 'blur'}
        ],
        enable: [
          {required: true, message: '启用状态[0 - 禁用，1 - 启用]不能为空', trigger: 'blur'}
        ],
        categoryId: [
          {required: true, message: '所属分类不能为空', trigger: 'blur'}
        ],
        showDesc: [
          {required: true, message: '快速展示【是否展示在介绍上；0-否 1-是】不能为空', trigger: 'blur'}
        ]
      }
    }
  },
  //Vue的钩子函数，它在 Vue 实例创建完成后被调用
  created() {
    this.getCategories();
  },
  //显示添加商品属性界面的watch,当选择的级联ID变化，就重新获取该分类的所有属性分组信息
  watch: {
    cascadedCategoryId(path) { //监控到 cascadedCategoryId变化
      // console.log("路径=", path); path数据格式 [1,21,301]
      this.attrGroups = [];
      this.dataForm.attrGroupId = "";
      this.dataForm.categoryId = path[path.length - 1];
      if (path && path.length == 3) {
        this.$http({
          url: this.$http.adornUrl(`/commodity/attrgroup/list/${path[path.length - 1]}`),
          method: "get",
          params: this.$http.adornParams({page: 1, limit: 10000000})
        }).then(({data}) => {
          if (data && data.code === 0) {
            //将返回的当前分类对应的属性组信息 覆盖 this.attrGroups
            this.attrGroups = data.page.list;
          } else {
            this.$message.error(data.msg);
          }
        });
      } else if (path.length == 0) {
        this.dataForm.categoryId = "";
      } else {
        this.$message.error("请选择正确的分类");
        this.dataForm.categoryId = "";
      }
    }
  },
  methods: {
    // 该方法获取树形列表各层级的数据，将其赋值给级联选择器el-cascader
    getCategories() {
      this.dataListLoading = true
      this.$http({
        // url: 'http://localhost:5050/api/commodity/category/list/tree',
        url: this.$http.adornUrl("/commodity/category/list/tree"),
        method: 'get',
      }).then(({data}) => { //data加{}会进行对象解构
        this.categorys = data.data
      })
    },
    init(id) {
      this.dataForm.attrId = id || 0
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.attrId) {
          this.$http({
            url: this.$http.adornUrl(`/commodity/attr/info/${this.dataForm.attrId}`),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.dataForm.attrName = data.attr.attrName
              this.dataForm.searchType = data.attr.searchType
              this.dataForm.icon = data.attr.icon
              this.dataForm.valueSelect = data.attr.valueSelect
              this.dataForm.attrType = data.attr.attrType
              this.dataForm.enable = data.attr.enable
              this.dataForm.categoryId = data.attr.categoryId
              this.dataForm.showDesc = data.attr.showDesc
            }
          })
        }
      })
    },
    // 表单提交
    dataFormSubmit() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.$http({
            url: this.$http.adornUrl(`/commodity/attr/${!this.dataForm.attrId ? 'save' : 'update'}`),
            method: 'post',
            data: this.$http.adornData({
              'attrId': this.dataForm.attrId || undefined,
              'attrName': this.dataForm.attrName,
              'searchType': this.dataForm.searchType,
              'icon': this.dataForm.icon,
              'valueSelect': this.dataForm.valueSelect,
              'attrType': this.dataForm.attrType,
              'enable': this.dataForm.enable,
              'categoryId': this.dataForm.categoryId,
              'attrGroupId': this.dataForm.attrGroupId, //在添加时把属性所在组id 也发送
              'showDesc': this.dataForm.showDesc
            })
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功',
                type: 'success', duration: 1500, onClose: () => {
                  this.visible = false
                  this.$emit('refreshDataList')
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
        }
      })
    }
  }
}
</script>
