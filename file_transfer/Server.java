import java.io.*;
import java.net.*;

// Certifique-se de ter a biblioteca org.json disponível
import org.json.JSONObject;

/*
O ServerSocket é a classe responsável por escutar solicitações de clientes em uma porta específica. 
Ele espera por conexões de clientes e cria um Socket para cada conexão estabelecida.

Quando um cliente tenta se conectar ao servidor, o ServerSocket aceita a conexão, 
criando um novo Socket para essa comunicação. Esse Socket é usado para se comunicar com o cliente.

A classe ClientHandler estende Thread e sobrescreve o método run(), 
que contém o código para tratar a comunicação com o cliente.

Para compilar e executar no terminal:
$ cd JSON
$ javac -cp ./lib/json-20240303.jar Server.java
$ java -cp .:./lib/json-20240303.jar Server
*/

// Classe do servidor
public class Server {
    public static void main(String[] args) {
        int port = 12345; // Porta que o servidor vai escutar

        // Cria o socket
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor escutando na porta " + port);
            
            // Loop de execução
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

            // BufferedReader e PrintWriter são usados para ler e escrever dados através do Socket.
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            
            ) {
                // Recebe o conteúdo do arquivo enviado
                StringBuilder fileContent = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null && !line.isEmpty()) {
                    fileContent.append(line).append("\n");
                }

                // Salvar o conteúdo do arquivo localmente
                String fileName = "./server_files/received_from_client.json";
                try (FileWriter fileWriter = new FileWriter(fileName)) {
                    fileWriter.write(fileContent.toString());
                }

                // Processar o conteúdo JSON
                JSONObject json = new JSONObject(fileContent.toString());
                int x = json.getInt("x");
                int y = json.getInt("y");
                int sum = x + y;

                // Criar o JSON de resposta
                JSONObject responseJson = new JSONObject();
                responseJson.put("resultado", sum);

                // Enviar o JSON de resposta de volta ao cliente
                out.println(responseJson.toString());

        } catch (IOException e) {
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



