<template>
  <el-row :gutter="20">
    <el-col :span="5">
      <el-tree ref="categoryTree" :data="data" :props="defaultProps" node-key="id" @node-click="nodeclick">
        <span class="custom-tree-node" slot-scope="{ node, data }">
          <span>{{ node.label }}</span>
        </span>
      </el-tree>

    </el-col>
    <el-col :span="19">
      <div class="mod-config">
        <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
          <el-form-item>
            <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-button @click="getDataList()">查询</el-button>
            <el-button v-if="isAuth('commodity:attrgroup:save')" type="primary"
              @click="addOrUpdateHandle()">新增</el-button>
            <el-button v-if="isAuth('commodity:attrgroup:delete')" type="danger" @click="deleteHandle()"
              :disabled="dataListSelections.length <= 0">批量删除</el-button>
          </el-form-item>
        </el-form>
        <el-table :data="dataList" border v-loading="dataListLoading" @selection-change="selectionChangeHandle"
          style="width: 100%;">
          <el-table-column type="selection" header-align="center" align="center" width="50">
          </el-table-column>
          <el-table-column prop="id" header-align="center" align="center" label="id">
          </el-table-column>
          <el-table-column prop="name" header-align="center" align="center" label="组名">
          </el-table-column>
          <el-table-column prop="sort" header-align="center" align="center" label="排序">
          </el-table-column>
          <el-table-column prop="description" header-align="center" align="center" label="说明">
          </el-table-column>
          <el-table-column prop="icon" header-align="center" align="center" label="组图标">
          </el-table-column>
          <el-table-column prop="categoryId" header-align="center" align="center" label="所属分类 id">
          </el-table-column>
          <el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="relationAttrHandle(scope.row.id)">关联</el-button>
              <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
              <el-button type="text" size="small" @click="deleteHandle(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex"
          :page-sizes="[10, 20, 50, 100]" :page-size="pageSize" :total="totalPage"
          layout="total, sizes, prev, pager, next, jumper">
        </el-pagination>
        <!-- 显示 新增/修改 的弹窗 -->
        <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
        <!-- 显示 属性-属性组关联关系 的弹窗 -->
        <RelationUpdate v-if="relationVisible" ref="relationUpdate" @refreshData="getDataList"></RelationUpdate>
      </div>
    </el-col>
  </el-row>
</template>

<script>
//引入vue文件，名称由 <add-or-update>和<RelationUpdate>标签的ref属性决定
import AddOrUpdate from './attrgroup-add-or-update'
import RelationUpdate from './attrgroup-attr-relation'

export default {
  //要使用引入的vue文件就必须进行声明
  components: {
    AddOrUpdate,
    RelationUpdate
  },
  data() { //数据池
    return {
      //显示某个属性分组下关联的所有基本属性情况,用于控制是否显示对话框
      relationVisible: false,
      addOrUpdateVisible: false,
      dataForm: {
        key: ''
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      data: [],
      defaultProps: { //指定属性名，如果name改为aaa,那么data中的name也要改为aaa
        children: 'childrenCategories',
        label: 'name'
      },
      catId: 0 //categoryId,表示所属分层id
    }
  },
  //Vue的钩子函数，当点击属性分组页时自动调用
  activated() {
    this.getDataList()
  },
  created() {
    this.getCategories()
  },
  methods: { //方法池
    //当点击el-tree的树形目录时调用
    nodeclick(data) {
      if (data.catLevel == 3) { //如果点击的树形目录是第3级分类，就把它的categoryId携带给后端程序
        this.catId = data.id
        this.getDataList()
      }
    },
    // 该方法获取分类带层级的列表
    getCategories() {
      this.dataListLoading = true
      this.$http({
        // 当点击分类管理按钮时调用该url路径的方法，显示元素到页面
        // url: 'http://localhost:5050/api/commodity/category/list/tree',
        url: this.$http.adornUrl("/commodity/category/list/tree"),
        method: 'get',
      }).then(({ data }) => { //data加{}会进行对象解构
        this.data = data.data; //将方法获取到的元素赋予data域
      })
    },
    // 点击查询时，获取数据列表
    getDataList() {
      this.dataListLoading = true
      this.$http({
        url: this.$http.adornUrl(`/commodity/attrgroup/list/${this.catId}`),
        method: 'get',
        params: this.$http.adornParams({
          'page': this.pageIndex,
          'limit': this.pageSize,
          'key': this.dataForm.key
        })
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.dataList = data.page.list
          this.totalPage = data.page.totalCount
        } else {
          this.dataList = []
          this.totalPage = 0
        }
        this.dataListLoading = false
      })
    },
    // 每页数
    sizeChangeHandle(val) {
      this.pageSize = val
      this.pageIndex = 1
      this.getDataList()
    },
    // 当前页
    currentChangeHandle(val) {
      this.pageIndex = val
      this.getDataList()
    },
    // 多选
    selectionChangeHandle(val) {
      this.dataListSelections = val
    },
    // 点击新增/修改按钮时调用
    addOrUpdateHandle(id) {
      this.addOrUpdateVisible = true //显示对话框
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id)
      })
    },
    // 点击关联按钮时调用， 属性-属性组关联关系
    relationAttrHandle(groupId) {
      this.relationVisible = true //显示对话框
      this.$nextTick(() => {
        this.$refs.relationUpdate.init(groupId)
      })
    },
    // 删除
    deleteHandle(id) {
      var ids = id ? [id] : this.dataListSelections.map(item => {
        return item.id
      })
      this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: this.$http.adornUrl('/commodity/attrgroup/delete'),
          method: 'post',
          data: this.$http.adornData(ids, false)
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.getDataList()
              }
            })
          } else {
            this.$message.error(data.msg)
          }
        })
      })
    }
  }
}
</script>
