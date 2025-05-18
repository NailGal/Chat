import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.netology.Client;
import ru.netology.Server;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ServerIntegrationTest {
    private Server server;
    private Thread serverThread;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() throws IOException {
        // Создаем временный файл настроек сервера с портом 1111
        Path serverSettingsPath = Paths.get("server/src/main/resources/settings.txt");
        Files.createDirectories(serverSettingsPath.getParent());
        Files.write(serverSettingsPath, "port=5588".getBytes());

        // Перенаправляем вывод сервера для проверки
        System.setOut(new PrintStream(outContent));
        server = new Server();
        //server.start();
        serverThread = new Thread(server::start);
        serverThread.start();
    }

    @TempDir
    Path tempDir;

    @Test
    void testClientConnection() throws InterruptedException, IOException {
        // Эмулируем ввод имени пользователя и команды выхода
        String simulatedInput = "TEST_USER\n/exit\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));


        // Запускаем клиент в отдельном потоке
        //Client client = new Client();
        File settingsFile = tempDir.resolve("settings.txt").toFile();
        Files.writeString(settingsFile.toPath(), "host=localhost\nport=5588");

        Client client = new Client();
        client.readSettings(settingsFile.toString());
        Thread clientThread = new Thread(client::start);
        clientThread.start();

        // Даем время на обработку подключения
        Thread.sleep(3000);

        // Проверяем вывод сервера
        String serverOutput = outContent.toString();
        assertTrue(serverOutput.contains("TEST_USER присоединился к чату!"));

        // Завершаем клиент
        clientThread.interrupt();
    }

    @AfterEach
    void tearDown() throws IOException {
        // Останавливаем сервер и восстанавливаем стандартный вывод
        server.stop();
        System.setOut(originalOut);

        // Удаляем временный файл настроек сервера
        Path serverSettingsPath = Paths.get("server/src/main/resources/settings.txt");
        Files.deleteIfExists(serverSettingsPath);
    }
}


/*
// ServerIntegrationTest.java
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.Client;
import ru.netology.Server;
import ru.netology.ClientHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class ServerIntegrationTest {
    private Server server;
    private Thread serverThread;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    //Socket socket = new Socket();

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        server = new Server();
        serverThread = new Thread(() -> server.start());
        serverThread.start();
    }

    @Test
    void testClientConnection() throws InterruptedException, IOException {
        Client client = new Client();

        Thread.sleep(1000);
        assertTrue(outContent.toString().contains("TEST_USER joined"));
    }

    @AfterEach
    void tearDown() {
        server.stop();
        System.setOut(originalOut);
    }
}
*/