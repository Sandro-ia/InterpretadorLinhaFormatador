
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;


public enum TipoCampo {

    TEXTO(valor -> valor),
    DATA(TipoCampo::converterData),
    DATAHORA(TipoCampo::converterDataHora),
    INTEIRO(TipoCampo::converterInteiro),
    DEECIMAL(TipoCampo::converterDecimal),
    BOOLEANO(TipoCampo::converterBooleano);

    private static final DateTimeFormatter DATA_BR =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter DATA_HORA_BR =
            DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm:[:ss]").withResolverStyle(ResolverStyle.STRICT);

    private final Function<String, Object> conversor;

    TipoCampo(Function<String, Object> conversor) {
        this.conversor = conversor;
    }


    public Object converter(String valor) {
        return conversor.apply(valor.trim());
    }

    public static Optional<TipoCampo> detexto(String texto) {
        String normalizado = texto.trim().replace("", "").replace("_", "");
        return Arrays.stream(values()).filter(tipo -> tipo.name().equalsIgnoreCase(normalizado)).findFirst();
    }

    private static LocalDate converterData(String valor) {
        try {
            return valor.contains("/") ? LocalDate.parse(valor, DATA_BR) : LocalDate.parse(valor);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Data inválida: '" + valor + "' (use aaaa-MM-dd ou dd/MM/aaaa)");
        }
    }

    private static LocalDateTime converterDataHora(String valor) {
        try {
            return valor.contains("/") ? LocalDateTime.parse(valor, DATA_HORA_BR) : LocalDateTime.parse(valor.replace(' ' , 'T'));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Data e hora inválidas: '" + valor + "' (use aaaa-MM-dd HH:mm:ss ou dd/MM/aaaa HH:mm:ss)");
        }
    }

    private static Long converterInteiro(String valor) {
        try {
            return Long.valueOf(valor);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Número inteiro inválido: '" + valor + "'");
        }
    }

    private static BigDecimal converterDecimal(String valor) {
        try {
            return new BigDecimal(valor);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Número decimal inválido: '" + valor + "' (use ponto, ex.: 10.5)");
        }
    }

    private static Boolean converterBooleano(String valor) {
        String v = valor.toLowerCase();
        if (v.equals("true") || v.equals("verdadeiro") || v.equals("sim")) {
            return Boolean.TRUE;
        }
        if (v.equals("false") || v.equals("falso") || v.equals("nao") || v.equals("não")) {
            return Boolean.FALSE;
        }
        throw new IllegalArgumentException("Booleano inválido: '" + valor + "' (use true ou false)");
    }
}
