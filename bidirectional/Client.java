import java.io.*;
import java.net.*;

/*
Para compilar e executar no terminal:
$ cd STRINGS
$ javac Client.java
$ java Client
*/

public class Client {
    public static void main(String[] args) {
        String hostname = "localhost"; // Endereço do servidor
        int port = 12345; // Porta do servidor

        System.out.println("CLIENTE INICIADO");
        System.out.println("Enviando na porta " + port + "\n");

        try (

            // O Socket é criado e conectado ao servidor especificado pelo hostname e port
            Socket socket = new Socket(hostname, port);

            // BufferedReader in é usado para ler dados do servidor.
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // PrintWriter out é usado para enviar dados ao servidor.
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // BufferedReader stdIn é usado para ler a entrada do usuário a partir do console
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            
            ) {
                // Quando receber mensagem de volta do servidor, mostrar no terminal    
                Thread serverListener = new Thread(() -> {
                    try {
                        String serverMessage;
                        while ((serverMessage = in.readLine()) != null) {
                            System.out.println("<server> " + serverMessage);
                        }
                    } catch (IOException e) {
                        System.out.println("Erro ao ler mensagem do servidor: " + e.getMessage());
                    }
                });
    
                serverListener.start();
                
                // Enviar mensagem ao servidor
                String userInput;
                while ((userInput = stdIn.readLine()) != null) {
                    out.println(userInput);
                }
    
                serverListener.join(); // Aguarda o término da thread do listener do servidor

        } catch (UnknownHostException e) {
            System.out.println("Host desconhecido: " + hostname);
        } catch (IOException | InterruptedException e) {
            System.out.println("Erro de I/O: " + e.getMessage());
        }
    }
}
