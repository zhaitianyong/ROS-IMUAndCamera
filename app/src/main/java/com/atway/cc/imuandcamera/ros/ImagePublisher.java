package com.atway.cc.imuandcamera.ros;

import android.util.Log;

import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;

import sensor_msgs.CompressedImage;
import sensor_msgs.Image;
import std_msgs.String;

public class ImagePublisher extends AbstractNodeMain {

    public Publisher<Image> publisher;

    public ConnectedNode connectedNode;
    @Override
    public GraphName getDefaultNodeName() {
        Log.d("ImagePublisher", "GraphName.of(\"android/image\")");
        return GraphName.of("android/image");
    }


    @Override
    public void onStart(ConnectedNode connectedNode) {
        Log.d("ImagePublisher", "onStart(ConnectedNode connectedNode) ");
        this.connectedNode = connectedNode;
        publisher=connectedNode.newPublisher("android/camera/image", Image._TYPE);
    }

}
