package model;

import java.math.BigDecimal;
import java.util.Vector;

public class Converter {
	 
	public static Vector<Double> geographicCoordinatesToUTM(double latitude, double longitude) {
			Vector<Double> coordinates = new Vector<Double>();
	        BigDecimal a = new BigDecimal("6378388.0");
	        BigDecimal b = new BigDecimal("6356911.946130");
	        
	        BigDecimal e_1 = a.pow(2);
	        BigDecimal e_2 = b.pow(2);
	        BigDecimal e_3= e_1.subtract(e_2);
	        
	        double eccentricity_2=Math.sqrt(e_3.doubleValue())/b.doubleValue();
	        double exp=Math.pow(eccentricity_2,2);
	        double c=Math.pow(a.doubleValue(),2)/b.doubleValue();
	        
	        int huson=(int)Math.floor((longitude/6)+31);
	        int meridianohuson=huson*6-183;
	        
	        double loradians = longitude*Math.PI/180;
	        double lambda=loradians-((meridianohuson*Math.PI)/180);
	        
	        double laradians = latitude*Math.PI/180;
	        double A=Math.cos(laradians)*Math.sin(lambda);
	        
	        
	        BigDecimal e6= new BigDecimal(A);
	        BigDecimal one = new BigDecimal("1");
	        BigDecimal e7= one.subtract(e6);
	        BigDecimal e8= one.add(e6);
	        double aux= e8.doubleValue()/e7.doubleValue();
	        double xi=0.5*Math.log(aux);
	        
	        double ni=(c/(1+exp*Math.pow(Math.pow(Math.cos(laradians),2),0.5))*0.9996);
	        double zeta=(exp/2)*Math.pow(xi,2)*Math.pow(Math.cos(laradians),2);
	        double eta=Math.atan(Math.tan(laradians)/Math.cos(lambda))-laradians;
	        double a_1=Math.sin(2*laradians);
	        double a_2=a_1*Math.pow(Math.cos(laradians),2);
	        double j_2=laradians+(a_1/2);
	        double j_4=((3*j_2)+a_2)/4;
	        double j_6=(5*j_4+a_2*Math.pow(Math.cos(laradians),2))/3;
	        double alfa=(0.75)*exp;
	        double beta=(1.6666666)*Math.pow(alfa,2);
	        double gamma=(1.2962962)*Math.pow(alfa,3);
	        double bfi=0.9996*c*(laradians-(alfa*j_2)+(beta*j_4)-(gamma*j_6));
	        coordinates.add(xi*ni*(1+zeta/3)+500000);
	        
	        if(latitude<0){
	        	coordinates.add(eta*ni*(1+zeta)+bfi+10000000);
	        }
	        else{
	        	coordinates.add(eta*ni*(1+zeta)+bfi);
	        }
	        return coordinates;
	}
}
