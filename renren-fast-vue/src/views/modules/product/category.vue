<template>
  <div>
    <div style="display: flex; align-items: center">
      <el-switch
        style="display: block"
        v-model="isDraggable"
        active-color="#13ce66"
        inactive-color="#ff4949"
        active-text="允许拖拽"
        inactive-text="禁止拖拽"
      >
      </el-switch>
      <el-button
        type="danger"
        round
        style="margin-left: 60px"
        @click="batchDelete"
        >批量删除</el-button
      >
    </div>
    <el-tree
      :data="menus"
      :props="defaultProps"
      @node-click="handleNodeClick"
      :expand-on-click-node="false"
      show-checkbox
      node-key="catId"
      :default-expanded-keys="expandedKey"
      :draggable="isDraggable"
      :allow-drop="allowDrop"
      @node-drop="handleDrop"
      ref="menuTree"
    >
      <span class="custom-tree-node" slot-scope="{ node, data }">
        <span>{{ node.label }}</span>
        <span>
          <el-button
            v-if="node.level <= 2"
            type="text"
            size="mini"
            @click="() => append(data)"
          >
            Append
          </el-button>
          <el-button
            v-if="node.isLeaf"
            type="text"
            size="mini"
            @click="() => remove(node, data)"
          >
            Delete
          </el-button>
          <el-button type="text" size="mini" @click="() => edit(data)">
            Edit
          </el-button>
        </span>
      </span></el-tree
    >
    <el-dialog title="提示" :visible.sync="dialogVisible">
      <el-form :model="category">
        <el-form-item label="分类名称">
          <el-input v-model="category.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="图标">
          <el-input v-model="category.icon" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="计量单位">
          <el-input
            v-model="category.productUnit"
            autocomplete="off"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitCategory">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      menus: [],
      defaultProps: {
        children: "children",
        label: "name",
      },
      expandedKey: [],
      dialogVisible: false,
      category: {
        catId: null,
        name: "",
        parentCid: 0,
        catLevel: 0,
        showStatus: 1,
        sort: 0,
        icon: "",
        productUnit: "",
        productCount: 0,
      },
      dialogType: "",
      updateNodes: [],
      isDraggable: false,
    };
  },
  methods: {
    handleNodeClick(data) {
      console.log(data);
    },

    getMenus() {
      this.$http({
        url: this.$http.adornUrl("/product/category/list/tree"),
        method: "get",
      }).then(({ data }) => {
        console.log("查询到数据", data.data);
        this.menus = data.data;
      });
    },

    batchDelete() {
      let checkedIds = [];
      let checkedNames = [];
      let checkedNodes = this.$refs.menuTree.getCheckedNodes();
      console.log("checkedNodes", checkedNodes);
      for (let i = 0; i < checkedNodes.length; i++) {
        checkedIds.push(checkedNodes[i].catId);
        checkedNames.push(checkedNodes[i].name);
      }
      this.$confirm(
        `此操作将批量删除【${checkedNames}】分类, 是否继续?`,
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(() => {
          this.$http({
            url: this.$http.adornUrl(`/product/category/delete`),
            method: "post",
            data: this.$http.adornData(checkedIds, false),
          })
            .then(({ data }) => {
              console.log("批量删除成功");
              this.$message({
                type: "success",
                message: "批量删除成功!",
              });
              this.getMenus();
            })
            .catch(() => {
              this.$message({
                type: "error",
                message: "批量删除失败",
              });
            });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消批量删除",
          });
        });
    },

    allowDrop(draggingNode, dropNode, type) {
      // 当前节点拥有层数 与 添加到的父节点所在层数 之和 应小于等于3
      console.log(draggingNode, dropNode, type);
      var maxLevel = draggingNode.level;
      maxLevel = this.childLevel(draggingNode, maxLevel);
      console.log("maxLevel", maxLevel);
      let currentLevel = maxLevel - draggingNode.level + 1;
      console.log("currentLevel", currentLevel);
      if (type == "inner") {
        return dropNode.level + currentLevel <= 3;
      } else {
        return dropNode.parent.level + currentLevel <= 3;
      }
    },

    childLevel(node, maxLevel) {
      if (node.childNodes != null && node.childNodes.length > 0) {
        for (let i = 0; i < node.childNodes.length; i++) {
          if (node.childNodes[i].level > maxLevel) {
            maxLevel = node.childNodes[i].level;
          }
          maxLevel = this.childLevel(node.childNodes[i], maxLevel);
        }
      }
      return maxLevel;
    },

    handleDrop(draggingNode, dropNode, dropType, ev) {
      console.log("tree drop: ", draggingNode, dropNode, dropType);
      // 更新父节点id
      let pCid = 0;
      let siblings = null;
      this.updateNodes = [];
      if (dropType == "inner") {
        pCid = dropNode.data.catId;
        siblings = dropNode.childNodes;
        console.log("pCid", pCid);
        console.log("siblings", siblings);
      } else {
        pCid =
          dropNode.parent.data.catId == undefined
            ? 0
            : dropNode.parent.data.catId;
        siblings = dropNode.parent.childNodes;
        console.log("pCid", pCid);
        console.log("siblings", siblings);
      }
      // 更新顺序sort
      for (let i = 0; i < siblings.length; i++) {
        if (siblings[i].data.catId == draggingNode.data.catId) {
          let catLevel = draggingNode.level;
          if (siblings[i].level != draggingNode.level) {
            catLevel = siblings[i].level;
            this.updateChildLevel(siblings[i]);
          }
          this.updateNodes.push({
            catId: siblings[i].data.catId,
            sort: i,
            parentCid: pCid,
            catLevel: catLevel,
          });
        } else {
          this.updateNodes.push({ catId: siblings[i].data.catId, sort: i });
        }
      }
      console.log("updateNodes", this.updateNodes);
      this.$http({
        url: this.$http.adornUrl(`/product/category/update/batch`),
        method: "post",
        data: this.$http.adornData(this.updateNodes, false),
      }).then(({ data }) => {
        console.log("菜单修改成功");
        this.$message({
          type: "success",
          message: "菜单修改成功!",
        });
        this.getMenus();
        this.expandedKey = [pCid];
      });
    },

    updateChildLevel(node) {
      if (node.childNodes != null && node.childNodes.length > 0) {
        for (let i = 0; i < node.childNodes.length; i++) {
          this.updateNodes.push({
            catId: node.childNodes[i].data.catId,
            catLevel: node.childNodes[i].level,
          });
          this.updateChildLevel(node.childNodes[i]);
        }
      }
    },

    submitCategory() {
      if (this.dialogType == "append") {
        this.appendCategory();
      }
      if (this.dialogType == "edit") {
        this.editCategory();
      }
    },

    append(data) {
      this.dialogType = "append";
      this.category.name = "";
      console.log("append数据", data);
      this.dialogVisible = true;
      this.category.parentCid = data.catId;
      this.category.catLevel = data.catLevel * 1 + 1; // 乘1是避免data.catLevel是字符串
      this.category.catId = null;
      this.category.showStatus = 1;
      this.category.sort = 0;
      this.category.icon = "";
      this.category.productUnit = "";
      this.category.productCount = 0;
    },

    appendCategory() {
      console.log("要提交的category表单数据", this.category);
      this.$http({
        url: this.$http.adornUrl(`/product/category/save`),
        method: "post",
        data: this.$http.adornData(this.category, false),
      }).then(({ data }) => {
        this.dialogVisible = false;
        console.log("添加成功");
        this.$message({
          type: "success",
          message: "添加成功!",
        });
        this.getMenus();
        this.expandedKey = [this.category.parentCid];
      });
    },

    remove(node, data) {
      console.log("removeNode", node);
      console.log("remove数据", data);
      var ids = [data.catId];
      this.$confirm(`此操作将删除【${data.name}】分类, 是否继续?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.$http({
            url: this.$http.adornUrl(`/product/category/delete`),
            method: "post",
            data: this.$http.adornData(ids, false),
          }).then(({ data }) => {
            console.log("删除成功");
            this.$message({
              type: "success",
              message: "删除成功!",
            });
            this.getMenus();
            this.expandedKey = [node.data.parentCid];
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },

    edit(data) {
      this.dialogType = "edit";
      console.log("edit数据", data);
      this.dialogVisible = true;
      this.$http({
        url: this.$http.adornUrl(`/product/category/info/${data.catId}`),
        method: "get",
      }).then(({ data }) => {
        console.log("查询到数据", data);
        this.category = data.category;
      });
    },

    editCategory() {
      console.log("要提交的category表单数据", this.category);
      var { catId, name, icon, productUnit } = this.category;
      this.$http({
        url: this.$http.adornUrl(`/product/category/update`),
        method: "post",
        data: this.$http.adornData({ catId, name, icon, productUnit }, false),
      }).then(({ data }) => {
        this.dialogVisible = false;
        console.log("修改成功");
        this.$message({
          type: "success",
          message: "修改成功!",
        });
        this.getMenus();
        this.expandedKey = [this.category.parentCid];
      });
    },
  },
  created() {
    this.getMenus();
  },
};
</script>

<style>
</style>