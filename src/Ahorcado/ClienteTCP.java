package Ahorcado;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClienteTCP {

	private static int puerto = 45003;
	private static DataInputStream lector = null;
	private static DataOutputStream escritor = null;

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		boolean seguirJugando = true;
		Socket socket = null;
		try {
			socket = new Socket("localhost", puerto);
			escritor = new DataOutputStream(socket.getOutputStream());
			lector = new DataInputStream(socket.getInputStream());
			while (seguirJugando) {
				conversacion();
				System.out.println("Quieres volver a jugar? si/no");
				String s = sc.nextLine().toLowerCase();
				escritor.writeUTF(s);
				if (!s.equals("si")) {
					seguirJugando = false;
				}
			}
			System.out.println("Fin del juego");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static void conversacion() {

		Scanner sc = new Scanner(System.in);
		int longitud = 0;

		do {
			System.out.println("De cuantas letras quieres la palabra? elige entre 3 y 6 caracteres");
			try {

				longitud = Integer.parseInt(sc.nextLine());
			} catch (Exception e) {
				longitud = -1;
			}

		} while (longitud < 3 || longitud > 6);
		try {
			escritor.writeInt(longitud);
		} catch (Exception e) {

		}
		try {

			boolean seguir = true;
			while (seguir) {

				System.out.println("Escribe una letra para mandar al servidor:");
				String letraElegida = sc.nextLine();
				escritor.writeUTF(letraElegida);

				String nuevosasteriscos = lector.readUTF();
				String respuesta = lector.readUTF();
				String horca = lector.readUTF();
				String mensajeletrasusadas = lector.readUTF();

				System.out.println(nuevosasteriscos);
				System.out.println(respuesta + "\n" + horca);
				System.out.println(mensajeletrasusadas);

				if (respuesta.equals("Estas muerto") || respuesta.equals("HAS GANADO!!!!")) {
					seguir = false;
				}

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
