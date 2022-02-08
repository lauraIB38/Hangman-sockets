package Ahorcado;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ServidorTCP {
	/**
	 * @author laura
	 * @fecha 31/12/2021 programa: juego del ahorcado, realizado mediante socket
	 *        stream.
	 */

	private static int puerto = 45003;
	private static DataInputStream lector = null;
	private static DataOutputStream escritor = null;

	public static void main(String[] args) {
		boolean seguirJugando = true;
		ServerSocket servidor = null;
		Socket socket = null;
		try {

			servidor = new ServerSocket(puerto);
			System.out.println("Servidor arrancado.....Esperando cliente");
			socket = servidor.accept();
			lector = new DataInputStream(socket.getInputStream());
			escritor = new DataOutputStream(socket.getOutputStream());

			while (seguirJugando) {
				conversacion();
				if (!lector.readUTF().equals("si")) {
					seguirJugando = false;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
				servidor.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void conversacion() {

		int contador = 0;
		String palabraAsteriscos = "";
		String palabraBase = "";
		try {
			palabraBase = palabra(lector.readInt());
		} catch (IOException e) {
			e.printStackTrace();
		}

		String letrasUsadas = "";
		for (int i = 0; i < palabraBase.length(); i++) {
			palabraAsteriscos += "*";
		}

		try {

			boolean seguir = true;
			while (seguir) {
				String nuevaPalabra = "";
				String letraDelCliente = lector.readUTF();
				letraDelCliente = letraDelCliente.toLowerCase();

				if (letrasUsadas.contains(letraDelCliente)) {
					escritor.writeUTF(palabraAsteriscos);
					escritor.writeUTF("YA USASTE ESA LETRA !!!");
					escritor.writeUTF(horca(contador));
					escritor.writeUTF("Letras usadas : " + letrasUsadas);
				} else {
					boolean acierto = false;
					letrasUsadas += letraDelCliente.charAt(0);

					for (int i = 0; i < palabraBase.length(); i++) {

						if (letraDelCliente.charAt(0) == palabraBase.charAt(i)) {
							nuevaPalabra += palabraBase.charAt(i);
							acierto = true;
						} else {
							nuevaPalabra += palabraAsteriscos.charAt(i);
						}

					}
					palabraAsteriscos = nuevaPalabra;

					escritor.writeUTF(palabraAsteriscos);
					if (palabraAsteriscos.equals(palabraBase)) {
						escritor.writeUTF("HAS GANADO!!!!");
						seguir = false;

					} else {

						if (acierto == false) {
							contador++;
							if (contador == 6) {
								escritor.writeUTF("Estas muerto");
								seguir = false;
							} else {
								escritor.writeUTF("has fallado");
							}

						} else {
							escritor.writeUTF("Acertaste !!!");
						}
					}

					escritor.writeUTF(horca(contador));
					escritor.writeUTF("Letras usadas : " + letrasUsadas);

				}

			}

			System.out.println("FIN DEL JUEGO");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static String horca(int contador) {
		String horca0 = " ------+" + "\n |" + "\n |" + "\n |" + "\n |" + "\n =======";
		String horca1 = " ------+" + "\n |     O" + "\n |" + "\n |" + "\n |" + "\n =======";
		String horca2 = " ------+" + "\n |     O" + "\n |     |" + "\n |" + "\n |" + "\n =======";
		String horca3 = " ------+" + "\n |     O" + "\n |    /|" + "\n |" + "\n |" + "\n =======";
		String horca4 = " ------+" + "\n |     O" + "\n |    /|\\" + "\n |" + "\n |" + "\n =======";
		String horca5 = " ------+" + "\n |     O" + "\n |    /|\\" + "\n |    /" + "\n |" + "\n =======";
		String horca6 = " ------+" + "\n |     O" + "\n |    /|\\" + "\n |    / \\" + "\n |" + "\n =======";
		String[] array = { horca0, horca1, horca2, horca3, horca4, horca5, horca6 };
		return array[contador];
	}

	private static String palabra(int longitud) {
		Random r = new Random();
		String[][] baseDatos = { { "ria", "paz", "bar", "sal", "mar" }, { "tela", "mano", "ramo", "rosa", "pala" },
				{ "falda", "gafas", "cesta", "pista", "tosta" }, { "casaca", "cosaco", "candil", "carnet", "castor" } };

		return baseDatos[longitud - 3][r.nextInt(5)];
	}

}
