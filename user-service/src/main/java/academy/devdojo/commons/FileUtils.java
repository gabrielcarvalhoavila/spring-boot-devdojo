package academy.devdojo.commons;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
@RequiredArgsConstructor
public class FileUtils {

    private final ResourceLoader resourceLoader;

    public String readFromFile(String filename) throws IOException {
        var file = Paths.get(filename);
        return new String(Files.readAllBytes(file));
    }
}
