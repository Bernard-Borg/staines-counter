package StainesCounter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationLoader {
    //Loads application theme from theme.json using nested objects
    public Theme loadTheme() throws LoadingException {
        final String defaultThemePath = "src/main/resources/presets/preset_1.json";
        final String themePath = "src/main/resources/configuration/theme.json";

        Theme theme;

        try {
            theme = getThemeFromFile(themePath);

            if (theme != null) {
                return theme;
            }
        } catch (NumberFormatException | JSONException e) {
            System.err.println("Invalid personal theme file");
        }

        try {
            theme = getThemeFromFile(defaultThemePath);

            if (theme != null) {
                JOptionPane.showMessageDialog(null,
                        "Your theme file was not found or was invalid, using the default instead",
                        "Error - Personal Theme File Missing", JOptionPane.WARNING_MESSAGE);

                return theme;
            } else {
                throw new LoadingException("No theme file found");
            }
        } catch (NumberFormatException e) {
            throw new LoadingException("Personal theme file and default theme file have invalid colour values");
        } catch (JSONException e) {
            throw new LoadingException("Personal theme file and default theme file are invalid/corrupt");
        }
    }

    public boolean isValidTheme(String filePath) {
        Theme theme;

        try {
            theme = getThemeFromFile(filePath);

            if (theme == null) {
                return false;
            }
        } catch (NumberFormatException | JSONException e) {
            return false;
        }

        return true;
    }

    public Theme getThemeFromFile(String filePath) throws NumberFormatException, JSONException {
        Theme loadedTheme;

        File themeFile = new File(filePath);

        if (!themeFile.exists()) {
            return null;
        }

        try (FileInputStream fis = new FileInputStream(themeFile)) {
            JSONTokener tokener = new JSONTokener(fis);
            JSONObject object = new JSONObject(tokener);

            Color buttonBack = Color.decode(object.getString("buttonBackground"));
            Color buttonFore = Color.decode(object.getString("buttonForeground"));
            Color textAreaBack = Color.decode(object.getString("textAreaBackground"));
            Color textAreaFore = Color.decode(object.getString("textAreaForeground"));
            Color background = Color.decode(object.getString("background"));

            loadedTheme = new Theme(buttonBack, buttonFore, textAreaBack, textAreaFore, background);
            return loadedTheme;
        } catch (IOException e) {
            System.err.println("IO failed"); //Log this
            return null;
        }
    }

    //Loads application phrases from phrases.json using an array of objects
    public List<Phrase> loadButtons() throws JSONException, LoadingException {
        final String phrasesPath = "src/main/resources/configuration/phrases.json";
        final File phrasesFile = new File(phrasesPath);

        List<Phrase> loadedPhrases = new ArrayList<>();

        if (!phrasesFile.exists()) {
            throw new LoadingException("Phrases.json does not exist");
        }

        try (FileInputStream fis = new FileInputStream(phrasesFile)) {
            JSONTokener tokener = new JSONTokener(fis);
            JSONObject object = new JSONObject(tokener);

            JSONArray array = object.getJSONArray("array");
            int arrayLength = array.length();

            for (int i = 0; i < arrayLength; i++) {
                JSONObject temp = array.getJSONObject(i);
                loadedPhrases.add(new Phrase(temp.getString("text"),
                        temp.getString("phrase")));
            }

            return loadedPhrases;
        } catch (IOException e) {
            System.err.println("IO failed");
            return null;
        }
    }
}
