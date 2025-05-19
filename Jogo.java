
import java.util.*;

class Jogo {
    List<Propriedade> tabuleiro;
    List<Jogador> jogadores;
    int rodadas;
    boolean timeout;
    Jogador vencedor;

    Jogo(List<Propriedade> config) {
        tabuleiro = new ArrayList<>();
        for (Propriedade p : config)
            tabuleiro.add(new Propriedade(p.venda, p.aluguel));

        jogadores = new ArrayList<>(Arrays.asList(
            new Jogador("1", Comportamento.IMPULSIVO),
            new Jogador("2", Comportamento.EXIGENTE),
            new Jogador("3", Comportamento.CAUTO),
            new Jogador("4", Comportamento.ALEATORIO)
        ));
        Collections.shuffle(jogadores);
        rodadas = 0;
    }

    void executar() {
        int turno = 0;
        for (; rodadas < 1000; rodadas++) {
            Jogador j = jogadores.get(turno);
            if (!j.ativo) {
                turno = (turno + 1) % jogadores.size();
                continue;
            }

            j.jogar(tabuleiro);
            verificarFalencia(j);

            if (jogadores.stream().filter(x -> x.ativo).count() == 1) break;
            turno = (turno + 1) % jogadores.size();
        }

        timeout = rodadas == 1000;
        vencedor = jogadores.stream().filter(j -> j.ativo)
            .max(Comparator.comparingInt(j -> j.saldo)).orElse(jogadores.get(0));
    }

    void verificarFalencia(Jogador j) {
        if (j.saldo < 0) {
            j.ativo = false;
            for (Propriedade p : tabuleiro)
                if (p.dono == j) p.dono = null;
        }
    }
}
