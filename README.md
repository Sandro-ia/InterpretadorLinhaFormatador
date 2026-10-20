# Conversor de Campos para JSON, XML e YAML — Java

Exercício prático desenvolvido durante o Bootcamp da [DIO](https://www.dio.me/), integrando vários recursos da linguagem Java: **Map, Generics, Enums, Optional, Streams, Lambda, StringBuilder, BigDecimal, Wrappers e a API `java.time`**.

## 📋 Descrição

O programa lê campos digitados no console, um por linha, e ao final exibe **todos eles** em três formatos: **JSON**, **XML** e **YAML**.

## ⌨️ Formato de entrada

Cada linha segue o padrão:

```
NOME_DO_CAMPO;VALOR;TIPO;
```

> `NOME_DO_CAMPO` é o nome que você escolhe para o campo (ex.: `nome`, `idade`). O `;` final é opcional. Digite `FIM` para gerar os resultados.

### Tipos aceitos

| Tipo | Exemplo de valor | Observação |
|---|---|---|
| `texto` | `Sandro` | |
| `data` | `19/10/2026` ou `2026-10-19` | Datas impossíveis (ex.: 31/02) são rejeitadas |
| `datahora` | `2026-10-19 21:30:00` ou `19/10/2026 21:30:00` | |
| `inteiro` | `30` | |
| `decimal` | `5500.75` | Use **ponto** como separador |
| `booleano` | `true` / `false` | Também aceita `sim` / `não` |
| `array:<tipo>` | `java\|lambda\|streams` | Valores separados por `\|` |

### Objetos internos e arrays de objetos

- **Objeto interno:** use ponto no nome → `endereco.rua;Rua das Flores;texto;`
- **Array de objetos:** use índice no nome → `contatos[0].tipo;celular;texto;`

## ▶️ Exemplo

**Entrada:**
```
nome;Sandro;texto;
idade;30;inteiro;
ativo;true;booleano;
cadastro;19/10/2026;data;
tags;java|lambda;array:texto;
endereco.rua;Rua das Flores;texto;
endereco.numero;123;inteiro;
contatos[0].tipo;celular;texto;
contatos[0].numero;11999990000;texto;
FIM
```

**Saída em JSON:**
```json
{
  "nome": "Sandro",
  "idade": 30,
  "ativo": true,
  "cadastro": "2026-10-19",
  "tags": ["java", "lambda"],
  "endereco": {
    "rua": "Rua das Flores",
    "numero": 123
  },
  "contatos": [
    {
      "tipo": "celular",
      "numero": "11999990000"
    }
  ]
}
```

**Saída em XML:**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<dados>
  <nome>Sandro</nome>
  <idade>30</idade>
  <ativo>true</ativo>
  <cadastro>2026-10-19</cadastro>
  <tags>
    <item>java</item>
    <item>lambda</item>
  </tags>
  <endereco>
    <rua>Rua das Flores</rua>
    <numero>123</numero>
  </endereco>
  <contatos>
    <item>
      <tipo>celular</tipo>
      <numero>11999990000</numero>
    </item>
  </contatos>
</dados>
```

**Saída em YAML:**
```yaml
nome: Sandro
idade: 30
ativo: true
cadastro: 2026-10-19
tags:
  - java
  - lambda
endereco:
  rua: Rua das Flores
  numero: 123
contatos:
  - tipo: celular
    numero: "11999990000"
```

Linhas com erro (ex.: `idade;abc;inteiro;`) são ignoradas com uma mensagem explicando o motivo, sem interromper o programa.

## 🧱 Estrutura

| Classe | Responsabilidade |
|---|---|
| `Main` | Lê as linhas do console e dispara os formatadores |
| `TipoCampo` | Enum com os tipos aceitos; cada um guarda sua Lambda de conversão |
| `InterpretadorDeLinha` | Interpreta `NOME;VALOR;TIPO;` e converte arrays com Streams |
| `Documento` | Guarda os campos numa árvore (`Map` e `List`), com objetos internos e arrays |
| `Formatador` | Interface funcional que define o contrato de formatação |
| `JsonFormatador`, `XmlFormatador`, `YamlFormatador` | Geram o texto de saída de cada formato |

## ▶️ Como executar

1. Clone o repositório:
   ```bash
   git clone https://github.com/Sandro-ia/InterpretadorLinhaFormatador.git
   ```
2. Abra o projeto no IntelliJ IDEA (Java 16 ou superior).
3. Execute a classe `Main.java` e digite os campos no console.

## 🛠️ Tecnologias

- Java 21

## ✍️ Autor

Desenvolvido por [Sandro](https://github.com/Sandro-ia) durante o Bootcamp DIO.
