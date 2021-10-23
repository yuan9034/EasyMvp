package com.xdja.easymvp.base.delegate

import android.app.Application
import android.content.ComponentCallbacks2
import android.content.Context
import android.os.Environment
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.Utils
import com.bumptech.glide.util.Preconditions
import com.xdja.easymvp.base.App
import com.xdja.easymvp.base.AppComponentCallbacks
import com.xdja.easymvp.base.BaseApplication
import com.xdja.easymvp.di.component.AppComponent
import com.xdja.easymvp.di.module.GlobalConfigModule
import com.xdja.easymvp.integration.ConfigModule
import com.xdja.easymvp.integration.ManifestParser
import com.xdja.easymvp.integration.cache.IntelligentCache
import org.json.JSONObject
import xcrash.ICrashCallback
import xcrash.TombstoneParser
import xcrash.XCrash
import java.io.File

/**
 * @author yuanwanli
 * AppDelegate 可以代理 Application 的生命周期,在对应的生命周期,执行对应的逻辑,因为 Java 只能单继承
 * 所以当遇到某些三方库需要继承于它的 Application 的时候,就只有自定义 Application 并继承于三方库的 Application
 * 这时就不用再继承 BaseApplication,只用在自定义Application中对应的生命周期调用AppDelegate对应的方法
 * (Application一定要实现APP接口),框架就能照常运行
 * @see BaseApplication
 * @date 2020/6/24
 */
class AppDelegate(context: Context) : App, AppLifeCycles {
    protected var mActivityLifecycle: Application.ActivityLifecycleCallbacks? = null
    private var mApplication: Application? = null
    private var mAppComponent: AppComponent? = null
    private var mModules: List<ConfigModule>? = null
    private var mAppLifeCycles: MutableList<AppLifeCycles>? = mutableListOf()
    private var mActivityLifecycles: MutableList<Application.ActivityLifecycleCallbacks>? =
        mutableListOf()
    private var mComponentCallbacks: ComponentCallbacks2? = null

    init {
        //用反射,将AndroidManifest.xml中带有ConfigModule标签的class转换成对象集合(List<ConfigModule>)
        this.mModules = ManifestParser(context).parse()
        mModules?.forEach {
            //将框架外部,开发者实现的Application的生命周期回调(AppLifeCycles)存入mAppLifeCycles集合
            it.injectAppLifecycle(context, mAppLifeCycles!!)
            //将框架外部,开发者实现的Activity的生命周期回调(ActivityLifecycleCallbacks)存入mActivityLifecycles集合
            it.injectActivityLifecycle(context, mActivityLifecycles!!)
        }
    }

    override fun getAppComponent(): AppComponent {
        Preconditions.checkNotNull(
            mAppComponent,
            "${AppComponent::class.java.name} == null, first call ${javaClass.name}#onCreate(Application) in ${if (mApplication == null) Application::class.java.name else mApplication!!.javaClass.name}#onCreate()"
        )
        return mAppComponent!!
    }

    override fun attachBaseContext(base: Context) {
        mAppLifeCycles?.forEach {
            it.attachBaseContext(base)
        }
    }

    override fun onCreate(application: Application) {
        this.mApplication = application
        injectAppComponent()
        initEasyMvp()
        Utils.init(application)
        initCrash(application)
    }

    /**
     * mvp框架初始化参数
     */
    private fun initEasyMvp() {
        //将 ConfigModule 的实现类的集合存放到缓存 Cache, 可以随时获取
        mAppComponent?.extras?.put(
            IntelligentCache.getKeyOfKeep(ConfigModule::class.java.name),
            mModules!!
        )
        this.mModules = null
        //注册框架内部已实现Activity生命周期逻辑
        mApplication!!.registerActivityLifecycleCallbacks(mActivityLifecycle)
        //注册框架外部,开发者扩展的Activity生命周期逻辑
        //每个ConfigModule的实现类可以声明多个Activity的生命周期回调
        //也可以有N个ConfigModule的实现类(支持插件化项目各个module的独特需求)
        mActivityLifecycles?.forEach {
            mApplication!!.registerActivityLifecycleCallbacks(it)
        }
        mComponentCallbacks = AppComponentCallbacks(mApplication!!, mAppComponent)
        //注册回调,内存紧张时释放部分内存
        mApplication!!.registerComponentCallbacks(mComponentCallbacks)
        //执行框架外部,开发者扩展的App onCreate 逻辑
        mAppLifeCycles?.forEach {
            it.onCreate(mApplication!!)
        }
    }

    //注入AppComponent
    private fun injectAppComponent() {
        val globalConfigModule = getGlobalConfigModule(mApplication!!, mModules)
        mAppComponent = AppComponent()
        mAppComponent!!.inject(globalConfigModule)
        mActivityLifecycle = mAppComponent!!.mActivityLifecycle
    }

    //
    private fun getGlobalConfigModule(
        context: Context,
        modules: List<ConfigModule>?
    ): GlobalConfigModule {
        val builder = GlobalConfigModule.builder()
        //
        modules?.forEach {
            it.applyOptions(context, builder)
        }
        return builder.build()
    }

    override fun onTerminate(application: Application) {
        mApplication!!.unregisterActivityLifecycleCallbacks(mActivityLifecycle)
        mApplication!!.unregisterComponentCallbacks(mComponentCallbacks)
        mActivityLifecycles?.forEach {
            mApplication!!.unregisterActivityLifecycleCallbacks(it)
        }
        mAppLifeCycles?.forEach {
            it.onTerminate(mApplication!!)
        }
        this.mAppComponent = null
        this.mActivityLifecycle = null
        this.mActivityLifecycles = null
        this.mComponentCallbacks = null
        this.mAppLifeCycles = null
        this.mApplication = null
    }

    /**
     * XCrash初始化
     */
    private fun initCrash(application: Application) {
        val crashCallBack = ICrashCallback { logPath, emergency ->
            val crashFile =
                Environment.getExternalStorageDirectory().absolutePath + "/banglaile/app/crash.txt"
            val errorFile = File(crashFile)
            val success = FileUtils.createOrExistsFile(errorFile)
            if (success) {
                errorFile.writeText(
                    JSONObject(
                        TombstoneParser.parse(
                            logPath,
                            emergency
                        ) as Map<*, *>
                    ).toString()
                )
            }
        }
        XCrash.init(
            application,
            XCrash.InitParameters().setAppVersion(AppUtils.getAppVersionName())
                .setJavaRethrow(true)
                .setJavaLogCountMax(10)
                .setJavaDumpAllThreadsWhiteList(arrayOf("^main$", "^Binder:.*", ".*Finalizer.*"))
                .setJavaDumpAllThreadsCountMax(10)
                .setJavaCallback(crashCallBack)
                .setNativeRethrow(true)
                .setNativeLogCountMax(10)
                .setNativeDumpAllThreadsWhiteList(
                    arrayOf(
                        "^xcrash\\.sample$",
                        "^Signal Catcher$",
                        "^Jit thread pool$",
                        ".*(R|r)ender.*",
                        ".*Chrome.*"
                    )
                )
                .setNativeDumpAllThreadsCountMax(10)
                .setNativeCallback(crashCallBack)
                .setAnrRethrow(true)
                .setAnrLogCountMax(10)
                .setAnrCallback(crashCallBack)
                .setPlaceholderCountMax(3)
                .setPlaceholderSizeKb(512)
                .setLogFileMaintainDelayMs(1000)
        )
    }
}