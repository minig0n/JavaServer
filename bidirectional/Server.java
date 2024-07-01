import java.io.*;
import java.net.*;

/*
O ServerSocket é a classe responsável por escutar solicitações de clientes em uma porta específica. 
Ele espera por conexões de clientes e cria um Socket para cada conexão estabelecida.

Quando um cliente tenta se conectar ao servidor, o ServerSocket aceita a conexão, 
criando um novo Socket para essa comunicação. Esse Socket é usado para se comunicar com o cliente.

A classe ClientHandler estende Thread e sobrescreve o método run(), 
que contém o código para tratar a comunicação com o cliente.

Para compilar e executar no terminal:
$ cd STRINGS
$ javac Server.java
$ java Server
*/

// Classe do servidor
public class Server {
    public static void main(String[] args) {
        int port = 12345; // Porta que o servidor vai escutar

        // Cria o socket
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("SERVIDOR INICIADO");
            System.out.println("Escutando na porta " + port + "\n");
            
            // Main
            while (true) {
                // Aceita a conexão do cliente e devolve um Socket usado para comunicação com ele
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress());
                
                // Para permitir que o servidor lide com múltiplos clientes simultaneamente, cada conexão é tratada por uma nova thread
                new ClientHandler(clientSocket).start();
            }

        // Se tiver erro ao iniciar
        } catch (IOException e) {
            System.out.println("Erro ao iniciar o servidor: " + e.getMessage());
        }
    }
}

// Classe para tratar a comunicação com o cliente
class ClientHandler extends Thread {
    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    public void run() {
        
        try (

            // BufferedReader in é usado para ler dados do servidor.
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // PrintWriter out é usado para enviar dados ao servidor.
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // BufferedReader stdIn é usado para ler a entrada do usuário a partir do console
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
            
            ) {
                // Quando receber mensagem do cliente, enviar resposta
                String inputLine;
                Thread clientListener = new Thread(
                    () -> {
                        try {
                            String clientMessage;
                            while ((clientMessage = in.readLine()) != null) {

                                // Mostra no terminal o que foi recebido do cliente
                                System.out.println("<client> " + clientMessage);

                                // Envia resposta para o cliente
                                out.println("Eco: " + clientMessage);
                            }
                        } catch (IOException e) {
                            System.out.println("Erro ao ler mensagem do cliente: " + e.getMessage());
                        }
                    });

            clientListener.start(); // Inicia a Thread

            while ((inputLine = stdIn.readLine()) != null) {
                out.println(inputLine); // Envia a mensagem do servidor para o cliente
            }

            clientListener.join(); // Aguarda o término da thread do listener do cliente

        } catch (IOException | InterruptedException e) {
            System.out.println("Erro ao comunicar com o cliente: " + e.getMessage());

        } finally {
            try {
                // Fechar o socket quando o cliente se desconectar
                clientSocket.close();

            } catch (IOException e) {
                System.out.println("Erro ao fechar o socket do cliente: " + e.getMessage());
            }
        }
    }
}
