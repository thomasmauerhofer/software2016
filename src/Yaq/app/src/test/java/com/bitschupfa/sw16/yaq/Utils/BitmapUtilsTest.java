package com.bitschupfa.sw16.yaq.Utils;

import android.graphics.Bitmap;

import com.bitschupfa.sw16.yaq.utils.BitmapUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class BitmapUtilsTest {
    @Test
    public void testEncodeDecode() throws Exception {
        Bitmap originalBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565);

        String encodedBitmap = BitmapUtils.encodeBitmap(originalBitmap);
        Bitmap decodedBitmap = BitmapUtils.decodeBitmap(encodedBitmap);

        assertEquals("Decoded image must have the same height.",
                originalBitmap.getHeight(), decodedBitmap.getHeight());
        assertEquals("Decoded image must have the same width.",
                originalBitmap.getWidth(), decodedBitmap.getWidth());

        // note that it is not possible to check if the bitmap is actually the same since the
        // encoding function compresses the original.
    }
}