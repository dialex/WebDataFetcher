import com.codeborne.selenide.Configuration;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class Fetcher {

    public static void main(String[] args) {
        initWebFetcher();
        try {
            Config config = readConfig();
            List<String> results = fetchData(config.targets);
            writeResults(results);
        } catch (Exception e) {
            System.out.println("The program will exit due to a fatal error: " + e.getMessage());
            System.out.println("\nDebug details: " + e.getStackTrace());
        }
    }

    private static void writeResults(List<String> results) {
        if (results.size() == 0) {
            System.out.println("No results.");
        } else {
            for (String result : results)
                System.out.println(result);
        }
    }

    private static List<String> fetchData(List<Target> targets) {
        List<String> fetched = new ArrayList<>(targets.size());
        for (Target target : targets) {
            String data = navigateAndExtract(target.url, target.selector);
            fetched.add(data);
        }
        return fetched;
    }

    private static Config readConfig() throws IOException {
        String CONFIG_FILENAME = "targets.json";
        try {
            System.out.println("Looking for file " + System.getProperty("user.dir") + File.separator + CONFIG_FILENAME);
            JsonReader reader = new JsonReader(new FileReader(CONFIG_FILENAME));
            return new Gson().fromJson(reader, Config.class);
        } catch (Exception e) {
            throw new IOException(String.format("Could not read config file \"%s\".", CONFIG_FILENAME), e);
        }
    }

    private static String navigateAndExtract(String url, String selector) {
        URL validUrl = validateUrl(url);
        if (validUrl != null) {
            // Navigate
            open(validUrl);
            // Extract
            return $(selector).getText();
        } else {
            System.out.println("Ignoring malformed url: " + url);
            return "";
        }
    }

    private static URL validateUrl(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            System.out.println("Attempting to fix malformed url...");
            url = "http://" + url;
        }
        // 2nd attempt after fix
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    private static void initWebFetcher() {
        Configuration.browser = "chrome";
        Configuration.timeout = 1000 * 3;
    }
}
