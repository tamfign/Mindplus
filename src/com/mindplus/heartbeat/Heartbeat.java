package com.mindplus.heartbeat;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.SSLSocketFactory;

import com.mindplus.connection.HeartbeatController;

public class Heartbeat {
	private int interval;
	private Timer timer;
	private HeartbeatController controller;
	private TimerTask beatTask;
	private String host;
	private int port;

	public Heartbeat(String host, int port, int interval, HeartbeatController controller)
			throws UnsupportedEncodingException, IOException {
		this.interval = interval;
		this.timer = new Timer();
		this.controller = controller;
		this.beatTask = getTimerTask();
		this.host = host;
		this.port = port;
	}

	public void run() {
		timer.schedule(beatTask, 0, interval);
	}

	public void cancel() {
		this.timer.cancel();
	}

	private TimerTask getTimerTask() {
		return new TimerTask() {
			@Override
			public void run() {
				write(controller.getHeartBeatMsg());
			}
		};
	}

	private void write(String msg) {
		Socket socket = null;
		try {
			socket = getSocket(this.host, this.port);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
			writer.write(msg + "\n");
			writer.flush();
		} catch (IOException e) {
			System.out.println("Fail to beat.");
		} finally {
			if (socket != null)
				close(socket);
		}
	}

	private void close(Socket socket) {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Socket getSocket(String host, int port) throws UnknownHostException, IOException {
		SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
		return factory.createSocket(host, port);
	}
}
