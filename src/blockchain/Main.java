package blockchain;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

  public static void main(String[] args)
      throws ExecutionException, InterruptedException, TimeoutException {
    int threadsCount = Runtime.getRuntime().availableProcessors();
    ExecutorService executorService = Executors.newFixedThreadPool(threadsCount);

    BlockChain blockChain = new BlockChain();

    List<Callable<Block>> collect = IntStream.range(1, threadsCount + 1)
        .mapToObj(name -> new Miner(blockChain, Integer.toString(name)))
        .collect(Collectors.toList());

    for (int i = 0; i < 5; i++) {
      Block block = executorService.invokeAny(collect, 2, TimeUnit.MINUTES);
      blockChain.increaseId();
      String handleLeadingZeroesLog = blockChain.handleLeadingZeroes(block.getGeneratedTimeSeconds());
      block.setLeadingZeroesLog(handleLeadingZeroesLog);
      block.setCreatedByMiner(Thread.currentThread().getName());
      blockChain.addBlock(block);
    }

    executorService.shutdown();
    try {
      if (!executorService.awaitTermination(1, TimeUnit.SECONDS)) {
        executorService.shutdownNow();
      }
    } catch (InterruptedException e) {
      executorService.shutdownNow();
    }

    blockChain.print();
  }
}
