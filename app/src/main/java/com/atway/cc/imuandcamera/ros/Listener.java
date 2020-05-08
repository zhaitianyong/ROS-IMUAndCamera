package com.atway.cc.imuandcamera.ros;

import android.util.Log;

import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;

import std_msgs.String;

/**
 * Node Lister
 * Topic chatter
 * Subscriber android/listener
 */
public class Listener extends AbstractNodeMain {
    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("android/listener");
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {
        //final Log log=connectedNode.getLog();
        Subscriber<String> subscriber=connectedNode.newSubscriber("chatter", String._TYPE);
        subscriber.addMessageListener(new MessageListener<String>() {
            @Override
            public void onNewMessage(std_msgs.String message) {
                Log.d("Listener","I heard: \"" + message.getData() + "\"");
            }
        });
    }

}
