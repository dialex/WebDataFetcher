package com.diogonunes;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

abstract class Helper {

    static void helloWorld() {
        List<String> greet = Arrays.asList(
                "  _    _                    _           _  _         _              ",
                " | |  | |                  | |         | |(_)       | |             ",
                " | |_ | |__    __ _  _ __  | | __    __| | _   __ _ | |  ___ __  __ ",
                " | __|| '_ \\  / _` || '_ \\ | |/ /   / _` || | / _` || | / _ \\\\ \\/ / ",
                " | |_ | | | || (_| || | | ||   <   | (_| || || (_| || ||  __/ >  <  ",
                "  \\__||_| |_| \\__,_||_| |_||_|\\_\\   \\__,_||_| \\__,_||_| \\___|/_/\\_\\ ",
                "                                                                    ",
                "                                                                    "
        );
        writeToScreen(greet);
    }

    static String extract(WebDriver browser, String selector) {
        WebElement element = Helper.waitUntilVisible(browser, selector);
        if (element != null) {
            System.out.println("Extracting...");
            return element.getText();
        } else return "";
    }

    static void navigate(WebDriver browser, String url) {
        URL validUrl = Helper.validateUrl(url);
        if (validUrl != null) {
            System.out.println("\tNavigating...");
            browser.navigate().to(validUrl);
        } else {
            System.out.println("Ignoring malformed url: " + url);
        }
    }

    static void writeToFile(List<String> results) {
        String FILENAME = "fetched.txt";
        try {
            Path file = Paths.get(FILENAME);
            Files.write(file, results, Charset.forName("UTF-8"));
            System.out.println("Results exported to " + System.getProperty("user.dir") + File.separator + FILENAME);
        } catch (IOException e) {
            System.out.println("Could not write results to file: " + e.getMessage());
        }
    }

    private static void writeToScreen(List<String> results) {
        for (String result : results)
            System.out.println(result);
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

    private static WebElement waitUntilVisible(WebDriver browser, String selector) {
        try {
            System.out.print("\tLocating... ");
            return (new WebDriverWait(browser, 5)).until(
                    ExpectedConditions.visibilityOfElementLocated(By.cssSelector(selector)));
        } catch (TimeoutException e) {
            System.out.println("\t\t Unable to locate element with given selector");
            return null;
        }
    }

}