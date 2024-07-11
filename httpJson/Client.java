import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/*
Para compilar e executar no terminal:
$ cd http-example
$ javac -cp .:gson-2.8.6.jar com/example/HttpClientExample.java
$ java -cp .:gson-2.8.6.jar com.example.HttpClientExample
*/

public class Client {
    public static void main(String[] args) {
        String hostname = "localhost"; // Endereço do servidor
        int port = 8080; // Porta do servidor
        String jsonFilePath = "./client_folder/simulation.json"; // Caminho para o arquivo JSON

        System.out.println("CLIENTE INICIADO");
        System.out.println("Enviando na porta " + port + "\n");

        try {
            // Lê o arquivo JSON
            String jsonString = readFile(jsonFilePath);

            // Monta a URL do servidor com o contexto "/simulation"
            String url = "http://" + hostname + ":" + port + "/simulation";

            // Quando receber entrada do usuário, enviar ao servidor e mostrar a resposta
            while (true) {
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                // Configura a requisição para POST
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setRequestProperty("Content-Type", "application/json");

                // Envia a mensagem ao servidor
                OutputStream os = con.getOutputStream();
                os.write(jsonString.getBytes());
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

                // Salvar o conteúdo do arquivo localmente
                String fileName = "./client_folder/received_from_server.json";
                try (FileWriter fileWriter = new FileWriter(fileName)) {
                    fileWriter.write(response.toString());
                }

                // Converte a resposta JSON para objeto
                Gson gson = new Gson();
                JsonObject jsonResponse = gson.fromJson(response.toString(), JsonObject.class);
                // JsonObject results = jsonResponse.getAsJsonObject("results");
                int operation = jsonResponse.get("operation").getAsInt();
                int other_result = jsonResponse.get("other_result").getAsInt();

                // Imprime a resposta do servidor
                System.out.println("<server> results.operation: " + operation);
                System.out.println("<server> results.other_result: " + other_result);
                break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para ler o conteúdo do arquivo
    private static String readFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        StringBuilder stringBuilder = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        reader.close();
        return stringBuilder.toString();
    }
}
