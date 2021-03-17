package me.sharminnipu.notificationwithreadpdf

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder:Notification.Builder
    private  val channelId="me.sharminnipu.notificationwithreadpdf"
    private  val description="Prescription Download completed"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        downloadPrescription.setOnClickListener {
            prescriptionNotification()
        }

    }


    private fun prescriptionNotification(){
        val random = Random()
        val m = random.nextInt(9999 - 1000) + 1000
       // val mFileName= SimpleDateFormat("yyyyMMdd-HHmmss", Locale.getDefault()).format(System.currentTimeMillis())
        val mFilePath= Environment.getExternalStorageDirectory().toString()+"/"+"sample"+".pdf"
        val file = File(mFilePath)

        // Log.d(AppConfig.APP_TAG, "File to download = $file")
        val mime = MimeTypeMap.getSingleton()
        val ext = file.name.substring(file.name.indexOf(".") + 1)
        val type = mime.getMimeTypeFromExtension(ext)
        val openFile = Intent(Intent.ACTION_VIEW, Uri.fromFile(file))
        openFile.setDataAndType(Uri.fromFile(file), type)
        openFile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        val pendingIntent = PendingIntent.getActivity(this, 0, openFile, 0)

        // val intent=Intent(this,MainActivity::class.java)
        // val pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)

        //custom layout
        // val contentViews=RemoteViews(packageName,R.layout.activity_main)
        // contentViews.setTextViewText(R.id.downloadPrescription,"Hello world")


        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){

            notificationChannel= NotificationChannel(channelId,description,NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor=Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            builder=Notification.Builder(this,channelId)
                    .setContentTitle("Prescription")
                    .setContentText("Download completed")
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setOngoing(true)
                    .setAutoCancel(true)
                    // .setContent(contentVViews)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(this.resources,R.drawable.ic_launcher))
                    .setContentIntent(pendingIntent)

        }else{



            builder=Notification.Builder(this)
                    .setContentTitle("Prescription")
                    .setContentText("Download completed")
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setOngoing(true)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(this.resources,R.drawable.ic_launcher))
                    .setContentIntent(pendingIntent)

        }

         //single notification
        //  notificationManager.notify(1234,builder.build())
        //multiple notification
        notificationManager.notify(m,builder.build())


    }
}