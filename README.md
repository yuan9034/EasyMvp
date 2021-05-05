# Mvp快速开发框架
#### 本框架采用mvp架构，整合koin,retrofit,协程,lifecycle等主流库，并结合EasyMvpTemplate一键生成需要的view,presenter,model以及依赖注入相关代码
##### 术语解释：
###### 1.koin 是一个用于 Kotlin的实用型轻量级依赖注入框架，采用纯 Kotlin 编写而成，仅使用功能解析，无代理、无代码生成、无反射
###### 2.retrofit 一款非常流行的网络请求框架
###### 3.协程 一种计算机编程语言概念,kotlin协程的作用可以简单理解为像写同步代码一样编写异步操作
###### 4.lifecycle Android官方api,本框架用来跟view的生命周期绑定取消异步操作

### 使用步骤
###### 1.添加依赖,依赖方式如下:
implementation 'com.yuanwanli:easymvp:0.0.3'
###### 2.自定义Application继承BaseApplication

```
class MyApp : BaseApplication() {
    override fun onCreate() {
        startKoin {
            //logger
            androidLogger(Level.DEBUG)
            //android context
            androidContext(this@MyApp)
        }
        //启动startKoin 后 可以使用
        loadKoinModules(EasyMvpModule.theLibModule)
        super.onCreate()
    }
}
```
AndroidManifest指定application

```
<application
        android:name=".MyApp"
```

###### 3.AndroidManifest 配置框架自定义属性

```
<meta-data
            android:name="com.xdja.app.config.GlobalConfiguration"
            android:value="ConfigModule" />
```
详见demo
##### 目前框架支持的配置有
###### 1.联网的baseurl
###### 2.自定义应用缓存目录
###### 3.自定义缓存策略(内置lrucache策略以及IntelligentCache策略)
###### 4.自定义打印（网络打印内容以及格式)
###### 5.自定义图片加载策略（内置Glide加载)
###### 6.自定义GlobalHttpHandler（处理Http请求和响应结果的处理类）
###### 7.自定义gson配置
###### 8.自定义Retrofit配置
###### 9.自定义OKHttp配置
详见demo

### mvp代码一键生成
###### 1.下载EasyMvpTemplate 地址： https://github.com/yuan9034/EasyMvpTemplate
###### 2.复制EasyMvpTemplate文件夹到你自己的AndroidStudio安装目录里面的Activity模板目录，重启AndroidStudio
Windows : AS安装目录/plugins/android/lib/templates/activities
Mac : /Applications/AndroidStudio.app/Contents/plugins/android/lib/templates/activities
###### 3.AndroidStudio里面 new->Activity->EasyMvp 全家桶 即可创建对应的mvp相关代码
如图
![]( http://xdjacdn.flyou.ren/yuanwanli/2020/07/18/1595039358(1).png )
按照下图提示，进项相应的输入
![]( http://xdjacdn.flyou.ren/yuanwanli/2020/07/18/1595039860(1).png )
###### 4.将生成的koin注入的module加入到application的初始化代码中
生成的代码在项目包名-di-XXXModule.kt里面（XXX为上步骤你输入koin注入的module名称）
比如我的demo里面的生成的TestModule,生成代码之后加入到application的初始化代码中

```
val TestModule = module {
    scope<TestActivity> {
        scoped<TestContract.Model> { TestModel(get()) }
        scoped {
            TestPresenter(get(), this.getSource())
        }
    }
}
```

```
class MyApp : BaseApplication() {
    override fun onCreate() {
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MyApp)
            //此处将koin生成的module加入，可以用+号添加多个
            modules(TestModule)
        }
        loadKoinModules(EasyMvpModule.theLibModule)
        super.onCreate()
    }
}
```

将TestModule
注意：由于该架构采用纯kotlin开发,而且采用了java不具备的协程功能，故该模板只能生成kotlin代码

### model里面io操作建议以下两种方式
```
    #方式一 利用flow函数发射数据源 类似rxjava
    fun getTest1(): Flow<TestBean> = flow {
        emit(mRepositoryManager!!.obtainRetrofitService(ServerApi::class.java)
            .getServer("https://wanandroid.com/wxarticle/chapters/json", 2))
    }
    #方式二 利用suspend函数里面的withContext切换线程并返回
    suspend fun getTest(): TestBean {
        return withContext(Dispatchers.IO){
            mRepositoryManager!!.obtainRetrofitService(ServerApi::class.java)
                .getServer("https://wanandroid.com/wxarticle/chapters/json", 2)
        }
    }
```
### 相对应的presenter里面协程使用方式
示例代码如下:

```
    # 针对model方式一的调用，类似rxjava，框架自动解绑订阅
    fun getTest1() {
        launch {
            mModel!!.getTest1()
                .flowOn(Dispatchers.IO)
                .collect {
                    mRootView!!.showBean(it)
                }
        }
    }
    # 针对model方式二的调用，框架自动解绑订阅
    fun getTest() {
        launch {
            delay(2000)
            val bean = mModel!!.getTest()
            mRootView!!.showBean(bean)
        }
    }
```
launch函数本身运行在主线程
basepresenter封装了mainScope，finallyBlock（协程体执行结束回调），failBlock（协程体抛出异常回调）
### EventBus使用指南
框架内使用EventBusManager管理EventBus,用户自己决定使用AndroidEventbus还是Eventbus,取决于用户依赖"org.simple:androideventbus:1.0.5.1"还是依赖"org.greenrobot:eventbus:3.2.0"。可同时使用。
BaseActivity,BaseFragment里面的useEventbus默认返回true,无需再次注册

