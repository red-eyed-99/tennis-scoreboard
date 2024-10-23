package repositories;

public interface Repository<E> {

    void persist(E entity);
}
