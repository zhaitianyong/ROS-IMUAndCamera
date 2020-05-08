package com.atway.cc.imuandcamera.ros;

import android.util.Log;

import org.ros.concurrent.CancellableLoop;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeListener;
import org.ros.node.topic.Publisher;

import std_msgs.String;
/**
 * Node Talker
 * Topic chatter
 * Subscriber android/talker
 */
public class Talker extends AbstractNodeMain {
    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("android/talker");
    }


    @Override
    public void onStart(ConnectedNode connectedNode) {
        final Publisher<String> publisher=connectedNode.newPublisher("chatter", String._TYPE);

        connectedNode.executeCancellableLoop(new CancellableLoop() {
            private int sequenceNumber;

            @Override
            protected void setup() {
                sequenceNumber=0;
            }

            @Override
            protected void loop() throws InterruptedException {
                Log.d("talker" , "Android Hello world! "+sequenceNumber);
                std_msgs.String str=publisher.newMessage();
                str.setData("Android Hello world! "+sequenceNumber);
                publisher.publish(str);
                sequenceNumber++;
                Thread.sleep(1000);
            }

        });
    }
}
