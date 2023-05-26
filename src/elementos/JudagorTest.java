package elementos;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class JudagorTest {

	@ParameterizedTest
	@CsvSource({//Comprobamos que los numeros aleatorios de la fuerza esten entre los valores permitidos dependiendo de cada rol
		"ELFO, 5",
		"OGRO, 7",
		"MAGO, 4",
		"GUERRERO, 6"
		
	})
	void testGetFuerzaParaLuchar(PlayerType tipo, int fuerzaLiminte) {
		Jugador j1 = new Jugador(tipo);
		assert(j1.getFuerzaParaLuchar()<=fuerzaLiminte);
		assert(j1.getFuerzaParaLuchar()>=1);
	}
	
	
	 @ParameterizedTest
	 @ValueSource(ints = {0,1,2,3,4,5,6})
	 void testLucha(int valor) {

		 Jugador uno = new Jugador(PlayerType.OGRO);
		 Jugador dos = new Jugador(PlayerType.MAGO);
		
		 uno.setDinero(5);
		
		
		 assert(uno.lucha(dos)==valor);
		
	}

}
