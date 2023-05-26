package logicaJuego;

import static logicaJuego.JuegoUtils.crearSeparador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import elementos.Coordenada;
import elementos.Element;
import elementos.ElementType;
import elementos.Jugador;
import elementos.JugadorException;
import elementos.PlayerType;

public class Juego {

	private Map<Coordenada, Element> tablero;
	private List<Coordenada> coordenadaJugadores;
	private int jugadorJuega;
	private int dado; // Dado para ver los movimientos del jugador que juega
	private List<Jugador> listaJugadores;
	//Le agregamos una lista de jugadores para la gestion de la posicion de los jugadores del tablero

	/**
	 * Contructor que añade en orden los jugadores a la lista asignando las coordenadas al tablero y
	 * a los jugadores agrega elementos aleatorios al mismo.
	 * @param tipos
	 */
	public Juego(PlayerType[] tipos) {
		super();
		this.tablero= new HashMap<>();
		this.coordenadaJugadores = new ArrayList<>();		
		this.jugadorJuega=0;
		this.listaJugadores= new ArrayList<>();
		for (PlayerType pT : tipos) {
			if(pT!=null) {
				
				listaJugadores.add(new Jugador(pT));
				
			}
		}
		
		meterCordenadasEnTablas();
		asignarCoordenadasJugadores();	
		meterJugadoresTablero();
		elementosRandom();
	}
	
	/**
	 * Metodo que agrega los jugadores al tablero de juego
	 */
	public void meterJugadoresTablero(){
		
		int contador=0;
		
		for (Coordenada c : coordenadaJugadores) {
			if(c!=null) {
				tablero.put(c, listaJugadores.get(contador++));
			}
		}
	}
	
	/**
	 * Metodo que crea y asigna todas las coordenadas que podude tener el tablero al mapa
	 */
	public void meterCordenadasEnTablas() {
        for (int i=0; i<Constantes.TAMANNO; i++) {
            for (int a=0; a<Constantes.TAMANNO; a++) {
                Coordenada c = new Coordenada(i, a);
                tablero.put(c, null);

            }
        }
    }
	
	/**
	 *Metodo que agrega de forma aleatoria los elementos al tablero 
	 */
	public void elementosRandom() {
        int contadorMondedas = 0, contadorGemas = 0, contadorPociones = 0, contadorRocas = 0, contador = 0;

        while (contador != 12) {
            Coordenada c = new Coordenada(Math.round(new Random().nextInt(0, 9)),
                    Math.round(new Random().nextInt(0, 9)));
            if (!this.coordenadaJugadores.contains(c) && tablero.get(c) == null && contadorMondedas < 3) {
                tablero.replace(c, null, new Element(ElementType.DINERO));
                contadorMondedas++;
                contador++;
            } else if (!this.coordenadaJugadores.contains(c) && tablero.get(c) == null && contadorGemas < 3) {
                tablero.replace(c, null, new Element(ElementType.GEMA));
                contadorGemas++;
                contador++;
            } else if (!this.coordenadaJugadores.contains(c) && tablero.get(c) == null && contadorPociones < 3) {
                tablero.replace(c, null, new Element(ElementType.POCION));
                contadorPociones++;
                contador++;
            } else if (!this.coordenadaJugadores.contains(c) && tablero.get(c) == null && contadorRocas < 3) {
                tablero.replace(c, null, new Element(ElementType.ROCA));
                contadorRocas++;
                contador++;
            }

        }

    }
	
	/**
	 * Metodo que asigna coordenadas de forma aleatoria a los jugadores
	 */
	public void asignarCoordenadasJugadores() {
		
		Coordenada aux=null;
		
		int contador=0;
		for (Jugador j : listaJugadores) {
			aux=coordenadaRandom();
			if(j!=null&&!coordenadaJugadores.contains(aux)&&coordenadaJugadores.size()<4) {	
				coordenadaJugadores.add(aux);
				j.setCoor(coordenadaJugadores.get(contador));
				contador++;
			}
		}
		
	}
	
	/**
	 * Metodo que devuelve coordenadas aleatorias
	 * @return
	 */
	public Coordenada coordenadaRandom() {		
		return new Coordenada(new Random().nextInt(0, Constantes.TAMANNO),new Random().nextInt(Constantes.TAMANNO));
	}

