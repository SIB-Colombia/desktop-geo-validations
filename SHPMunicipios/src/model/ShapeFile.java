package model;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import client.ClientConfig;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.index.LockTimeoutException;
import org.geotools.index.TreeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;



import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.index.strtree.STRtree;

public class ShapeFile {

        /**
         * Tag for a record whose country has another different error
         */
        public final static String E_OTHER = "C";
        private STRtree tree;
        private String typeName = "";
        private FeatureCollection<SimpleFeatureType, SimpleFeature> collection;
        private GeometryFactory gf = new GeometryFactory();
        private DataStore dataStore;
        private FeatureSource<SimpleFeatureType, SimpleFeature> featureSource;
        private Map<String, Serializable> connectParameters;

        /**
         * 
         * @param shapefile
         *            the file with .SHP format of the Shape File
         */
        public ShapeFile(File shapefile) {
                if (init(shapefile)) {
                        loadPolygonsInSRT(collection.features());
                }
        }


        private boolean init(File file) {
                names = new LinkedList<String[]>();
                connectParameters = new HashMap<String, Serializable>();
                try {
                        connectParameters.put("url", file.toURI().toURL());
                        connectParameters.put("create spatial index", true);
                        dataStore = DataStoreFinder.getDataStore(connectParameters);
                        String[] typeNames = dataStore.getTypeNames();
                        typeName = typeNames[0];
                        featureSource = dataStore.getFeatureSource(typeName);
                        collection = featureSource.getFeatures();
                        return true;
                } catch (MalformedURLException e) {
                        e.printStackTrace();
                } catch (IOException e) {
                        e.printStackTrace();
                }
                return false;
        }

        /**
         * @uml.property name="candidates"
         * @uml.associationEnd multiplicity="(0 -1)" ordering="true"
         *                     inverse="base:model.NamedGeometry"
         */
        private List<NamedGeometry> candidates;
        private List<String[]> names;

        @SuppressWarnings("unchecked")
        /**
         * Get the polygons names which to the coordinates belong
         * @param latitude of the point
         * @param longitude of the point
         * @return a list of the names of the polygons
         */
        public List<String[]> polygonsForAPoint(double latitude, double longitude) {
        		//Vector<Double> coordenadas = Converter.geographicCoordinatesToUTM(latitude, longitude);
       			 Point pointPol = gf.createPoint(new Coordinate(longitude,latitude));
                 candidates = tree.query(pointPol.getEnvelopeInternal());
                 names.clear();
                 if (candidates.size() > 0) {
                         try {
                                 for (NamedGeometry candidate : candidates) {
                                 	if (candidate.getGeomtery().distance(pointPol) == 0.0) {
                                 		String[] name = {candidate.getName().toString(), candidate.getName2().toString()};
                                        names.add(name);
                                 	}
                                 }
                                 pointPol = null;
                         } catch (Exception e) {
                                 System.err.println("Error calculating polygons for point Lat:"
                                                 + latitude + " Lng:" + longitude);
                                 System.err.println("candidates:" + candidates);
                                 e.printStackTrace();
                         }
                 }
               
                return names;
        }

         /**
         * @param shapeFile
         *            To use
         * @param featureAttributeForGeometryName
         *            The attribute name to use for the key
         * @param limit
         *            of items in the cache
         * @param start
         *            the starting geometry (combine with limit for paging)
         * @throws TreeException
         * @throws IOException
         * @throws IOException
         * @throws LockTimeoutException
         * 
         */
        private void loadPolygonsInSRT(FeatureIterator<SimpleFeature> iterator) {

                if (tree == null) {
                        tree = new STRtree();
                        // loop over each polygon, get the cells it covers and do the
                        // intersection
                        iterator = collection.features();
                        try {

                                Geometry geometryLoad;
                                String geometryLoadNameMunicipio;
                                String geometryLoadNameDepartamento;
                                while (iterator.hasNext()) {

                                        SimpleFeature feature = iterator.next();
                                        geometryLoad = (Geometry) feature.getDefaultGeometry();
                                        
                                        geometryLoadNameDepartamento = feature
                                                .getAttribute(ClientConfig.getInstance().nameColumnDepartamen)
                                                + "";
                                        
                                        geometryLoadNameMunicipio = feature
                                                .getAttribute(ClientConfig.getInstance().nameColumnNombreEnt)
                                                + "";
                                       
    
                                        tree.insert(geometryLoad.getEnvelopeInternal(),
                                                        new NamedGeometry(geometryLoad, geometryLoadNameDepartamento, geometryLoadNameMunicipio));
                                }
                        } finally {
                                iterator.close();
                        }
                        tree.build();

                } else {
                        return;
                }

        }

        /**
         * Get the collection limits of the features in the shape file
         * 
         * @return
         */
        public ReferencedEnvelope collectionLimits() {
                return collection.getBounds();
        }
}
