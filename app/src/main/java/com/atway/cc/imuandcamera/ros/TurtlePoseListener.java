package com.atway.cc.imuandcamera.ros;

import android.util.Log;

import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;

import geometry_msgs.Twist;

public class TurtlePoseListener extends AbstractNodeMain {

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("android/turtle/cmd_vel");
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {
        //final Log log=connectedNode.getLog();
        Subscriber<Twist> subscriber=connectedNode.newSubscriber("turtle1/cmd_vel", Twist._TYPE);
        subscriber.addMessageListener(new MessageListener<Twist>() {
            @Override
            public void onNewMessage(Twist twist) {
               Log.d("Twist", twist.toString());
            }
        });
    }
}
