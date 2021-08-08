package UniversitiesUsers;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class User {
    protected int position;
    protected byte math_score;
    protected byte fiz_inf_score;
    protected byte rus_score;
    protected byte individ_score;
    protected byte agreement;
    protected String snils_Regnumb;
    protected boolean hostel;
    protected boolean prerogative;
    protected boolean without_exam;

    public abstract List<Object> formatterForGSheets();

    protected List<Object> preFormatterForGSheets() {
        var list = new ArrayList<>();
        list.add(String.valueOf(position));
        list.add(String.valueOf(math_score+fiz_inf_score+rus_score+individ_score));
        list.add(String.valueOf(math_score));
        list.add(String.valueOf(fiz_inf_score));
        list.add(String.valueOf(rus_score));
        list.add(String.valueOf(individ_score));
        list.add(agreement == 1 ? "Подано" : agreement == 2 ? "Не подано" : "Согласие на др. конкурсе");
        list.add(snils_Regnumb);
        list.add(prerogative ? "Есть" : "Нету");
        list.add(hostel ? "С общежитием" : "Без общежития");
        list.add(without_exam ? "Да" : "Нет");
        return list;
    }
}
