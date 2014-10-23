package be.bendem.bot.georges.core;

import java.util.function.Consumer;

/**
 * @author bendem
 */
public class CallBack<T> implements com.ircclouds.irc.api.Callback<T> {

    private final Consumer<T> success;
    private final Consumer<Exception> failure;

    public CallBack(Consumer<T> success, Consumer<Exception> failure) {
        this.success = success;
        this.failure = failure;
    }

    @Override
    public void onSuccess(T aObject) {
        if (success != null) {
            success.accept(aObject);
        }
    }

    @Override
    public void onFailure(Exception aExc) {
        if (failure != null) {
            failure.accept(aExc);
        }
    }

}
