package com.example.networkeducation

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
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

    private val MY_TAG = "MyDebugTagSuperClass"

    private var button: Button? = null
    private var progressBar: ProgressBar? = null
    private val urlString = "https://rickandmortyapi.com/api"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button = findViewById(R.id.button)
        progressBar = findViewById(R.id.progressBar)
        button?.setOnClickListener { requestRickAndMorty() }

    }

    private fun requestRickAndMorty() {
        button?.isVisible = false
        progressBar?.isVisible = true
        val requestThread = Thread(object : Runnable {
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

                runOnUiThread {
                    button?.isVisible = true
                    progressBar?.isVisible = false
                }
            }
        })

        requestThread.name = "RickAndMortyThread"
        requestThread.start()
    }
}



// Примитивный пример потока. из особенностей. Класс MyLooper нельзя создать из вне.
// Именно поток его создает
class MyThread constructor(private val runnable: Runnable) {
    private val looper: MyLooper = MyLooper()

    fun start() {
        post(runnable)
        looper.start()
    }

    fun post(runnable: Runnable) {
        looper.post(runnable)
    }

    // класс с бесконечным циклом, опрашивающий очередь задач
    private class MyLooper {


        private val messageQueue = mutableListOf<Runnable>()
        fun start() {
            while (true) {
                if (messageQueue.isNotEmpty()) {
                    messageQueue.first().run()
                    messageQueue.removeAt(0)
                }
            }
        }

        fun post(runnable: Runnable) {
            messageQueue.add(runnable)
        }

        fun postDelay(runnable: Runnable, delay: Long) {
            messageQueue.add(runnable)
        }
    }
}









