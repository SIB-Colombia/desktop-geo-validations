package model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Record implements Serializable {

    private long id;
    private double latitude;
    private double longitude;
    private String area;
    private String areaCalculado;
    private String areacheck;

    public Record( long id, double latitude, double longitude,String area) {
            super();
            this.id = id;
            this.latitude = latitude;
            this.longitude = longitude;
            this.area = area;
            this.areaCalculado = "/N";
            this.areacheck = "false";
            
    }

    
    public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public double getLatitude() {
            return latitude;
    }

    public double getLongitude() {
            return longitude;
    }
	

    public void setLatitude(double latitude) {
            this.latitude = latitude;
    }
	
    public void setLongitude(double longitude) {
            this.longitude = longitude;
    }
    
    
   	public String getArea() {
		return area;
	}


	public void setArea(String area) {
		this.area = area;
	}


	public String getAreaCalculado() {
		return areaCalculado;
	}


	public void setAreaCalculado(String areaCalculado) {
		this.areaCalculado = areaCalculado;
	}


	public String getAreacheck() {
		return areacheck;
	}


	public void setAreacheck(String areacheck) {
		this.areacheck = areacheck;
	}


	public String toString() {
            return id + " " + latitude + "	" + longitude + "	"+ area + "	" + areaCalculado + "	"+ areacheck ;
    }
}

