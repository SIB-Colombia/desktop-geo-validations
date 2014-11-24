package client.manage;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import client.ClientConfig;
import model.Record;
import model.ShapeFile;

public class ShapeFileManager {

        /**
         * @uml.property name="shapefile"
         * @uml.associationEnd multiplicity="(1 1)" ordering="true"
         *                     inverse="base:model.ShapeFile"
         */
        private ShapeFile shapefile;
        private Map<String, String> municipios;

        public ShapeFile getShapefile() {
                return shapefile;
        }

        public void setShapefile(ShapeFile shapefile) {
                this.shapefile = shapefile;
        }

        /**
         * @param departments
         */
        public void setMunicipios(Map<String, String> municipios) {
                this.municipios = municipios;
        }

        public ShapeFileManager() {
        	shapefile = new ShapeFile(new File(ClientConfig.getInstance().shapeFile));
        }

        /**
         * Get the departments names according to the records coordinates
         * 
         * @param records
         *            to get the departments names according to the coordinates
         * @return names of departments for all possibles departments of all these
         *         records
         */
        public void getMunicipiosFromCoordinates(List<Record> records) {
            Set<String[]> municipios = new LinkedHashSet<String[]>();
            for (Record rec : records) {
            	municipios.clear();
            	municipios.addAll(shapefile.polygonsForAPoint(rec.getLatitude(), rec.getLongitude()));
        		for (String[] municipio : municipios) {
        			if(rec.getDepartamento().equalsIgnoreCase(municipio[0])){
        				rec.setDepartamentocheck("true");
        			}
        			if(rec.getMunicipio().equalsIgnoreCase(municipio[1])&&rec.getDepartamento().equalsIgnoreCase(municipio[0])){
        				rec.setMunicipiocheck("true");
        			}
        			rec.setMunicipioCalculado(municipio[1].toLowerCase());
        			rec.setDepartamentoCalculado(municipio[0].toLowerCase());
                }
            }
            
            return;
        }
}

