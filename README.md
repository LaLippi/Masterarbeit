# Masterarbeit

## Voraussetzungen
Mit diesen Versionen wurde die Anwendung entwickelt. Andere Versionen können ebenfalls funktionieren.
- Ubuntu 20.04 (auf Windows 10 WSL2)
- Java 17.0.6
- Apache Maven 3.6.3
- Docker 20.10.21
- mysql (```apt install mysql-client-core-8.0```)

## Docker Netzwerk erstellen
- ```docker network create mc_questions_nw```

## Datenbank

1. ```docker run --name mc_questions_db --network=mc_questions_nw -p 127.0.0.1:3306:3306 -e MYSQL_ROOT_PASSWORD=```[sicheres root-Passwort]``` -d mysql:5.7.29```
2. ```mysql -uroot -p -P 3306 --protocol=tcp < Anwendung/DB-Schema.sql```

Anschließend läuft ein Docker-Container mit dem Namen ```mc_questions_db``` und kann durch ```docker stop mc_questions_db``` und ```docker start mc_questions_db``` gestartet bzw. gestoppt werden.

## Acra
Erzeugen des Docker-Images:
1. In den Ordner "Anwendung" wechseln
2. TLS-Zertifikat erstellen: ```openssl req -new -newkey rsa:4096 -x509 -sha256 -days 365 -nodes -out AcraTLSCert.crt -keyout AcraTLSKey.key```
3. ```./install_acra.sh```

Starten des Containers: ```./Anwendung/acra_server.sh```

Der Container gibt Log-Daten im Konsolenfenster aus, daher wird zum Starten der Anwendung etc. eine weitere Konsole empfohlen.

## Webanwendung

Bauen des JARs:
1. ```cd ```[...]```/Implementation/mc_questions```
2. ```./mvnw clean package```

Ausführen:
1. Datenbank-Docker-Container starten (```docker start mc_questions_db```)
2. Acra-Docker-Container starten (```./acra_server.sh```)
3. ```java -jar ```[...]```/Implementation/mc_questions/target/mc_questions-1.0.jar```

Zugriff: Im Browser über ```https://localhost:8443```

## Lizenz
Dieses Projekt wurde unter der GNU GPLv3-Lizenz veröffentlicht.
