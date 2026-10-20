import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
        Documento documento = new Documento();

        exibirInstrucoes();

        while (true) {
            System.out.print("> ");
            if (!scanner.hasNextLine()) {
                break;
            }
            String linha = scanner.nextLine().trim();

            if (linha.equalsIgnoreCase("FIM")) {
                break;
            }
            if (linha.isEmpty()) {
                continue;
            }

            try {
                InterpretadorDeLinha.aplicar(linha, documento);
            } catch (IllegalArgumentException e) {
                System.out.println("  Linha ignorada: " + e.getMessage());
            }
        }

        if (documento.getRaiz().isEmpty()) {
            System.out.println("Nenhum campo informado.");
            return;
        }

        Map<String, Formatador> formatadores = new LinkedHashMap<>();
        formatadores.put("JSON", new JsonFormatador());
        formatadores.put("XML", new XmlFormatador());
        formatadores.put("YAML", new YamlFormatador());

        formatadores.forEach((nome, formatador) -> {
            System.out.println("\n===== " + nome + " =====");
            System.out.println(formatador.formatar(documento.getRaiz()));
        });
    }

    private static void exibirInstrucoes() {
        System.out.println("=== Conversor de campos para JSON, XML e YAML ===");
        System.out.println("Digite um campo por linha no padrão: NOME_CAMPO;VALOR;TIPO;");
        System.out.println("Digite FIM para gerar os resultados.\n");
        System.out.println("Tipos: texto | data | datahora | inteiro | decimal | booleano | array:<tipo>");
        System.out.println("  data      -> aaaa-MM-dd ou dd/MM/aaaa");
        System.out.println("  datahora  -> aaaa-MM-dd HH:mm:ss ou dd/MM/aaaa HH:mm:ss");
        System.out.println("  decimal   -> use ponto (10.5)");
        System.out.println("  array     -> valores separados por |   ex.: tags;java|lambda;array:texto;");
        System.out.println("  objeto interno  -> ponto no nome      ex.: endereco.rua;Rua A;texto;");
        System.out.println("  array de objetos -> índice no nome    ex.: contatos[0].nome;Ana;texto;\n");
    }
}