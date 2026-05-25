package com.hust.soict.ict.aims.models.dto.response.order;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class OrderItemResponse {

    private String productId;
    private String productName;
    private int quantity;
    private long unitPrice;
    private long itemTotalPrice;

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public long getUnitPrice() { return unitPrice; }
    public void setUnitPrice(long unitPrice) { this.unitPrice = unitPrice; }

    public long getItemTotalPrice() { return itemTotalPrice; }
    public void setItemTotalPrice(long itemTotalPrice) { this.itemTotalPrice = itemTotalPrice; }
}