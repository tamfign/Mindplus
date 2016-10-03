package com.tamfign.configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.tamfign.main.ServerArguments;
import com.tamfign.model.RouterListController;
import com.tamfign.model.ServerListController;

public class Configuration {

	private static Configuration _instance = null;
	private ServerConfig itself = null;
	private String certPath = null;
	// TODO hardcode? configurable? cert pass
	public static final char certPass[] = "123456".toCharArray();
	// TODO hardcode? configurable? cert alia main pass
	public static final char certAliaMainPass[] = "123456".toCharArray();

	private Configuration(ServerArguments arguments) throws IOException {
		ConfigurationHandler configHandler = new ConfigurationHandler();
		SAXParser parser = null;

		try {
			parser = SAXParserFactory.newInstance().newSAXParser();
			InputStream is = new FileInputStream(arguments.getServerConfigPath());
			parser.parse(is, configHandler);
		} catch (ParserConfigurationException | SAXException e) {
			e.printStackTrace();
		}

		getItOwnConfig(arguments.getServerId());

		if (itself != null) {
			itself.setActived(true);
			itself.setItselft(true);
		} else {
			throw new IOException("No matched ServerId");
		}

		certPath = arguments.getCertPath();
	}

	private void getItOwnConfig(String serverId) {
		itself = RouterListController.getInstance().get(serverId);
		if (itself == null) {
			itself = ServerListController.getInstance().get(serverId);
		}
	}

	public static String getCertPath() {
		return _instance.certPath;
	}

	public static String getServerId() {
		return _instance.itself.getId();
	}

	public static int getClientPort() {
		return _instance.itself.getClientPort();
	}

	public static int getCoordinationPort() {
		return _instance.itself.getCoordinationPort();
	}

	public static Configuration init(ServerArguments arguments) throws IOException {
		if (_instance == null) {
			_instance = new Configuration(arguments);
		}
		return _instance;
	}
}
