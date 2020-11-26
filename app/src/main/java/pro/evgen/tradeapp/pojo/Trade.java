package pro.evgen.tradeapp.pojo;

import android.util.Log;

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
    @PrimaryKey
    @SerializedName("id")
    @Expose
    @NonNull
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
    private Double amount;
    @SerializedName("otherCoin")
    @Expose
    private String otherCoin;
    @SerializedName("otherAmount")
    @Expose
    private Double otherAmount;
    @SerializedName("ethAmount")
    @Expose
    private Double ethAmount;
    @SerializedName("hash")
    @Expose
    private String hash;
    @SerializedName("block")
    @Expose
    private Integer block;
    @SerializedName("confirmed")
    @Expose
    private Boolean confirmed;
    @SerializedName("lastPrice")
    @Expose
    private Double lastPrice;
    @SerializedName("lastGas")
    @Expose
    private Double lastGas;
    @SerializedName("blockDate")
    @Expose
    private Long blockDate;
    @SerializedName("ownerCount")
    @Expose
    private Integer ownerCount;
    @Ignore
    @SerializedName("psWeekApy")
    @Expose
    private Object psWeekApy;
    @Ignore
    @SerializedName("psIncomeUsd")
    @Expose
    private Object psIncomeUsd;

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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getOtherCoin() {
        return otherCoin;
    }

    public void setOtherCoin(String otherCoin) {
        this.otherCoin = otherCoin;
    }

    public Double getOtherAmount() {
        return otherAmount;
    }

    public void setOtherAmount(Double otherAmount) {
        this.otherAmount = otherAmount;
    }

    public Double getEthAmount() {
        return ethAmount;
    }

    public void setEthAmount(Double ethAmount) {
        this.ethAmount = ethAmount;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Integer getBlock() {
        return block;
    }

    public void setBlock(Integer block) {
        this.block = block;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    public Double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(Double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public Double getLastGas() {
        return lastGas;
    }

    public void setLastGas(Double lastGas) {
        this.lastGas = lastGas;
    }

    public Long getBlockDate() {
        return blockDate;
    }

    public void setBlockDate(Long blockDate) {
        this.blockDate = blockDate;
    }

    public Integer getOwnerCount() {
        return ownerCount;
    }

    public void setOwnerCount(Integer ownerCount) {
        this.ownerCount = ownerCount;
    }

    public Object getPsWeekApy() {
        return psWeekApy;
    }

    public void setPsWeekApy(Object psWeekApy) {
        this.psWeekApy = psWeekApy;
    }

    public Object getPsIncomeUsd() {
        return psIncomeUsd;
    }

    public void setPsIncomeUsd(Object psIncomeUsd) {
        this.psIncomeUsd = psIncomeUsd;
    }

}
