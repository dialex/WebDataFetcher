import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Fetcher {

    public static void main(String[] args) {
        try {
            Config config = readConfig();
            if (config.targets.size() > 0) {
                Object results = fetchData(config.targets);
                writeResults(results);
            } else {
                System.out.println("No tasks found on config file. Shutting down...");
            }
        } catch (Exception e) {
            System.out.println("The program will exit due to a fatal error: " + e.getMessage());
            System.out.println("\nDebug details: " + e.getStackTrace());
        }
    }

    private static void writeResults(Object results) {

    }

    private static Object fetchData(Object targets) {
        return null;
    }

    private static Config readConfig() throws IOException {
        String CONFIG_FILENAME = "targets.json";
        try {
            System.out.println("Looking for file " + System.getProperty("user.dir") + File.separator + CONFIG_FILENAME);
            JsonReader reader = new JsonReader(new FileReader(CONFIG_FILENAME));
            return new Gson().fromJson(reader, Config.class);
        }
        catch (Exception e) {
            throw new IOException(String.format("Could not read config file \"%s\".", CONFIG_FILENAME), e);
        }
    }
}
