package com.transporteruser.bean;

import java.io.Serializable;
import java.util.Comparator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bid implements Serializable, Comparator<Bid>
{

    @SerializedName("bidId")
    @Expose
    private String bidId;
    @SerializedName("leadId")
    @Expose
    private String leadId;
    @SerializedName("transporterId")
    @Expose
    private String transporterId;
    @SerializedName("transporterName")
    @Expose
    private String transporterName;
    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName("remark")
    @Expose
    private String remark;
    @SerializedName("estimatedDate")
    @Expose
    private String estimatedDate;
    private final static long serialVersionUID = 4882899390678335241L;

    public String getBidId() {
        return bidId;
    }

    public void setBidId(String bidId) {
        this.bidId = bidId;
    }

    public String getLeadId() {
        return leadId;
    }

    public void setLeadId(String leadId) {
        this.leadId = leadId;
    }

    public String getTransporterId() {
        return transporterId;
    }

    public void setTransporterId(String transporterId) {
        this.transporterId = transporterId;
    }

    public String getTransporterName() {
        return transporterName;
    }

    public void setTransporterName(String transporterName) {
        this.transporterName = transporterName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getEstimatedDate() {
        return estimatedDate;
    }

    public void setEstimatedDate(String estimatedDate) {
        this.estimatedDate = estimatedDate;
    }

    @Override
    public int compare(Bid bid, Bid t1) {
        int rate = (int) (bid.getAmount()-t1.getAmount());
        return rate;
    }
}
