<template>
  <div>
    <el-upload action='' list-type="picture" :multiple="false" :show-file-list="showFileList" :file-list="fileList"
      :before-upload="beforeUpload" :on-remove="handleRemove" :on-preview="handlePreview">
      <el-button size="small" type="primary">点击上传</el-button>
      <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过10MB</div>
    </el-upload>
    <!-- <el-dialog :visible.sync="dialogVisible">
      <img width="100%" :src="fileList[0].url" alt="">
    </el-dialog> -->
  </div>
</template>
<script>
import { policy } from './policy'
export default {
  name: 'singleUpload',
  props: {
    value: String
  },
  computed: {
    imageUrl() {
      return this.value;
    },
    imageName() {
      if (this.value != null && this.value !== '') {
        return this.value.substring(this.value.lastIndexOf("/") + 1);
      } else {
        return null;
      }
    },
    fileList() {
      return [{
        name: this.imageName,
        url: this.imageUrl
      }]
    },
    showFileList: {
      get: function () {
        return this.value !== null && this.value !== '' && this.value !== undefined;
      },
      set: function (newValue) {
      }
    }
  },
  data() {
    return {
      dialogVisible: false
    };
  },
  methods: {
    emitInput(val) {
      this.$emit('input', val)
    },
    handleRemove(file, fileList) {
      this.emitInput('');
    },
    handlePreview(file) {
      this.dialogVisible = true;
    },
    beforeUpload(file) {
      // 上传之前先调用policy_minio组件的policy方法获取签名url
      return new Promise((resolve, reject) => {
        policy(file.name).then(response => {
          let url = response.data.url;
          //将文件名改为后台返回的（原文件名前拼了段uuid），不然同名文件会覆盖
          let newFileName = response.data.name;
          let imageType = "image/" + newFileName.substring(newFileName.lastIndexOf(".") + 1);
          let newFile = new File([file], response.data.name, { type: imageType });
          this.$axios.request({
            url: response.data.host,
            method: 'put',
            data: newFile
          }).then((res) => {
            this.showFileList = true;
            this.fileList.pop();
            this.fileList.push({ name: file.name, url: url });
            this.emitInput(this.fileList[0].url);
          }).catch(() => {
            console.log("响应数据:上传失败");
          })

        }).catch(err => {
          console.log(JSON.stringify(err));
          reject(false)
        })
      })
    },
    handleUploadSuccess(res, file) {
    }
  }
}
</script>
<style></style>