package be.bendem.bendembot.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * @author bendem
 */
public class EnumUtils {

    /**
     * Joins the names of the values of an enum
     *
     * @param enumType the type of the enum
     * @param c the characters used to join the values
     * @param <E> the enum to join the values from
     * @return the names of the values joined together
     */
    public static <E extends Enum<E>> String joinValues(Class<E> enumType, String c) {
        return StringUtils.join(Arrays.stream(enumType.getEnumConstants()).map(Enum::name).iterator(), c);
    }

    /**
     * Get an enum values from its name
     *
     * @param enumType the type of the enum to get the value from
     * @param name the name of the value to get
     * @param <E> the enum to get the value from
     * @return the value of the enum corresponding to the name
     */
    public static <E extends Enum<E>> E getIgnoreCase(Class<E> enumType, String name) {
        return getIgnoreCase(enumType, name, null);
    }

    /**
     * Get an enum values from its name
     *
     * @param enumType the type of the enum to get the value from
     * @param name the name of the value to get
     * @param ifEmptyOrNull the value to return if the name is null, empty or
     *     doesn't correspong to an enum value
     * @param <E> the enum to get the value from
     * @return the value of the enum corresponding to the name
     */
    public static <E extends Enum<E>> E getIgnoreCase(Class<E> enumType, String name, E ifEmptyOrNull) {
        return getIgnoreCase(enumType, name, ifEmptyOrNull, ifEmptyOrNull);
    }

    /**
     * Get an enum values from its name
     *
     * @param enumType the type of the enum to get the value from
     * @param name the name of the value to get
     * @param ifEmptyOrNull the value to return if the name is null or empty
     * @param def the value to return if there were no corresponding value
     * @param <E> the enum to get the value from
     * @return the value of the enum corresponding to the name
     */
    public static <E extends Enum<E>> E getIgnoreCase(Class<E> enumType, String name, E ifEmptyOrNull, E def) {
        if(name == null || name.isEmpty()) {
            return ifEmptyOrNull;
        }
        for(E e : enumType.getEnumConstants()) {
            if(e.name().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return def;
    }

}