	/**
	 * Mueve el jugador en el tablero
	 * 
	 * @param direccion
	 * @return resultado de la operación
	 * @throws JuegoException
	 * @throws JugadorException
	 */
	public String moverJugador(char direccion) throws JuegoException, JugadorException {

		String resultado = "";
		Jugador jugador = (Jugador) this.tablero.get(this.coordenadaJugadores.get(jugadorJuega));

		Coordenada coordDestino = getNextPosition(direccion);

		// Tengo que ver que hay en la nueva casilla
		Element elemento = this.tablero.get(coordDestino);

		if (elemento != null) { // Hay algo en la casilla
			if (elemento instanceof Jugador) {
				// Después de la lucha los jugadores no se mueven
				resultado = luchar(resultado, jugador, coordDestino, elemento);
				
			} else if (elemento.getType() == ElementType.ROCA) {
				resultado = encuentraRoca(resultado, jugador, coordDestino);
				
			} else if (elemento.getType() == ElementType.GEMA) {
				jugador.encuentraGema();
				this.cambiaJugadorAPosicion(coordDestino);

			} else if (elemento.getType() == ElementType.DINERO) {
				jugador.encuentraDinero();
				this.cambiaJugadorAPosicion(coordDestino);

			} else if (elemento.getType() == ElementType.POCION) {
				jugador.encuentraPocion();
				this.cambiaJugadorAPosicion(coordDestino);

			}

		} else {
			this.cambiaJugadorAPosicion(coordDestino);
		}

		return resultado;
	}
	
	private String encuentraRoca(String resultado, Jugador jugador, Coordenada coordDestino) {
		int resultadoRoca = jugador.encuentraRoca();
		switch (resultadoRoca) {
				case Constantes.ROMPE_ROCA_CON_GEMA:
					resultado = String.format("El jugador %s rompe la roca con una gema.", jugador.getNombre());
					this.cambiaJugadorAPosicion(coordDestino);
					break;
		
				case Constantes.GANA_A_LA_ROCA:
					resultado = String.format("El jugador %s gana a la roca.", jugador.getNombre());
					this.cambiaJugadorAPosicion(coordDestino);
					break;
		
				case Constantes.PIERDE_A_LA_ROCA:
					resultado = String.format("El jugador %s pierde. No se mueve.", jugador.getNombre());
					break;
		}
		return resultado;
	}

	private String luchar(String resultado, Jugador jugador, Coordenada coordDestino, Element elemento) {
		Jugador enemigo = (Jugador) elemento;
		int resultadoLucha = jugador.lucha(enemigo);
		switch (resultadoLucha) {
		case Constantes.EMPATE:
			resultado = "Empate entre los jugadores";
			break;
		case Constantes.GANA_USA_POCIMA:
			resultado = "El jugador " + jugador.getNombre() + " gana. Le quita una pócima al enemigo";
			break;
		case Constantes.GANA_DINERO:
			resultado = "El jugador " + jugador.getNombre() + " gana. Le quita el dinero al enemigo";
			this.eliminarJugador(coordDestino);
			break;
		case Constantes.GANA_MUERE:
			resultado = "El jugador " + jugador.getNombre() + " gana. El enemigo muere";
			this.eliminarJugador(coordDestino);
			// Si se elimina el jugador que juega el dado se pone a 0 para que no siga
			// tirando
			break;
		case Constantes.PIERDE_USA_POCIMA:
			resultado = "El enemigo " + enemigo.getNombre() + " gana. Le quita una pócima al jugador";
			break;
		case Constantes.PIERDE_DINERO:
			resultado = "El enemigo " + enemigo.getNombre() + " gana. Le quita el dinero al jugador";
			this.eliminarJugador(this.coordenadaJugadores.get(jugadorJuega));
			break;
		case Constantes.PIERDE_MUERE:
			resultado = "El enemigo " + enemigo.getNombre() + " gana. El jugador muere";
			this.eliminarJugador(this.coordenadaJugadores.get(jugadorJuega));
			dado = 0;
			// Decrementamos en uno el jugador, para que no se salte al siguiente
			// ya que al borrarlo el siguiente apunta al siguiente, y al incrementarlo
			// se le salta
			this.jugadorJuega--;
			break;
		}
		return resultado;
	}

	/**
	 * comprueba la direccion que quieres moverte si puedes moverte te devuelve las coordenadas
	 * @param direction
	 * @return
	 */
	private Coordenada getNextPosition(char direction) {
		
		Coordenada aux = null;
		this.tablero.put(this.listaJugadores.get(jugadorJuega).getCoor(), null);
		if(direction=='E' && obtenerCoordenadaJugadorJuega().goRight()) {
			aux= new Coordenada(obtenerCoordenadaJugadorJuega().getX(),obtenerCoordenadaJugadorJuega().getY());
		}else if(direction=='O' && obtenerCoordenadaJugadorJuega().goLeft()){
			aux=new Coordenada(obtenerCoordenadaJugadorJuega().getX(),obtenerCoordenadaJugadorJuega().getY());
		}else if(direction=='N' && obtenerCoordenadaJugadorJuega().goDown()){
			aux=new Coordenada(obtenerCoordenadaJugadorJuega().getX(),obtenerCoordenadaJugadorJuega().getY());
		}else if(direction=='S' && obtenerCoordenadaJugadorJuega().goUp()){
			aux=new Coordenada(obtenerCoordenadaJugadorJuega().getX(),obtenerCoordenadaJugadorJuega().getY());
		}
		
		return aux;
	}

