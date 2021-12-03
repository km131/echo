# echo_kt

#### 介绍
- 在线音乐软件，最新版大小7M左右
- 可以从多个网站搜索和下载音乐,界面简洁

部分界面截图如下：
![输入图片说明](https://images.gitee.com/uploads/images/2021/0630/100254_08933354_8318407.png "16250184101727.png")

#### 软件架构
基于MVVM模式集成谷歌官方推荐的JetPack组件库：Lifecycle、ViewModel、LiveData、DataBinDing、Navigation、Room、hilt、paging组件。
音频播放状态管理采用RxJava,网络通讯采用Retrofit

项目采用单Activity+多Fragment实现，

整个项目全部使用Kotlin语言。

#### 使用说明

1.  如需要访问本地音乐请手动开启权限
2.  如有需要请联系作者2602241712@qq.com