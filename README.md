## Para rodar o servidor e cliente, abra dois terminais.

### Para comunicação simples do cliente com o servidor use a pasta unidirectional.

No terminal do servidor:

```
cd unidirectional
javac Server.java
java Server
```

No terminal do cliente:

```
cd unidirectional
javac Client.java
java Client
```

### Para comunicação dupla use a pasta bidirectional.

No terminal do servidor:

```
cd bidirectional
javac Server.java
java Server
```

No terminal do cliente:

```
cd bidirectional
javac Client.java
java Client
```

### Para passar arquivos do tipo json use a pasta file_transfer.

No terminal do servidor:

```
cd file_transfer
javac -cp ./lib/json-20240303.jar Server.java
java -cp .:./lib/json-20240303.jar Server
```

No terminal do cliente:

```
cd file_transfer
javac -cp ./lib/json-20240303.jar Client.java
java -cp .:./lib/json-20240303.jar Client
```

### Para utilizar requisições HTTP com JSON use a pasta httpJson.

No terminal do servidor:

```
cd httpJson
javac -cp ./lib/gson-2.11.0.jar Server.java
java -cp .:./lib/gson-2.11.0.jar Server
```

No terminal do cliente:

```
cd httpJson
javac -cp ./lib/gson-2.11.0.jar Client.java
java -cp .:./lib/gson-2.11.0.jar Client
```
