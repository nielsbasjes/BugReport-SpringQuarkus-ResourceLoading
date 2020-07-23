package nl.basjes.content;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public final class ContentProvider {

    private static final String DEFAULT_RESOURCES = "classpath*:Content/**/*.txt";
    private static final Logger LOG               = LoggerFactory.getLogger(ContentProvider.class);

    private ContentProvider() {
    }

    public static String getContent() {
        return getContent(DEFAULT_RESOURCES);
    }

    public static String getContent(String resourcePattern) {

        StringBuilder content = new StringBuilder();

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            LOG.info("Loading resource pattern: {}", resourcePattern);
            Resource[] resourceArray = resolver.getResources(resourcePattern);

            LOG.info("Found {} entr(y/ies)", resourceArray.length);
            for (Resource resource : resourceArray) {
                LOG.info("Loading entry: {}", resource.toString());
                content.append(read(resource.getInputStream())).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    private static String read(InputStream inputStream) throws IOException {
        // https://www.baeldung.com/convert-input-stream-to-string#java
        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
            (inputStream, Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }
        return textBuilder.toString();
    }


}
