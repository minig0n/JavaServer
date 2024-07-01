import java.io.*;
import java.net.*;

// Certifique-se de ter a biblioteca org.json disponível
import org.json.JSONObject;

/*
Para compilar e executar no terminal:
$ cd JSON
$ javac -cp ./lib/json-20240303.jar Client.java
$ java -cp .:./lib/json-20240303.jar Client
*/

public class Client {
    public static void main(String[] args) {
        String hostname = "localhost"; // Endereço do servidor
        int port = 12345; // Porta do servidor

        String filePath = "./client_files/teste.json"; // Caminho do arquivo JSON a ser enviado

        try (

            // O Socket é criado e conectado ao servidor especificado pelo hostname e port
            Socket socket = new Socket(hostname, port);

            // BufferedReader in é usado para ler dados do servidor.
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // PrintWriter out é usado para enviar dados ao servidor.
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // BufferedReader stdIn é usado para ler a entrada do usuário a partir do console
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        
            // BufferedReader fileReader é usado para ler a entrada de arquivos
            BufferedReader fileReader = new BufferedReader(new FileReader(filePath));
            
            ) {
                // Enviar o conteúdo do arquivo JSON ao servidor
                String line;
                while ((line = fileReader.readLine()) != null) {
                    out.println(line);
                }
                out.println(); // Finaliza a mensagem
                System.out.println("Texto do arquivo enviado: x = 1 e y = 2");

                // Receber a resposta do servidor
                String responseLine;
                StringBuilder responseBuilder = new StringBuilder();
                while ((responseLine = in.readLine()) != null) {
                    responseBuilder.append(responseLine).append("\n");
                }
                JSONObject responseJson = new JSONObject(responseBuilder.toString().trim());
                int resultado = responseJson.getInt("resultado");
                System.out.println("Resposta do servidor: x + y = " + resultado);

                // Salvar o conteúdo do arquivo localmente
                String fileName = "./client_files/received_from_server.json";
                try (FileWriter fileWriter = new FileWriter(fileName)) {
                    fileWriter.write(responseBuilder.toString());
                }

        } catch (UnknownHostException e) {
            System.out.println("Host desconhecido: " + hostname);
        } catch (IOException e) {
            System.out.println("Erro de I/O: " + e.getMessage());
        }
    }
}

