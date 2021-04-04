package com.example.udpdemo;

public class VideoCreateBean {

    private String roomId;
    private String host;
    private int number;
    private boolean loop;
    private int port;

    public int getPort() {
        return port;
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    private String barrage;

    public String getBarrage() {
        return barrage;
    }

    public void setBarrage(String barrage) {
        this.barrage = barrage;
    }


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    private int status;
    private int duration;
    private String videoId = "1";

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    private String path;

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    private long startTime;

    @Override
    public String toString() {
        return "VideoCreateBean{" +
                "roomId='" + roomId + '\'' +
                ", number=" + number +
                ", status=" + status +
                ", duration=" + duration +
                ", host=" + host +
                ", videoId=" + videoId +
                ", startTime=" + startTime +
                '}';
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
