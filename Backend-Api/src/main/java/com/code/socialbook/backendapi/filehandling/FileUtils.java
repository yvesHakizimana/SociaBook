package com.code.socialbook.backendapi.filehandling;

import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class FileUtils {

    public static byte[] readFromFileLocation(String fileUrl) {
        if(StringUtils.isBlank(fileUrl))
            return null;
        try{
            Path filePath = new File(fileUrl).toPath();
            return Files.readAllBytes(filePath);

        } catch (IOException ex){
            log.warn("No file found in this path {}.", fileUrl);
        }
        return null;
    }
}
