package blockchain;

import java.util.concurrent.Callable;

public class Miner implements Callable<Block> {

  private final BlockChain blockChain;

  public Miner(BlockChain blockChain, String threadName) {
    this.blockChain = blockChain;
    Thread.currentThread().setName(threadName);
  }

  @Override
  public Block call() throws Exception {
    return blockChain.generateNewBlock();
  }
}
