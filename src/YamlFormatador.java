import java.util.List;
import java.util.Map;
import java.util.Set;

public class YamlFormatador implements Formatador {

    private static final String RECUO = "  ";
    private static final Set<String> RESERVADAS =
            Set.of("true", "false", "yes", "no", "on", "off", "null", "~", "y", "n");

    @Override
    public String formatar(Map<String, Object> dados) {
        StringBuilder sb = new StringBuilder();
        escreverMapa(sb, dados, 0);
        return sb.toString().stripTrailing();
    }

    private void escreverMapa(StringBuilder sb, Map<?, ?> mapa, int nivel) {
        String recuo = RECUO.repeat(nivel);
        for (Map.Entry<?, ?> entrada : mapa.entrySet()) {
            sb.append(recuo).append(entrada.getKey()).append(':');
            escreverValorDaChave(sb, entrada.getValue(), nivel);
        }
    }

    private void escreverValorDaChave(StringBuilder sb, Object valor, int nivel) {
        if (valor instanceof Map<?, ?> mapa) {
            if (mapa.isEmpty()) {
                sb.append(" {}\n");
            } else {
                sb.append('\n');
                escreverMapa(sb, mapa, nivel + 1);
            }
        } else if (valor instanceof List<?> lista) {
            if (lista.isEmpty()) {
                sb.append(" []\n");
            } else {
                sb.append('\n');
                escreverLista(sb, lista, nivel + 1);
            }
        } else {
            sb.append(' ').append(valorSimples(valor)).append('\n');
        }
    }

    private void escreverLista(StringBuilder sb, List<?> lista, int nivel) {
        String recuo = RECUO.repeat(nivel);
        for (Object item : lista) {
            if (item instanceof Map<?, ?> mapa && !mapa.isEmpty()) {
                // Escreve o objeto um nível abaixo e troca o recuo da 1ª linha por "- "
                StringBuilder temporario = new StringBuilder();
                escreverMapa(temporario, mapa, nivel + 1);
                sb.append(recuo).append("- ").append(temporario.substring(RECUO.length() * (nivel + 1)));
            } else {
                sb.append(recuo).append("- ").append(valorSimples(item)).append('\n');
            }
        }
    }

    private String valorSimples(Object valor) {
        if (valor == null) {
            return "null";
        }
        if (valor instanceof String texto) {
            return precisaDeAspas(texto) ? entreAspas(texto) : texto;
        }
        return Formatador.textoDe(valor);
    }

    private boolean precisaDeAspas(String texto) {
        if (texto.isEmpty() || !texto.equals(texto.strip())) {
            return true;
        }
        if ("-?:,[]{}#&*!|>'\"%@`".indexOf(texto.charAt(0)) >= 0) {
            return true;
        }
        if (texto.contains(": ") || texto.contains(" #") || texto.endsWith(":") || texto.contains("\n") || texto.contains("\t")) {
            return true;
        }
        if (RESERVADAS.contains(texto.toLowerCase())) {
            return true;
        }
        return texto.matches("[-+]?[0-9][0-9_.,eE+:-]*");
    }

    private String entreAspas(String texto) {
        return "\"" + texto.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t") + "\"";
    }
}
