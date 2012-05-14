/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlgenerator;

import java.io.*;

/**
 *
 * @author Nihanth
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static final String newline = System.getProperty("line.separator");
    public static final String indent = "    ";
    public static final String[] common_package_names = {
        "frameworks_res_",
        "com_android_systemui_",
        "com_android_browser_",
        "com_android_calculator2_",
        "com_android_calendar_",
        "com_android_camera_",
        "com_android_contacts_",
        "com_android_deskclock_",
        "com_android_email_",
        "com_android_fm_",
        "com_android_inputmethod_latin_",
        "com_android_launcher_",
        "com_android_mms_",
        "com_android_music_",
        "com_android_phone_",
        "com_android_providers_calendar_",
        "com_android_providers_downloads_ui_",
        "com_android_quicksearchbox_",
        "com_android_settings_",
        "com_android_wallpaper_",
        "com_cooliris_media_",
        "com_google_android_apps_genie_geniewidget_",
        "com_google_android_carhome_",
        "com_google_android_gm_",
        "com_google_android_googlequicksearchbox_",
        "com_google_android_talk_",
        "net_cactii_flash2_"
    };

    public static void main(String[] args) {
        int pos = 0;
        if (args.length < 2) {
            out("Usage: java -jar XMLGenerator.jar <package names separated by spaces> [-f] <input directory> <output file>");
            System.exit(1);
        }
        boolean FULL = args.length > 2 &&
                args[args.length - 3].toLowerCase().equals("-f");
        int num_addn_pkgs = FULL ? args.length - 3 : args.length - 2;
        String additional_package_names[] = new String[num_addn_pkgs];
        for (; pos < num_addn_pkgs; pos++) {
            additional_package_names[pos] = args[pos];
        }
        if (FULL) pos++;
        File ipf = new File(args[pos++]);
        if (!ipf.exists()) {
            out("No such directory");
            System.exit(1);
        }
        if (!ipf.isDirectory()) {
            out("Not a directory");
            System.exit(1);
        }
        out("Generating xml items for " + ipf.getPath() + "...");
        String drawables[] = ipf.list(new FilenameFilter() {

            public boolean accept(File f, String name) {
                return name.endsWith(".png");
            }
        });
        String output = "";
        if (FULL) output += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + newline
                                +"<resource-redirections>" + newline;
        for (String main : drawables) {
            main = main.replaceAll(".png", "");
            main = main.replaceAll(".9", "");
            String redirect = main.toString();
            for (String tmp : common_package_names) {
                main = main.replaceAll(tmp, "");
            }
            for (String tmp : additional_package_names) {
                if (!tmp.endsWith("_")) tmp += "_";
                main = main.replaceAll(tmp, "");
            }
            output += indent + "<item name=\"drawable/" + main + "\">@drawable/" + redirect + "</item>" + newline;
        }
        if (FULL) output += "</resource-redirections>";
        out("Writing output file...");
        try {
            File opf = new File(args[pos++]);
            //opf.createNewFile();
            FileWriter fw = new FileWriter(opf);
            fw.write(output);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            System.out.println("Some problem occurred.... Please send the following info to nhnt11:");
            e.printStackTrace(System.err);
        }
        out("Done!");
    }

    static void out(String str) {
        System.out.println(str);
    }
}
