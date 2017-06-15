import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.io.IOException;

public class Fetcher {

    public static void main(String[] args) {
        try {
            Config targets = readConfig();
            Object results = fetchData(targets);
            writeResults(results);
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
            JsonReader reader = new JsonReader(new FileReader(CONFIG_FILENAME));
            return new Gson().fromJson(reader, Config.class);
        }
        catch (Exception e) {
            throw new IOException(String.format("Could not read config file \"%s\".", CONFIG_FILENAME), e);
        }
    }
}
