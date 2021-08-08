package Universities;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import UniversitiesUsers.User;
import UniversitiesUsers.User_MIREA;

import java.io.IOException;
import java.util.ArrayList;

public class MIREA {
    private final ArrayList<User_MIREA> listOfUsers = new ArrayList<>();

    public ArrayList<User> getAllUsers(String link, int score) {
        return new ArrayList<>(getAllUsersMIREA(link, score));
    }

    // Not void, because of possibility using this function later (as public of course)
    private ArrayList<User_MIREA> getAllUsersMIREA(String link, int score) {
        Document doc = null;
        try {
            doc = Jsoup.connect(link).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        var pre_parsed = doc.body().getElementsByClass("namesTable");
        Elements rows = pre_parsed.select("tr");

        User_MIREA user;
        for (int i = 1; i < rows.size(); i++) { //first row is the col names so skip it.
            Element row = rows.get(i);
            Elements cols = row.select("td");
            if (Integer.parseInt(cols.get(7).text()) < score) break;
            user = createNewMIREAUser(cols);
            listOfUsers.add(user);
        }
        return listOfUsers;
    }

    /**
     * This function creates new MIREA User, by taking as information base, parsed data, that we contain in column
     * @param cols Parsed data from column
     * @return new MIREA User that bases on data from @param cols
     */
    private User_MIREA createNewMIREAUser(Elements cols) {
        var user = new User_MIREA();
        for (var elem:cols) {
            var attr_class_val = elem.attr("class");
            var text_of_elem = elem.text();
            switch (attr_class_val) {
                case "num" -> user.setPosition(Integer.parseInt(text_of_elem));
                case "fio" -> user.setSnils_Regnumb(text_of_elem);
                case "accepted" -> user.setAgreement((byte) (text_of_elem.equals("нет") ? 2 : 1));
                case "campus" -> user.setHostel(text_of_elem.equals("требуется"));
                case "marks" -> {
                    String[] cut = text_of_elem.split(" ");
                    for (int i = 0; i < cut.length; i++) {
                        switch (i) {
                            case 0 -> user.setMath_score(Byte.parseByte(cut[0]));
                            case 1 -> user.setFiz_inf_score(Byte.parseByte(cut[1]));
                            case 2 -> user.setRus_score(Byte.parseByte(cut[2]));
                        }
                    }
                }
                case "achievments" -> user.setIndivid_score(Byte.parseByte(text_of_elem));
                case "status" -> {
                    if (text_of_elem.contains("Согласие на др. конкурсе")) user.setAgreement((byte) 3);
                    user.setPrerogative(text_of_elem.contains("Преим. право"));
                }
            }
            user.setWithout_exam(false);
        }
        return user;
    }
}
