package UniversitiesUsers;

import java.util.List;


public class User_MEI extends User {


    @Override
    public List<Object> formatterForGSheets() {
        var list = preFormatterForGSheets();
        list.add(snils_Regnumb.equals("16845695627") ? "Да" : "Нет");
        return list;
    }
}