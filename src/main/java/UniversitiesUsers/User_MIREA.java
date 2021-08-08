package UniversitiesUsers;

import java.util.List;


public class User_MIREA extends User {

    @Override
    public List<Object> formatterForGSheets() {
        var list = preFormatterForGSheets();
        list.add(snils_Regnumb.equals("168-456-956-27") ? "Да" : "Нет");
        return list;
    }

}
