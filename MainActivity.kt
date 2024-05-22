import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var cameraManager: CameraManager
    private var isFlashOn: Boolean = false
    private lateinit var toggleButton: Button

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        toggleButton = findViewById(R.id.toggleButton)

        // Check for camera permission
        checkCameraPermission()

        toggleButton.setOnClickListener {
            if (isFlashOn) {
                turnOffFlashlight()
                isFlashOn = false
                toggleButton.text = "Turn On"
            } else {
                turnOnFlashlight()
                isFlashOn = true
                toggleButton.text = "Turn Off"
            }
        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            // Permission already granted, proceed with using the flashlight
            // You can call turnOnFlashlight() here
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, proceed with using the flashlight
                    // You can call turnOnFlashlight() here
                } else {
                    // Permission denied, inform the user or handle it gracefully
                }
            }
        }
    }

    private fun turnOnFlashlight() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager.setTorchMode(cameraManager.cameraIdList[0], true)
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
            // Handle the exception (e.g., show an error message to the user)
        }
    }

    private fun turnOffFlashlight() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager.setTorchMode(cameraManager.cameraIdList[0], false)
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
            // Handle the exception (e.g., show an error message to the user)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFlashOn) {
            turnOffFlashlight()
        }
    }
}

