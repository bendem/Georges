package be.bendem.bendembot.usermanagement;

import fr.ribesg.alix.api.Source;
import org.apache.commons.lang3.Validate;

import java.util.Set;
import java.util.TreeSet;

/**
 * @author bendem
 */
public class UserEntry {

    private final String        host;
    private final String        name;
    private       int           warningCount;
    private       int           banCount;
    private       int           reputation;
    private final Set<BanEntry> banEntries;

    public UserEntry(Source user) {
        Validate.isTrue(user.isUser(), "Not a user");
        banEntries = new TreeSet<>();
        this.host = user.getHostName();
        this.name = user.getName();
        warningCount = 0;
        banCount = 0;
        reputation = 5;
    }

    public String getHost() {
        return host;
    }

    public String getName() {
        return name;
    }

    public int getWarningCount() {
        return warningCount;
    }

    public void setWarningCount(int warningCount) {
        this.warningCount = warningCount;
    }

    public int getBanCount() {
        return banCount;
    }

    public void setBanCount(int banCount) {
        this.banCount = banCount;
    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }

    public Set<BanEntry> getBanEntries() {
        return banEntries;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }

        UserEntry userEntry = (UserEntry) o;
        return host.equals(userEntry.host) && name.equals(userEntry.name);
    }

    @Override
    public int hashCode() {
        int result = host.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
