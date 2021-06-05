package laurenlei.nasdaq;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.util.regex.Pattern;

import static org.openqa.selenium.firefox.GeckoDriverService.GECKO_DRIVER_EXE_PROPERTY;

/**
 * Download Firefox driver - <https://github.com/mozilla/geckodriver/releases>.
 */
public class NasdaqApplication {
    private static String USAGE = "Usage: java -jar nasdaq-0.0.1.jar [yyyy-MM-dd]";

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println(USAGE);
            System.exit(1);
        }

        String date = args[0];
        Pattern pattern = Pattern.compile("^[0-9]{4}-[0-9]{2}-[0-9]{2}$");
        if (!pattern.matcher(date).matches()) {
            System.out.println(USAGE);
            System.exit(1);
        }

        WebDriver driver = createDriver();
        driver.get("https://www.nasdaq.com/market-activity/dividends");
        driver.get("https://api.nasdaq.com/api/calendar/dividends?date=" + date);
        WebElement jsonElement = driver.findElement(By.tagName("pre"));
        System.out.println(jsonElement.getText());
        driver.quit();
    }

    public static WebDriver createDriver() {
        System.setProperty(GECKO_DRIVER_EXE_PROPERTY, "geckodriver_linux64");
        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("devtools.jsonview.enabled", false);
        FirefoxOptions options = new FirefoxOptions().setProfile(profile);
        return new FirefoxDriver(options);
    }
}
