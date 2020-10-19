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
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationLoader {
    //Loads application theme from theme.json using nested objects
    public Theme loadTheme() throws LoadingException {
        final String defaultThemePath = "presets/preset_1.json";
        final String myDocumentsPath = new JFileChooser().getFileSystemView().getDefaultDirectory().toString();
        final String stainesDirectoryPath = myDocumentsPath + "/Staines Counter";
        final String themePath = stainesDirectoryPath + "/theme.json";

        final File stainesFolder = new File(stainesDirectoryPath);
        final File themeFile = new File(themePath);

        if (!stainesFolder.exists()) {
            stainesFolder.mkdir();
        }

        if (!themeFile.exists()) {
             try {
                 themeFile.createNewFile();

                 InputStream is = ConfigurationLoader.class.getClassLoader().getResourceAsStream(defaultThemePath);

                 if (is == null) {
                     throw new LoadingException("Default theme file missing, unable to continue, " +
                             "please re-install the program.");
                 }

                 try {
                     Files.copy(is, themeFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                 } catch (IOException ex) {
                     throw new LoadingException("Creating temporary theme file while copying failed");
                 }
             } catch (IOException e) {
                 throw new LoadingException("Program was unable to create theme file, please restart the program");
             }
        }

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
            theme = getThemeFromJar(defaultThemePath);

            if (theme != null) {
                JOptionPane.showMessageDialog(null,
                        "Your theme file was invalid/corrupt, using the default instead",
                        "Error - Personal Theme File Missing", JOptionPane.WARNING_MESSAGE);

                return theme;
            } else {
                throw new LoadingException("No theme file found");
            }
        } catch (NumberFormatException e) {
            throw new LoadingException("Personal theme file and default theme file have invalid colour values");
        } catch (JSONException e) {
            throw new LoadingException("Personal theme file and default theme file are invalid/corrupt");
        } catch (IOException e) {
            throw new LoadingException("Creating temporary theme file failed");
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
        File themeFile = new File(filePath);

        if (!themeFile.exists()) {
            return null;
        }

        return decodeFile(themeFile);
    }

    public Theme getThemeFromJar(String filePath) throws NumberFormatException, JSONException, IOException {
        InputStream is = ConfigurationLoader.class.getClassLoader().getResourceAsStream(filePath);

        Path temp = Files.createTempFile("resource-theme", ".tmp");
        Files.copy(is, temp, StandardCopyOption.REPLACE_EXISTING);

        return decodeFile(temp.toFile());
    }

    public Theme decodeFile(File file) {
        Theme loadedTheme;

        try (FileInputStream fis = new FileInputStream(file)) {
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
        final String phrasesPath = "configuration/phrases.json";

        List<Phrase> loadedPhrases = new ArrayList<>();

        InputStream is = ConfigurationLoader.class.getClassLoader().getResourceAsStream(phrasesPath);

        if (is == null) {
            throw new LoadingException("Phrases.json does not exist");
        }

        try {
            Path temp = Files.createTempFile("resource-phrases", ".tmp");
            Files.copy(is, temp, StandardCopyOption.REPLACE_EXISTING);

            try (FileInputStream fis = new FileInputStream(temp.toFile())) {
                JSONTokener tokener = new JSONTokener(fis);
                JSONObject object = new JSONObject(tokener);

                JSONArray array = object.getJSONArray("array");
                int arrayLength = array.length();

                for (int i = 0; i < arrayLength; i++) {
                    JSONObject tempJSON = array.getJSONObject(i);
                    loadedPhrases.add(new Phrase(tempJSON.getString("text"),
                            tempJSON.getString("phrase")));
                }

                return loadedPhrases;
            } catch (IOException e) {
                System.err.println("IO failed");
                return null;
            }
        } catch (IOException e) {
            throw new LoadingException("Creating temporary phrases file while copying failed");
        }
    }
}
