package com.cj.library.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.cj.library.extension.closeSafe
import com.cj.library.extension.notNullNorEmpty
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.PrintWriter
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

object FileUtil {
    fun copyFile(source: File, target: File): Boolean {
        val create: Boolean
        var success = false
        if (source.exists() && source.isFile) {
            if (target.parentFile != null && !target.parentFile.exists()) {
                target.parentFile.mkdirs()
            }
            create = target.createNewFile()
            if (create || target.isFile && target.canWrite()) {
                success = IOUtil.copy(FileInputStream(source), FileOutputStream(target))
            }
        }
        if (!success) {
            Log.e("FileUtil","Copy file " + source.absolutePath + " to " +
                    target.absolutePath + " error!")
        }
        return success
    }

    fun copyFile(sourceFilename: String, targetFilename: String) = copyFile(File(sourceFilename), File(targetFilename))

    fun deleteFile(filename: String?) = filename.notNullNorEmpty() && deleteFile(File(filename))

    fun deleteFile(file: File) = file.exists() && file.isFile && file.delete()

    /**
     * 删除文件夹中的文件,文件夹不删除
     *
     * @param folderPath
     */
    fun deleteFiles(folderPath: String) {
        val file = File(folderPath)
        if (file.isDirectory) {
            val childFiles = file.listFiles()
            if (childFiles == null || childFiles.isEmpty()) {
                file.delete()
                return
            }
            for (i in childFiles.indices) {
                childFiles[i].delete()
            }
        }
    }

    /**
     * 递归删除folderPath文件夹中的文件及子文件夹, folderPath文件夹不删除
     *
     * @param folderPath
     */
    @Synchronized
    fun recursionDeleteFolder(folderPath: String) {
        val file = File(folderPath)
        if (file.isDirectory){
            val listFiles: Array<File>? = file.listFiles()
            if (listFiles == null || listFiles.isEmpty()) return
            for (childFile in listFiles){
                deleteFolder(childFile)
            }
        }
    }

    /**
     * 递归删除文件夹
     * @param file
     */
    fun deleteFolder(file: File?) {
        if (file == null) {
            return
        }
        if (file.isFile) {
            file.delete()
            return
        }
        if (file.isDirectory) {
            val childFile = file.listFiles()
            if (childFile == null || childFile.isEmpty()) {
                file.delete()
                return
            }
            for (f in childFile) {
                deleteFolder(f)
            }
            file.delete()
        }
    }

