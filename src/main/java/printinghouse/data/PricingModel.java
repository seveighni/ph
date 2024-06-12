package printinghouse.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;

import printinghouse.exceptions.PageSizeNotSupported;
import printinghouse.exceptions.PaperTypeNotSupported;

public class PricingModel implements Serializable {
    private HashMap<PaperType, BigDecimal> basePrices;
    private BigDecimal priceMultiplier;
    private int orderDiscountThreshold;
    private BigDecimal orderDiscountPercentage;

    public PricingModel(
            HashMap<PaperType, BigDecimal> basePrices,
            BigDecimal priceMultiplier,
            int orderDiscountThreshold,
            BigDecimal orderDiscountPercentage) {
        this.basePrices = basePrices;
        this.priceMultiplier = priceMultiplier;
        if (orderDiscountThreshold < 0) {
            throw new IllegalArgumentException("Order discount threshold must be greater than or equal to 0");
        }
        this.orderDiscountThreshold = orderDiscountThreshold;
        if (orderDiscountPercentage.compareTo(BigDecimal.ZERO) < 0 || orderDiscountPercentage.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException("Order discount percentage must be between 0 and 1");
        }
        this.orderDiscountPercentage = orderDiscountPercentage;
    }

    // calculatePrizePerPage returns the price for a single page depending on the
    // paper type and page size.
    public BigDecimal calculatePrizePerPage(PaperType paperType, PageSize pageSize, boolean applyDiscount)
            throws PaperTypeNotSupported, PageSizeNotSupported {
        if (!basePrices.containsKey(paperType)) {
            throw new PaperTypeNotSupported("Paper type is not supported by this pricing model");
        }
        var sizes = new PageSize[] { PageSize.A5, PageSize.A4, PageSize.A3, PageSize.A2, PageSize.A1 };

        if (!Arrays.stream(sizes).anyMatch(size -> size.equals(pageSize))) {
            throw new PageSizeNotSupported("Page size is not supported by this pricing model");
        }

        // get the index of pageSize in sizes array
        int index = Arrays.asList(sizes).indexOf(pageSize);
        BigDecimal additionalIncrease = BigDecimal.valueOf(index).multiply(priceMultiplier);

        BigDecimal basePrice = basePrices.get(paperType);
        var price = basePrice.multiply(additionalIncrease.add(new BigDecimal("1")));
        if (!applyDiscount) {
            return price;
        }
        return price.multiply(new BigDecimal("1").subtract(orderDiscountPercentage));
    }

    public int getOrderDiscountThreshold() {
        return orderDiscountThreshold;
    }
}
