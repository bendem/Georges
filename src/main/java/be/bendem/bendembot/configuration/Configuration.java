package be.bendem.bendembot.configuration;

import be.bendem.bendembot.IrcClient;
import fr.ribesg.alix.api.Server;
import fr.ribesg.alix.api.bot.config.AlixConfiguration;
import fr.ribesg.alix.api.bot.util.configuration.YamlDocument;
import fr.ribesg.alix.api.network.ssl.SSLType;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * @author bendem
 */
public class Configuration extends AlixConfiguration {

    private Set<String> admins;
    private String       farooKey;
    private String       esperPass;

    public Configuration(IrcClient bot) {
        super("georges.yml");
        this.farooKey = "CHANGEME";
        this.esperPass = "CHANGEME";
        this.admins = new HashSet<>(1);
        this.admins.add("bendem");
        try {
            load(bot);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void loadMainAdditional(final YamlDocument mainDocument) {
        this.farooKey = mainDocument.getString("farooKey");
        this.esperPass = mainDocument.getString("esperPass");
        this.admins.addAll(mainDocument.getStringList("admins"));
    }

    @Override
    protected void saveMainAdditional(final YamlDocument mainDocument) {
        mainDocument.set("farooKey", this.farooKey);
        mainDocument.set("esperPass", this.esperPass);
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

}
