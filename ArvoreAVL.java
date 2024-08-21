import java.util.Random;

class NoAVL {
    int chave, altura;
    NoAVL esquerda, direita;

    NoAVL(int d) {
        chave = d;
        altura = 1;
    }
}

class ArvoreAVL {
    NoAVL raiz;

    int altura(NoAVL N) {
        if (N == null)
            return 0;
        return N.altura;
    }

    int obterFatorBalanceamento(NoAVL N) {
        if (N == null)
            return 0;
        return altura(N.esquerda) - altura(N.direita);
    }

    NoAVL rotacaoDireita(NoAVL y) {
        NoAVL x = y.esquerda;
        NoAVL T2 = x.direita;

        x.direita = y;
        y.esquerda = T2;

        y.altura = Math.max(altura(y.esquerda), altura(y.direita)) + 1;
        x.altura = Math.max(altura(x.esquerda), altura(x.direita)) + 1;

        return x;
    }

    NoAVL rotacaoEsquerda(NoAVL x) {
        NoAVL y = x.direita;
        NoAVL T2 = y.esquerda;

        y.esquerda = x;
        x.direita = T2;

        x.altura = Math.max(altura(x.esquerda), altura(x.direita)) + 1;
        y.altura = Math.max(altura(y.esquerda), altura(y.direita)) + 1;

        return y;
    }

    NoAVL inserir(NoAVL no, int chave) {
        if (no == null)
            return (new NoAVL(chave));

        if (chave < no.chave)
            no.esquerda = inserir(no.esquerda, chave);
        else if (chave > no.chave)
            no.direita = inserir(no.direita, chave);
        else
            return no;

        no.altura = 1 + Math.max(altura(no.esquerda), altura(no.direita));

        int balanceamento = obterFatorBalanceamento(no);

        if (balanceamento > 1 && chave < no.esquerda.chave)
            return rotacaoDireita(no);

        if (balanceamento < -1 && chave > no.direita.chave)
            return rotacaoEsquerda(no);

        if (balanceamento > 1 && chave > no.esquerda.chave) {
            no.esquerda = rotacaoEsquerda(no.esquerda);
            return rotacaoDireita(no);
        }

        if (balanceamento < -1 && chave < no.direita.chave) {
            no.direita = rotacaoDireita(no.direita);
            return rotacaoEsquerda(no);
        }

        return no;
    }

    NoAVL removerNo(NoAVL raiz, int chave) {
        if (raiz == null)
            return raiz;

        if (chave < raiz.chave)
            raiz.esquerda = removerNo(raiz.esquerda, chave);
        else if (chave > raiz.chave)
            raiz.direita = removerNo(raiz.direita, chave);
        else {
            if ((raiz.esquerda == null) || (raiz.direita == null)) {
                NoAVL temp = null;
                if (temp == raiz.esquerda)
                    temp = raiz.direita;
                else
                    temp = raiz.esquerda;

                if (temp == null) {
                    temp = raiz;
                    raiz = null;
                } else
                    raiz = temp;
            } else {
                NoAVL temp = menorValorNo(raiz.direita);

                raiz.chave = temp.chave;

                raiz.direita = removerNo(raiz.direita, temp.chave);
            }
        }

        if (raiz == null)
            return raiz;

        raiz.altura = Math.max(altura(raiz.esquerda), altura(raiz.direita)) + 1;

        int balanceamento = obterFatorBalanceamento(raiz);

        if (balanceamento > 1 && obterFatorBalanceamento(raiz.esquerda) >= 0)
            return rotacaoDireita(raiz);

        if (balanceamento > 1 && obterFatorBalanceamento(raiz.esquerda) < 0) {
            raiz.esquerda = rotacaoEsquerda(raiz.esquerda);
            return rotacaoDireita(raiz);
        }

        if (balanceamento < -1 && obterFatorBalanceamento(raiz.direita) <= 0)
            return rotacaoEsquerda(raiz);

        if (balanceamento < -1 && obterFatorBalanceamento(raiz.direita) > 0) {
            raiz.direita = rotacaoDireita(raiz.direita);
            return rotacaoEsquerda(raiz);
        }

        return raiz;
    }

    NoAVL menorValorNo(NoAVL no) {
        NoAVL atual = no;
        while (atual.esquerda != null)
            atual = atual.esquerda;
        return atual;
    }

    void imprimirPreOrdem(NoAVL no) {
        if (no != null) {
            System.out.println("Nó: " + no.chave + " Fator de Balanceamento: " + obterFatorBalanceamento(no));
            imprimirPreOrdem(no.esquerda);
            imprimirPreOrdem(no.direita);
        }
    }
}

public class Main {
    public static void main(String[] args) {
        ArvoreAVL arvore = new ArvoreAVL();
        Random random = new Random();

        for (int i = 0; i < 100; i++) {
            int num = random.nextInt(1001) - 500;
            arvore.raiz = arvore.inserir(arvore.raiz, num);
        }

        System.out.println("Árvore AVL após inserções:");
        arvore.imprimirPreOrdem(arvore.raiz);

        for (int i = 0; i < 20; i++) {
            int num = random.nextInt(1001) - 500;
            arvore.raiz = arvore.removerNo(arvore.raiz, num);
        }

        System.out.println("\nÁrvore AVL após remoções:");
        arvore.imprimirPreOrdem(arvore.raiz);
    }
}
