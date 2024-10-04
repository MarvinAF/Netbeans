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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Servidor {
    
public static void sincronizarTableros(String[][] matrizPrincipal, String[][] matrizSecundaria) {
    int filas = matrizPrincipal.length;
    int columnas = matrizPrincipal[0].length;

    // Iterar sobre ambas matrices y sincronizar los valores
    for (int i = 0; i < filas; i++) {
        for (int j = 0; j < columnas; j++) {
            // Si en la matriz principal hay un espacio vacío, lo sincronizamos en la secundaria
            if (matrizPrincipal[i][j].equals(" ")) {
                matrizSecundaria[i][j] = " ";  // Actualizar la matriz secundaria

                // Revisar las casillas circundantes
                for (int x = i - 1; x <= i + 1; x++) {
                    for (int y = j - 1; y <= j + 1; y++) {
                        // Verificar que la casilla esté dentro de los límites de la matriz
                        if (x >= 0 && x < filas && y >= 0 && y < columnas) {
                            // Si la casilla es un número, copiarlo a la secundaria y poner "X" en la principal
                            if (esNumero(matrizPrincipal[x][y])) {
                                matrizSecundaria[x][y] = matrizPrincipal[x][y];  // Copiar el número a la secundaria
                                matrizPrincipal[x][y] = "X";  // Cambiar el número a "X" en la principal
                            }
                        }
                    }
                }
            }
        }
    }
}

// Función auxiliar para verificar si una cadena representa un número
public static boolean esNumero(String valor) {
    try {
        Integer.parseInt(valor);  // Intenta convertir el valor a número
        return true;  // Si la conversión es exitosa, es un número
    } catch (NumberFormatException e) {
        return false;  // Si ocurre una excepción, no es un número
    }
}


//ESTA FUNCION DESTAPA RECURSIVAMENTE TODOS LOS CONTORNOS DE LA CASILLA SI CONTIENE 0
//ES EL METODO DEL PROFE
//IGUAL MODIFICA LA MATRIZ NUMERICA Y DEJA LIBRES LOS ESPACIOS QUE TENIAN CERO PARA COPIARLAS EN LA DEL CLIENTE

public static void destaparCeldas(String[][] tablero, boolean[][] descubiertas, int fila, int columna) {
    // Comprobar que las coordenadas están dentro de los límites del tablero
    tablero[0][0]= "-";
    if (fila < 0 || fila >= tablero.length || columna < 0 || columna >= tablero[0].length) {
        return; // Fuera de límites, salir
    }

    // Si la celda ya está descubierta, salir
    if (descubiertas[fila][columna]) {
        return;
    }

    // Marcar la celda como descubierta
    descubiertas[fila][columna] = true;

    // Si la celda contiene un número mayor a 0, no hacer más cambios
    if (!tablero[fila][columna].equals("0")) {
        return;
    }

    // Si la celda es "0", cambiarla a "X" para indicar que está descubierta
    tablero[fila][columna] = " ";

    // Continuar destapando las casillas adyacentes (8 direcciones)
    for (int i = -1; i <= 1; i++) {
        for (int j = -1; j <= 1; j++) {
            if (i != 0 || j != 0) { // Evitar la celda actual
                destaparCeldas(tablero, descubiertas, fila + i, columna + j);
            }
        }
    }
}


    
public static int EjecucionTablero(String x, String y, String[][] matriz, String[][]matriz2, int mina) {
    // Convertir la letra (x) en el índice correcto
    int columna = x.toUpperCase().charAt(0) - 'A' + 1;  // Convertir letra a índice, por ejemplo 'A' -> 1

    // Convertir el número (y) en el índice correcto
    int fila = Integer.parseInt(y);  // Convertir el número string a entero para la fila

    // Acceder a la casilla de la matriz
    String valorCasilla = matriz[fila][columna];  // Matriz que pasas como parámetro

    // Decidir según el valor de la casilla
    if (valorCasilla.equals("0")) {
        System.out.println("Es un 0, sigue jugando...");
        
        //imprimirMatriz(matriz);
        boolean[][] descubiertas = new boolean[matriz.length][matriz[0].length];
        destaparCeldas(matriz,descubiertas, fila,columna);
        
        // Aquí podrías hacer algo, como descubrir más casillas
        sincronizarTableros(matriz, matriz2);

        
        
    } else if (Integer.parseInt(valorCasilla) > 0) {
        System.out.println("Es un número mayor a 0, mostrar al jugador...");
       
        // Mostrar el valor al jugador
        
        
    } else if (valorCasilla.equals("-1")) {
        System.out.println("Es una mina, juego terminado.");
        mina = 1;
        // Enviar mensaje de fin del juego
    }
    
    return mina;

}


    public static String[][] generarMatrizOculta(int filas, int columnas) {
    String[][] matriz = new String[filas][columnas];  // Crear matriz de cualquier tamaño
    
    // Rellenar la primera fila con letras (A, B, C, ..., según las columnas)
    for (int j = 1; j < columnas; j++) {
        matriz[0][j] = String.valueOf((char) ('A' + j - 1));  // Letra A hasta el número de columnas
    }

    // Rellenar la primera columna con números (1, 2, 3, ..., según las filas)
    for (int i = 1; i < filas; i++) {
        matriz[i][0] = String.valueOf(i);  // Números de 1 a filas-1
    }

    // Rellenar el resto de la matriz con guiones inicialmente
    for (int i = 1; i < filas; i++) {
        for (int j = 1; j < columnas; j++) {
            matriz[i][j] = "-";
        }
    }
    
    return matriz;
}

    // Función para imprimir la matriz
    public static void imprimirMatriz(String[][] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                if (matriz[i][j] == null) {
                    System.out.print(" \t");
                } else {
                    System.out.print(matriz[i][j] + "\t");
                }
            }
            System.out.println();
        }
    }
    public static void rellenarPistas(String[][] matriz) {
        int filas = matriz.length;
        int columnas = matriz[0].length;

        // Recorrer toda la matriz, comenzando en la fila 1 y columna 1 (evitando las coordenadas)
        for (int i = 1; i < filas; i++) {
            for (int j = 1; j < columnas; j++) {
                // Solo contar minas en casillas que no tienen mina
                if (!matriz[i][j].equals("-1")) {
                    int minasCircundantes = contarMinasCircundantes(matriz, i, j);
                    matriz[i][j] = String.valueOf(minasCircundantes);
                }
            }
        }
    }

    // Función auxiliar para contar las minas alrededor de una casilla
    public static int contarMinasCircundantes(String[][] matriz, int fila, int columna) {
        int minas = 0;
        int filas = matriz.length;
        int columnas = matriz[0].length;

        // Recorrer las 8 casillas circundantes
        for (int i = fila - 1; i <= fila + 1; i++) {
            for (int j = columna - 1; j <= columna + 1; j++) {
                // Verificar que las casillas estén dentro de los límites
                if (i >= 1 && i < filas && j >= 1 && j < columnas) { // Cambia 0 a 1
                    // Incrementar el contador si hay una mina
                    if (matriz[i][j].equals("-1")) {
                        minas++;
                    }
                }
            }
        }
        return minas;
    }

    // Función para crear la matriz con coordenadas en nivel fácil
    public static String[][] facil() {
        String[][] matriz = new String[10][10];  // Crear matriz de 10x10 (9x9 más fila y columna de coordenadas)
        
        Random random = new Random();
        int minasColocadas = 0;

        // Rellenar la primera fila con letras (A, B, C, ..., I)
        for (int j = 1; j < 10; j++) {
            matriz[0][j] = String.valueOf((char) ('A' + j - 1));  // Letra A hasta I
        }

        // Rellenar la primera columna con números (1, 2, 3, ..., 9)
        for (int i = 1; i < 10; i++) {
            matriz[i][0] = String.valueOf(i);
        }

        // Rellenar el resto de la matriz con ceros inicialmente
        for (int i = 1; i < 10; i++) {
            for (int j = 1; j < 10; j++) {
                matriz[i][j] = "0";
            }
        }

        // Colocar 9 minas (-1) en posiciones aleatorias
        while (minasColocadas < 9) {
            int fila = random.nextInt(9) + 1;  // Índice aleatorio para la fila (1 a 9)
            int columna = random.nextInt(9) + 1;  // Índice aleatorio para la columna (1 a 9)

            // Solo coloca la mina si la posición está vacía
            if (!matriz[fila][columna].equals("-1")) {
                matriz[fila][columna] = "-1";
                minasColocadas++;
            }
        }
        System.out.println("con -1:");
        //imprimirMatriz(matriz);  // Llamada al método para imprimir

        System.out.println("con pistas:");
        rellenarPistas(matriz);
        
        imprimirMatriz(matriz);  // Llamada al método para imprimir

        return matriz;
    }
    
    
    // Función para crear la matriz con coordenadas en nivel intermedio
    public static String[][] intermedio() {
        String[][] matriz = new String[17][17];  // Crear matriz de 17x17 (16x16 más fila y columna de coordenadas)
        
        Random random = new Random();
        int minasColocadas = 0;

        // Rellenar la primera fila con letras (A, B, C, ..., P)
        for (int j = 1; j < 17; j++) {
            matriz[0][j] = String.valueOf((char) ('A' + j - 1));
        }

        // Rellenar la primera columna con números (1, 2, ..., 16)
        for (int i = 1; i < 17; i++) {
            matriz[i][0] = String.valueOf(i);
        }

        // Rellenar el resto de la matriz con ceros inicialmente
        for (int i = 1; i < 17; i++) {
            for (int j = 1; j < 17; j++) {
                matriz[i][j] = "0";
            }
        }

        // Colocar 40 minas (-1) en posiciones aleatorias
        while (minasColocadas < 40) {
            int fila = random.nextInt(16) + 1;  // Índice aleatorio para la fila (1 a 16)
            int columna = random.nextInt(16) + 1;  // Índice aleatorio para la columna (1 a 16)

            // Solo coloca la mina si la posición está vacía
            if (!matriz[fila][columna].equals("-1")) {
                matriz[fila][columna] = "-1";
                minasColocadas++;
            }
        }
        
        System.out.println("con -1:");
        imprimirMatriz(matriz);  // Llamada al método para imprimir
        System.out.println("con pistas:");
        rellenarPistas(matriz);
        
        imprimirMatriz(matriz);  // Llamada al método para imprimir

        return matriz;
    }

    // Función para crear la matriz con coordenadas en nivel difícil
    public static String[][] dificil() {
        String[][] matriz = new String[17][31];  // Crear matriz de 17x31 (16x30 más fila y columna de coordenadas)
        
        Random random = new Random();
        int minasColocadas = 0;

        // Rellenar la primera fila con letras (A, B, C, ..., AD)
        for (int j = 1; j < 31; j++) {
            matriz[0][j] = String.valueOf((char) ('A' + j - 1));
        }

        // Rellenar la primera columna con números (1, 2, ..., 16)
        for (int i = 1; i < 17; i++) {
            matriz[i][0] = String.valueOf(i);
        }

        // Rellenar el resto de la matriz con ceros inicialmente
        for (int i = 1; i < 17; i++) {
            for (int j = 1; j < 31; j++) {
                matriz[i][j] = "0";
            }
        }

        // Colocar 99 minas (-1) en posiciones aleatorias
        while (minasColocadas < 99) {
            int fila = random.nextInt(16) + 1;  // Índice aleatorio para la fila (1 a 16)
            int columna = random.nextInt(30) + 1;  // Índice aleatorio para la columna (1 a 30)

            // Solo coloca la mina si la posición está vacía
            if (!matriz[fila][columna].equals("-1")) {
                matriz[fila][columna] = "-1";
                minasColocadas++;
            }
        }
        
        System.out.println("con -1:");
        //imprimirMatriz(matriz);  // Llamada al método para imprimir
        System.out.println("con pistas:");
        rellenarPistas(matriz);
        
        imprimirMatriz(matriz);  // Llamada al método para imprimir


        return matriz;
    }

    public static void main(String[] args) {
        try {
            ServerSocket s = new ServerSocket(1234);
            System.out.println("Servidor iniciado en el puerto " + s.getLocalPort());
            
            for (;;) {
                Socket cliente = s.accept();
                System.out.println("Cliente conectado desde: " + cliente.getInetAddress() + ": " + cliente.getPort());

                // Enviar el menú al cliente
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(cliente.getOutputStream()), true); // Auto-flush
                
               //---------------------------------------------------- ENVIAR EL MENU----------------------------------------------------


                String menu = "Escoge un nivel de dificultad: ";
                pw.println(menu); // Enviar el menú

                //----------------------------------------------------Leer el nivel enviado por el cliente desde el socket----------------------------------------------------

                
                BufferedReader br = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                String nivel = br.readLine(); // Leer el nivel desde el cliente
                System.out.println("Recibiendo datos del cliente, nivel escogido: " + nivel);

                // Confirmación
                String confirmacion = "Hola, has escogido el nivel: " + nivel;
                pw.println(confirmacion); // Enviar la confirmación al cliente
                
                
                //ENTRADA Y SALIDA DE OBJETOS
                ObjectOutputStream oos = new ObjectOutputStream(cliente.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(cliente.getInputStream());
                
                //----------------------------------------------------ENTRA A UN SWITCH DONDE ENTRARÁ SEGUN EL NIVEL ----------------------------------------------------
                /*
                1. FACIIL
                2. INTERMEDIO
                3. DIFICIL
                */
                
                switch (nivel) {
                    case "1":
                        //HACE LA MATRIZ NUMERICA DE LA FACIL -> LA RELLENA DE 0 , -1 Y NUMEROS MAYORES A 0
                        String[][] matrizNumerica = facil();
                        //HACE LA MATRIZ QUE SE MANDA A CLIENTE -> ESTA LLENA DE PURAS "-"
                        String[][] matrizCliente = generarMatrizOculta(10,10);
                        
                        //HACE UN OBJETO Y LE MANDA EL TABLERO DE matrizCliente
                        Tablero ob2 = new Tablero(null,null,matrizCliente.clone());
                        oos.writeObject(ob2);
                        oos.flush();
                      
                        
                        //AQUI SE LEERA LO QUE EL CLIENTE QUIERE HACER, SI USAR UNA BANDERA O DESTAPAR CASILLA
                        
                        String accion; 
                        Tablero ob2I;

                        // Leer la acción del cliente
                        accion = br.readLine();
                        System.out.println("Recibiendo datos del cliente, acción escogida: " + accion);

                         // SE ENVIAN LAS COORDENADAS PARA DESTAPAR EN LA MATRIZ QUE SI TIENE NUMEROS ( ES LA QUE SE LLAMA matrizNumerica)
                        ob2I = (Tablero) ois.readObject();
                        System.out.println("x:" + ob2I.getX() + " y:" + ob2I.getY());
                        int mina=0;
                        
                        
                        //SI ESCOGIÓ DESTAPAR ENTRA AL CASO 1
                        //SI ESCOGIÓ PONER BANDERA, ENTRA AL CASO 2
                            switch (accion) {
                                case "1":
                                    // AQUÍ SE MANDARÁN LAS COORDENADAS Y SE REVISARÁ EN LA FUNCION SI HAY UN 0,-1 Ó UN >0 EN LA CASILLA
                                    //DEPENDE DE QUE HAYA, SE HARÁ ALGO
                                    //DEVUELVE UN ENTERO QUE SI ES 1 ES QUE ENCONTRÓ UNA MINA
                                    //->REVISAR LA FUNCION
                                    mina = EjecucionTablero(ob2I.getX(), ob2I.getY(), matrizNumerica,matrizCliente,mina);
                                    imprimirMatriz(matrizNumerica);

                                    // Actualizar el tablero del cliente
                                    String[][] nuevaMatriz = new String[matrizCliente.length][];
                                    for (int i = 0; i < matrizCliente.length; i++) {
                                        nuevaMatriz[i] = matrizCliente[i].clone();
                                    }

                                    // Enviar el tablero actualizado al cliente
                                    Tablero ob5 = new Tablero(null, null, nuevaMatriz);
                                    oos.writeObject(ob5);
                                    oos.flush();

                                    break;

                                case "2":
                                    // Acciones adicionales (por ejemplo, insertar bandera)
                                    break;
                            }
                    break;  // Termina el caso, evita que siga ejecutando otros casos
                    case "2":
                        matrizNumerica = intermedio();
                        matrizCliente = generarMatrizOculta(17,17);
                        ob2 = new Tablero(null,null,matrizCliente.clone());
                        oos.writeObject(ob2);
                        oos.flush();
                        System.out.println("Cliente conectado.. Enviando objeto con los datos\nX:"+ob2.getX()+" Y:"+ob2.getY());
                        // Leer accion enviada por el cliente desde el socket
                        accion = br.readLine(); // Leer el nivel desde el cliente

                        
                        System.out.println("Recibiendo datos del cliente, accion escogida: " + accion);
                        
                        ob2I = (Tablero) ois.readObject();
                        System.out.println("x:" + ob2I.getX() + " y:" + ob2I.getY());

                        switch(accion){
                            case "1":
                                //EjecucionTablero(ob2I.getX(), ob2I.getY(), matrizNumerica);
                                sincronizarTableros(matrizNumerica, matrizCliente);
                                matrizCliente[0][0]="-";
                                String[][] nuevaMatriz = new String[matrizCliente.length][];
                                for (int i = 0; i < matrizCliente.length; i++) {
                                    nuevaMatriz[i] = matrizCliente[i].clone();
                                }
                                
                                Tablero ob5 = new Tablero(null, null, nuevaMatriz);
                                
                                System.out.println("Enviando objeto con los datos\nX:" + ob5.getX() + " Y:" + ob5.getY());
                                oos.writeObject(ob5);
                                oos.flush();

                                break;
                            case "2":
                                break;
                        }
                        break;  
                    // Puedes agregar más casos si es necesario
                    case "3":
                        matrizNumerica = dificil();
                        matrizCliente = generarMatrizOculta(17,31);
                        ob2 = new Tablero(null,null,matrizCliente.clone());
                        oos.writeObject(ob2);
                        oos.flush();
                        System.out.println("Cliente conectado.. Enviando objeto con los datos\nX:"+ob2.getX()+" Y:"+ob2.getY());
                        // Leer accion enviada por el cliente desde el socket
                        accion = br.readLine(); // Leer el nivel desde el cliente

                        
                        System.out.println("Recibiendo datos del cliente, accion escogida: " + accion);
                        
                        ob2I = (Tablero) ois.readObject();
                        System.out.println("x:" + ob2I.getX() + " y:" + ob2I.getY());

                        switch(accion){
                            case "1":
                                //EjecucionTablero(ob2I.getX(), ob2I.getY(), matrizNumerica);
                                sincronizarTableros(matrizNumerica, matrizCliente);
                                matrizCliente[0][0]="-";
                                String[][] nuevaMatriz = new String[matrizCliente.length][];
                                for (int i = 0; i < matrizCliente.length; i++) {
                                    nuevaMatriz[i] = matrizCliente[i].clone();
                                }
                                
                                Tablero ob5 = new Tablero(null, null, nuevaMatriz);
                                
                                System.out.println("Enviando objeto con los datos\nX:" + ob5.getX() + " Y:" + ob5.getY());
                                oos.writeObject(ob5);
                                oos.flush();

                                break;
                            case "2":
                                break;
                        }
                        break;                      // Puedes agregar más casos si es necesario
                    default:
                        // Código que se ejecuta si ninguno de los casos anteriores coincide
                        break;
                }

                // Cerrar el socket después de procesar el cliente
                cliente.close(); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
