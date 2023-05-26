package logicaJuego;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import elementos.PlayerType;

class JuegoTest {

	@Test
	void testAddCoordenadaJugadores() {
		PlayerType[] tipos = new PlayerType[4];
		tipos[0]=PlayerType.ELFO;
		tipos[1]= PlayerType.GUERRERO;
		tipos[2]= PlayerType.MAGO;
		tipos[3]=PlayerType.OGRO;
		Juego j = new Juego(tipos);
		j.asignarCoordenadasJugadores();
		
		assertEquals(4, j.getCoordenadaJugadores().size());
	
		
	}

}
