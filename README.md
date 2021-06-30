# echo_kt

#### 介绍
在线音乐软件，页面设计有点单调，
可以从多个网站搜索和下载音乐，方便寻找音乐,界面简洁没有广告
下载软件点击上方代码区的安装包文件夹或者在发行版里下载apk文件

部分界面截图如下：
![输入图片说明](https://images.gitee.com/uploads/images/2021/0630/100254_08933354_8318407.png "16250184101727.png")

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