package client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class ClientConfig {

	private static ClientConfig instance;

	private ClientConfig() {
		init();
	}

	public static ClientConfig getInstance() {
		if (instance == null) {
			instance = new ClientConfig();
		}
		return instance;
	}

	private static final String XML_FILE = "client_config_areas.xml";
	private static final String XML_IP_ADDR = "ip_addr";
	private static final String XML_PATH = "path";
	private static final String XML_NAME = "name";
	private static final String XML_RECORDS = "records";
	private static final String XML_FID = "fid";
	private static final String XML_DESCRIP = "descrip";
	private static final String XML_COMMUNICATION_TYPE = "communication_type";
	public static final String LOCAL_COMMUNICATION = "LOCAL";
	public static final String HTTP_PROXY_SERVER = "server";
	public static final String HTTP_PROXY_PORT = "port";

	/**
	 * IP/Domain address of the Http-Proxy for connection to Biogeomancer
	 * service.
	 */
	public static String httpProxyServer;
	/**
	 * Http-Proxy port for connection to Biogeomancer service.
	 */
	public static String httpProxyPort;
	/**
	 * IP address of the server to connect
	 */
	public String server_ipaddr;
	/**
	 * full path of the shape file in .SHP extension
	 */
	public String shapeFile;
	/**
	 * Amount of records to work by round
	 */
	public int quantityRecords = 10000;
	/**
	 * Name of the shape file column of the ISO department value
	 */
	public String nameColumnFid;
	/**
	 * Name of the shape file column of the area name value
	 */
	public String nameColumnDescrip;
	/**
	 * The way how it connects to the server. RMI or TCP
	 */
	public String communication_type;

	/**
	 * Initialize the variables of the client configuration. It must be called
	 * at the beginning otherwise there will be a error if they are used
	 */
	private void init() {
		try {
			File file = new File(XML_FILE);
			/**
			 * if the configuration file does not exist yet, then create the
			 * default one
			 */
			if (!file.exists()) {
				createDeafaultXML();
			}
			readFromXML();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ClientConfig.getInstance();
	}

	@SuppressWarnings("unchecked")
	private void readFromXML() throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder(false);
		Document doc = builder.build(new File(XML_FILE));
		Element config = doc.getRootElement();

		Element server = config.getChild("server");
		server_ipaddr = server.getAttributeValue(XML_IP_ADDR);
		communication_type = server.getAttributeValue(XML_COMMUNICATION_TYPE);

		quantityRecords = Integer.parseInt(config.getChild("work")
				.getAttributeValue(XML_RECORDS));

		Element shape = config.getChild("shape");
		shapeFile = shape.getAttributeValue(XML_PATH);

		List<Element> columns = (List<Element>) shape.getChildren();
		for (Element column : columns) {
			String name = column.getAttributeValue(XML_NAME);
			if (name.equalsIgnoreCase(XML_FID)) {
				nameColumnFid = column.getText();
			} else {
				if (name.equalsIgnoreCase(XML_DESCRIP)) {
					nameColumnDescrip = column.getText();
				} else {

					// unknow column
				}
			}
		}

		Element proxy = config.getChild("Proxy");
		if (proxy != null) {
			Element httpProxy = proxy.getChild("HttpProxy");
			if (httpProxy != null) {
				httpProxyServer = httpProxy
						.getAttributeValue(HTTP_PROXY_SERVER);
				httpProxyPort = httpProxy.getAttributeValue(HTTP_PROXY_PORT);
			}
		}
	}

	private void createDeafaultXML() throws IOException {
		Element config = new Element("config");

		Element server = new Element("server");
		server.setAttribute(XML_IP_ADDR, "localhost");
		server.setAttribute(XML_COMMUNICATION_TYPE, LOCAL_COMMUNICATION);

		Element shape = new Element("shape");
		shape.setAttribute(XML_PATH,
				"./AreasMaritimas/Shapefile/cbiamarjsrgs_polygon.shp");

		Element columnFid = new Element("column");
		columnFid.setAttribute(XML_NAME, XML_FID);
		columnFid.setText("FID");

		Element columnDescrip = new Element("column");
		columnDescrip.setAttribute(XML_NAME, XML_DESCRIP);
		columnDescrip.setText("DESCRIP");

		shape.addContent(columnFid);
		shape.addContent(columnDescrip);

		Element work = new Element("work");
		work.setAttribute(XML_RECORDS, "10000");

		Element proxy = new Element("Proxy");
		Element httpProxy = new Element("HttpProxy");
		proxy.addContent(httpProxy);
		httpProxy.setAttribute(HTTP_PROXY_SERVER, "proxy4.ciat.cgiar.org");
		httpProxy.setAttribute(HTTP_PROXY_PORT, "8080");

		config.addContent(server);
		config.addContent(proxy);
		config.addContent(work);
		config.addContent(shape);

		Document doc = new Document(config);

		XMLOutputter outp = new XMLOutputter();
		outp.setFormat(Format.getPrettyFormat());
		FileOutputStream file = new FileOutputStream(XML_FILE);
		outp.output(doc, file);

		file.flush();
		file.close();

	}

}