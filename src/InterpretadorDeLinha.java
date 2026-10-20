import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class InterpretadorDeLinha {

    private static final String PREFIXO_ARRAY = "array:";
    private static final String SEPARADOR_ARRAY = "\\|";

    public static void aplicar(String linha, Documento documento) {

        String[] partes = linha.split(";", -1);

        if (partes.length < 3) {
            throw new IllegalArgumentException("Use o padrão NOME_CAMPO;VALOR;TIPO;");
        }
        for (int i = 3; i < partes.length; i++) {
            if (!partes[i].trim().isEmpty()) {
                throw new IllegalArgumentException(
                        "Há ';' a mais na linha (o valor não pode conter ';').");
            }
        }

        String nome = partes[0].trim();
        String valor = partes[1];
        String tipo = partes[2].trim();

        documento.adicionarCampo(nome, converter(valor, tipo));
    }

    private static Object converter(String valor, String tipo) {
        if (tipo.toLowerCase().startsWith(PREFIXO_ARRAY)) {
            TipoCampo tipoDoElemento = descobrirTipo(tipo.substring(PREFIXO_ARRAY.length()));

            if (valor.isBlank()) {
                return new ArrayList<Object>();
            }

            return Arrays.stream(valor.split(SEPARADOR_ARRAY, -1))
                    .map(tipoDoElemento::converter)
                    .collect(Collectors.toCollection(ArrayList::new));
        }
        return descobrirTipo(tipo).converter(valor);
    }

    private static TipoCampo descobrirTipo(String texto) {
        return TipoCampo.detexto(texto).orElseThrow(() -> new IllegalArgumentException(
                "Tipo inválido: '" + texto.trim()
                        + "'. Use: texto, data, datahora, inteiro, decimal, booleano ou array:<tipo>"));
    }
}
