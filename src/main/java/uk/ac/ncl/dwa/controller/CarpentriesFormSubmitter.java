package uk.ac.ncl.dwa.controller;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

public class CarpentriesFormSubmitter {
    public static void main(String[] args) {
        WebDriver driver = new FirefoxDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            driver.get("https://amy.carpentries.org/forms/self-organised/");

            // Wait until the form is loaded
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email")));

            driver.findElement(By.id("id_personal")).click();
            driver.findElement(By.id("id_personal")).sendKeys("Jannetta");
            driver.findElement(By.id("id_family")).sendKeys("Steyn");
            driver.findElement(By.id("id_email")).sendKeys("jannetta.steyn@newcastle.ac.uk");
            driver.findElement(By.id("id_secondary_email")).sendKeys("carol.booth2@newcastle.ac.uk");
            driver.findElement(By.cssSelector(".select2-container--focus .select2-selection")).click();
            driver.findElement(By.cssSelector(".select2-search--dropdown > .select2-search__field")).sendKeys("Newcastle University");
            driver.findElement(By.cssSelector(".select2-search--dropdown > .select2-search__field")).sendKeys(Keys.ENTER);
            driver.findElement(By.id("id_institution_department")).click();
            driver.findElement(By.id("id_institution_department")).sendKeys("RSE Team");
            // click in person radio button
            driver.findElement(By.cssSelector("#div_id_online_inperson .custom-control:nth-child(2) > .custom-control-label")).click();
            // workshop format select Other
            driver.findElement(By.cssSelector("#div_id_workshop_format .custom-control:nth-child(4) > .custom-control-label")).click();
            driver.findElement(By.id("id_workshop_format_other")).click();
            driver.findElement(By.id("id_workshop_format_other")).sendKeys("One day");
            driver.findElement(By.id("id_start")).click();
            driver.findElement(By.id("id_start")).sendKeys("2025-06-25");
            driver.findElement(By.id("id_end")).sendKeys("2025-06-25");
            driver.findElement(By.id("id_workshop_url")).sendKeys("https://nclrse-training.github.io/2025-06-25-NCL/");
            driver.findElement(By.cssSelector(".custom-control:nth-child(16) > .custom-control-label")).click();
            driver.findElement(By.id("id_workshop_types_other_explain")).click();
            driver.findElement(By.id("id_workshop_types_other_explain")).sendKeys("Programming with Python");
            driver.findElement(By.id("select2-id_country-container")).click();
            driver.findElement(By.cssSelector(".select2-search--dropdown > .select2-search__field")).sendKeys("United Kingdom");
            driver.findElement(By.cssSelector(".select2-search--dropdown > .select2-search__field")).sendKeys(Keys.ENTER);
            driver.findElement(By.id("select2-id_language-container")).click();
            driver.findElement(By.cssSelector(".select2-search--dropdown > .select2-search__field")).sendKeys("english");
            driver.findElement(By.cssSelector(".select2-search--dropdown > .select2-search__field")).sendKeys(Keys.ENTER);
            driver.findElement(By.cssSelector("#div_id_workshop_listed .custom-control:nth-child(2) > .custom-control-label")).click();
            driver.findElement(By.cssSelector("#div_id_public_event .custom-control:nth-child(3) > .custom-control-label")).click();
            driver.findElement(By.id("div_id_data_privacy_agreement")).click();
            driver.findElement(By.cssSelector("#div_id_code_of_conduct_agreement > .custom-control-label")).click();
            driver.findElement(By.cssSelector("#div_id_data_privacy_agreement > .custom-control-label")).click();
            driver.findElement(By.cssSelector("#div_id_host_responsibilities > .custom-control-label")).click();
            driver.switchTo().frame(0);
            driver.findElement(By.cssSelector(".recaptcha-checkbox-border")).click();
            driver.switchTo().defaultContent();
            driver.findElement(By.id("submit-id-submit")).click();

            // Wait to confirm submission (e.g., success message or redirect)
            wait.until(ExpectedConditions.urlContains("forms/self-organised/thank-you"));

            System.out.println("✅ Form submitted successfully!");

        } catch (Exception e) {
            System.err.println("❌ An error occurred during form submission:");
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
