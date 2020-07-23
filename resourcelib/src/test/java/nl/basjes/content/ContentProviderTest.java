package nl.basjes.content;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertTrue;

public class ContentProviderTest {

    private static final Logger LOG = LoggerFactory.getLogger(ContentProviderTest.class);

    @Test
    public void testContent() {
        String content = ContentProvider.getContent();
        LOG.warn("Content= {}", content);

        // NOTE: ordering of the loaded resources is NOT guaranteed!
        assertTrue(content.contains("Hello"));
        assertTrue(content.contains("World"));
    }
}
