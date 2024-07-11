import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;

import example.Results;
import example.Simulation;

public class Server {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/simulation", new SimulationHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Servidor iniciado na porta " + port);
    }

    static class SimulationHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                // Lê o corpo da requisição
                InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                StringBuilder jsonString = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    jsonString.append(line);
                }

                // Salvar o conteúdo do arquivo localmente
                String fileName = "./server_folder/received_from_client.json";
                try (FileWriter fileWriter = new FileWriter(fileName)) {
                    fileWriter.write(jsonString.toString());
                }

                // Converte o JSON para um objeto Simulation
                Gson gson = new Gson();
                Simulation simulation = gson.fromJson(jsonString.toString(), Simulation.class);

                // Realiza alguma operação (exemplo simples de soma dos valores de wind e draft)
                int operationResult = simulation.getEnvironment().getWind() + simulation.getEnvironment().getDraft();

                // Cria o objeto de resposta
                Results results = new Results();
                results.setOperation(operationResult);
                results.setResult(2);

                // Converte o objeto de resposta para JSON
                String jsonResponse = gson.toJson(results);

                // Configura a resposta
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, jsonResponse.length());

                OutputStream os = exchange.getResponseBody();
                os.write(jsonResponse.getBytes());
                os.close();
            } else {
                exchange.sendResponseHeaders(405, -1); // Método não permitido
            }
        }
    }
}


