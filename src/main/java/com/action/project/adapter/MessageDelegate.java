package com.action.project.adapter;

import com.action.project.entity.Order;
import com.action.project.entity.Packaged;

import java.io.File;
import java.util.Map;

/**
 * @author: action
 * @create: 2025/3/31 16:16
 **/
public class MessageDelegate {

    public void handleMessage(byte[] messageBody) {
        System.out.println("默认方法，消息内容: " + new String(messageBody));
    }

    public void consumeMessage(byte[] messageBody) {
        System.out.println("字节数组方法，消息内容: : " + new String(messageBody));
    }

    public void consumeMessage(String messageBody) {
        System.out.println("字符串方法，消息内容: : " + messageBody);
    }

    public void method1(String messageBody){
        System.out.println("method1，收到消息内容: " + messageBody);
    }

    public void method2(String messageBody){
        System.out.println("method2，收到消息内容: " + messageBody);
    }

    public void consumeMessage(Map messageBody) {
        System.out.println("map方法，消息内容: : " + messageBody);
    }

    public void consumeMessage(Order order) {
        System.out.println("order对象，消息内容,id:" + order.getId());
    }

    public void consumeMessage(Packaged pack) {
        System.out.println("Package对象，消息内容,id : " + pack.getId());
    }

    public void consumeMessage(File file) {
        System.out.println("文本对象方法，消息内容: : " + file.getName());
    }

}
