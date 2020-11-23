package pro.evgen.tradeapp.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TradeInfo {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("coin")
    @Expose
    private String coin;
    @SerializedName("owner")
    @Expose
    private String owner;
    @SerializedName("amount")
    @Expose
    private double amount;
    @SerializedName("otherCoin")
    @Expose
    private String otherCoin;
    @SerializedName("otherAmount")
    @Expose
    private double otherAmount;
    @SerializedName("ethAmount")
    @Expose
    private double ethAmount;
    @SerializedName("hash")
    @Expose
    private String hash;
    @SerializedName("block")
    @Expose
    private int block;
    @SerializedName("confirmed")
    @Expose
    private boolean confirmed;
    @SerializedName("lastPrice")
    @Expose
    private double lastPrice;
    @SerializedName("lastGas")
    @Expose
    private double lastGas;
    @SerializedName("blockDate")
    @Expose
    private int blockDate;
    @SerializedName("ownerCount")
    @Expose
    private int ownerCount;
    @SerializedName("psWeekApy")
    @Expose
    private Object psWeekApy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getOtherCoin() {
        return otherCoin;
    }

    public void setOtherCoin(String otherCoin) {
        this.otherCoin = otherCoin;
    }

    public double getOtherAmount() {
        return otherAmount;
    }

    public void setOtherAmount(double otherAmount) {
        this.otherAmount = otherAmount;
    }

    public double getEthAmount() {
        return ethAmount;
    }

    public void setEthAmount(double ethAmount) {
        this.ethAmount = ethAmount;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public int getBlock() {
        return block;
    }

    public void setBlock(int block) {
        this.block = block;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public double getLastGas() {
        return lastGas;
    }

    public void setLastGas(double lastGas) {
        this.lastGas = lastGas;
    }

    public int getBlockDate() {
        return blockDate;
    }

    public void setBlockDate(int blockDate) {
        this.blockDate = blockDate;
    }

    public int getOwnerCount() {
        return ownerCount;
    }

    public void setOwnerCount(int ownerCount) {
        this.ownerCount = ownerCount;
    }

    public Object getPsWeekApy() {
        return psWeekApy;
    }

    public void setPsWeekApy(Object psWeekApy) {
        this.psWeekApy = psWeekApy;
    }
}
