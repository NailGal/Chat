// ServerTest.java


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.netology.Server;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;
import static ru.netology.Server.SETTINGS_FILE;

class ServerTest {
    private static final String SETTINGS_FILE = "server/src/main/resources/settings.txt";
    int port;
    //private static final String SETTINGS_FILE = "/server/src/main/resources/settings.txt";

    @TempDir
    Path tempDir;

    @Test
    void testReadPortFromValidSettings() throws IOException {
        //File settingsFile = tempDir.resolve("settings.txt").toFile();
        //Files.writeString(settingsFile.toPath(), "port=5555");

        Server server = new Server();
        port = server.readPortFromSettings();



        int portExpected = 5588;

        assertEquals(portExpected, port);
    }

    @Test
    void testReadPortFromInvalidSettings() {
        Server server = new Server();
        int port = server.readPortFromSettings();


        assertEquals(1112, port);
    }
}




