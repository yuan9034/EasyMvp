package com.xdja.easymvp.http.imageloader.glide

import android.graphics.Bitmap
import androidx.annotation.IntRange
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.xdja.easymvp.utils.FastBlur
import java.security.MessageDigest

/**
 * @author yuanwanli
 * @des   高斯模糊
 * @date 2020/7/7
 */
class BlurTransformation(@IntRange(from = 0) radius: Int) : BitmapTransformation() {
    private val ID = BlurTransformation::class.java.name
    private val ID_BYTES = ID.toByteArray(Key.CHARSET)
    private var mRadius = radius
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap? {
        return FastBlur.doBlur(toTransform, mRadius, true)
    }

    override fun equals(other: Any?): Boolean {
        return other is BlurTransformation
    }

    override fun hashCode(): Int {
        return ID.hashCode()
    }
}