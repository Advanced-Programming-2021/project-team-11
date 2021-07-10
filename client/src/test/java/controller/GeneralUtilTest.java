package controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GeneralUtilTest {
    @Test
    void formatEnumNameTest() {
        Assertions.assertEquals("Warrior", GeneralUtil.formatEnumName("WARRIOR"));
        Assertions.assertEquals("Sea Serpent", GeneralUtil.formatEnumName("SEA_SERPENT"));
    }
}
