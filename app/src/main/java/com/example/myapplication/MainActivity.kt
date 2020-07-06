package com.example.myapplication

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedInputStream
import java.lang.Exception
import java.net.Socket


class MainActivity : AppCompatActivity() {

    var input =""
    var handler : DisplayHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val port = 9000
        val ip = "34.64.100.91"
        handler = DisplayHandler()


        val runnable : Runnable = object : Runnable{
            override  fun run(){


                Log.d("thread-1","thread is made")

                val socket : Socket = Socket(ip,port)
                Log.d("thread-1", "connect")


                val buff : ByteArray = ByteArray(1024)

                try{
                    val inStream : BufferedInputStream = BufferedInputStream (socket.inputStream)

                    var read = 0
                    while(true){
                        if(socket == null){
                            break
                        }
                        read = inStream.read(buff,0,1024)

                        if(read<0){
                            break
                        }

                        val tempArr : ByteArray = ByteArray(12)
                        System.arraycopy(buff,0,tempArr,0,12)
                        input = String(tempArr)
                        Log.d("thread-1", input)
                        handler?.sendEmptyMessage(0)


                    }

                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }
        button.setOnClickListener {

            val thread: Thread = Thread(runnable)
            thread.start()
        }

    }

    inner class DisplayHandler : Handler(){
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            textview.text = input
        }
    }


}
