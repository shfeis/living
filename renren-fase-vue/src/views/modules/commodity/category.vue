<template>
  <div>
    <!--    批量删除的按钮-->
    <el-button type="danger" @click="batchDelete()">
      批量删除
    </el-button>
    <!--    element的树形图-->
    <el-tree
      :data="data"
      :props="defaultProps"
      ref="categoryTree"
      show-checkbox
      :default-expanded-keys="expandedkey"
      node-key="id"
      :expand-on-click-node="false">
     <span class="custom-tree-node" slot-scope="{ node, data }">
        <span>{{ node.label }}</span>
        <span>
          <el-button
            v-if="node.level <= 2"
            type="text"
            size="mini"
            @click="() => append(data)">
            添加
          </el-button>
          <el-button
            type="text"
            size="mini"
            @click="() => edit(data)">
            编辑
          </el-button>
          <el-button
            v-if="node.childNodes.length == 0"
            type="text"
            size="mini"
            @click="() => remove(node, data)">
            删除
          </el-button>
        </span>
      </span>
    </el-tree>
    <!--    element的对话框-->
    <el-dialog title="添加/修改 分类" :visible.sync="dialogVisible" width="30%">
      <el-form :model="category">
        <el-form-item label="分类名">
          <el-input v-model="category.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="图标">
          <el-input v-model="category.icon" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="单位">
          <el-input v-model="category.proUnit" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addOrUpdate()">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
