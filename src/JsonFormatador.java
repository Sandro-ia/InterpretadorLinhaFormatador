import javax.script.SimpleScriptContext;
import javax.xml.crypto.dsig.spec.XSLTTransformParameterSpec;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonFormatador implements Formatador {

    private static final String RECUO = " ";

    @Override
    public String formatar(Map<String, Object> dados) {
        StringBuilder sb = new StringBuilder();
        escreverObjeto(sb, dados, 0);
        return sb.toString();
    }

    private void escreverValor(StringBuilder sb, Object valor, int nivel) {
        if (valor instanceof Map<?, ? > mapa) {
            escreverObjeto(sb, mapa, nivel);
        } else if (valor instanceof List<?> lista) {
            escreverLista(sb, lista, nivel);
        } else {
            sb.append(valorSimples(valor));
        }
    }

    private void escreverObjeto(StringBuilder sb, Map<?, ?> mapa, int nivel) {
        if (mapa.isEmpty()) {
            sb.append("{}");
            return;
        }
        sb.append("{\n");
        String recuoInterno = RECUO.repeat(nivel + 1);
        int contador = 0;
        for (Map.Entry<?, ?> entrada : mapa.entrySet()) {
            sb.append(recuoInterno).append('"').append(escapar(entrada.getKey().toString())).append("\": ");
            escreverValor(sb, entrada.getValue(), nivel + 1 );
            contador++;
            if (contador < mapa.size()) {
                sb.append(',');
            }
            sb.append('\n');
        }
        sb.append(RECUO.repeat(nivel)).append('}');
    }

    private void escreverLista(StringBuilder sb, List<?> lista, int nivel) {

        boolean soValoresSimples = lista.stream().noneMatch(item -> item instanceof Map || item instanceof List);

        if (soValoresSimples) {
            sb.append(lista.stream().map(this::valorSimples).collect(Collectors.joining(",","[","]")));
            return;
        }

        sb.append("[\n");
        String recuoInterno = RECUO.repeat(nivel + 1);
        for (int i = 0; i < lista.size(); i++) {
            sb.append(recuoInterno);
            escreverValor(sb, lista.get(i), nivel + 1);
            if (i < lista.size() - 1) {
                sb.append(',');
            }
            sb.append('\n');
        }
        sb.append(RECUO.repeat(nivel)).append(']');
    }

    private String valorSimples(Object valor) {
        if (valor == null) {
            return "null";
        }
        if (valor instanceof Number || valor instanceof Boolean) {
            return Formatador.textoDe(valor);
        }
        return "\"" + escapar(Formatador.textoDe(valor)) + "\"";
    }

    private String escapar(String texto) {
        StringBuilder sb = new StringBuilder();
        for (char c : texto.toCharArray()) {
            switch (c) {
                case '"' -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                default -> {
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
                }
            }
        }
        return sb.toString();
    }
}
