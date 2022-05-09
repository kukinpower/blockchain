package blockchain;

import java.util.ArrayList;
import java.util.List;

public class BlockChain {

  private volatile int id;
  private final List<Block> blocks;
  private volatile int leadingZeroes;

  public BlockChain() {
    this.leadingZeroes = 0;
    this.blocks = new ArrayList<>();
    this.id = 1;
  }

  public List<Block> getBlocks() {
    return blocks;
  }

  private Block getLastBlock() {
    return blocks.get(blocks.size() - 1);
  }

  public void addBlock(Block block) {
    blocks.add(block);
  }

  public Block generateNewBlock() {

    return new Block(id, blocks.isEmpty() ? "0" : getLastBlock().getHash(), leadingZeroes);
  }

  public synchronized void increaseLeadingZeroes() {
    ++leadingZeroes;
  }

  public synchronized void decreaseLeadingZeroes() {
    --leadingZeroes;
  }

  public synchronized String handleLeadingZeroes(long generatedTimeSeconds) {
    if (generatedTimeSeconds < 15) {
      increaseLeadingZeroes();
      return "N was increased to " + leadingZeroes;
    } else if (generatedTimeSeconds > 20) {
      decreaseLeadingZeroes();
      return "N was decreased by 1";
    }

    return "N stays the same";
  }

  public void print() {
    for (int i = 0; i < blocks.size(); i++) {
      System.out.print(blocks.get(i));
      if (i + 1 < blocks.size()) {
        System.out.println();
        System.out.println();
      }
    }
  }

  public synchronized void increaseId() {
    ++id;
  }
}
