package elementos;

import java.util.Objects;

import logicaJuego.Constantes;

public class Coordenada {
	
	private int x;
    private int y;

    public Coordenada() {
        super();
    }

    public Coordenada(int x, int y) {
        this();
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public boolean equals(Object obj) {
        return this==obj || obj!=null && obj instanceof Coordenada && obj.hashCode()==this.hashCode();
    }

    @Override
    public String toString() {
        return "Coordenadas X: " + x + ", Y: " + y;
    }

    public boolean goRight() {

        boolean mover=false;

        if(this.getX()<9) {
            this.x++;
            mover=true;
        }

        return mover;
    }

    public boolean goLeft() {
    	boolean seMovio= false;
    	if (this.getX()>1) {
    		this.x--;
    		seMovio=true;
    	}
    	
        return seMovio;
    }

    public boolean goUp() {
        boolean mover=false;

        if(this.getY()<10) {
            this.y++;
            mover=true;
        }

        return mover;
    }

    public boolean goDown() {
    	boolean seMovio= false;
    	if (getY()>0) {
    		this.y--;
    		seMovio=true;
    	}
    	
        return seMovio;
    }

    public Coordenada clone() {
        return this;
    }

}
