package com.example.app2;

import android.text.TextUtils;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Set;
import java.util.UUID;

public class Demo {



    public static void main(String[] args) {

        new DeviceSearcher() {
            @Override
            public void onSearchStart() {
//                startSearch(); // 主要用于在UI上展示正在搜索
            }

            @Override
            public void onSearchFinish(Set<VideoCreateBean> deviceSet) {
                Demo.deviceSet = deviceSet;
                for (VideoCreateBean deviceBean : deviceSet) {
                    String s = deviceBean.toString();
                    System.out.println(s);

                }
            }
        }.start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                ServerReceviedByUdp();
            }
        }).start();
    }
    private static MulticastSocket ms;
    private static DatagramSocket socket;
    private static Set<VideoCreateBean> deviceSet;
    /**
     * 收消息
     */
    public static void ServerReceviedByUdp() {
        try {
            if (socket == null) {
                socket = new DatagramSocket(10025);
            }
            while (true) {
                byte[] data = new byte[4 * 1024];
                DatagramPacket packet = new DatagramPacket(data, data.length);
                socket.receive(packet);
                String message = new String(packet.getData(), packet.getOffset(), packet.getLength());
                System.out.println("收到信息为：" + message);
                if (deviceSet != null) {
                    System.out.println("设备大小:"+deviceSet.size());
                    for (VideoCreateBean deviceBean : deviceSet) {
                        send(deviceBean.getHost());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void send(String ip) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DatagramPacket dataPacket = null;
                try {
                    if (ms == null) {
                        ms = new MulticastSocket();
                    }
                    ms.setTimeToLive(4);
                    //将本机的IP（这里可以写动态获取的IP）地址放到数据包里，其实server端接收到数据包后也能获取到发包方的IP的
                    byte[] data = ("\n收到主机发送的消息IP:"+ip).getBytes();
                    //224.0.0.1为广播地址
                    InetAddress address = InetAddress.getByName(ip);
                    //这个地方可以输出判断该地址是不是广播类型的地址
                    System.out.println(address.isMulticastAddress());
                    dataPacket = new DatagramPacket(data, data.length, address,
                            10025);
                    ms.send(dataPacket);
                    System.out.println("发送成功");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
