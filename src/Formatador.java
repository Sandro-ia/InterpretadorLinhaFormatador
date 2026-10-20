import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java. util.Map;
import java.util.Objects;

@FunctionalInterface
public interface Formatador {

    String formatar(Map<String, Object> dados);

    static String textoDe(Object valor) {
        if (valor instanceof BigDecimal numero) {
            return numero.toPlainString();
        }
        if (valor instanceof LocalDateTime datahora) {
            return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(datahora);
        }
        return String.valueOf(valor);
    }
}
