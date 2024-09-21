package com.example.agoravoicecalling
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.agora.rtc2.ChannelMediaOptions
import io.agora.rtc2.Constants
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.RtcEngineConfig

class MainActivity : AppCompatActivity() {

    private lateinit var rtcEngine: RtcEngine




    //    // Fill in the App ID obtained from the Agora Console
    private val appId = "6a7a297a62c7476c9ab0363eec7cc427"
//
//    // Fill in the channel name
    private val channelName = "Sudhir"
//
//    // Fill in the temporary token generated from Agora Console
    private val token = "007eJxTYJh65lT8zpjf+VzXv+45Gbbi9WKHy56s0RzRNl3/7V2bar4oMJglmicaWZonmhklm5uYmyVbJiYZGJsZp6YmmycnmxiZmwS8TGsIZGTYFH2ZiZEBAkF8Nobg0pSMzCIGBgAyIiIk"
//

    private var isMuted = false  // Flag to track mute state

    private val rtcEventHandler = object : IRtcEngineEventHandler() {
        override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
            runOnUiThread {
                Toast.makeText(this@MainActivity, "Joined channel: $channel", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onUserJoined(uid: Int, elapsed: Int) {
            runOnUiThread {
                Toast.makeText(this@MainActivity, "User joined: $uid", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onUserOffline(uid: Int, reason: Int) {
            runOnUiThread {
                Toast.makeText(this@MainActivity, "User offline: $uid", Toast.LENGTH_SHORT).show()
            }
        }
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Check permissions and initialize Agora
        if (checkSelfPermission()) {
            initializeAgoraEngine()
        } else {
            requestPermissions()
        }

        // Handle voice on/off button click
        val toggleVoiceButton: Button = findViewById(R.id.btn_toggle_voice)
        toggleVoiceButton.setOnClickListener {
            toggleVoice(toggleVoiceButton)
        }
    }


    private fun initializeAgoraEngine() {
        try {
            val config = RtcEngineConfig()
            config.mContext = this
            config.mAppId = appId
            config.mEventHandler = rtcEventHandler
            rtcEngine = RtcEngine.create(config)

            // Enable local audio
            rtcEngine.enableAudio() // Ensure audio is enabled

            joinChannel()  // Join the channel after initializing
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }




    private fun joinChannel() {
        val options = ChannelMediaOptions()
        options.channelProfile = Constants.CHANNEL_PROFILE_COMMUNICATION
        options.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER
        options.publishMicrophoneTrack = true  // Enable microphone publishing
        options.autoSubscribeAudio = true      // Automatically subscribe to audio streams

        rtcEngine.joinChannel(token, channelName, 0, options)
    }


    // Toggle the microphone (mute/unmute)
    private fun toggleVoice(button: Button) {
        isMuted = !isMuted
        rtcEngine.muteLocalAudioStream(isMuted)
        if (isMuted) {
            button.text = "Turn Voice On"
            Toast.makeText(this, "Microphone muted", Toast.LENGTH_SHORT).show()
        } else {
            button.text = "Turn Voice Off"
            Toast.makeText(this, "Microphone unmuted", Toast.LENGTH_SHORT).show()
        }
    }

    // Request microphone permission
    private fun checkSelfPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), PERMISSION_REQ_ID)
    }

    // Handle permissions request result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQ_ID && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initializeAgoraEngine()
        } else {
            Toast.makeText(this, "Permissions required", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        rtcEngine.leaveChannel()
        RtcEngine.destroy()
    }

    companion object {
        private const val PERMISSION_REQ_ID = 22
    }
}











//package com.example.agoravoicecalling
//
//
//import android.Manifest
//import android.content.pm.PackageManager
//import android.os.Build
//import android.os.Bundle
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import io.agora.rtc2.ChannelMediaOptions
//import io.agora.rtc2.Constants
//import io.agora.rtc2.IRtcEngineEventHandler
//import io.agora.rtc2.RtcEngine
//import io.agora.rtc2.RtcEngineConfig
//
//
//class MainActivity : AppCompatActivity() {
//    // Fill in the App ID obtained from the Agora Console
//    private val appId = "6a7a297a62c7476c9ab0363eec7cc427"
//
//    // Fill in the channel name
//    private val channelName = "Sudhir"
//
//    // Fill in the temporary token generated from Agora Console
//    private val token = "007eJxTYJh65lT8zpjf+VzXv+45Gbbi9WKHy56s0RzRNl3/7V2bar4oMJglmicaWZonmhklm5uYmyVbJiYZGJsZp6YmmycnmxiZmwS8TGsIZGTYFH2ZiZEBAkF8Nobg0pSMzCIGBgAyIiIk"
//
////    private var mRtcEngine: RtcEngine? =null
//    private lateinit var mRtcEngine: RtcEngine;
//
//    private val mRtcEventHandler: IRtcEngineEventHandler = object : IRtcEngineEventHandler() {
//        // Callback when successfully joining the channel
//        override fun onJoinChannelSuccess(channel: String, uid: Int, elapsed: Int) {
//            super.onJoinChannelSuccess(channel, uid, elapsed)
//            runOnUiThread {
//                Toast.makeText(
//                    this@MainActivity,
//                    "Join channel success",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//
//        // Callback when a remote user or host joins the current channel
//        override fun onUserJoined(uid: Int, elapsed: Int) {
//            runOnUiThread {
//                Toast.makeText(this@MainActivity, "User joined: " + uid, Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        // Callback when a remote user or host leaves the current channel
//        override fun onUserOffline(uid: Int, reason: Int) {
//            super.onUserOffline(uid, reason)
//            runOnUiThread {
//                Toast.makeText(
//                    this@MainActivity,
//                    "User offline: $uid",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//    }
//
//
//    /*
//    private fun initializeAndJoinChannel() {
//        try {
//            // Create an RtcEngineConfig instance and configure it
//            val config = RtcEngineConfig()
//            config.mContext = baseContext
//            config.mAppId = appId
//            config.mEventHandler = mRtcEventHandler
//            // Create and initialize an RtcEngine instance
//            mRtcEngine = RtcEngine.create(config)
//
//            mRtcEngine?.let {
//                // Enable the video module
//                it.enableVideo()
//
//                // Enable local preview
//                it.startPreview()
//
//                // Create a SurfaceView object and make it a child object of FrameLayout
//                val container = findViewById<FrameLayout>(R.id.local_video_view_container)
//                val surfaceView = SurfaceView(baseContext)
//                container.addView(surfaceView)
//                // Pass the SurfaceView object to the SDK and set the local view
//                it.setupLocalVideo(VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, 0))
//
//                // Create an instance of ChannelMediaOptions and configure it
//                val options = ChannelMediaOptions()
//                // Set the user role to BROADCASTER or AUDIENCE according to the scenario
//                options.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER
//                // In the video calling scenario, set the channel profile to CHANNEL_PROFILE_COMMUNICATION
//                options.channelProfile = Constants.CHANNEL_PROFILE_COMMUNICATION
//
//                // Join the channel using a temporary token and channel name, setting uid to 0 means the engine will randomly generate a username
//                // The onJoinChannelSuccess callback will be triggered upon success
//                it.joinChannel(token, channelName, 0, options)
//            } ?: run {
//                Log.e("MainActivity", "Failed to create RtcEngine instance")
//            }
//        } catch (e: Exception) {
//            Log.e("MainActivity", "Error initializing and joining channel", e)
//        }
//    }
//    */
//
//    private fun initializeAndJoinChannel() {
//        try {
//            // Create an RtcEngineConfig object and configure it
//            val config = RtcEngineConfig()
//            config.mContext = baseContext
//            config.mAppId = appId
//            config.mEventHandler = mRtcEventHandler
//            // Create and initialize the RtcEngine
//            mRtcEngine = RtcEngine.create(config)
//        } catch (e: Exception) {
//            throw RuntimeException("Check the error.")
//        }
//        // Create a ChannelMediaOptions object and configure it
//        val options = ChannelMediaOptions()
//        // Set the user role to BROADCASTER (host) or AUDIENCE (audience)
//        options.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER
//        // Set the channel profile
//        options.channelProfile = Constants.CHANNEL_PROFILE_COMMUNICATION
//        // Publish the audio collected by the microphone
//        options.publishMicrophoneTrack = true
//        // Automatically subscribe to all audio streams
//        options.autoSubscribeAudio = true
//        // Join the channel using a uid, temporary token and channel name.
//        // Ensure that the uid is unique within the channel.
//        // If you set the uid to 0, the engine  generates a random uid.
//        // The onJoinChannelSuccess callback is triggered upon success.
//        mRtcEngine.joinChannel(token, channelName, 0, options)
//    }
//
//
//
//
//    private val requiredPermissions: Array<String>
//        // Obtain recording, camera and other permissions required to implement real-time audio and video interaction
//        get() =// Determine the permissions required when targetSDKVersion is 31 or above
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//                arrayOf(
//                    Manifest.permission.RECORD_AUDIO,  // Recording permission
//                    Manifest.permission.CAMERA,  // Camera permission
//                    Manifest.permission.READ_PHONE_STATE,  // Permission to read phone status
//                    Manifest.permission.BLUETOOTH_CONNECT // Bluetooth connection permission
//                )
//            } else {
//                arrayOf(
//                    Manifest.permission.RECORD_AUDIO,
//                    Manifest.permission.CAMERA
//                )
//            }
//
//    private fun checkPermissions(): Boolean {
//        for (permission in requiredPermissions) {
//            val permissionCheck = ContextCompat.checkSelfPermission(this, permission)
//            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
//                return false
//            }
//        }
//        return true
//    }
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//
//        // If authorized, initialize RtcEngine and join the channel
//        if (checkPermissions()) {
//            initializeAndJoinChannel()
//        } else {
//            ActivityCompat.requestPermissions(
//                this,
//                requiredPermissions, PERMISSION_REQ_ID
//            )
//        }
//    }
//
//    // System permission request callback
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (checkPermissions()) {
//            initializeAndJoinChannel()
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        if (::mRtcEngine.isInitialized) {
//            // Stop local video preview
//            mRtcEngine.stopPreview()
//            // Leave the channel
//            mRtcEngine.leaveChannel()
//        }
//    }
//
//
//    companion object {
//        private const val PERMISSION_REQ_ID = 22
//    }
//}
//
