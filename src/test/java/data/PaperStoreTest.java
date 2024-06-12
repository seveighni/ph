package data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import printinghouse.data.PageSize;
import printinghouse.data.PaperStore;
import printinghouse.data.PaperType;
import printinghouse.data.PricingModel;
import printinghouse.exceptions.PaperTypeNotSupported;

public class PaperStoreTest {

    @Test
    void testBuyPaper() {
        HashMap<PaperType, BigDecimal> basePrices = new HashMap<>();
        basePrices.put(PaperType.Newspaper, new BigDecimal("0.5"));
        basePrices.put(PaperType.Standard, new BigDecimal("1.0"));
        basePrices.put(PaperType.Glossy, new BigDecimal("1.5"));
        PricingModel pricingModel = new PricingModel(basePrices, new BigDecimal("0.1"), 100, new BigDecimal("0.1"));
        var store = new PaperStore("Test Store", pricingModel);

        try {
            var price = store.buyPaper(PaperType.Newspaper, PageSize.A5, 10);
            assertEquals(new BigDecimal("5.00"), price);
            price = store.buyPaper(PaperType.Newspaper, PageSize.A4, 10);
            assertEquals(new BigDecimal("5.50"), price);
            price = store.buyPaper(PaperType.Newspaper, PageSize.A3, 10);
            assertEquals(new BigDecimal("6.00"), price);
            price = store.buyPaper(PaperType.Newspaper, PageSize.A2, 10);
            assertEquals(new BigDecimal("6.50"), price);
            price = store.buyPaper(PaperType.Newspaper, PageSize.A1, 10);
            assertEquals(new BigDecimal("7.00"), price);

            price = store.buyPaper(PaperType.Standard, PageSize.A5, 10);
            assertEquals(new BigDecimal("10.00"), price);
            price = store.buyPaper(PaperType.Standard, PageSize.A4, 10);
            assertEquals(new BigDecimal("11.00"), price);
            price = store.buyPaper(PaperType.Standard, PageSize.A3, 10);
            assertEquals(new BigDecimal("12.00"), price);
            price = store.buyPaper(PaperType.Standard, PageSize.A2, 10);
            assertEquals(new BigDecimal("13.00"), price);
            price = store.buyPaper(PaperType.Standard, PageSize.A1, 10);
            assertEquals(new BigDecimal("14.00"), price);

            price = store.buyPaper(PaperType.Glossy, PageSize.A5, 10);
            assertEquals(new BigDecimal("15.00"), price);
            price = store.buyPaper(PaperType.Glossy, PageSize.A4, 10);
            assertEquals(new BigDecimal("16.50"), price);
            price = store.buyPaper(PaperType.Glossy, PageSize.A3, 10);
            assertEquals(new BigDecimal("18.00"), price);
            price = store.buyPaper(PaperType.Glossy, PageSize.A2, 10);
            assertEquals(new BigDecimal("19.50"), price);
            price = store.buyPaper(PaperType.Glossy, PageSize.A1, 10);
            assertEquals(new BigDecimal("21.00"), price);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    void testBuyPaperNotSupportedPaperType() {
        HashMap<PaperType, BigDecimal> basePrices = new HashMap<>();
        basePrices.put(PaperType.Newspaper, new BigDecimal("0.5"));
        basePrices.put(PaperType.Standard, new BigDecimal("1.0"));
        PricingModel pricingModel = new PricingModel(basePrices, new BigDecimal("0.1"), 100, new BigDecimal("0.1"));
        var store = new PaperStore("Test Store", pricingModel);

        assertThrows(PaperTypeNotSupported.class, () -> store.buyPaper(PaperType.Glossy, PageSize.A5, 10));
    }
}
