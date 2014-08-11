package be.bendem.bendembot.usermanagement;

import org.apache.commons.lang3.Validate;

/**
 * @author bendem
 */
public class Mode {

    private boolean op;
    private boolean voiced;
    private boolean banned;
    private final String channel;

    public Mode(String channel, String modeString) {
        Validate.notNull(channel, "Channel can't be null");
        Validate.notNull(modeString, "Mode String can't be null");
        this.channel = channel;
        // TODO Parse mode string
    }

    public boolean isOp() {
        return op;
    }

    public void setOp(boolean op) {
        this.op = op;
    }

    public boolean isVoiced() {
        return voiced;
    }

    public void setVoiced(boolean voiced) {
        this.voiced = voiced;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public String getChannel() {
        return channel;
    }

}
