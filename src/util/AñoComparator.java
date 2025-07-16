
package util;

import modelo.Vehiculo;
import java.util.Comparator;

public class AñoComparator implements Comparator<Vehiculo> {
    @Override
    public int compare(Vehiculo v1, Vehiculo v2) {
        return Integer.compare(v1.getAño(), v2.getAño());
    }
}
