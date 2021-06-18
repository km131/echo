# echo_kt

#### 介绍
在线音乐软件，页面设计有点简陋，
可以从多个网站搜索和下载音乐，方便寻找音乐，解决不知道哪个网站有版权的问题。
工具的本意是聚合搜索，API 是从公开的网络中获得，不是破解版，听不了付费歌曲。

部分界面截图如下
![输入图片说明](https://images.gitee.com/uploads/images/2021/0618/101003_ab04b96e_8318407.jpeg "echo1.JPG")
![输入图片说明](https://images.gitee.com/uploads/images/2021/0618/101044_ab064074_8318407.jpeg "echo2.JPG")
![输入图片说明](https://images.gitee.com/uploads/images/2021/0618/101055_3f7854dd_8318407.jpeg "echo3.JPG")
![输入图片说明](https://images.gitee.com/uploads/images/2021/0618/101112_ae9ac73f_8318407.jpeg "echo4.JPG")

#### 软件架构
基础框架选用MVVM，选用的Jetpack组件包括Lifecycle、ViewModel、LiveData、DataBinDing、Navigation、Room。

项目基于Navigation由单Activity多Fragment实现，

整个项目全部使用Kotlin语言，广泛应用了协程编写了大量的扩展函数。


#### 使用说明

1.  如需要访问本地音乐请手动开启存储权限
2.  该软件使用时要求网络流畅

#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request
