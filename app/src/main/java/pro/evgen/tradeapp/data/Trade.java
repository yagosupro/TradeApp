package pro.evgen.tradeapp.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

@Entity(tableName = "info")
public class Trade {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "type",
            "coin",
            "amount",
            "otherCoin",
            "otherAmount",
            "ethAmount",
            "hash",
            "block",
            "confirmed",
            "lastPrice",
            "lastGas",
            "blockDate"
    })
//    @PrimaryKey(autoGenerate = true)
//    private int id;

    @JsonProperty("type")
    private String type;

    @JsonProperty("coin")
    private String coin;
    @JsonProperty("amount")
    private double amount;
    @JsonProperty("otherCoin")
    private String otherCoin;
    @JsonProperty("otherAmount")
    private double otherAmount;
    @JsonProperty("ethAmount")
    private double ethAmount;
    @PrimaryKey
    @JsonProperty("hash")
    @NonNull
    private String hash;
    @JsonProperty("block")
    private int block;
    @JsonProperty("confirmed")
    private boolean confirmed;
    @JsonProperty("lastPrice")
    private double lastPrice;
    @JsonProperty("lastGas")
    private double lastGas;
    @JsonProperty("blockDate")
    private int blockDate;
    @JsonIgnore
    @Ignore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

//    @JsonIgnore
//    public int getId() {
//        return id;
//    }
//
//    @JsonIgnore
//    public void setId(int id) {
//        this.id = id;
//    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("coin")
    public String getCoin() {
        return coin;
    }

    @JsonProperty("coin")
    public void setCoin(String coin) {
        this.coin = coin;
    }

    @JsonProperty("amount")
    public double getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(double amount) {
        this.amount = amount;
    }

    @JsonProperty("otherCoin")
    public String getOtherCoin() {
        return otherCoin;
    }

    @JsonProperty("otherCoin")
    public void setOtherCoin(String otherCoin) {
        this.otherCoin = otherCoin;
    }

    @JsonProperty("otherAmount")
    public double getOtherAmount() {
        return otherAmount;
    }

    @JsonProperty("otherAmount")
    public void setOtherAmount(double otherAmount) {
        this.otherAmount = otherAmount;
    }

    @JsonProperty("ethAmount")
    public double getEthAmount() {
        return ethAmount;
    }

    @JsonProperty("ethAmount")
    public void setEthAmount(double ethAmount) {
        this.ethAmount = ethAmount;
    }

    @JsonProperty("hash")
    public String getHash() {
        return hash;
    }

    @JsonProperty("hash")
    public void setHash(String hash) {
        this.hash = hash;
    }

    @JsonProperty("block")
    public int getBlock() {
        return block;
    }

    @JsonProperty("block")
    public void setBlock(int block) {
        this.block = block;
    }

    @JsonProperty("confirmed")
    public boolean isConfirmed() {
        return confirmed;
    }

    @JsonProperty("confirmed")
    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    @JsonProperty("lastPrice")
    public double getLastPrice() {
        return lastPrice;
    }

    @JsonProperty("lastPrice")
    public void setLastPrice(double lastPrice) {
        this.lastPrice = lastPrice;
    }

    @JsonProperty("lastGas")
    public double getLastGas() {
        return lastGas;
    }

    @JsonProperty("lastGas")
    public void setLastGas(double lastGas) {
        this.lastGas = lastGas;
    }

    @JsonProperty("blockDate")
    public int getBlockDate() {
        return blockDate;
    }

    @JsonProperty("blockDate")
    public void setBlockDate(int blockDate) {
        this.blockDate = blockDate;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
