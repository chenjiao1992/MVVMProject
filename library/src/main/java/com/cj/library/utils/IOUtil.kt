package com.cj.library.utils

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


object IOUtil {
    fun copy(inputStream: InputStream, outputStream: OutputStream): Boolean {
        var success = true
        try {
            val buffer = ByteArray(1024)
            var read = inputStream.read(buffer)
            while (read != -1) {
                outputStream.write(buffer, 0, read)
                outputStream.flush()
                read = inputStream.read(buffer)
            }
        } catch (e: IOException) {
            success = false
        } finally {
            try {
                inputStream.close()
            } catch (e: IOException) {
            }
            try {
                outputStream.close()
            } catch (e: IOException) {
            }
        }
        return success
    }
}