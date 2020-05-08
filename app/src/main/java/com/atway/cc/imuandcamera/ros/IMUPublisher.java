package com.atway.cc.imuandcamera.ros;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.util.Log;

import androidx.core.util.TimeUtils;

import org.ros.concurrent.CancellableLoop;
import org.ros.internal.message.RawMessage;
import org.ros.message.Time;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;

import geometry_msgs.Quaternion;
import geometry_msgs.Vector3;
import sensor_msgs.Imu;
import std_msgs.Header;

public class IMUPublisher extends AbstractNodeMain {

    private SensorManager sensorManager;

    public IMUPublisher(SensorManager sensorManager){
        this.sensorManager = sensorManager;
    }


    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("android/imuPublisher");
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {
        super.onStart(connectedNode);
        final Publisher<Imu> publisher = connectedNode.newPublisher("android/sensor/imu", Imu._TYPE);



        connectedNode.executeCancellableLoop(new CancellableLoop() {
            SensorEventListener sensorEventListener;
            Imu imu;
            long accelTime, gyroTime, qutaTime;
            @Override
            protected void setup() {
                imu = publisher.newMessage();
                // 加速度计
                Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                // 陀螺仪
                Sensor gyroScope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
                // 获得旋转向量
                Sensor rotationVector = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
                sensorEventListener = new SensorEventListener() {
                    @Override
                    public void onSensorChanged(SensorEvent event) {
                        if(event.sensor.getType() == 1){
                            imu.getLinearAcceleration().setX(event.values[0]);
                            imu.getLinearAcceleration().setY(event.values[1]);
                            imu.getLinearAcceleration().setZ(event.values[2]);
                            imu.setLinearAccelerationCovariance(new double[]{0.01D, 0.0D, 0.0D, 0.0D, 0.1D, 0.0D, 0.0D, 0.0D, 0.1D});
                            accelTime = event.timestamp;
                        }else  if(event.sensor.getType() == 4){
                            imu.getAngularVelocity().setX(event.values[0]);
                            imu.getAngularVelocity().setY(event.values[1]);
                            imu.getAngularVelocity().setZ(event.values[2]);
                            imu.setAngularVelocityCovariance(new double[]{0.0025D, 0.0D, 0.0D, 0.0D, 0.0025D, 0.0D, 0.0D, 0.0D, 0.0025D});

                            gyroTime = event.timestamp;

                        }else if(event.sensor.getType() == 11){
                            float[] arrayOfFloat = new float[4];
                            // 把旋转向量转换成为四元数
                            SensorManager.getQuaternionFromVector(arrayOfFloat, event.values);
                            imu.getOrientation().setW(arrayOfFloat[0]);
                            imu.getOrientation().setX(arrayOfFloat[1]);
                            imu.getOrientation().setY(arrayOfFloat[2]);
                            imu.getOrientation().setZ(arrayOfFloat[3]);
                            imu.setOrientationCovariance(new double[]{0.001D, 0.0D, 0.0D, 0.0D, 0.001D, 0.0D, 0.0D, 0.0D, 0.001D});
                            qutaTime = event.timestamp;
                        }
                        if(accelTime!=0 && gyroTime!=0 && qutaTime!=0){
                            long l1 = System.currentTimeMillis();
                            long l2 = SystemClock.uptimeMillis();
                            imu.getHeader().setStamp(Time.fromMillis(event.timestamp/1000000 + l1 - l2));
                            imu.getHeader().setFrameId("/imu");
                            publisher.publish(imu);
                            imu = publisher.newMessage();
                            accelTime = 0;
                            gyroTime = 0;
                            qutaTime = 0;
                        }
                    }

                    @Override
                    public void onAccuracyChanged(Sensor sensor, int accuracy) {

                    }
                };
                sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_GAME);
                sensorManager.registerListener(sensorEventListener, gyroScope, SensorManager.SENSOR_DELAY_GAME);
                sensorManager.registerListener(sensorEventListener, rotationVector, SensorManager.SENSOR_DELAY_GAME);

            }

            @Override
            protected void loop() throws InterruptedException {

            }

            @Override
            public void cancel() {
                sensorManager.unregisterListener(sensorEventListener);
                super.cancel();
            }

        });


    }
}
