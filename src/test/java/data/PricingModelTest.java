package data;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import printinghouse.data.PageSize;
import printinghouse.data.PaperType;
import printinghouse.data.PricingModel;
import printinghouse.exceptions.PaperTypeNotSupported;

public class PricingModelTest {
    @Test
    void testCalculatePricePerPageNoDiscount() {
        HashMap<PaperType, BigDecimal> basePrices = new HashMap<>();
        basePrices.put(PaperType.Newspaper, new BigDecimal("0.5"));
        basePrices.put(PaperType.Standard, new BigDecimal("1.0"));
        basePrices.put(PaperType.Glossy, new BigDecimal("1.5"));
        PricingModel pricingModel = new PricingModel(basePrices, new BigDecimal("0.1"), 100, new BigDecimal("0.1"));

        try {
            assertEquals(new BigDecimal("0.50"), pricingModel.calculatePrizePerPage(PaperType.Newspaper, PageSize.A5, false));
            assertEquals(new BigDecimal("0.55"), pricingModel.calculatePrizePerPage(PaperType.Newspaper, PageSize.A4, false));
            assertEquals(new BigDecimal("0.60"), pricingModel.calculatePrizePerPage(PaperType.Newspaper, PageSize.A3, false));
            assertEquals(new BigDecimal("0.65"), pricingModel.calculatePrizePerPage(PaperType.Newspaper, PageSize.A2, false));
            assertEquals(new BigDecimal("0.70"), pricingModel.calculatePrizePerPage(PaperType.Newspaper, PageSize.A1, false));

            assertEquals(new BigDecimal("1.00"), pricingModel.calculatePrizePerPage(PaperType.Standard, PageSize.A5, false));
            assertEquals(new BigDecimal("1.10"), pricingModel.calculatePrizePerPage(PaperType.Standard, PageSize.A4, false));
            assertEquals(new BigDecimal("1.20"), pricingModel.calculatePrizePerPage(PaperType.Standard, PageSize.A3, false));
            assertEquals(new BigDecimal("1.30"), pricingModel.calculatePrizePerPage(PaperType.Standard, PageSize.A2, false));
            assertEquals(new BigDecimal("1.40"), pricingModel.calculatePrizePerPage(PaperType.Standard, PageSize.A1, false));

            assertEquals(new BigDecimal("1.50"), pricingModel.calculatePrizePerPage(PaperType.Glossy, PageSize.A5, false));
            assertEquals(new BigDecimal("1.65"), pricingModel.calculatePrizePerPage(PaperType.Glossy, PageSize.A4, false));
            assertEquals(new BigDecimal("1.80"), pricingModel.calculatePrizePerPage(PaperType.Glossy, PageSize.A3, false));
            assertEquals(new BigDecimal("1.95"), pricingModel.calculatePrizePerPage(PaperType.Glossy, PageSize.A2, false));
            assertEquals(new BigDecimal("2.10"), pricingModel.calculatePrizePerPage(PaperType.Glossy, PageSize.A1, false));
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    void testCalculatePricePerPageWithDiscount() {
        HashMap<PaperType, BigDecimal> basePrices = new HashMap<>();
        basePrices.put(PaperType.Newspaper, new BigDecimal("0.5"));
        basePrices.put(PaperType.Standard, new BigDecimal("1.0"));
        basePrices.put(PaperType.Glossy, new BigDecimal("1.5"));
        PricingModel pricingModel = new PricingModel(basePrices, new BigDecimal("0.1"), 100, new BigDecimal("0.20"));

        try {
            assertEquals(new BigDecimal("0.4000"), pricingModel.calculatePrizePerPage(PaperType.Newspaper, PageSize.A5, true));
            assertEquals(new BigDecimal("0.4400"), pricingModel.calculatePrizePerPage(PaperType.Newspaper, PageSize.A4, true));
            assertEquals(new BigDecimal("0.4800"), pricingModel.calculatePrizePerPage(PaperType.Newspaper, PageSize.A3, true));
            assertEquals(new BigDecimal("0.5200"), pricingModel.calculatePrizePerPage(PaperType.Newspaper, PageSize.A2, true));
            assertEquals(new BigDecimal("0.5600"), pricingModel.calculatePrizePerPage(PaperType.Newspaper, PageSize.A1, true));

            assertEquals(new BigDecimal("0.8000"), pricingModel.calculatePrizePerPage(PaperType.Standard, PageSize.A5, true));
            assertEquals(new BigDecimal("0.8800"), pricingModel.calculatePrizePerPage(PaperType.Standard, PageSize.A4, true));
            assertEquals(new BigDecimal("0.9600"), pricingModel.calculatePrizePerPage(PaperType.Standard, PageSize.A3, true));
            assertEquals(new BigDecimal("1.0400"), pricingModel.calculatePrizePerPage(PaperType.Standard, PageSize.A2, true));
            assertEquals(new BigDecimal("1.1200"), pricingModel.calculatePrizePerPage(PaperType.Standard, PageSize.A1, true));

            assertEquals(new BigDecimal("1.2000"), pricingModel.calculatePrizePerPage(PaperType.Glossy, PageSize.A5, true));
            assertEquals(new BigDecimal("1.3200"), pricingModel.calculatePrizePerPage(PaperType.Glossy, PageSize.A4, true));
            assertEquals(new BigDecimal("1.4400"), pricingModel.calculatePrizePerPage(PaperType.Glossy, PageSize.A3, true));
            assertEquals(new BigDecimal("1.5600"), pricingModel.calculatePrizePerPage(PaperType.Glossy, PageSize.A2, true));
            assertEquals(new BigDecimal("1.6800"), pricingModel.calculatePrizePerPage(PaperType.Glossy, PageSize.A1, true));
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    void testNotSupportedPageType(){ 
        HashMap<PaperType, BigDecimal> basePrices = new HashMap<>();
        basePrices.put(PaperType.Newspaper, new BigDecimal("0.5"));
        PricingModel pricingModel = new PricingModel(basePrices, new BigDecimal("0.1"), 100, new BigDecimal("0.1"));

        assertThrows(PaperTypeNotSupported.class, () -> pricingModel.calculatePrizePerPage(PaperType.Standard, PageSize.A5, false));
    }
}
