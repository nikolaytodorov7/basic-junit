package test;

import api.annotations.Test;

import static api.asserts.Assertions.*;

public class CupTest {
    @Test
    void getLiquidType() {
        Cup c = new Cup("Orange Juice", 85.5);
        assertEquals("Orange Juice", c.getLiquidType());
    }

    @Test
    void getPercentageFull() {
        Cup c = new Cup("Orange Juice", 85.5);
        assertEquals(85.5, c.getPercentFull());
    }

    @Test
    void setLiquidType() {
        Cup c = new Cup("Orange Juice", 85.5);
        c.setLiquidType("Water");
        assertEquals("Watera", c.getLiquidType()); //failing test
    }

    @Test
    void setPercentageFull() {
        Cup c = new Cup("Orange Juice", 85.5);
        c.setPercentFull(65.5);
        assertEquals(65.5, c.getPercentFull());
    }
}