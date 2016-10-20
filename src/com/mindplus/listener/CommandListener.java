package com.mindplus.listener;

import java.io.IOException;
import java.net.Socket;

import com.mindplus.connection.Connector;
import com.mindplus.connection.ConnectorInf;

public abstract class CommandListener implements Runnable {
	private Socket socket = null;
	private ConnectorInf connetor = null;

	public CommandListener(Connector connetor, Socket socket) {
		this.connetor = connetor;
		this.socket = socket;
	}

	@Override
	public void run() {
		String cmd;
		try {
			while (socket.isConnected()) {
				cmd = getConnector().readCmd(socket);
				if (cmd != null && !"".equals(cmd)) {
					handleRequest(cmd);
				}
			handleDisconnect();
			}
		} catch (IOException e) {
			handleDisconnect();
		} finally {
			getConnector().close(socket);
		}
	}

	protected abstract void handleDisconnect();

	protected abstract void handleRequest(String cmd);

	protected Socket getSocket() {
		return this.socket;
	}

	protected ConnectorInf getConnector() {
		return this.connetor;
	}
}
