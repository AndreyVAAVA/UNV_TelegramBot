package Universities;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import UniversitiesUsers.User;
import UniversitiesUsers.User_Polytech;

import java.util.ArrayList;

public class Polytech {
    private final ArrayList<User_Polytech> listOfUsers = new ArrayList<>();

    public ArrayList<User> getAllUsers(String link, int score) {
        return new ArrayList<>(getAllUsersPolytech(link, score));
    }

    // Not void, because of possibility using this function later (as public of course)
    private ArrayList<User_Polytech> getAllUsersPolytech(String link, int score) {
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");
        var opt = new ChromeOptions();
        opt.addArguments("--headless");
        WebDriver driver = new ChromeDriver(opt);
        driver.get(link);
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Document doc = Jsoup.parse(driver.getPageSource());
        var pre_parsed = doc.body().getElementsByClass("check");
        Elements rows = pre_parsed.select("tr");
        driver.close();
        User_Polytech user;
        for (int i = 7; i < rows.size(); i++) { //first row is the col names so skip it.
            Element row = rows.get(i);
            Elements cols = row.select("td");
            if (Integer.parseInt(cols.get(14).text().replaceAll(" ", "").replaceAll(" ", "")) < score && cols.get(17).text().contains("0")) break;
            user = createNewPolytechUser(cols);
            listOfUsers.add(user);
        }
        return listOfUsers;
    }

    /**
     * This function creates new Polytech User, by taking as information base, parsed data, that we contain in column
     * @param cols Parsed data from column
     * @return new Polytech User that bases on data from @param cols
     */
    private User_Polytech createNewPolytechUser(Elements cols) {
        var user = new User_Polytech();
        var inform = new ArrayList<String>();
        for (var elem:cols) {
            var text_of_elem = elem.text();
            inform.add(text_of_elem.contains(" ") ? text_of_elem.replaceAll(" ", "") : text_of_elem);
        }
        user.setPosition(Integer.parseInt(inform.get(0)));
        user.setSnils_Regnumb(inform.get(2));
        user.setMath_score(Byte.parseByte(inform.get(5).replaceAll(" ", "").replaceAll(" ", "")));
        user.setFiz_inf_score(Byte.parseByte(inform.get(7).replaceAll(" ", "").replaceAll(" ", "")));
        user.setRus_score(Byte.parseByte(inform.get(9).replaceAll(" ", "").replaceAll(" ", "")));
        user.setIndivid_score(Byte.parseByte(inform.get(13).replaceAll(" ", "").replaceAll(" ", "")));
        user.setHostel(inform.get(15).replaceAll(" ", "").equals("нужд."));
        user.setAgreement((byte) (inform.get(16).contains("да") ? 1 : inform.get(16).contains("подано на") ? 3 : 2));
        user.setWithout_exam(Integer.parseInt(inform.get(17).replaceAll(" ", "").replaceAll(" ", "")) > 0);
        user.setPrerogative(!inform.get(18).isEmpty());
        return user;
    }

}
