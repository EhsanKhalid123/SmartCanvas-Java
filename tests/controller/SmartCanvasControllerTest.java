package controller;

import javafx.scene.paint.Color;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SmartCanvasControllerTest {

    private SmartCanvasController smc;

    @Before
    public void setUp() {
        smc = new SmartCanvasController();
    }

    @Test
    public void hexConvert() {
        String hexValue = smc.hexConvert(Color.AQUA);
        Assert.assertEquals("#00FFFFFF", hexValue);
    }

}