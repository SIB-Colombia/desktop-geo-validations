package client.manage;

import java.io.File;
import java.text.Normalizer;
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
        private Map<String, String> areas;

        public ShapeFile getShapefile() {
                return shapefile;
        }

        public void setShapefile(ShapeFile shapefile) {
                this.shapefile = shapefile;
        }

        /**
         * @param departments
         */
        public void setAreas(Map<String, String> areas) {
                this.areas = areas;
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
        public void getAreasFromCoordinates(List<Record> records) {
            Set<String> areas = new LinkedHashSet<String>();
            for (Record rec : records) {
            	areas.clear();
            	areas.addAll(shapefile.polygonsForAPoint(rec.getLatitude(), rec.getLongitude()));
        		for (String area : areas) {
        			String areaCalculada = "";
        			if(area.toString().equals("Mar Caibe")){
        				areaCalculada = "Mar Caribe";
        			}else{
        				if(area.toString().equals("Cayo Serranilla, 15° 47' 50'' N, 79° 51' 20'' W")){
        					areaCalculada = "Cayo Serranilla";
        				}else{
        					if(area.toString().equals("Cayo Bajo Nuevo, 15° 51' 0'' N, 78° 38' 0'' W")){
            					areaCalculada = "Cayo Bajo Nuevo";
        					}else{
        						areaCalculada = area.toString().toLowerCase();
	        				}
	        			}
        			}
        			String areaNormalizada = Normalizer.normalize(areaCalculada, Normalizer.Form.NFD);
        			areaNormalizada = areaNormalizada.replaceAll("[^\\p{ASCII}]", "").replace(" ","").toLowerCase();
        			
        			String areaNormalizadaRec = Normalizer.normalize(rec.getArea(), Normalizer.Form.NFD);
        			areaNormalizadaRec = areaNormalizadaRec.replaceAll("[^\\p{ASCII}]", "").replace(" ","").toLowerCase();
        			
    				if(areaNormalizada.matches("(.*)"+areaNormalizadaRec+"(.*)")){
        				rec.setAreacheck("true");
        			}
        			rec.setAreaCalculado(areaCalculada);
                }
            }
            
            return;
        }
}

