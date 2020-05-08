package com.atway.cc.imuandcamera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.atway.cc.imuandcamera.ros.IMUPublisher;
import com.google.common.base.Preconditions;

import org.ros.android.RosActivity;
import org.ros.android.view.camera.RosCameraPreviewView;
import org.ros.exception.RosRuntimeException;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

import java.io.IOException;
import java.net.URI;
import java.util.List;

public class CameraActivity extends RosActivity {


    private int cameraId;
    private RosCameraPreviewView rosCameraPreviewView;
    String masterUrl;
    private ImageButton imageButtonReverse;
    public CameraActivity() {
        super("CameraTutorial", "CameraTutorial");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        rosCameraPreviewView = (RosCameraPreviewView) findViewById(R.id.ros_camera_preview_view);
        imageButtonReverse = findViewById(R.id.imgBtnReverse);

        imageButtonReverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numberOfCameras = Camera.getNumberOfCameras();
                final Toast toast;
                if (numberOfCameras > 1) {
                    cameraId = (cameraId + 1) % numberOfCameras;
                    rosCameraPreviewView.releaseCamera();
                    rosCameraPreviewView.setCamera(getCamera());
                    toast = Toast.makeText(getApplicationContext(), "Switching cameras.", Toast.LENGTH_SHORT);
                } else {
                    toast = Toast.makeText(getApplicationContext(), "No alternative cameras to switch to.", Toast.LENGTH_SHORT);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toast.show();
                    }
                });
            }
        });
        Bundle bundle = this.getIntent().getExtras();
        masterUrl = bundle.getString("masterUrl");
        Log.d("masterUrl", masterUrl);

    }


    @Override
    public void startMasterChooser() {
        try {
            Intent data = new Intent();
            data.putExtra("ROS_MASTER_URI", masterUrl);
            super.onActivityResult(0, RESULT_OK, data);
        }catch (RosRuntimeException e) {
            // Socket problem
            Log.d("Camera Tutorial", "RosRuntimeException 连接错误");
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    @Override
    protected void init(NodeMainExecutor nodeMainExecutor) {

        try {
            cameraId = 0;
            rosCameraPreviewView.setCamera(getCamera());
            java.net.Socket socket = new java.net.Socket(getMasterUri().getHost(), getMasterUri().getPort());
            java.net.InetAddress local_network_address = socket.getLocalAddress();
            socket.close();
            SensorManager sensorManager =(SensorManager) getSystemService(Context.SENSOR_SERVICE);

            IMUPublisher imuPublisher = new IMUPublisher(sensorManager);
            NodeConfiguration nodeConfiguration =
                    NodeConfiguration.newPublic(local_network_address.getHostAddress(), getMasterUri());
            nodeMainExecutor.execute(rosCameraPreviewView, nodeConfiguration);
            nodeMainExecutor.execute(imuPublisher, nodeConfiguration);
        } catch (IOException e) {
            // Socket problem
            Log.e("Camera Tutorial", "socket error trying to get networking information from the master uri");
        } catch (RosRuntimeException e){
            Log.d("Camera Tutorial", "RosRuntimeException socket error trying to get networking information from the master uri");

        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        nodeMainExecutorService.forceShutdown();
    }

    private Camera getCamera() {
        Camera cam = Camera.open(cameraId);
        Camera.Parameters camParams = cam.getParameters();
        List<Camera.Size> sizes = camParams.getSupportedPreviewSizes();
        //StringBuilder sb = new StringBuilder();
        for (Camera.Size item : sizes){
            Log.d("camera:", String.format("%d x %d", item.width, item.height));
            //sb.append(String.format("%d x %d; ", item.width, item.height));
        }

        //camParams.setPreviewSize(1280, 720);
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
        //            if (camParams.getSupportedFocusModes().contains(
        //                    Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
        //                camParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        //            } else {
        //       camParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        //           // }
        //}
        //cam.setParameters(camParams);
        return cam;
    }
}
