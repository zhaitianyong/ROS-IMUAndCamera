# ROS-IMUAndCamera





编译Android项目，

完成后配置roscore 主机的IP地址，

如：http://localhost:11311





由于手机端默认发布的话题为

 IMU:  /android/sensor/imu
相机信息：/camera/camera_info
预览图像：/camera/image/compressed

预览的图像为压缩格式 Type: sensor_msgs/CompressedImage，一般处理的都是格式 Type: sensor_msgs/Imag；

可以启动launch 文件下的，android_republish_image_raw.launch；重新发布即可



```
roslaunch android_republish_image_raw.launch

```

然后就可以在rviz的image中订阅 /camera/image_raw 话题



imu 的话，需要安装imu_tools 工具