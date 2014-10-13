package be.bendem.bendembot.configuration;

import be.bendem.bendembot.Georges;
import fr.ribesg.alix.api.bot.config.AlixConfiguration;
import fr.ribesg.alix.api.bot.util.configuration.YamlDocument;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * @author bendem
 */
public class Configuration extends AlixConfiguration {

    public static final String DEFAULT = "CHANGEME";

    private Set<String> admins;
    private String farooKey;
    private String esperPass;
    private String twitterApiKey;
    private String twitterApiKeySecret;
    private String twitterAccessToken;
    private String twitterAccessTokenSecret;

    public Configuration(Georges bot) {
        super("georges.yml");
        this.farooKey = DEFAULT;
        this.esperPass = DEFAULT;
        this.twitterApiKey = DEFAULT;
        this.twitterApiKeySecret = DEFAULT;
        this.twitterAccessToken = DEFAULT;
        this.twitterAccessTokenSecret = DEFAULT;
        this.admins = new HashSet<>(1);
        this.admins.add("bendem");
        try {
            load(bot);
        } catch(IOException e) {
            Georges.getLogger().error(e);
        }
    }

    @Override
    protected void loadMainAdditional(final YamlDocument mainDocument) {
        this.farooKey = mainDocument.getString("farooKey");
        this.esperPass = mainDocument.getString("esperPass");
        this.twitterApiKey = mainDocument.getString("twitterApiKey");
        this.twitterApiKeySecret = mainDocument.getString("twitterApiKeySecret");
        this.twitterAccessToken = mainDocument.getString("twitterAccessToken");
        this.twitterAccessTokenSecret = mainDocument.getString("twitterAccessTokenSecret");
        this.admins.addAll(mainDocument.getStringList("admins"));
    }

    @Override
    protected void saveMainAdditional(final YamlDocument mainDocument) {
        mainDocument.set("farooKey", this.farooKey);
        mainDocument.set("esperPass", this.esperPass);
        mainDocument.set("twitterApiKey", this.twitterApiKey);
        mainDocument.set("twitterApiKeySecret", this.twitterApiKeySecret);
        mainDocument.set("twitterAccessToken", this.twitterAccessToken);
        mainDocument.set("twitterAccessTokenSecret", this.twitterAccessTokenSecret);
        mainDocument.set("admins", new LinkedList<>(this.admins));
    }

    public Set<String> getAdmins() {
        return admins;
    }

    public String getFarooKey() {
        return farooKey;
    }

    public String getEsperPass() {
        return esperPass;
    }

    public String getTwitterApiKey() {
        return twitterApiKey;
    }

    public String getTwitterApiKeySecret() {
        return twitterApiKeySecret;
    }

    public String getTwitterAccessToken() {
        return twitterAccessToken;
    }

    public String getTwitterAccessTokenSecret() {
        return twitterAccessTokenSecret;
    }
}
