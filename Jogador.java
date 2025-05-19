
import java.util.*;

class Jogador {
    String nome;
    Comportamento comportamento;
    int saldo = 300;
    int posicao = 0;
    boolean ativo = true;
    Random random = new Random();

    Jogador(String nome, Comportamento comportamento) {
        this.nome = nome;
        this.comportamento = comportamento;
    }

    void jogar(List<Propriedade> tabuleiro) {
        int dado = random.nextInt(6) + 1;
        posicao = (posicao + dado) % tabuleiro.size();
        if (posicao + dado >= tabuleiro.size()) saldo += 100;

        Propriedade p = tabuleiro.get(posicao);

        if (p.dono == null) {
            if (deveComprar(p) && saldo >= p.venda) {
                saldo -= p.venda;
                p.dono = this;
            }
        } else if (p.dono != this) {
            saldo -= p.aluguel;
            p.dono.saldo += p.aluguel;
        }
    }

    boolean deveComprar(Propriedade p) {
    if (saldo < p.venda || p.temDono()) return false;

    return switch (comportamento) {
        case IMPULSIVO -> true;
        case EXIGENTE -> p.aluguel > 50;
        case CAUTELOSO -> (saldo - p.venda) >= 80;
        case ALEATORIO -> new Random().nextBoolean();
    };
}
