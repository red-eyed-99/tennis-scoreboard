package dao;

public interface Repository<E> {

    void save(E entity);
}
