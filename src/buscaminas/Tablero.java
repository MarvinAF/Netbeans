package buscaminas;

/**
 *
 * @author cesar
 * @author Sami
 */
import java.io.Serializable;

class Tablero implements Serializable{
        String x;
	String y;
        String [][] m;
       
	Tablero(String x, String y, String [][] m){
		this.x = x;
		this.y = y;
                this.m = m;
	}

    public String getX() {
        return x;
    }

    public String getY() {
        return y;
    }


    
    public String[][] getM(){
        return this.m.clone();
    }
               
        
}