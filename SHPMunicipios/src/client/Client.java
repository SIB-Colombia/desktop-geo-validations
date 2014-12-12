package client;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import client.manage.ShapeFileManager;
import model.Record;
import model.ShapeFile;

public class Client {

        /**
         * @uml.property name="factory"
         * @uml.associationEnd multiplicity="(1 1)" ordering="true"
         *                     inverse="base:iclientmanager.ClientFactory"
         */
        private Long initTime;

        /**
         * @uml.property name="records"
         * @uml.associationEnd multiplicity="(0 -1)" ordering="true"
         *                     inverse="base:model.Record"
         */
        private List<Record> records = new ArrayList<Record>();

        /**
         * @uml.property name="shapeFileManager"
         * @uml.associationEnd multiplicity="(1 1)" ordering="true"
         *                     inverse="base:client.correctormanager.ShapeFileManager"
         */
        private ShapeFileManager shapeFileManager;
        private ShapeFile shapeFile;
        private static Scanner reader;
        private static PrintWriter writer;

        private int worked;

        public static void main(String[] args) throws IOException {
        	
        	Calendar fecha = Calendar.getInstance();
        	File in = new File("Municipios/input/municipios.txt");
        	if(in.exists() && !in.isDirectory()){
		    	FileWriter out = new FileWriter("Municipios/output/municipiosValidacion_"+ fecha.get(Calendar.DAY_OF_MONTH) + "-" + (fecha.get(Calendar.MONTH)+1) +  "-" + fecha.get(Calendar.YEAR) +".txt");
		
		        reader = new Scanner(in);
		        writer = new PrintWriter(out);
		        Client client = new Client();
		        client.start();
		        reader.close();
        	}else{
        		System.out.println("No se encontro el archivo");
        	}
           
        }

        /**
         * Creates the client whit the properties set
         */
        public Client() {
	        shapeFileManager = new ShapeFileManager();
	        shapeFile = new ShapeFile(new File(ClientConfig.getInstance().shapeFile));
	        initTime = System.currentTimeMillis();
        }

        /**
         * starts the client
         */
        public void start() {

                try {
                        initTime = System.currentTimeMillis();
                        System.out.println("< Asking work"
                                        + new Date());
            	        while(reader.hasNext()) {
            	        	String line = reader.nextLine();
            	        	String[] splited = line.split("\t");
            	        	records.add(new Record(Long.parseLong(splited[0]),Double.parseDouble(splited[1].replace(",", ".")),Double.parseDouble(splited[2].replace(",", ".")),splited[4],splited[3]));
            	        }
                        work();
                        System.out.println("# Work finished " + worked + " in "
                                        + (System.currentTimeMillis() - initTime) + "ms "
                                        + new Date());

                }catch (Exception e) {
                        System.out.println("There was a problem asking for work.");
                        System.out.println(e.getMessage());
                }finally{
                        System.exit(0);
                }

        }

       /**
         * 
         * inserts the results
         * 
         * @param parts
         *            the number of parts in which the results will be send
         * @throws RemoteException
         */
        private void insertResults() throws RemoteException {

                System.out.println("< Sending worked records to server.");
                try {
                	 LinkedList<Record> goodRecords = new LinkedList<Record>();
         	        for (Record r : records) {
         	        	goodRecords.add(r);
         	        }
         	        
         	        if(goodRecords.size() > 0) {
                 		for(Record goodRecord: goodRecords){
                 			writer.println(goodRecord.toString());
                 		}
         	        }
         	        
         	        System.out.println("< Inserted " + records.size() + new Date());
         	        writer.close();
                    records = null;
                } catch (Exception e) {
                        System.out.println("ERROR:");
                        System.out.println(e.getMessage());
                        e.printStackTrace();
                        System.out.println();
                        System.out.println(".....Trying to send back the results....");
                        try {
                                Thread.sleep(20000);
                        } catch (InterruptedException e1) {
                                e1.printStackTrace();
                        }
                        insertResults();
                }
        }

        /**
         * evaluate the records after received
         */
        private void work() {
                long tiempoTotal = System.currentTimeMillis();
                try {
                        shapeFileManager.getMunicipiosFromCoordinates(records);
                        
                        System.out.println("Total time :"
                                        + (System.currentTimeMillis() - tiempoTotal)
                                        + "ms FreeMem:" + Runtime.getRuntime().freeMemory());
                        try {
                        	insertResults();
                        } catch (RemoteException e) {
                                System.out.println("Can't connect to the RMI server.");
                                System.out.println("ERROR: ");
                                System.out.println(e.getMessage());
                        }
                } catch (Exception e) {
                        System.out.println("There was a problem");
                        System.out.println("ERROR:");
                        System.out.println(e.getMessage());
                }

        }
}