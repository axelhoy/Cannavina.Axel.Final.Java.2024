package gestor;

public interface ICrud<T> {
    void crear(T item);
    java.util.List<T> leer();
    void actualizar(int index, T item);
    void eliminar(int index);
    T obtenerPorId(int id);
}
