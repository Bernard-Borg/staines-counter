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
    public Theme loadTheme() throws JSONException, LoadingException, InvalidColorException {
        final String defaultThemePath = "src/main/presets/preset_1.json";
        final String themePath = "src/main/theme.json";

        Theme loadedTheme;
        File themeFile = new File(themePath);

        if (!themeFile.exists()) {
            themeFile = new File(defaultThemePath);

            JOptionPane.showMessageDialog(null,
                    "Your theme file was not found, using the default instead",
                    "Error - Personal Theme File Missing", JOptionPane.WARNING_MESSAGE);

            if (!themeFile.exists()) {
                throw new LoadingException("No theme file found!");
            }
        }

        try (FileInputStream fis = new FileInputStream(themeFile)) {
            JSONTokener tokener = new JSONTokener(fis);
            JSONObject object = new JSONObject(tokener);

            JSONObject buttonBackgroundJSON = object.getJSONObject("buttonBackground");
            JSONObject buttonForegroundJSON = object.getJSONObject("buttonForeground");
            JSONObject textAreaBackgroundJSON = object.getJSONObject("textAreaBackground");
            JSONObject textAreaForegroundJSON = object.getJSONObject("textAreaForeground");
            JSONObject backgroundJSON = object.getJSONObject("background");

            Color buttonBack = validateColor(buttonBackgroundJSON.getInt("r"),
                    buttonBackgroundJSON.getInt("g"),
                    buttonBackgroundJSON.getInt("b"));

            Color buttonFore = validateColor(buttonForegroundJSON.getInt("r"),
                    buttonForegroundJSON.getInt("g"),
                    buttonForegroundJSON.getInt("b"));

            Color textAreaBack = validateColor(textAreaBackgroundJSON.getInt("r"),
                    textAreaBackgroundJSON.getInt("g"),
                    textAreaBackgroundJSON.getInt("b"));

            Color textAreaFore = validateColor(textAreaForegroundJSON.getInt("r"),
                    textAreaForegroundJSON.getInt("g"),
                    textAreaForegroundJSON.getInt("b"));

            Color background = validateColor(backgroundJSON.getInt("r"),
                    backgroundJSON.getInt("g"),
                    backgroundJSON.getInt("b"));

            loadedTheme = new Theme(buttonBack, buttonFore, textAreaBack, textAreaFore, background);
            return loadedTheme;
        } catch (IOException e) {
            System.err.println("IO failed");
            return null;
        }
    }

    public Color validateColor(int r, int g, int b) throws InvalidColorException {
        if (r < 0 || g < 0 || b < 0 || r > 255 || g > 255 || b > 255) {
            throw new InvalidColorException();
        } else {
            return new Color(r, g, b);
        }
    }

    //Loads application phrases from phrases.json using an array of objects
    public List<Phrase> loadButtons() throws JSONException, LoadingException {
        final String phrasesPath = "src/main/phrases.json";
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
