package Universities;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import UniversitiesUsers.User;
import UniversitiesUsers.User_MEI;

import java.io.IOException;
import java.util.ArrayList;

public class MEI {
    private final ArrayList<User_MEI> listOfUsers = new ArrayList<>();

    public ArrayList<User> getAllUsers(String link, int score) {
        return new ArrayList<>(getAllUsersMEI(link, score));
    }

    // Not void, because of possibility using this function later (as public of course)
    private ArrayList<User_MEI> getAllUsersMEI(String link, int score) {

        Document doc = null;
        try {
            doc = Jsoup.connect(link).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        var pre_parsed = doc.body().getElementsByClass("concurs-list thin-grid competitive-group-table");
        Elements rows = pre_parsed.select("tr");

        int student_counter = 0;
        User_MEI user;
        for (int i = 1; i < rows.size(); i++) { //first row is the col names so skip it.
            Element row = rows.get(i);
            var attr_class_val = row.attr("class");
            var attr_style_val = row.attr("style");
            if (attr_style_val.equals("background-color:#F0F0FF")) {
                continue;
            }
            student_counter++;
            Elements cols = row.select("td");
            if (Integer.parseInt(cols.get(0).text()) < score && !attr_class_val.equals("acceptedPoint accepted benefit") && !attr_class_val.equals("accepted benefit")) break;
            user = createNewMEIUser(cols);
            switch (attr_class_val) {
                case "acceptedPoint accepted" -> fillOtherFields(user, student_counter, false, (byte) 1);
                case "accepted" -> fillOtherFields(user, student_counter, false, user.getAgreement() != 3 ? (byte) 2 : (byte) 3);
                case "accepted benefit" -> fillOtherFields(user, student_counter, true, user.getAgreement() != 3 ? (byte) 2 : (byte) 3);
                default -> fillOtherFields(user, student_counter, true, (byte) 1);
            }
        }
        return listOfUsers;
    }

    private void fillOtherFields(User_MEI user, int position, boolean isWithoutExam, byte condOfAccepted) {
        user.setPosition(position);
        user.setWithout_exam(isWithoutExam);
        user.setAgreement(condOfAccepted);
        listOfUsers.add(user);
    }

    /**
     * This function creates new MEI User, by taking as information base, parsed data, that we contain in column
     * @param cols Parsed data from column
     * @return new MEI User that bases on data from @param cols
     */
    private User_MEI createNewMEIUser(Elements cols) {
        var agreement_in_an = "Согласие в другой КГ";
        var with = "с/о";
        var without = "б/о";
        var snils_check = "СНИЛС: ";
        var reg_numb_check = "Рег.номер: ";
        var inform = new ArrayList<String>();
        for (var elem:cols) {
            var attr_style_val = elem.attr("style");
            var text_of_elem = elem.text();
            if (attr_style_val.equals("color:gray;") || text_of_elem.equals(" ") || text_of_elem.contains("подано") || text_of_elem.contains("н/я")) {
                continue;
            }
            if (text_of_elem.equals(without)) {
                inform.add(without);
                continue;
            } else if (text_of_elem.equals(with)) {
                inform.add(with);
                continue;
            }
            if (text_of_elem.equals(agreement_in_an)) {
                inform.add(agreement_in_an);
                continue;
            }
            if (text_of_elem.contains(snils_check)) {
                inform.add(text_of_elem.replace(snils_check, ""));
                continue;
            }
            else if (text_of_elem.contains(reg_numb_check)) {
                inform.add(text_of_elem.replace(reg_numb_check, ""));
                continue;
            }
            inform.add(text_of_elem);
        }
        var user = new User_MEI();
        user.setMath_score(Byte.parseByte(inform.get(2)));
        user.setFiz_inf_score(Byte.parseByte(inform.get(3)));
        user.setRus_score(Byte.parseByte(inform.get(4)));
        user.setIndivid_score(Byte.parseByte(inform.get(5)));
        user.setSnils_Regnumb(inform.get(6));
        user.setHostel(inform.get(7).equals(with));
        if (inform.contains(agreement_in_an)) user.setAgreement((byte) 3);
        if (inform.contains("Преимущественное право")) user.setPrerogative(true);
        return user;
    }
}
