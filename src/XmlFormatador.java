import java.util.List;
import java.util.Map;

public class XmlFormatador implements Formatador {

    private static final String RECUO = " ";

    @Override
    public String formatar(Map<String, Object> dados) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" ecoding=\"UTF-8\"?>\n");
        escreverElemento(sb, "dados", dados, 0);
        return sb.toString().stripTrailing();
    }

    private void escreverElemento(StringBuilder sb, String tag, Object valor, int nivel) {
        String recuo = RECUO.repeat(nivel);

        if (valor instanceof Map<?,?> mapa) {
            sb.append(recuo).append('<').append(tag).append(">\n");
            mapa.forEach((chave, filho) -> escreverElemento(sb, chave.toString(), filho, nivel + 1));
            sb.append(recuo). append("</").append(tag).append(">\n");

        } else if (valor instanceof List<?> lista) {
            sb.append(recuo).append('<').append(tag).append(">\n");
            lista.forEach(item -> escreverElemento(sb, "item", item, nivel + 1));
            sb.append(recuo).append("</").append(tag).append(">\n");

        } else if (valor == null) {
            sb.append(recuo).append('<').append(tag).append("/>\n");
            
        } else {
            sb.append(recuo).append('<').append(tag).append('>').append(escapar(Formatador.textoDe(valor))).append("</").append(tag).append(">\n");
        }
    }

    private String escapar(String texto) {
        return texto.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}
