package Bot;

import Google.GoogleSheetInteraction;
import Universities.MAI;
import Universities.MEI;
import Universities.MIREA;
import Universities.Polytech;
import UniversitiesUsers.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class UniversityListHelperBot extends TelegramLongPollingBot {
    @Override
    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        SendMessage message;
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Set variables
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();

            if (message_text.equals("/markup")) {
                message = SendMessage.builder() // Create a message object object
                        .chatId(Long.toString(chat_id))
                        .text("Here is your keyboard")
                        .build();
                // Create ReplyKeyboardMarkup object
                ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
                // Create the keyboard (list of keyboard rows)
                List<KeyboardRow> keyboard = new ArrayList<>();
                // Create a keyboard row
                KeyboardRow row = new KeyboardRow();
                // Set each button, you can also use KeyboardButton objects if you need something else than text
                row.add("МЭИ");
                row.add("МАИ");
                // Add the first row to the keyboard
                keyboard.add(row);
                // Create another keyboard row
                row = new KeyboardRow();
                // Set each button for the second line
                row.add("МИРЭА");
                row.add("Политех");
                // Add the second row to the keyboard
                keyboard.add(row);
                // Set the keyboard to the markup
                keyboardMarkup.setKeyboard(keyboard);
                // Add it to the message
                message.setReplyMarkup(keyboardMarkup);
                try {
                    execute(message); // Sending our message object to user
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (message_text.equals("/hide")) {
                message = SendMessage.builder()
                        .chatId(Long.toString(chat_id))
                        .text("Keyboard hidden")
                        .build();
                ReplyKeyboardRemove keyboardMarkup = new ReplyKeyboardRemove();
                keyboardMarkup.setRemoveKeyboard(true);
                message.setReplyMarkup(keyboardMarkup);
                try {
                    execute(message); // Call method to send the photo
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (message_text.equals("МЭИ")) {
                message = buildMessageForUNV(chat_id);
                message.setReplyMarkup(formInlineKey("Электроника и наноэлектроника", "Радиотехника",
                        "Биотехнические системы и технологии, приборостроение", "Электроэнергетика и электротехника ИЭТЭ", "Электроэнергетика и электротехника ИЭЭ",
                        "elnik_and_nan_MEI", "rad_MEI", "biosis_and_tex_or_instr_MEI", "elener_and_electrtx_IETE_MEI", "elener_and_electrtx_IEE_MEI"));
                try {
                    execute(message); // Sending our message object to user
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (message_text.equals("МИРЭА")) {
                message = buildMessageForUNV(chat_id);
                message.setReplyMarkup(formInlineKey("Приборостроение", "Радиотехника", "Конструирование и технология электронных средств",
                        "Лазерная техника и лазерные технологии", "Электроника и наноэлектроника", "instr_MIREA", "rad_MIREA",
                        "constr_and_tech_el_sr_MIREA", "laser_tech_and_laser_techn_MIREA", "elenik_and_nan_MIREA"));
                try {
                    execute(message); // Sending our message object to user
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (message_text.equals("Политех")) {
                message = buildMessageForUNV(chat_id);
                message.setReplyMarkup(formInlineKey("Большие и открытые данные", "Радиотехника", "Информационная безопасность",
                        "Корпоративные информационные системы", "Электроэнергетика и электротехника", "BigData_Polytech", "rad_Polytech",
                        "inf_sec_Polytech", "corp_inf_sys_Polytech", "elener_and_electrtx_Polytech"));
                try {
                    execute(message); // Sending our message object to user
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (message_text.equals("МАИ")) {
                message = buildMessageForUNV(chat_id);
                message.setReplyMarkup(formInlineKey("Приборостроение", "Радиотехника", "Нанотехнологии и микросистемная техника",
                        "Конструирование и технология электронных средств", "Электроэнергетика и электротехника", "instr_MAI", "rad_MAI",
                        "nan_and_mic_techn_MAI","constr_and_tech_el_sr_MAI", "elener_and_electrtx_MAI"));
                try {
                    execute(message); // Sending our message object to user
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        } else if (update.hasCallbackQuery()) {
            // Set variables

            String call_data = update.getCallbackQuery().getData();
            int message_id = update.getCallbackQuery().getMessage().getMessageId();
            long chat_id = update.getCallbackQuery().getMessage().getChatId();
            String answer = "";
            String spreadSheetIDMEI = "1Rf7wWtU0WeZ8E-q3_x4PYH2RPJTVvaYScBTlN1IuYUg";
            String spreadSheetIDMIREA = "1xYL2hR9NDieAUr8lyNCBe5UYXmQLUrWxvn4XocHO9S4";
            String spreadSheetIDPolytech = "14X1MaVl0b3V3wXyE6iydT2I_-OevSj70EXnbNFob2Ko";
            String spreadSheetIDMAI = "1ocNouogSe__fD_aT--aWhW8IR8ZPvkbJdPwxxepL7n8";
            switch (call_data) {
                case "elnik_and_nan_MEI" -> {
                    answer = "https://docs.google.com/spreadsheets/d/1Rf7wWtU0WeZ8E-q3_x4PYH2RPJTVvaYScBTlN1IuYUg/edit#gid=0";
                    helpToWriteToGSheets("https://www.pkmpei.ru/inform/list10bacc.html", spreadSheetIDMEI, "Электроника и наноэлектроника", "МЭИ", 240);
                }
                case "rad_MEI" -> {
                    answer = "https://docs.google.com/spreadsheets/d/1Rf7wWtU0WeZ8E-q3_x4PYH2RPJTVvaYScBTlN1IuYUg/edit#gid=601606037";
                    helpToWriteToGSheets("https://www.pkmpei.ru/inform/list19bacc.html", spreadSheetIDMEI, "Радиотехника", "МЭИ", 240);
                }
                case "biosis_and_tex_or_instr_MEI" -> {
                    answer = "https://docs.google.com/spreadsheets/d/1Rf7wWtU0WeZ8E-q3_x4PYH2RPJTVvaYScBTlN1IuYUg/edit#gid=962939804";
                    helpToWriteToGSheets("https://www.pkmpei.ru/inform/list15bacc.html", spreadSheetIDMEI, "Биотех. системы и технологии, приборостроение", "МЭИ", 240);
                }
                case "elener_and_electrtx_IETE_MEI" -> {
                    answer = "https://docs.google.com/spreadsheets/d/1Rf7wWtU0WeZ8E-q3_x4PYH2RPJTVvaYScBTlN1IuYUg/edit#gid=2028991939";
                    helpToWriteToGSheets("https://www.pkmpei.ru/inform/list11bacc.html", spreadSheetIDMEI, "Электроэнергетика и электротехника ИЭТЭ", "МЭИ", 217);
                }
                case "elener_and_electrtx_IEE_MEI" -> {
                    answer = "https://docs.google.com/spreadsheets/d/1Rf7wWtU0WeZ8E-q3_x4PYH2RPJTVvaYScBTlN1IuYUg/edit#gid=2003304044";
                    helpToWriteToGSheets("https://www.pkmpei.ru/inform/list13bacc.html", spreadSheetIDMEI, "Электроэнергетика и электротехника ИЭЭ", "МЭИ", 217);
                }
                case "rad_MIREA" -> {
                    answer = "https://docs.google.com/spreadsheets/d/1xYL2hR9NDieAUr8lyNCBe5UYXmQLUrWxvn4XocHO9S4/edit#gid=0";
                    helpToWriteToGSheets("https://priem.mirea.ru/accepted-entrants-list/personal_code_rating.php?competition=1700362536269950262",
                            spreadSheetIDMIREA, "Радиотехника", "МИРЭА", 214);
                }
                case "constr_and_tech_el_sr_MIREA" -> {
                    answer = "https://docs.google.com/spreadsheets/d/1xYL2hR9NDieAUr8lyNCBe5UYXmQLUrWxvn4XocHO9S4/edit#gid=234972622";
                    helpToWriteToGSheets("https://priem.mirea.ru/accepted-entrants-list/personal_code_rating.php?competition=1700362615293783350",
                            spreadSheetIDMIREA, "Конструирование и технология электронных средств", "МИРЭА", 237);
                }
                case "instr_MIREA" -> {
                    answer = "https://docs.google.com/spreadsheets/d/1xYL2hR9NDieAUr8lyNCBe5UYXmQLUrWxvn4XocHO9S4/edit#gid=1543141025";
                    helpToWriteToGSheets("https://priem.mirea.ru/accepted-entrants-list/personal_code_rating.php?competition=1700362791098035510",
                            spreadSheetIDMIREA, "Приборостроение", "МИРЭА", 237);
                }
                case "laser_tech_and_laser_techn_MIREA" -> {
                    answer = "https://docs.google.com/spreadsheets/d/1xYL2hR9NDieAUr8lyNCBe5UYXmQLUrWxvn4XocHO9S4/edit#gid=1010703827";
                    helpToWriteToGSheets("https://priem.mirea.ru/accepted-entrants-list/personal_code_rating.php?competition=1700362893096168758",
                            spreadSheetIDMIREA, "Лазерная техника и лазерные технологии", "МИРЭА", 214);
                }
                case "elenik_and_nan_MIREA" -> {
                    answer = "https://docs.google.com/spreadsheets/d/1xYL2hR9NDieAUr8lyNCBe5UYXmQLUrWxvn4XocHO9S4/edit#gid=1965077645";
                    helpToWriteToGSheets("https://priem.mirea.ru/accepted-entrants-list/personal_code_rating.php?competition=1700362711047646518",
                            spreadSheetIDMIREA, "Электроника и наноэлектроника", "МИРЭА", 214);
                }
                case "BigData_Polytech" -> {
                    answer = "https://docs.google.com/spreadsheets/d/14X1MaVl0b3V3wXyE6iydT2I_-OevSj70EXnbNFob2Ko/edit#gid=1810800497";
                    helpToWriteToGSheets("https://new.mospolytech.ru/postupayushchim/priem-v-universitet/rating-abiturientov/?qs=MDAwMDAwMDE3XzAxfDA5LjAzLjAzX8Hu6%2Fz46OUg6CDu8urw%2B%2FL75SDk4O3t%2B%2BV8zvft4P98wf7k5uXy7eD%2FIO7x7e7i4A%3D%3D",
                            spreadSheetIDPolytech, "Большие и открытые данные", "Политех", 240);
                }
                case "rad_Polytech" -> {
                    answer = "https://docs.google.com/spreadsheets/d/14X1MaVl0b3V3wXyE6iydT2I_-OevSj70EXnbNFob2Ko/edit#gid=0";
                    helpToWriteToGSheets("https://new.mospolytech.ru/postupayushchim/priem-v-universitet/rating-abiturientov/?qs=MDAwMDAwMDE3XzAxfDExLjAzLjAxX9Ho8fLl7Psg5ODr%2FO3l6SDx4v%2Fn6HzO9%2B3g%2F3zB%2FuTm5fLt4P8g7vHt7uLg",
                            spreadSheetIDPolytech, "Радиотехника", "Политех", 240);
                }
                case "inf_sec_Polytech" -> {
                    answer = "https://docs.google.com/spreadsheets/d/14X1MaVl0b3V3wXyE6iydT2I_-OevSj70EXnbNFob2Ko/edit#gid=1392197873";
                    helpToWriteToGSheets("https://new.mospolytech.ru/postupayushchim/priem-v-universitet/rating-abiturientov/?qs=MDAwMDAwMDE3XzAxfDEwLjAzLjAxfM737eD%2FfMH%2B5Obl8u3g%2FyDu8e3u4uA%3D",
                            spreadSheetIDPolytech, "Информационная безопасность", "Политех", 240);
                }
                case "corp_inf_sys_Polytech" -> {
                    answer = "https://docs.google.com/spreadsheets/d/14X1MaVl0b3V3wXyE6iydT2I_-OevSj70EXnbNFob2Ko/edit#gid=1413505498";
                    helpToWriteToGSheets("https://new.mospolytech.ru/postupayushchim/priem-v-universitet/rating-abiturientov/?qs=MDAwMDAwMDE3XzAxfDA5LjAzLjAzX8ru8O%2Fu8ODy6OLt%2B%2BUg6O307vDs4Pbo7u3t%2B%2BUg8ejx8uXs%2B3zO9%2B3g%2F3zB%2FuTm5fLt4P8g7vHt7uLg",
                            spreadSheetIDPolytech, "Корпоративные информационные системы", "Политех", 240);
                }
                case "elener_and_electrtx_Polytech" -> {
                    answer = "https://docs.google.com/spreadsheets/d/14X1MaVl0b3V3wXyE6iydT2I_-OevSj70EXnbNFob2Ko/edit#gid=545017919";
                    helpToWriteToGSheets("https://new.mospolytech.ru/postupayushchim/priem-v-universitet/rating-abiturientov/?qs=MDAwMDAwMDE3XzAxfDEzLjAzLjAyfM737eD%2FfMH%2B5Obl8u3g%2FyDu8e3u4uA%3D",
                            spreadSheetIDPolytech, "Электроэнергетика и электротехника", "Политех", 240);
                }
                case "instr_MAI" -> {
                    answer = "https://docs.google.com/spreadsheets/d/1ocNouogSe__fD_aT--aWhW8IR8ZPvkbJdPwxxepL7n8/edit#gid=1743277986";
                    helpToWriteToGSheets("//*[@id=\"spec_select\"]/option[13]",
                            spreadSheetIDMAI, "Приборостроение", "МАИ", 240);
                }
                case "rad_MAI" -> {
                    answer = "https://docs.google.com/spreadsheets/d/1ocNouogSe__fD_aT--aWhW8IR8ZPvkbJdPwxxepL7n8/edit#gid=55216359";
                    helpToWriteToGSheets("//*[@id=\"spec_select\"]/option[10]",
                            spreadSheetIDMAI, "Радиотехника", "МАИ", 240);
                }
                case "nan_and_mic_techn_MAI" -> {
                    answer = "https://docs.google.com/spreadsheets/d/1ocNouogSe__fD_aT--aWhW8IR8ZPvkbJdPwxxepL7n8/edit#gid=0";
                    helpToWriteToGSheets("//*[@id=\"spec_select\"]/option[29]",
                            spreadSheetIDMAI, "Нанотехнологии и микросистемная техника", "МАИ", 240);
                }
                case "constr_and_tech_el_sr_MAI" -> {
                    answer = "https://docs.google.com/spreadsheets/d/1ocNouogSe__fD_aT--aWhW8IR8ZPvkbJdPwxxepL7n8/edit#gid=530360566";
                    helpToWriteToGSheets("//*[@id=\"spec_select\"]/option[12]",
                            spreadSheetIDMAI, "Конструирование и технология электронных средств", "МАИ", 240);
                }
                case "elener_and_electrtx_MAI" -> {
                    answer = "https://docs.google.com/spreadsheets/d/1ocNouogSe__fD_aT--aWhW8IR8ZPvkbJdPwxxepL7n8/edit#gid=1946659607";
                    helpToWriteToGSheets("//*[@id=\"spec_select\"]/option[15]",
                            spreadSheetIDMAI, "Электроэнергетика и электротехника", "МАИ", 240);
                }
                default -> answer = "Ошибка";
            }

            EditMessageText new_message = EditMessageText.builder()
                    .chatId(Long.toString(chat_id))
                    .messageId(message_id)
                    .text(answer).build();
            try {
                execute(new_message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private SendMessage buildMessageForUNV(long chat_id) {
        return SendMessage.builder() // Create a message object object
                .chatId(Long.toString(chat_id))
                .text("Выберите Направление")
                .build();
    }

    private void helpToWriteToGSheets(String link, String spreadSheetID, String listName, String UNV_name, int score) {
        var users = new ArrayList<User>();
        List<List<Object>> userForGSheets = new ArrayList<>();
        switch (UNV_name) {
            case "МЭИ" -> {
                MEI mei = new MEI();
                users = mei.getAllUsers(link, score);
                users.forEach(x -> userForGSheets.add(x.formatterForGSheets()));
            }
            case "МИРЭА" -> {
                MIREA mirea = new MIREA();
                users = mirea.getAllUsers(link, score);
                users.forEach(x -> userForGSheets.add(x.formatterForGSheets()));
            }
            case "Политех" -> {
                Polytech polytech = new Polytech();
                users = polytech.getAllUsers(link, score);
                users.forEach(x -> userForGSheets.add(x.formatterForGSheets()));
            } case "МАИ" -> {
                MAI mai = new MAI();
                // link is XPath, because we access information with Selenium, by using XPath and .click() function
                users = mai.getAllUsers(link, score);
                users.forEach(x -> userForGSheets.add(x.formatterForGSheets()));
            }
        }
        try {
            GoogleSheetInteraction.writer(spreadSheetID, listName, userForGSheets);
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    private InlineKeyboardMarkup formInlineKey(String button1, String button2, String button3, String button4, String button5,
                                               String callbackData1, String callbackData2, String callbackData3, String callbackData4, String callbackData5) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline3 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline4 = new ArrayList<>();
        rowInline1.add(InlineKeyboardButton.builder().text(button1).callbackData(callbackData1).build());
        rowInline1.add(InlineKeyboardButton.builder().text(button2).callbackData(callbackData2).build());
        rowInline2.add(InlineKeyboardButton.builder().text(button3).callbackData(callbackData3).build());
        rowInline3.add(InlineKeyboardButton.builder().text(button4).callbackData(callbackData4).build());
        rowInline4.add(InlineKeyboardButton.builder().text(button5).callbackData(callbackData5).build());
        // Set the keyboard to the markup
        rowsInline.add(rowInline1);
        rowsInline.add(rowInline2);
        rowsInline.add(rowInline3);
        rowsInline.add(rowInline4);
        // Add it to the message
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    @Override
    public String getBotUsername() {
        // Return bot username
        // If bot username is @MyAmazingBot, it must return 'MyAmazingBot'
        return "tocomp.UniversityListHelperBot";
    }

    @Override
    public String getBotToken() {
        // Return bot token from BotFather
        return // enter your bot token here;
    }
}
