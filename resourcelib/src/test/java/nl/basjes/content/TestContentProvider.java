package nl.basjes.content;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestContentProvider {

    private static final Logger LOG = LoggerFactory.getLogger(TestContentProvider.class);

    @Test
    public void testContent() {
        LOG.warn("Content= {}", ContentProvider.getContent());
    }
}