    fun getFilePath(context: Context, uri: Uri): String? {
        val scheme = uri.scheme
        var data: String? = null
        if (scheme == null) {
            data = uri.path
        } else if (ContentResolver.SCHEME_FILE == scheme) {
            data = uri.path
        } else if (ContentResolver.SCHEME_CONTENT == scheme) {
            val cursor = context.contentResolver
                .query(uri, arrayOf(MediaStore.Images.ImageColumns.DATA), null, null, null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                    if (index > -1) {
                        data = cursor.getString(index)
                    }
                }
                cursor.close()
            }
        }
        return data
    }

    fun readBitmap(filename: String, width: Int, height: Int): Bitmap {
        var bitmap = BitmapFactory.decodeFile(filename)
        if (width > 0 && height > 0 &&
                (width != bitmap.width || height != bitmap.height)) {
            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true)
        }
        return bitmap
    }

    fun readBitmap(file: File, width: Int, height: Int): Bitmap? {
        return if (file.exists() && file.isFile) {
            readBitmap(file.absolutePath, width, height)
        } else {
            null
        }
    }

    fun readDrawable(filename: String, width: Int, height: Int): Drawable? {
        val drawable = Drawable.createFromPath(filename)
        if (drawable != null && width > 0 && height > 0) {
            drawable.setBounds(0, 0, width, height)
        }
        return drawable
    }

    fun readDrawable(file: File, width: Int, height: Int) = if (file.exists() && file.isFile) {
        readDrawable(file.absolutePath, width, height)
    } else {
        null
    }

    fun readTxt(inputStream: InputStream) = try {
        val inputStreamReader = InputStreamReader(inputStream)
        val bufferedReader = BufferedReader(inputStreamReader)
        val content = StringBuilder()
        var line = bufferedReader.readLine()
        while (line != null) {
            content.append(line)
            line = bufferedReader.readLine()
        }
        bufferedReader.close()
        inputStreamReader.close()
        content.toString()
    } catch (e: Exception) {
        Log.e("FileUtil","Read input stream error!")
        null
    }

    fun readTxt(filename: String) = readTxt(File(filename))

    fun readTxt(file: File) = if (file.exists() && file.isFile) {
        try {
            val fileInputStream = FileInputStream(file)
            val inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            val content = StringBuilder()
            var line = bufferedReader.readLine()
            while (line != null) {
                content.append(line)
                line = bufferedReader.readLine()
            }
            bufferedReader.close()
            inputStreamReader.close()
            fileInputStream.close()
            content.toString()
        } catch (e: IOException) {
            Log.e("FileUtil","Read txt file " + file.absolutePath + " error!")
            null
        }
    } else {
        null
    }

    fun writeBitmap(filename: String, bitmap: Bitmap): Boolean = writeBitmap(File(filename), bitmap)

    fun writeBitmap(file: File, bitmap: Bitmap) = try {
        val create = file.createNewFile()
        if (create || file.canWrite()) {
            val fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, fileOutputStream)
            fileOutputStream.close()
            fileOutputStream.flush()
        }
        true
    } catch (e: IOException) {
        Log.e("FileUtil","Write bitmap " + file.absolutePath + " error!")
        false
    }

    fun writeDrawable(filename: String, drawable: Drawable) = drawable is BitmapDrawable && writeBitmap(File(filename), drawable.bitmap)

    fun writeDrawable(file: File, drawable: Drawable) = drawable is BitmapDrawable && writeBitmap(file, drawable.bitmap)

    fun writeTxt(filename: String, content: String) = writeTxt(File(filename), content)

    fun writeTxt(file: File, content: String) = try {
        val create = file.createNewFile()
        if (create || file.canWrite()) {
            val printWriter = PrintWriter(file)
            printWriter.write(content)
            printWriter.close()
            printWriter.flush()
        }
        true
    } catch (e: IOException) {
        Log.e("FileUtil","Write file " + file.absolutePath + " error!")
        false
    }

    /**
     * 检查是否网络路径
     */
    fun checkIfRemotePath(path: String?) = path != null && path.isNotEmpty() && (path.startsWith("http://") || path.startsWith("https://"))

    fun generateUniqueFileName(filePath: String): String {
        var file = File(filePath)
        if (!file.exists()) {
            return filePath
        }
        //Get suffix
        var suffix = ""
        var nameWithoutSuffix = filePath
        val dotIndex = filePath.lastIndexOf(".")
        if (dotIndex >= 0 && dotIndex < filePath.length) {
            suffix = filePath.substring(dotIndex)
            nameWithoutSuffix = filePath.substring(0, dotIndex)
        }
        var i = 0
        var path = filePath
        do {
            i++
            path = "$nameWithoutSuffix($i)$suffix"
            file = File(path)
        } while (file.exists())
        return path
    }

    fun generateUniqueFile(folder: File, name: String): File? {
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                return null
            }
        }
        var file = File(folder, name)
        return File(generateUniqueFileName(file.absolutePath))
    }

    fun checkExists(filePath: String?) = !filePath.isNullOrEmpty() && File(filePath).exists()

    fun makeDir(file: File, forceMake: Boolean): Boolean {
        if (file.exists()) {
            if (file.isFile) {
                if (forceMake) {
                    if (!file.delete()) return false
                    if (file.mkdir()) return true
                }
            } else {
                if (file.canWrite()) return true
            }
        } else {
            if (file.mkdir()) return true
        }
        return false
    }

    fun sizeToString(bytes: Long): String {
        val KB = 1024F
        val MB = KB * 1024F
        if (bytes < MB) {
            return String.format("%.1fKB", bytes / KB)
        }
        val GB = MB * 1024F
        if (bytes < GB) {
            return String.format("%.1fMB", bytes / MB)
        }
        return String.format("%.1fGB", bytes / GB)
    }

    fun getFileLength(path: String): Long {
        val file = File(path)
        if (file.exists()) {
            return file.length()
        }
        return 0L
    }

    /**
     * 获取指定文件夹内所有文件大小的和
     *
     * @param file file
     * @return size
     * @throws Exception
     */
    fun getFolderSize(file: File?): Long {
        file ?: return 0L
        var size: Long = 0
        try {
            val fileList = file.listFiles()
            for (aFileList in fileList!!) {
                size += if (aFileList.isDirectory) {
                    getFolderSize(aFileList)
                } else {
                    aFileList.length()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return size
    }

    fun getFolderFilePath(folderFile: File): List<String> {
        val list = mutableListOf<String>()
        try {
            val fileList = folderFile.listFiles()
            for (file in fileList) {
                if (file.isDirectory) {
                    list.addAll(getFolderFilePath(file))
                } else {
                    list.add(file.absolutePath)
                }
            }
        } catch (e: Exception) {

        }
        return list
    }

    fun getSmartDraweeUri(imagePath: String?): Uri?{
        return if (imagePath.isNullOrEmpty()) {
            null
        } else if (checkIfRemotePath(imagePath)) {
            Uri.parse(imagePath)
        } else {
            if (imagePath.startsWith("file://")) {
                Uri.parse(imagePath)
            } else {
                Uri.parse("file://$imagePath")
            }
        }
    }

    fun compressAndSave(str: String, path: String): String? {
        var output = FileOutputStream(path)
        var gzipOutput = GZIPOutputStream(output)
        try {
            gzipOutput.write(str.toByteArray())
            gzipOutput.flush()
            return path
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            gzipOutput.closeSafe()
            output.closeSafe()
        }
        return null
    }

    fun uncompress(path: String): String? {
        val out = ByteArrayOutputStream()
        val input = FileInputStream(path)
        try {
            val gzipInputStream = GZIPInputStream(input)
            val buffer = ByteArray(1024)
            var n = gzipInputStream.read(buffer)
            while (n >= 0) {
                out.write(buffer,0 ,n)
                n = gzipInputStream.read(buffer)
            }
            return out.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }finally {
            input.close()
            out.close()
        }
        return null
    }


}