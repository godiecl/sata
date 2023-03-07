package cl.ucn.fondef.sata;

import java.util.List;

/**
 * The List Utils.
 *
 * @author Programacion Avanzada.
 */
public final class ListUtils {

    /**
     * Can't instantiate.
     */
    private ListUtils() {
        // nothing here
    }

    /**
     * Cast a List.
     *
     * @param list raw list.
     * @param <T>  to parametrize.
     * @return the List parametrized.
     */
    @SuppressWarnings({"unchecked"})
    public static <T> List<T> cast(List<?> list) {
        return (List<T>) list;
    }
}
