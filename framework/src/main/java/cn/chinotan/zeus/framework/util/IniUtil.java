package cn.chinotan.zeus.framework.util;

import org.ini4j.Config;
import org.ini4j.Ini;
import org.ini4j.Profile;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

/**
 * @author xingcheng
 * @date 2019-09-29
 * @since 1.3.0.RELEASE
 **/
public class IniUtil {
    public static Map<String,String> parseIni(String string) {
        Config config = new Config();
        config.setGlobalSection(true);
        config.setGlobalSectionName("");
        Ini ini = new Ini();
        ini.setConfig(config);
        try {
            ini.load(new StringReader(string));
            Profile.Section section = ini.get("");
            return section;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String,String> parseIni(String sectionName,String string) {
        Ini ini = new Ini();
        try {
            ini.load(new StringReader(string));
            Profile.Section section = ini.get(sectionName);
            return section;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
