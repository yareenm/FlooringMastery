package com.sal.flooringmastery.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;

@Component
public class Products {
    private String productType;
    private BigDecimal costPSF;
    private BigDecimal laborCostPSF;

    public Products(){}

    @Autowired
    public Products(String productType) {
        this.productType = productType;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public BigDecimal getCostPSF() {
        return costPSF;
    }

    public void setCostPSF(BigDecimal costPSF) {
        this.costPSF = costPSF;
    }

    public BigDecimal getLaborCostPSF() {
        return laborCostPSF;
    }

    public void setLaborCostPSF(BigDecimal laborCostPSF) {
        this.laborCostPSF = laborCostPSF;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Products products = (Products) o;
        return Objects.equals(productType, products.productType) && Objects.equals(costPSF, products.costPSF) && Objects.equals(laborCostPSF, products.laborCostPSF);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productType, costPSF, laborCostPSF);
    }

    @Override
    public String toString() {
        return "Products{" +
                "productType='" + productType + '\'' +
                ", costPSF=" + costPSF +
                ", laborCostPSF=" + laborCostPSF +
                '}';
    }
}
