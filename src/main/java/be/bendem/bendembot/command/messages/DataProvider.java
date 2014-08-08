package be.bendem.bendembot.command.messages;

import be.bendem.bendembot.Context;

/**
 * @author bendem
 */
public interface DataProvider<T> {

    public T provide(Context context);

}
