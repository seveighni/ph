package printinghouse.data;

import java.io.Serializable;
import java.math.BigDecimal;

import printinghouse.exceptions.PageSizeNotSupported;
import printinghouse.exceptions.PaperTypeNotSupported;

public class PaperStore implements Serializable {
    private String name;
    private PricingModel pricing;

    public PaperStore(String name, PricingModel pricing) {
        this.name = name;
        this.pricing = pricing;
    }

    public BigDecimal buyPaper(PaperType paperType, PageSize pageSize, int count)
            throws PaperTypeNotSupported, PageSizeNotSupported {
        if (count <= 0) {
            throw new IllegalArgumentException("Cannot buy paper. Count must be greater than 0.");
        }
        var applyDiscount = count > pricing.getOrderDiscountThreshold();
        var pricePerSheet = pricing.calculatePrizePerPage(paperType, pageSize, applyDiscount);
        return pricePerSheet.multiply(BigDecimal.valueOf(count));
    }

    public String getName() {
        return name;
    }
}
