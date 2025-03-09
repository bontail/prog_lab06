build-java:
	./gradlew build

upload-gelios: build-java
	uplitmo build/libs/Lab06Gradle-1.0-server.jar
	# citmo
	# export COLLECTION_FILENAME=dump.csv;java -jar Lab06Gradle-1.0-server.jar


run-client: build-java
# 	sudo openvpn ~/Desktop/students.ovpn
# 	java -jar build/libs/Lab06Gradle-1.0-client.jar


# save command
# echo -n '1/1;{"command": "SAVE_COLLECTION"}' | nc -u -w0 localhost 22233