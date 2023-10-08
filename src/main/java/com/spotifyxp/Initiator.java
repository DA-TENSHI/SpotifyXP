package com.spotifyxp;


import com.spotifyxp.api.OAuthPKCE;
import com.spotifyxp.api.Player;
import com.spotifyxp.audio.Quality;
import com.spotifyxp.factory.Factory;
import com.spotifyxp.lastfm.LastFM;
import com.spotifyxp.webController.HttpService;
import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.injector.Injector;
import com.spotifyxp.lib.libLanguage;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.logging.ConsoleLoggingModules;
import com.spotifyxp.panels.SplashPanel;
import com.spotifyxp.setup.Setup;
import com.spotifyxp.stabilizer.GlobalExceptionHandler;
import com.spotifyxp.support.LinuxSupportModule;
import com.spotifyxp.support.MacOSSupportModule;
import com.spotifyxp.support.SteamDeckSupportModule;
import com.spotifyxp.theming.ThemeLoader;
import com.spotifyxp.threading.DefThread;
import com.spotifyxp.updater.Updater;
import com.spotifyxp.api.SpotifyAPI;
import com.spotifyxp.background.BackgroundService;
import com.spotifyxp.configuration.Config;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.dialogs.LoginDialog;
import com.spotifyxp.listeners.KeyListener;
import com.spotifyxp.panels.ContentPanel;
import com.spotifyxp.utils.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Initiator {
    public static StartupTime startupTime;
    static DefThread hook = new DefThread(ContentPanel::saveCurrentState);
    public static DefThread thread = new DefThread(new Runnable() {
        @Override
        public void run() {
            while (!past) {
                int s = Integer.parseInt(startupTime.getMMSSCoded().split(":")[1]);
                if (s > 10) {
                    if(GraphicalMessage.stuck()) {
                        System.exit(0);
                    }else{
                        past = true;
                        break;
                    }
                }
            }
        }
    });
    public static boolean past = false;
    public static void main(String[] args) {
        new SplashPanel().show();
        SplashPanel.linfo.setText("Storing startup millis...");
        startupTime = new StartupTime();
        SplashPanel.linfo.setText("Parsing arguments...");
        PublicValues.argParser.parseArguments(args);
        if(PublicValues.debug) {
            PublicValues.logger.setColored(!System.getProperty("os.name").toLowerCase().contains("win"));
            PublicValues.logger.setShowTime(false);
            ConsoleLoggingModules modules = new ConsoleLoggingModules("Module");
            modules.setColored(!System.getProperty("os.name").toLowerCase().contains("win"));
            modules.setShowTime(false);
        }else{
            System.setOut(new java.io.PrintStream(new java.io.OutputStream() {
                @Override public void write(int b) {}
            }) {
                @Override public void flush() {}
                @Override public void close() {}
                @Override public void write(int b) {}
                @Override public void write(byte[] b) {}
                @Override public void write(byte[] buf, int off, int len) {}
                @Override public void print(boolean b) {}
                @Override public void print(char c) {}
                @Override public void print(int i) {}
                @Override public void print(long l) {}
                @Override public void print(float f) {}
                @Override public void print(double d) {}
                @Override public void print(char[] s) {}
                @Override public void print(String s) {}
                @Override public void print(Object obj) {}
                @Override public void println() {}
                @Override public void println(boolean x) {}
                @Override public void println(char x) {}
                @Override public void println(int x) {}
                @Override public void println(long x) {}
                @Override public void println(float x) {}
                @Override public void println(double x) {}
                @Override public void println(char[] x) {}
                @Override public void println(String x) {}
                @Override public void println(Object x) {}
                @Override public java.io.PrintStream printf(String format, Object... args) { return this; }
                @Override public java.io.PrintStream printf(java.util.Locale l, String format, Object... args) { return this; }
                @Override public java.io.PrintStream format(String format, Object... args) { return this; }
                @Override public java.io.PrintStream format(java.util.Locale l, String format, Object... args) { return this; }
                @Override public java.io.PrintStream append(CharSequence csq) { return this; }
                @Override public java.io.PrintStream append(CharSequence csq, int start, int end) { return this; }
                @Override public java.io.PrintStream append(char c) { return this; }
            });
            System.setErr(new java.io.PrintStream(new java.io.OutputStream() {
                @Override public void write(int b) {}
            }) {
                @Override public void flush() {}
                @Override public void close() {}
                @Override public void write(int b) {}
                @Override public void write(byte[] b) {}
                @Override public void write(byte[] buf, int off, int len) {}
                @Override public void print(boolean b) {}
                @Override public void print(char c) {}
                @Override public void print(int i) {}
                @Override public void print(long l) {}
                @Override public void print(float f) {}
                @Override public void print(double d) {}
                @Override public void print(char[] s) {}
                @Override public void print(String s) {}
                @Override public void print(Object obj) {}
                @Override public void println() {}
                @Override public void println(boolean x) {}
                @Override public void println(char x) {}
                @Override public void println(int x) {}
                @Override public void println(long x) {}
                @Override public void println(float x) {}
                @Override public void println(double x) {}
                @Override public void println(char[] x) {}
                @Override public void println(String x) {}
                @Override public void println(Object x) {}
                @Override public java.io.PrintStream printf(String format, Object... args) { return this; }
                @Override public java.io.PrintStream printf(java.util.Locale l, String format, Object... args) { return this; }
                @Override public java.io.PrintStream format(String format, Object... args) { return this; }
                @Override public java.io.PrintStream format(java.util.Locale l, String format, Object... args) { return this; }
                @Override public java.io.PrintStream append(CharSequence csq) { return this; }
                @Override public java.io.PrintStream append(CharSequence csq, int start, int end) { return this; }
                @Override public java.io.PrintStream append(char c) { return this; }
            });
        }
        SplashPanel.linfo.setText("Detecting operating system...");
        if(!System.getProperty("os.name").toLowerCase().contains("win")) {
            if(System.getProperty("os.name").toLowerCase().toLowerCase().contains("mac")) {
                if(!PublicValues.customSaveDir) {
                    SplashPanel.linfo.setText("Found MacOS! Applying MacOS patch...");
                    new MacOSSupportModule();
                }
            }else {
                if(System.getProperty("os.name").toLowerCase().contains("steamos")) {
                    SplashPanel.linfo.setText("Found SteamOS! Applying SteamDeck patch...");
                    if(!PublicValues.customSaveDir) {
                        new LinuxSupportModule();
                    }
                    new SteamDeckSupportModule();
                }else {
                    if(!PublicValues.customSaveDir) {
                        SplashPanel.linfo.setText("Found Linux! Applying Linux patch...");
                        new LinuxSupportModule();
                    }
                }
            }
        }
        SplashPanel.linfo.setText("Checking required folders...");
        SplashPanel.linfo.setText("Initializing config...");
        PublicValues.config = new Config();
        SplashPanel.linfo.setText("Loading Extensions...");
        new Injector().autoInject();
        SplashPanel.linfo.setText("Setting up globalexceptionhandler...");
        Thread.setDefaultUncaughtExceptionHandler(new GlobalExceptionHandler());
        SplashPanel.linfo.setText("Storing program arguments...");
        PublicValues.args = args;
        SplashPanel.linfo.setText("Init Language...");
        PublicValues.language = new libLanguage();
        PublicValues.language.setLanguageFolder("lang");
        PublicValues.language.setNoAutoFindLanguage(libLanguage.Language.getCodeFromName(PublicValues.config.get(ConfigValues.language.name)));
        SplashPanel.linfo.setText("Parsing audio quality info...");
        try {
            PublicValues.quality = Quality.valueOf(PublicValues.config.get(ConfigValues.audioquality.name));
        }catch (IllegalArgumentException exception) {
            //This should not happen but when it happens don't crash SpotifyXP
            PublicValues.quality = Quality.NORMAL;
        }
        SplashPanel.linfo.setText("Checking setup...");
        if(!PublicValues.foundSetupArgument) {
            new Setup();
            startupTime = new StartupTime();
        }
        SplashPanel.linfo.setText("Init Themes...");
        ThemeLoader loader = new ThemeLoader();
        try {
            loader.loadTheme(PublicValues.config.get(ConfigValues.theme.name));
        } catch (ThemeLoader.UnknownThemeException e) {
            ConsoleLogging.warning("Unknown Theme: '" + PublicValues.config.get(ConfigValues.theme.name) + "'! Trying to load theme differently");
            try {
                loader.tryLoadTheme(PublicValues.config.get(ConfigValues.theme.name));
            }catch (Exception e2) {
                ConsoleLogging.warning("Failed loading theme! SpotifyXP is now ugly");
            }
        }
        try {
            Files.copy(new Resources().readToInputStream("SpotifyXP-Updater.jar"), Paths.get(PublicValues.appLocation + "/SpotifyXP-Updater.jar"), StandardCopyOption.REPLACE_EXISTING);
        }catch (Exception e) {
            ConsoleLogging.Throwable(e);
            ExceptionDialog.open(e);
            Updater.disable = true; //Disabling updater
        }
        try {
            if(new File(PublicValues.appLocation, "LOCK").createNewFile()) {
                new File(PublicValues.appLocation, "LOCK").deleteOnExit();
            }
        }catch (Exception e) {
            ExceptionDialog.open(e);
            ConsoleLogging.Throwable(e);
            ConsoleLogging.warning("Couldn't create LOCK! SpotifyXP may be instable");
        }
        SplashPanel.linfo.setText("Checking login...");
        if(PublicValues.config.get(ConfigValues.username.name).isEmpty()) {
            new LoginDialog().open(); //Show login dialog if no username is set
            startupTime = new StartupTime();
        }
        SplashPanel.linfo.setText("Add shutdown hook...");
        Runtime.getRuntime().addShutdownHook(hook.getRawThread()); //Gets executed when SpotifyXP is closing
        SplashPanel.linfo.setText("Creating api...");
        thread.start();
        Factory.getSpotifyAPI();
        Player player = Factory.getPlayer();
        past = true;
        SplashPanel.linfo.setText("Creating keylistener...");
        new KeyListener().start();
        SplashPanel.linfo.setText("Create advanced api key...");
        PublicValues.elevated = Factory.getPkce();
        Factory.getUnofficialSpotifyApi();
        SplashPanel.linfo.setText("Init Last.fm");
        new LastFM();
        SplashPanel.linfo.setText("Creating contentPanel...");
        if(PublicValues.isSteamDeckMode) {
            new SteamDeckSupportModule();
        }
        ContentPanel panel = new ContentPanel(player);
        SplashPanel.linfo.setText("Starting background services...");
        new BackgroundService().start();
        SplashPanel.hide();
        Updater.UpdateInfo info = new Updater().updateAvailable();
        DefThread thread = new DefThread(new Runnable() {
            @Override
            public void run() {
                if(info.updateAvailable) {
                    String version = info.version;
                    ContentPanel.feedbackupdaterversionfield.setText(PublicValues.language.translate("ui.updater.available") + version);
                    new Updater().invoke();
                }else{
                    if(new Updater().isNightly()) {
                        ContentPanel.feedbackupdaterversionfield.setText(PublicValues.language.translate("ui.updater.nightly"));
                        ContentPanel.feedbackupdaterdownloadbutton.setVisible(false);
                    } else {
                        ContentPanel.feedbackupdaterversionfield.setText(PublicValues.language.translate("ui.updater.notavailable"));
                    }
                }
            }
        });
        thread.start();
        ConsoleLogging.info(PublicValues.language.translate("startup.info.took").replace("{}", startupTime.getMMSS()));
        SplashPanel.hide();
        panel.open();
        new HttpService();
    }
}
