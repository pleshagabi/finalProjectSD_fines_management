package data.server.model.nullobject;

/**
 * Created by plesha on 01-Jun-18.
 */
public class NullDriver extends AbstractDriver {
    @Override
    public int getIdDriver() {
        return -1;
    }

    @Override
    public boolean isNil() {
        return true;
    }
}
