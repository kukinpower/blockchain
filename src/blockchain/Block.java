package blockchain;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class Block {

  private final int id;
  private final long timeStamp;
  private final String prevHash;
  private final String hash;
  private final Duration generatedTime;
  private final long magicNumber;

  private String leadingZeroesLog = "";
  private String createdByMiner = "";

  public Block(int id, String prevHash, int leadingZeroes) {
    this.id = id;
    this.prevHash = prevHash;
    this.timeStamp = new Date().getTime();

    String generatedHash = "";
    long currentMagicNumber = 0;
    Instant start = Instant.now();
    if (leadingZeroes == 0) {
      currentMagicNumber = ThreadLocalRandom.current().nextLong();
      generatedHash = generateHash(currentMagicNumber);
    } else {
      String zeroesPrefixToCompare = "0".repeat(leadingZeroes);

      while (!isValidHash(generatedHash, zeroesPrefixToCompare)) {
        currentMagicNumber = ThreadLocalRandom.current().nextLong();
        generatedHash = generateHash(currentMagicNumber);
      }
    }
    Instant end = Instant.now();
    this.generatedTime = Duration.between(start, end);
    this.magicNumber = currentMagicNumber;
    this.hash = generatedHash;
  }

  public void setLeadingZeroesLog(String leadingZeroesLog) {
    this.leadingZeroesLog = leadingZeroesLog;
  }

  public void setCreatedByMiner(String minerName) {
    this.createdByMiner = "Created by miner # " + minerName;
  }

  public long getGeneratedTimeSeconds() {
    return generatedTime.getSeconds();
  }

  private String generateHash(long currentMagicNumber) {
    return StringUtil.applySha256(id + timeStamp + prevHash + currentMagicNumber);
  }

  private boolean isValidHash(String generatedHash, String zeroesPrefixToCompare) {
    return generatedHash.startsWith(zeroesPrefixToCompare);
  }

  @Override
  public String toString() {
    return "Block:\n"
        + createdByMiner + "\n"
        + "Balance: 100 VC\n"
        + "Id: " + id + "\n"
        + "Timestamp: " + timeStamp + "\n"
        + "Magic number: " + magicNumber + "\n"
        + "Hash of the previous block:\n"
        + prevHash + "\n"
        + "Hash of the block:\n"
        + hash + "\n"
        + "Block data:\n"
        + "No transactions\n"
        + "Block was generating for " + generatedTime.getSeconds() + " seconds\n"
        + leadingZeroesLog;
  }

  public int getId() {
    return id;
  }

  public long getTimeStamp() {
    return timeStamp;
  }

  public String getPrevHash() {
    return prevHash;
  }

  public String getHash() {
    return hash;
  }
}
