package model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Record implements Serializable {

    private long id;
    private double latitude;
    private double longitude;
    private String departamento;
    private String departamentoCalculado;
    private String departamentocheck;
    private String municipio;
    private String municipioCalculado;
    private String municipiocheck;

    public Record( long id, double latitude, double longitude,String departamento, String municipio) {
            super();
            this.id = id;
            this.latitude = latitude;
            this.longitude = longitude;
            this.departamento = departamento.toLowerCase();
            this.departamentoCalculado = "/N";
            this.departamentocheck = "false";
            this.municipio = municipio.toLowerCase();
            this.municipioCalculado = "/N";
            this.municipiocheck = "false";
            
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
    
   
	public String getDepartamento() {
		return departamento;
	}


	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}


	public String getDepartamentoCalculado() {
		return departamentoCalculado;
	}


	public void setDepartamentoCalculado(String departamentoCalculado) {
		this.departamentoCalculado = departamentoCalculado;
	}


	public String getDepartamentocheck() {
		return departamentocheck;
	}


	public void setDepartamentocheck(String departamentocheck) {
		this.departamentocheck = departamentocheck;
	}


	public String getMunicipio() {
		return municipio;
	}


	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}


	public String getMunicipioCalculado() {
		return municipioCalculado;
	}


	public void setMunicipioCalculado(String municipioCalculado) {
		this.municipioCalculado = municipioCalculado;
	}


	public String getMunicipiocheck() {
		return municipiocheck;
	}


	public void setMunicipiocheck(String municipiocheck) {
		this.municipiocheck = municipiocheck;
	}


	public String toString() {
            return id + " " + latitude + "	" + longitude + "	"+ departamento + "	" + departamentoCalculado + "	"+ departamentocheck + "	"+ municipio + "	" + municipioCalculado + "	"+ municipiocheck ;
    }
}