export default {
  data() {
    return {
      data: [], //data域
      dialogType: "", //标识对话框是编辑还是添加
      dialogVisible: false, //控制是否显示对话框
      category: {  //初始化对话框数据,属性为JavaBean属性
        id: null,
        name: "",
        parentId: 0,
        catLevel: 0,
        isShow: 1,
        sort: 0,
        icon: "",
        proUnit: "",
        proCount: 0
      },
      expandedkey: [],
      defaultProps: { //指定属性名，如果name改为aaa,那么data中的name也要改为aaa
        children: 'childrenCategories',
        label: 'name'
      }
    };
  },
  methods: {
    //处理批量删除的方法
    batchDelete() {
      //通过el-tree标签的ref属性获取到被选中的元素
      var checkedNodes = this.$refs.categoryTree.getCheckedNodes()
      //先收集被选中元素的id 和 名称
      var ids = []
      var categoryNames = []
      for (var checkedNode of checkedNodes) {
        ids.push(checkedNode.id)
        categoryNames.push(checkedNode.name)
      }
      //给出提示，如果用户确定删除，就调用后端程序完成批量删除
      this.$confirm(`是否批量删除【${categoryNames}】菜单？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => { //点击确定，删除
        this.$http({
          // 当点击分类管理按钮时调用该url路径的方法，显示元素到页面
          // url: 'http://localhost:9090/commodity/category/delete',
          url: this.$http.adornUrl("/commodity/category/delete"),
          method: 'post',
          data: this.$http.adornData(ids, false) //发出请求时携带id值
        }).then(({data}) => { //data加{}会进行对象解构
          //弹出删除成功的提示框
          this.$message({
            message: '批量删除成功',
            type: 'success'
          })
          this.getCategories()  //重新调用方法，刷新页面
        })
      }).catch(() => { //点击取消，不做修改
      })
    },
    //区分对话框是添加/修改的方法
    addOrUpdate() {
      if (this.dialogType == "add") {
        this.addCategory();
      }
      if (this.dialogType == "edit") {
        this.updateCategory()
      }

    },
    //点击编辑对话框确定按钮的方法
    updateCategory() {
      //将对话框提交的数据分别赋值给相应属性，对象解耦
      var {id, name, icon, proUnit} = this.category
      this.$http({
        // url: 'http://localhost:9090/commodity/category/update',
        url: this.$http.adornUrl("/commodity/category/update"),
        method: 'post',
        data: this.$http.adornData({id, name, icon, proUnit}, false)
      }).then(({data}) => { //data加{}会进行对象解构,如果$http成功执行就会执行then
        //弹出修改成功的提示框
        this.$message({
          message: '分类信息修改成功',
          type: 'success'
        })
        this.dialogVisible = false //关闭对话框
        this.getCategories()  //重新调用方法，刷新页面
        this.expandedkey = [this.category.parentId] //将添加的分类元素的父分层展开
      })
    },
    //点击编辑按钮时显示对话框的方法
    edit(data) {
      this.dialogType = "edit" //设置对话框类型
      this.dialogVisible = true //显示对话框
      //从后端获取数据库表元素
      this.$http({
        // url: `http://localhost:9090/commodity/category/info/${data.id}`,
        url: this.$http.adornUrl(`/commodity/category/info/${data.id}`),
        method: 'get',
      }).then(({data}) => { //data加{}会进行对象解构,如果$http成功执行就会执行then
        //将数据库中获取到的数据映射到对话框
        this.category.name = data.category.name
        this.category.icon = data.category.icon
        this.category.proUnit = data.category.proUnit
        this.category.id = data.category.id
        this.category.parentId = data.category.parentId
      })
    },
    //处理点击添加对话框确认按钮的方法
    addCategory() {
      this.$http({
        // 当点击分类管理按钮时调用该url路径的方法，显示元素到页面
        // url: 'http://localhost:9090/commodity/category/save',
        url: this.$http.adornUrl("/commodity/category/save"),
        method: 'post',
        data: this.$http.adornData(this.category, false) //发出请求时携带id值
      }).then(({data}) => { //data加{}会进行对象解构
        //弹出删除成功的提示框
        this.$message({
          message: '分类信息保存成功',
          type: 'success'
        })
        this.dialogVisible = false //关闭对话框
        this.getCategories()  //重新调用方法，刷新页面
        this.expandedkey = [this.category.parentId] //将添加的分类元素的父分层展开
      })
    },
    //点击添加按钮时触发
    append(data) {
      this.dialogType = "add"
      this.dialogVisible = true
      this.category.parentId = data.id
      this.category.sort = 0
      this.category.proUnit = ""
      this.category.proCount = 0
      this.category.name = ""
      this.category.isShow = 1
      this.category.id = null
      this.category.icon = ""
      this.category.catLevel = data.catLevel * 1 + 1
    },
    //点击删除按钮时触发
    remove(node, data) {
      var ids = [data.id] //获取要删除元素的id
      //点击删除按钮时弹窗是否确认
      this.$confirm(`是否删除【${data.name}】菜单？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => { //点击确定，删除
        this.$http({
          // 当点击分类管理按钮时调用该url路径的方法，显示元素到页面
          // url: 'http://localhost:9090/commodity/category/delete',
          url: this.$http.adornUrl("/commodity/category/delete"),
          method: 'post',
          data: this.$http.adornData(ids, false) //发出请求时携带id值
        }).then(({data}) => { //data加{}会进行对象解构
          //弹出删除成功的提示框
          this.$message({
            message: '删除成功',
            type: 'success'
          })
          this.getCategories()  //重新调用方法，刷新页面
          this.expandedkey = [node.parent.data.id] //将删除元素的父分层展开
        })
      }).catch(() => { //点击取消，不做修改
      })
    },
    // 该方法获取分类带层级的列表
    getCategories() {
      this.dataListLoading = true
      this.$http({
        // 当点击分类管理按钮时调用该url路径的方法，显示元素到页面
        // url: 'http://localhost:5050/api/commodity/category/list/tree',
        url: this.$http.adornUrl("/commodity/category/list/tree"),
        method: 'get',
      }).then(({data}) => { //data加{}会进行对象解构
        this.data = data.data; //将方法获取到的元素赋予data域
      })
    }
  },
  // Vue的生命周期，当打开页面时自动调用
  created() {
    this.getCategories();
  }
};
</script>

<style scoped>

</style>
