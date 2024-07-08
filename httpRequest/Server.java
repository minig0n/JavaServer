import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;

/*
O HttpServer é a classe responsável por escutar solicitações HTTP de clientes em uma porta específica.
Ele espera por conexões de clientes e cria um HttpExchange para cada requisição estabelecida.

A classe EchoHandler implementa HttpHandler e sobrescreve o método handle(), 
que contém o código para tratar a comunicação com o cliente.

Para compilar e executar no terminal:
$ cd STRINGS
$ javac Server.java
$ java Server
*/

// Classe do servidor HTTP
public class Server {
    public static void main(String[] args) throws IOException {
        int port = 8080; // Porta que o servidor vai escutar

        // Cria um servidor HTTP que escuta na porta especificada
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        // Define um contexto de requisição "/echo" que será tratado pela classe EchoHandler
        server.createContext("/echo", new EchoHandler());
        // Configura o servidor para utilizar um executor padrão
        server.setExecutor(null);
        // Inicia o servidor
        server.start();
        System.out.println("Servidor HTTP iniciado na porta " + port);
    }

    // Classe para tratar as requisições HTTP
    static class EchoHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Verifica se a requisição é do tipo POST
            if ("POST".equals(exchange.getRequestMethod())) {
                // Lê a entrada do cliente
                InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                String response = "Eco: " + sb.toString();

                // Envia a resposta de volta ao cliente
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
                
            } else {
                // Se o método HTTP não for POST, responde com 405 Method Not Allowed
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }
}