	/**
	 * cambia el elemento jugador del tablero a otra posicion 
	 * @param coordDestino
	 */
   private void cambiaJugadorAPosicion(Coordenada coordDestino) {
        this.listaJugadores.get(jugadorJuega).setCoor(coordDestino);
        this.tablero.put(coordDestino, this.listaJugadores.get(jugadorJuega));
        

    }
    
   /**
    * busca el jugador meviante las coordenadas del tablero y lo elimina
    * @param coordDestino
    */

    protected void eliminarJugador(Coordenada coordDestino) {
        for (Coordenada e: this.tablero.keySet()) {
            if (e.equals(coordDestino)) {
                this.coordenadaJugadores.remove(e);
                tablero.replace(e, null);
                this.listaJugadores.remove(tablero.get(e));
            }
        }

    }


	/**
	 * Escribe el tablero en formato no gráfico. Devuelve el string con la
	 * información
	 */
	@Override
	public String toString() {
		StringBuilder resul = new StringBuilder();
		resul.append(crearSeparador());
		resul.append("     |");

		for (int fila = 0; fila < Constantes.TAMANNO; fila++) {
			for (int columna = 0; columna < Constantes.TAMANNO; columna++) {
				Coordenada coor = new Coordenada(columna, fila);

				if (this.tablero.get(coor) != null) {
					if (this.tablero.get(coor).getType() != null) {
						resul.append(" " + this.tablero.get(coor).getType().getSymbol() + " ");
					}
					
				} else {
					resul.append("   ");
				}

				resul.append("|");
			}
			resul.append(System.lineSeparator()).append(crearSeparador()).append("     |");
		}
		resul.delete(resul.length() - 5, resul.length());
		return resul.toString();
	}

	
	/**
	 *imprime la informacion de las pertenencias de los jugadores 
	 * @return
	 */
	public String imprimeValoresJugadores() {
		
		StringBuilder sb = new StringBuilder();
		
		for (Jugador j : listaJugadores) {
			if(j!=null) {
				sb.append("Nombre: "+j.getNombre()+" ,Dinero: "+j.getDinero()+" ,Pociones: "+j.getPociones()+" ,Gemas: "+j.getGemas()+"\n");				
			}
			
		}	
		
		return sb.toString();
	}

	/**
	 * imprime los nombres de los jugadores 
	 * @return
	 */
	public String imprimeNombreJugadores() {
		StringBuilder sb = new StringBuilder();
		
		for (Jugador j : listaJugadores) {
			
			if(j!=null) {
				sb.append("Nombre: "+j.getNombre()+"\n");				
			}
			
		}	
		
		return sb.toString();
	}

	/**
	 * comprueba la lista de jugadores y el dinero del tablero y 
	devuelve un boolean si ya no queda dinero o un solo jugador*/
	public boolean isTerminado() {
        boolean terminado = true;
        if (this.listaJugadores.size()>1) {
            terminado = false;
        }


        for (Coordenada e: tablero.keySet()) {
            while (terminado) {
                if (tablero.get(e).equals(ElementType.DINERO)) {
                    terminado = false;
                }
            }

        }
        return terminado;
    }

	public int getValorDado() {
		return this.dado;
	}

	/**
	 *asigna al valor del dado segun los valores maximo del personaje aleatoriamente 
	 */
	public void setDado() {
		
		this.dado=this.listaJugadores.get(jugadorJuega).getVelocidadParaLuchar()==0?1:this.listaJugadores.get(jugadorJuega).getVelocidadParaLuchar();
		
	}

    public String getNombreJuegadorQueJuega() {
        return this.listaJugadores.get(jugadorJuega).getNombre();
    }

	public void proximoJugador() {		
		 this.jugadorJuega=(++jugadorJuega%(this.listaJugadores.size()));		
	}
	
	/**
	 *compara cual seria el ganador segun el dinero que tenga cada 1 o si solo quedo 1 en pie 
	 * @return
	 */
	public String getGanador() {
		
		int min=Integer.MIN_VALUE;
		
		Jugador aux=null;
		
		if(isTerminado()) {
			for (Jugador j : this.listaJugadores) {
				if(j!=null && j.getDinero()>min) {
					aux=j;
					min=j.getDinero();
				}
			}
		}
		
		return aux.getNombre();
	}

	/**
	 * devuelve el elemento que tiene la key del tablero mediante una coordenada de entrada
	 * @param coordenada
	 * @return
	 */
	public Element obtenerElementoTablero(Coordenada coordenada) {
		return this.tablero.get(coordenada);
	}

	/**
	 * devuelve las coordenadas del jugador que esta jugando en ese momento y se la asigna al tablero
	 * @return
	 */
	public Coordenada obtenerCoordenadaJugadorJuega() {
		return this.listaJugadores.get(this.jugadorJuega).getCoor();
	}

	/**
	 * decremanta en 1 el valor del dado
	 */
	public void decrementaDado() {
		this.dado--;
	}

	/**
	 * devuelve una lista de coordenadas de los jugadores por orden
	 * @return
	 */
	protected List<Coordenada> getCoordenadaJugadores() {
		return coordenadaJugadores;
	}

}
