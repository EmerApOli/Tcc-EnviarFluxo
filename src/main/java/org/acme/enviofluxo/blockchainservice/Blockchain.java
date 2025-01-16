package org.acme.enviofluxo.blockchainservice;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class Blockchain {
    private List<Block> chain;
    private int prefix; // Dificuldade de mineração

    public Blockchain() {
        this.prefix = 4; // Defina o nível de dificuldade desejado
        this.chain = new ArrayList<>();
        addBlock("Genesis Block");
    }

    public void addBlock(String data) {
        String previousHash = chain.isEmpty() ? "0" : chain.get(chain.size() - 1).getHash();
        Block newBlock = new Block(data, previousHash, System.currentTimeMillis());
        newBlock.mineBlock(prefix);
        chain.add(newBlock);
    }

    public List<Block> getChain() {
        return chain;
    }

    public boolean isChainValid() {
        for (int i = 1; i < chain.size(); i++) {
            Block currentBlock = chain.get(i);
            Block previousBlock = chain.get(i - 1);

            // Verifica se o hash do bloco atual está correto
            if (!currentBlock.getHash().equals(currentBlock.calculateBlockHash())) {
                return false;
            }

            // Verifica se o hash do bloco anterior está correto
            if (!currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
                return false;
            }
        }
        return true; // A cadeia é válida
    }
}
