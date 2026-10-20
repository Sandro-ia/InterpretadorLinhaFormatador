import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Documento {

    private static final int INDICE_MAXIMO = 999;
    private static final Pattern SEGMENTO =
            Pattern.compile("([A-Za-z_][A-Za-z0-9_]*)(?:\\[(\\d+)\\])?");

    // LinkedHashMap mantém a ordem em que os campos foram digitados
    private final Map<String, Object> raiz = new LinkedHashMap<>();

    public Map<String, Object> getRaiz() {
        return raiz;
    }

    public void adicionarCampo(String caminho, Object valor) {
        String[] segmentos = caminho.split("\\.", -1);
        Map<String, Object> atual = raiz;

        for (int i = 0; i < segmentos.length; i++) {
            Matcher matcher = SEGMENTO.matcher(segmentos[i]);
            if (!matcher.matches()) {
                throw new IllegalArgumentException("Nome de campo inválido: '" + segmentos[i]
                        + "' (use letras, números e _, começando por letra)");
            }

            String nome = matcher.group(1);
            String indiceTexto = matcher.group(2);
            boolean ultimo = (i == segmentos.length - 1);

            if (indiceTexto == null) {
                if (ultimo) {
                    atual.put(nome, valor);
                } else {
                    atual = obterOuCriarObjeto(atual, nome);
                }
            } else {
                int indice = lerIndice(indiceTexto);
                List<Object> lista = obterOuCriarLista(atual, nome);
                garantirTamanho(lista, indice);
                if (ultimo) {
                    lista.set(indice, valor);
                } else {
                    atual = obterOuCriarObjetoNaLista(lista, indice, nome);
                }
            }
        }
    }

    // ===================== Auxiliares =====================

    private int lerIndice(String texto) {
        try {
            int indice = Integer.parseInt(texto);
            if (indice > INDICE_MAXIMO) {
                throw new IllegalArgumentException("Índice muito grande (máximo " + INDICE_MAXIMO + ")");
            }
            return indice;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Índice inválido: " + texto);
        }
    }

    private void garantirTamanho(List<Object> lista, int indice) {
        while (lista.size() <= indice) {
            lista.add(null);
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> obterOuCriarObjeto(Map<String, Object> pai, String nome) {
        Object existente = pai.get(nome);
        if (existente == null) {
            Map<String, Object> novo = new LinkedHashMap<>();
            pai.put(nome, novo);
            return novo;
        }
        if (existente instanceof Map) {
            return (Map<String, Object>) existente;
        }
        throw new IllegalArgumentException("O campo '" + nome + "' já foi definido com outro tipo.");
    }

    @SuppressWarnings("unchecked")
    private List<Object> obterOuCriarLista(Map<String, Object> pai, String nome) {
        Object existente = pai.get(nome);
        if (existente == null) {
            List<Object> nova = new ArrayList<>();
            pai.put(nome, nova);
            return nova;
        }
        if (existente instanceof List) {
            return (List<Object>) existente;
        }
        throw new IllegalArgumentException("O campo '" + nome + "' já foi definido com outro tipo.");
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> obterOuCriarObjetoNaLista(List<Object> lista, int indice, String nome) {
        Object existente = lista.get(indice);
        if (existente == null) {
            Map<String, Object> novo = new LinkedHashMap<>();
            lista.set(indice, novo);
            return novo;
        }
        if (existente instanceof Map) {
            return (Map<String, Object>) existente;
        }
        throw new IllegalArgumentException(
                "A posição " + indice + " de '" + nome + "' já foi definida com outro tipo.");
    }
}

