import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Fetcher {

    private static WebDriver browser;

    public static void main(String[] args) {
        try {
            browser = new ChromeDriver();
            Config config = readConfig();
            List<String> results = fetchData(config.targets);
            export(results);
        } catch (Exception e) {
            System.out.println("The program will exit due to a fatal error: " + e.getMessage());
            System.out.println("\nDebug details: " + e.getStackTrace());
        } finally {
            if (browser != null)
                browser.quit();
        }
    }

    private static void export(List<String> results) {
        if (results.size() == 0) {
            System.out.println("No results.");
        } else {
            //writeToScreen(results);
            writeToFile(results);
        }
    }

    private static void writeToFile(List<String> results) {
        try {
            Path file = Paths.get("fetched.txt");
            Files.write(file, results, Charset.forName("UTF-8"));
        } catch (IOException e) {
            System.out.println("Could not write results to file: " + e.getMessage());
        }
    }

    private static void writeToScreen(List<String> results) {
        for (String result : results)
            System.out.println(result);
    }

    private static List<String> fetchData(List<Target> targets) {
        List<String> fetched = new ArrayList<>(targets.size());
        for (Target target : targets) {
            System.out.println("Target: " + target.url);
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
            System.out.println("\tNavigating...");
            browser.navigate().to(validUrl);
            System.out.println("\tExtracting...");
            return browser.findElement(By.cssSelector(selector)).getText();
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
}
