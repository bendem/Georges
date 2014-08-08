package be.bendem.bendembot.command.messages;

import be.bendem.bendembot.Context;

/**
 * @author bendem
 */
public enum Data {
    UserNick((c) -> c.getUser().getName()),
    UserHost((c) -> c.getUser().getHostName()),
    UserName((c) -> c.getUser().getUserName()),
    ServerName((c) -> c.getServer().getName()),
    ServerHost((c) -> c.getServer().getUrl()),
    Channel((c) -> c.getChannel().getName()),
    ChannelCount((c) -> String.valueOf(c.getChannel().getUsers().size())),
    ChannelOpCount((c) -> String.valueOf(c.getChannel().getOps().size())),
    ChannelVoiceCount((c) -> String.valueOf(c.getChannel().getVoiced().size())),
    ChannelTopic((c) -> c.getChannel().getTopic()),
    ;

    private final DataProvider<String> provider;

    Data(DataProvider<String> provider) {
        this.provider = provider;
    }

    public String getData(Context context) {
        return provider.provide(context);
    }

}
