package elementos;

import java.util.Random;

import logicaJuego.Constantes;

public class Jugador extends Element {

	private static ElementType elemento; 
	private int dinero;
	private int pociones;
	private int gemas;
	private PlayerType ju;
	private Coordenada coor;//Coordenada donde esta el jugador 
	
	
	/**
	 * Constructor donde indicaremos cual es el tipo de jugador y se lo asignamos al 
	 * Element
	 * 
	 */
	public Jugador(PlayerType type) {
        super(ElementType.valueOf(type.name()));
        this.ju=type;
        this.elemento=ElementType.valueOf(type.name());
        
    }
	
	
	public Coordenada getCoor() {
		return coor;
	}

	public void setCoor(Coordenada coor) {
		this.coor = coor;
	}

	
	public String getNombre() {
		return ju.name();
	}
	
	
	/**
	 * Devulve un número random entre 1 y la fuerza limite dependiendo
	 * del tipo de jugador que nos sirve para luchar.
	 * 
	 */
	public int getFuerzaParaLuchar() {
		return  Math.round(new Random().nextInt(1, this.getFuerza()));
	}
	
	/**
	 * @return numero maximo de la fuerza dependiendo del tipo de jugador
	 */
	private int getFuerza() {
		return ju.getFuerza();
	}
	
	

	/**
	 * @return numero maximo de la magia dependiendo del tipo de jugador
	 */
	private int getMagia() {
		return ju.getMagia();
		
	}
	
	
	/**
	 * Devulve un número random entre 1 y la magia limite dependiendo
	 * del tipo de jugador que nos sirve para la magia.
	 * 
	 */
	public int getMagiaParaLuchar() {
		return Math.round(new Random().nextInt(1, this.getMagia()));
	}
	
	
	/**
	 * @return numero maximo de la velocidad dependiendo del tipo de jugador
	 */
	private int getVelocidad() {
        return this.ju.getVelocidad();
    }
	
	
	/**
	 * Devulve un número random entre 1 y la velocidad limite dependiendo
	 * del tipo de jugador que nos sirve para la movernos.
	 * 
	 */
    public int getVelocidadParaLuchar() {
        return Math.round(new Random().nextInt(1, this.getVelocidad()));
    }

	public int getDinero() {
		return dinero;
	}

	public void setDinero(int dinero) {
		this.dinero = dinero;
	}

	public int getPociones() {
		return pociones;
	}

	public void setPociones(int pociones) {
		this.pociones = pociones;
	}

	public int getGemas() {
		return gemas;
	}

	public void setGemas(int gamas) {
		this.gemas = gamas;
	}
	
	
	/**
	 * @return toString del jugador
	 */
	public String resumen() {
		return String.format("Eres un %s, tienes %s dinero, %s pociones y %s gemas", this.getNombre(), this.getDinero(), this.getPociones(), this.getGemas());
	}
	
	
	public PlayerType getPlayer() {
		return this.ju;
	}
	
	
	/**
	 * Recibe como parametro de entrada el Jugador con el que lucharemos.
	 * Gana quien tenga mas fuerza para luchas y se lleva el dinero del perdedor si este tiene,
	 * si el perdedor tiene poción este no morira, si no si muere
	 * @param j
	 * @return constantes de lucha
	 */
	public int lucha(Jugador j) {    	
    	
    	int resultado = Constantes.EMPATE;
    	
    	if(this.getFuerzaParaLuchar()>j.getFuerzaParaLuchar()) {
    		if(j.getPociones()>0) {
    			resultado=Constantes.GANA_USA_POCIMA;
    			this.setPociones(this.getPociones()+1);
    			j.setPociones(j.getPociones()-1);
    		}else {
    			if(j.getDinero()>0) {
    				this.setDinero(j.getDinero());
    				j.setDinero(0);
    				resultado=Constantes.GANA_DINERO;
    			}else {
    				resultado=Constantes.GANA_MUERE;
    			}
    			
    		}
    		
    	}else if(this.getFuerzaParaLuchar()<j.getFuerzaParaLuchar()) {
    		if(this.getPociones()>0) {
    			j.setPociones(j.getPociones()+1);
    			this.setPociones(this.getPociones()-1);
    			resultado=Constantes.PIERDE_USA_POCIMA;
    		}else {
    			if(this.getDinero()>0) {
    				j.setDinero(this.getDinero());
    				this.setDinero(0);
    				resultado=Constantes.PIERDE_DINERO;
    			}else {
    				resultado=Constantes.PIERDE_MUERE;
    			}
    			
    		}
    	}
    	
        return resultado;
    }
	
	/**
	 * Metodo para luchar contra la roca, si el jugador tiene gemas rompe la roca con gemas,
	 * si no tiene gemas pero su megia es mayor o igual a cuatro puede ganar a la roca, si no la roca 
	 * gana.
	 * @return constantes de luchar con roca
	 */
    public int encuentraRoca() {
    	
    	int resultado=Constantes.PIERDE_A_LA_ROCA;
    	
    	if(this.getGemas()>0) {
    		resultado=Constantes.ROMPE_ROCA_CON_GEMA;
    	}else if(this.getMagia()>=4) {
    		resultado=Constantes.GANA_A_LA_ROCA;
    	}
    	
        return resultado;
    }
	
	public void encuentraDinero() {
		this.setDinero(this.getDinero()+1);
		
	}
	public void encuentraPocion() {
		this.setPociones(this.getPociones());
			
	}
	public void encuentraGema() {
		this.setGemas(this.getGemas()+1);
		
	}


}
