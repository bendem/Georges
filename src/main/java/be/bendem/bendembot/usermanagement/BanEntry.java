package be.bendem.bendembot.usermanagement;

import be.bendem.bendembot.utils.Time;
import org.apache.commons.lang3.Validate;

/**
 * @author bendem
 */
public class BanEntry implements Comparable<BanEntry> {

    private UserEntry userEntry;
    private String reason;
    private long start;
    private long duration;

    public BanEntry(UserEntry userEntry, String reason, long start, long duration) {
        this.userEntry = userEntry;
        this.reason = reason;
        this.start = start;
        this.duration = duration;
    }

    public UserEntry getUserEntry() {
        return userEntry;
    }

    public String getReason() {
        return reason;
    }

    public long expireIn() {
        return isExpired() ? 0 : Time.now() - start + duration;
    }

    public boolean isExpired() {
        return Time.since(start) > duration;
    }

    @Override
    public int compareTo(BanEntry banEntry) {
        Validate.notNull(banEntry);

        // Expired entries are last
        if(banEntry.isExpired()) {
            return -1;
        } else if(isExpired()) {
            return 1;
        }

        // Order from shortest expiration to longest
        return ((Long) expireIn()).compareTo(banEntry.expireIn());
    }

}
