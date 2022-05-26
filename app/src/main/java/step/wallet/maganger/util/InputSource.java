package step.wallet.maganger.util;

import java.io.FileNotFoundException;
import java.io.InputStream;

public interface InputSource {
    InputStream open() throws FileNotFoundException;

    long length();
}
