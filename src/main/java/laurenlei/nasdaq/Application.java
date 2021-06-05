package laurenlei.nasdaq;

import com.google.common.collect.Maps;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.util.Map;
import java.util.regex.Pattern;

import static org.openqa.selenium.firefox.GeckoDriverService.GECKO_DRIVER_EXE_PROPERTY;

/**
 * Download Firefox driver - <https://github.com/mozilla/geckodriver/releases>.
 * Download Chrome driver - <https://chromedriver.chromium.org/downloads>.
 *
 * Run headless in Linux:
 *   sudo apt-get install xvfb build-essential
 *   Xvfb :2 -screen 0 1024x768x24 &
 *   DISPLAY=:2 java -jar nasdaq.jar 2021-06-02
 *
 */
public class Application {
    private final static String USAGE = "Usage: java -jar nasdaq-0.0.1.jar [yyyy-MM-dd]";

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

        WebDriver driver = createChromeDriver();
        driver.get("https://api.nasdaq.com/api/calendar/dividends?date=" + date);
        WebElement jsonElement = driver.findElement(By.tagName("pre"));
        System.out.println(jsonElement.getText());
        driver.quit();
    }

    public static WebDriver createFireFoxDriver() {
        System.setProperty(GECKO_DRIVER_EXE_PROPERTY, "geckodriver_linux64");
        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("devtools.jsonview.enabled", false);
        FirefoxOptions options = new FirefoxOptions().setProfile(profile);
        return new FirefoxDriver(options);
    }

    public static WebDriver createChromeDriver() {
        System.setProperty("webdriver.chrome.driver", "chromedriver_linux64");
        Map<String, Object> perfs = Maps.newHashMap();
        ChromeOptions options = new ChromeOptions();
        // disable Chrome extension, the extensions are usually compressed and
        // it is a problem for company computer where it can't uncompress the
        // file because no permission.
        options.addArguments("args", "disable-extensions");
        options.setExperimentalOption("prefs", perfs);
        return new ChromeDriver(options);
    }
}
