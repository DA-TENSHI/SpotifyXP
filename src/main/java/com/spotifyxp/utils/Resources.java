package com.spotifyxp.utils;


import com.spotifyxp.logging.ConsoleLogging;
import org.apache.commons.io.IOUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;


public class Resources {
    public String readToString(String path) throws FileNotFoundException, Exception {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        InputStream stream = getClass().getResourceAsStream(path);
        if (stream == null) {
            ConsoleLogging.Throwable(new FileNotFoundException());
        }
        try {
            return IOUtils.toString(stream, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ConsoleLogging.Throwable(new Exception());
        return path;
    }
    public InputStream readToInputStream(String path) throws FileNotFoundException {
        if(!path.startsWith("/")) {
            path = "/" + path;
        }
        InputStream stream = getClass().getResourceAsStream(path);
        if(stream==null) {
            ConsoleLogging.Throwable(new FileNotFoundException());
        }
        return stream;
    }
}