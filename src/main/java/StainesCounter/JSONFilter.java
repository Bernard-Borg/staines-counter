package StainesCounter;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class JSONFilter extends FileFilter {
    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String s = f.getName();

        if (s.endsWith(".json") || s.endsWith(".JSON")) {
            return true;
        }

        return false;
    }

    @Override
    public String getDescription() {
        return "JSON (.json)";
    }
}
