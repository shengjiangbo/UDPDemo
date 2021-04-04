package com.example.udpdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private TextView view;
    private DatagramSocket socket;
    private InetSocketAddress socketAddr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = findViewById(R.id.tv_text);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataWithUDPSocket("发送消息给主机", socketAddr.getAddress().getHostAddress());
            }
        });

        new DeviceWaitingSearch(this, 1, "123456") {
            @Override
            public void onDeviceSearched(InetSocketAddress socketAddr) {
                MainActivity.this.socketAddr = socketAddr;
                Log.e("", "已上线，搜索主机：" + socketAddr.getAddress().getHostAddress() + ":" + socketAddr.getPort());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view.setText("已上线，搜索主机：" + socketAddr.getAddress().getHostAddress() + ":" + socketAddr.getPort());
                        sendDataWithUDPSocket("发送消息给主机", socketAddr.getAddress().getHostAddress());
                    }
                });
            }
        }.start();

        ServerReceviedByUdp();
    }


    /**
     * 发消息
     *
     * @param str
     * @param ipAddr
     */
    public void sendDataWithUDPSocket(String str, String ipAddr) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (socket == null) {
                        socket = new DatagramSocket(10025);
                    }
                    InetAddress serverAddress = InetAddress.getByName(ipAddr);
                    byte[] data = str.getBytes();
                    DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, 10025);
                    socket.send(packet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    /**
     * 收消息
     */
    public void ServerReceviedByUdp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (socket == null) {
                        socket = new DatagramSocket(10025);
                    }
                    while (true) {
                        byte[] data = new byte[4 * 1024];
                        DatagramPacket packet = new DatagramPacket(data, data.length);
                        socket.receive(packet);
                        String message = new String(packet.getData(), packet.getOffset(), packet.getLength());
                        if (!TextUtils.isEmpty(message)) {
                            if (message.contains("&quot;")) {
                                message = message.replaceAll("&quot;", "\"");
                            }

                            Log.e("", "收到信息为：" + message);
                            String finalMessage = message;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    view.append("\n");
                                    view.append(finalMessage);
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}