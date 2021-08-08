package Universities;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import UniversitiesUsers.User;
import UniversitiesUsers.User_MAI;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MAI {
    private final ArrayList<User_MAI> listOfUsers = new ArrayList<>();

    public ArrayList<User> getAllUsers(String XPath, int score) {
        return new ArrayList<>(getAllUsersMAI(XPath, score));
    }

    // Not void, because of possibility using this function later (as public of course)
    private ArrayList<User_MAI> getAllUsersMAI(String XPath, int score) {
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");
        /*var opt = new ChromeOptions();
        opt.addArguments("--headless");*/
        WebDriver driver = new ChromeDriver();
        driver.get("https://priem.mai.ru/rating/");
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        driver.findElement(By.xpath("//*[@id=\"place\"]/option[2]")).click();
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        driver.findElement(By.xpath("//*[@id=\"level_select\"]/option[3]")).click();
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        driver.findElement(By.xpath("//*[@id=\"pay_select\"]/option[2]")).click();
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        driver.findElement(By.xpath("//*[@id=\"form_select\"]/option[2]")).click();
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        driver.findElement(By.xpath(XPath)).click();
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Document doc = Jsoup.parse(driver.getPageSource());
        var pre_parsed = doc.body().getElementsByClass("table-responsive");
        Elements rows = pre_parsed.select("tr");
        //driver.close();
        User_MAI user;
/*        int y = XPath.equals("//*[@id=\"spec_select\"]/option[29]") ? 20 : XPath.equals("//*[@id=\"spec_select\"]/option[12]") ? 14 :
                XPath.equals("//*[@id=\"spec_select\"]/option[10]") ? 29 : XPath.equals("//*[@id=\"spec_select\"]/option[13]") ? 20 : 12;*/
        boolean state;
        for (int i = 1; i < rows.size(); i++) { //first row is the col names so skip it.
            Element row = rows.get(i);
            Elements cols = row.select("td");
            if (Integer.parseInt(cols.get(3).text()) < 240) break;
            user = createNewMAIUser(cols);
            state = row.attr("class").equals("notagree");
            user.setAgreement((byte) (state ? 2 : 1));
            if (!state) {
                var position = new StringBuilder(String.valueOf(user.getPosition()));
                user.setPosition(Integer.parseInt(position.deleteCharAt(position.length()-1).toString()));
            }
            listOfUsers.add(user);
        }
        return listOfUsers;
    }

    /**
     * This function creates new MAI User, by taking as information base, parsed data, that we contain in column
     * @param cols Parsed data from column
     * @return new MAI User that bases on data from @param cols
     */
    private User_MAI createNewMAIUser(Elements cols) {
        var user = new User_MAI();
        var inform = new ArrayList<String>();
        for (var elem:cols) {
            var text_of_elem = elem.text();
            if (text_of_elem.contains("БД")) continue;
            inform.add(text_of_elem);
            //inform.add(text_of_elem.contains(" ") ? text_of_elem.replaceAll(" ", "") : text_of_elem);
        }
        user.setPosition(Integer.parseInt(inform.get(0)));
        user.setSnils_Regnumb(inform.get(1));
        user.setMath_score(Byte.parseByte(inform.get(3)));
        user.setFiz_inf_score(Byte.parseByte(inform.get(4)));
        user.setRus_score(Byte.parseByte(inform.get(5)));
        user.setIndivid_score(Byte.parseByte(inform.get(6)));
        user.setHostel(inform.get(8).contains("✓"));
        return user;
    }
}
