import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/*
Para compilar e executar no terminal:
$ cd STRINGS
$ javac Client.java
$ java Client
*/

public class Client {
    public static void main(String[] args) {
        String hostname = "localhost"; // Endereço do servidor
        int port = 8080; // Porta do servidor

        System.out.println("CLIENTE INICIADO");
        System.out.println("Enviando na porta " + port + "\n");

        try {
            // Monta a URL do servidor com o contexto "/echo"
            String url = "http://" + hostname + ":" + port + "/echo";
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            String userInput;
            
            // Quando receber entrada do usuário, enviar ao servidor e mostrar a resposta
            while ((userInput = stdIn.readLine()) != null) {
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                
                // Configura a requisição para POST
                con.setRequestMethod("POST");
                con.setDoOutput(true);

                // Envia a mensagem ao servidor
                OutputStream os = con.getOutputStream();
                os.write(userInput.getBytes());
                os.flush();
                os.close();

                // Lê a resposta do servidor
                int responseCode = con.getResponseCode();
                System.out.println("Código de resposta: " + responseCode);

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                
                // Imprime a resposta do servidor
                System.out.println("<server> " + response.toString() + "\n");
            }

        } catch (Exception e) {
            System.out.println("Servidor desconectado\n");
            e.printStackTrace();
        }
    }
}
