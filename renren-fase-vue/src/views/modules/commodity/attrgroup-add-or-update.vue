<template>
  <!-- 点击新增和修改时，弹出的对话框 -->
  <el-dialog :title="!dataForm.id ? '新增' : '修改'" :close-on-click-modal="false" @close="dialogClose"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
      label-width="100px">
      <el-form-item label="组名" prop="name">
        <el-input v-model="dataForm.name" placeholder="组名"></el-input>
      </el-form-item>
      <el-form-item label="排序" prop="sort">
        <el-input v-model="dataForm.sort" placeholder="排序"></el-input>
      </el-form-item>
      <el-form-item label="说明" prop="description">
        <el-input v-model="dataForm.description" placeholder="说明"></el-input>
      </el-form-item>
      <el-form-item label="组图标" prop="icon">
        <el-input v-model="dataForm.icon" placeholder="组图标"></el-input>
      </el-form-item>
      <el-form-item label="所属分类ID" prop="categoryId">
        <!--
        级联选择器；v-model存放父层到子层的分类ID；options存放各层的基本属性；props指定options的哪些属性与v-model的哪些层绑定
        filterable placeholder="搜索: " 增加一个搜索功能，可以直接定位到指定分层，就不需要一级级的点击
        -->
        <el-cascader filterable placeholder="搜索: " v-model="cascadedCategoryId" :options="categorys"
          :props="props"></el-cascader>
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
      cascadedCategoryId: [], //[第一层分类ID, 第二层分类ID, 第三层分类ID]
      props: {
        value: "id",
        label: "name",
        children: "childrenCategories"
      },
      categorys: [],
      visible: false,
      dataForm: {
        id: 0,
        name: '',
        sort: '',
        description: '',
        icon: '',
        categoryId: ''
      },
      dataRule: {
        name: [
          { required: true, message: '组名不能为空', trigger: 'blur' }
        ],
        sort: [
          { required: true, message: '排序不能为空', trigger: 'blur' }
        ],
        description: [
          { required: true, message: '说明不能为空', trigger: 'blur' }
        ],
        icon: [
          { required: true, message: '组图标不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  //调用指定的方法
  created() {
    this.getCategories();
  },
  methods: {
    //当点击新增按钮时调用，作用是清空级联ID，防止新增时显示上次选中的所属分类ID
    dialogClose() {
      this.cascadedCategoryId = []
    },
    //实现新增和修改操作
    init(id) {
      this.dataForm.id = id || 0
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.id) {
          this.$http({
            url: this.$http.adornUrl(`/commodity/attrgroup/info/${this.dataForm.id}`),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.dataForm.name = data.attrgroup.name
              this.dataForm.sort = data.attrgroup.sort
              this.dataForm.description = data.attrgroup.description
              this.dataForm.icon = data.attrgroup.icon
              this.dataForm.categoryId = data.attrgroup.categoryId
              this.cascadedCategoryId = data.attrgroup.cascadedCategoryId
            }
          })
        }
      })
    },
    // 该方法获取树形列表各层级的数据，将其赋值给级联选择器el-cascader
    getCategories() {
      this.dataListLoading = true
      this.$http({
        // url: 'http://localhost:5050/api/commodity/category/list/tree',
        url: this.$http.adornUrl("/commodity/category/list/tree"),
        method: 'get',
      }).then(({ data }) => { //data加{}会进行对象解构
        this.categorys = data.data
      })
    },
    // 表单提交
    dataFormSubmit() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.$http({
            url: this.$http.adornUrl(`/commodity/attrgroup/${!this.dataForm.id ? 'save' : 'update'}`),
            method: 'post',
            data: this.$http.adornData({
              'id': this.dataForm.id || undefined,
              'name': this.dataForm.name,
              'sort': this.dataForm.sort,
              'description': this.dataForm.description,
              'icon': this.dataForm.icon,
              'categoryId': this.cascadedCategoryId[this.cascadedCategoryId.length - 1]
            })
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
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
