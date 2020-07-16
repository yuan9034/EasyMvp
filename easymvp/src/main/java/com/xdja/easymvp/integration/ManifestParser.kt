package com.xdja.easymvp.integration

import android.content.Context
import android.content.pm.PackageManager
import java.util.*

/**
 * @author yuanwanli
 * @des   用于解析AndroidManifest中的Meta属性
 * 配合[ConfigModule]使用
 * @date 2020/7/3
 */
class ManifestParser(context: Context) {
    private val context: Context = context
    fun parse(): List<ConfigModule> {
        val modules: MutableList<ConfigModule> =
            ArrayList()
        try {
            val appInfo = context.packageManager.getApplicationInfo(
                context.packageName, PackageManager.GET_META_DATA
            )
            if (appInfo.metaData != null) {
                for (key in appInfo.metaData.keySet()) {
                    if (MODULE_VALUE == appInfo.metaData[key]) {
                        modules.add(parseModule(key))
                    }
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            throw RuntimeException("Unable to find metadata to parse ConfigModule", e)
        }
        return modules
    }

    companion object {
        private const val MODULE_VALUE = "ConfigModule"
        private fun parseModule(className: String): ConfigModule {
            val clazz: Class<*>
            clazz = try {
                Class.forName(className)
            } catch (e: ClassNotFoundException) {
                throw IllegalArgumentException(
                    "Unable to find ConfigModule implementation",
                    e
                )
            }
            val module: Any
            module = try {
                clazz.newInstance()
            } catch (e: InstantiationException) {
                throw RuntimeException(
                    "Unable to instantiate ConfigModule implementation for $clazz",
                    e
                )
            } catch (e: IllegalAccessException) {
                throw RuntimeException(
                    "Unable to instantiate ConfigModule implementation for $clazz",
                    e
                )
            }
            if (module !is ConfigModule) {
                throw RuntimeException("Expected instanceof ConfigModule, but found: $module")
            }
            return module
        }
    }
}