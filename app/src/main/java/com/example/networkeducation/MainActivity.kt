package com.example.networkeducation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.ProtocolException
import java.net.SocketTimeoutException
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class MainActivity : AppCompatActivity() {

    val MY_TAG = "MyDebugTagSuperClass"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val urlString = "https://rickandmortyapi.com/api"





        Thread(object : Runnable {
            override fun run() {
                try {
                    // создали объект класса URL и передали в него ссылку
                    // (аналог Retrofit.Builder().setBaseUrl().build())
                    val url = URL(urlString)
                    // открыли соединение
                    val connection = url.openConnection() as HttpsURLConnection
                    connection.requestMethod = "GET"
                    connection.readTimeout = 10_000
                    connection.connectTimeout = 30_000
                    // установили соединение соединение
                    connection.connect()

                    // открытая книга
                    val stream: InputStream = connection.inputStream
                    // это маленький буффер для хранения читаемой строки
                    val reader = BufferedReader(InputStreamReader(stream))
                    val buf = StringBuilder()
                    var line: String?
                    // readLine отдаёт нам считываемую строчку
                    while (reader.readLine().also { line = it } != null) {
                        buf.append(line).append("\n")
                    }
                    Log.d(MY_TAG, "$buf")
                    Log.d(MY_TAG, "${connection.headerFields}")
                } catch (e: MalformedURLException) {
                    Log.d(MY_TAG, "${e.message}")
                    return
                } catch (e: ProtocolException) {
                    Log.d(MY_TAG, "${e.message}")
                    return
                } catch (e: SecurityException) {
                    Log.d(MY_TAG, "${e.message}")
                    return
                } catch (e: SocketTimeoutException) {
                    Log.d(MY_TAG, "${e.message}")
                    return
                } catch (e: IOException) {
                    Log.d(MY_TAG, "${e.message}")
                    return
                } catch (e: RuntimeException) {
                    Log.d(MY_TAG, "${e.message}")
                    return
                } catch (t: Throwable) {
                    Log.d(MY_TAG, "${t.message}")
                    return
                }
            }
        }).start()
    }
}
