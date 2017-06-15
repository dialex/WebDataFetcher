package com.diogonunes;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Fetcher {

    private static WebDriver browser;

    public static void main(String[] args) {
        try {
            browser = new ChromeDriver();
            Helper.helloWorld();
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

    private static Config readConfig() throws IOException {
        String FILENAME = "targets.json";
        try {
            System.out.println("Looking for file " + System.getProperty("user.dir") + File.separator + FILENAME);
            JsonReader reader = new JsonReader(new FileReader(FILENAME));
            return new Gson().fromJson(reader, Config.class);
        } catch (Exception e) {
            throw new IOException(String.format("Could not read config file \"%s\".", FILENAME), e);
        }
    }

    private static List<String> fetchData(List<Target> targets) {
        List<String> fetched = new ArrayList<>(targets.size());
        for (Target target : targets) {
            System.out.println("Target: " + target.url);
            Helper.navigate(browser, target.url);

            for (String selector : target.selectors) {
                String data = Helper.extract(browser, selector);
                fetched.add(data);
            }
        }
        return fetched;
    }

    private static void export(List<String> results) {
        if (results.size() == 0) {
            System.out.println("No results.");
        } else {
            //Helper.writeToScreen(results);
            Helper.writeToFile(results);
        }
    }
}
