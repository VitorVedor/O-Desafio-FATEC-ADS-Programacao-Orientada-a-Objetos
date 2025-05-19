
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        List<Propriedade> tabuleiro = carregarPropriedades("gameConfig.txt");

        Map<Comportamento, Integer> vitorias = new EnumMap<>(Comportamento.class);
        int timeouts = 0;
        int totalRodadas = 0;

        for (int i = 0; i < 300; i++) {
            Jogo jogo = new Jogo(tabuleiro);
            jogo.executar();

            vitorias.put(jogo.vencedor.comportamento, vitorias.getOrDefault(jogo.vencedor.comportamento, 0) + 1);
            if (jogo.timeout) timeouts++;
            totalRodadas += jogo.rodadas;
        }

        System.out.println("\n📊 RESULTADOS DA SIMULAÇÃO 📊");
        System.out.println("Partidas que terminaram por TIMEOUT: " + timeouts);
        System.out.printf("Média de turnos por partida: %.2f\n", totalRodadas / 300.0);

        for (Comportamento c : Comportamento.values()) {
            int v = vitorias.getOrDefault(c, 0);
            System.out.printf("Vitórias do %s: %.2f%% (%d)\n", c, (v / 300.0) * 100, v);
        }

        Comportamento melhor = Collections.max(vitorias.entrySet(), Map.Entry.comparingByValue()).getKey();
        System.out.println("Comportamento que MAIS VENCE: " + melhor);
    }

    private static List<Propriedade> carregarPropriedades(String arquivo) throws IOException {
        List<Propriedade> propriedades = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.trim().split("\s+");
                int venda = Integer.parseInt(partes[0]);
                int aluguel = Integer.parseInt(partes[1]);
                propriedades.add(new Propriedade(venda, aluguel));
            }
        }
        return propriedades;
    }
}
