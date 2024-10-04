/**
 * @author cesar
 * @author Sami
 */

package buscaminas;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class Cliente {
    public static void main(String[] args) {
        try {
            Socket cliente = new Socket("127.0.0.1", 1234);

            // Flujo para enviar datos al servidor
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(cliente.getOutputStream()), true); // Auto-flush
            // Leer datos desde la consola del cliente
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            // Leer la respuesta del servidor desde el socket
            BufferedReader br2 = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            String line= br2.readLine();

            //---------------------------------------------------- Leer el  MENU, ESCOGER EL NIVEL DE DIFICULTAD----------------------------------------------------
            System.out.println(line);
            // Leer el nivel desde la consola
            String nivel = br.readLine(); // El cliente debe ingresar su elección
            pw.println(nivel); // Enviar el nivel al servidor
            
            

            // Esperar confirmación del servidor
            String confirmacion = br2.readLine(); // Leer la confirmación del servidor
            System.out.println(confirmacion); // Mostrar la confirmación

            ObjectOutputStream oos = new ObjectOutputStream(cliente.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(cliente.getInputStream());
            
            
            //---------------------------------------------------- TABLERO CON PUROS "-"----------------------------------------------------

            
            Tablero ob2 = (Tablero) ois.readObject();
            System.out.println("Objeto recibido desde " + cliente.getInetAddress() + ":" + cliente.getPort() + " con los datos:");
            System.out.println("x:" + ob2.getX() + " y:" + ob2.getY());
            String[][]MatrizJuego = ob2.getM();
            System.out.println("Tablero del juego: \n");
            for(int i=0;i<MatrizJuego.length;i++){
                for(int j=0;j<MatrizJuego[1].length;j++){
                    System.out.print(MatrizJuego[i][j]+"    ");
                }//for
                System.out.println("");
            }
            
            //---------------------------------------------------- ESCOGER QUE ACCION HARÁ----------------------------------------------------
            System.out.println("\n\nOpciones:\n     1. Descubrir casilla\n     2. Insertar bandera\nEscoge una:");

            // Enviar la acción al servidor
            String accion = br.readLine(); // El cliente debe ingresar su elección
            pw.println(accion);
            

                       
            //---------------------------------------------------- LEER COORDENADAS QUE SE PASAN COMO UN OBJETO PERO SOLO X,Y----------------------------------------------------
            //se manda como NULL la matriz pq solo queremos las coordenadas para analizar que hay en esa casilla
            Scanner scanner = new Scanner(System.in);

            // Pedir las coordenadas al usuario
            System.out.println("Inserta la coordenada de la fila:");
            String valor1 = scanner.nextLine();

            System.out.println("Inserta la coordenada de la columna: ");
            String valor2 = scanner.nextLine();

            // Enviar el objeto Tablero al servidor
            Tablero juego = new Tablero(valor1, valor2, null);
            oos.writeObject(juego);
            oos.flush();
            
            
            
            //---------------------------------------------------- NOS MANDA EL TABLERO DE LAS QUE DESTAPAMOS O PERDIMOS----------------------------------------------------
            //Ahorita solo manda de las destapadas pero solo la primera vez
            //Recibimos un objeto solo con la matriz rellena

            // Recibir la respuesta del servidor (tablero actualizado)
            Tablero ob3 = (Tablero) ois.readObject();
            // Imprimir el tablero del juego
            String[][] MatrizJuego2 = ob3.getM();
            System.out.println("Tablero del juego: \n");
            for (int i = 0; i < MatrizJuego2.length; i++) {
                for (int j = 0; j < MatrizJuego2[2].length; j++) {
                    System.out.print(MatrizJuego2[i][j] + "    ");
                }
                System.out.println("");
            }

            // Cerrar flujos y socket&
            ois.close();
            br2.close();
            br.close();
            pw.close();
            cliente.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
