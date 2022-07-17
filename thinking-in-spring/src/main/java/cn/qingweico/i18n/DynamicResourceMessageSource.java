package cn.qingweico.i18n;

import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.*;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zqw
 * @date 2021/12/23
 */
public class DynamicResourceMessageSource extends AbstractMessageSource implements ResourceLoaderAware {

    private static final String resourceFileName = "application.properties";
    private static final String resourcePath = "META-INF/" + resourceFileName;
    private static final String ENCODING_TYPE = "UTF-8";

    private final Properties messageProperties;
    private final Resource messagePropertiesResource;
    private final ExecutorService executorService;

    private ResourceLoader resourceLoader;

    public DynamicResourceMessageSource() {
        this.messagePropertiesResource = getMessagePropertiesResource();
        this.messageProperties = loadMessageProperties();
        executorService = Executors.newSingleThreadExecutor();
        onMessagePropertiesChanged();
    }

    private void onMessagePropertiesChanged() {
        if (messagePropertiesResource.isFile()) {
            try {
                File messagePropertiesFile = messagePropertiesResource.getFile();
                Path messagePropertiesFilePath = messagePropertiesFile.toPath();
                // 获取资源文件所在的目录
                Path dirPath = messagePropertiesFilePath.getParent();
                FileSystem fileSystem = FileSystems.getDefault();
                WatchService watchService = fileSystem.newWatchService();
                dirPath.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
                processMessagePropertiesChanged(watchService);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void processMessagePropertiesChanged(WatchService watchService) {
        executorService.submit(() -> {
            for (; ; ) {
                WatchKey watchKey = watchService.take();
                try {
                    if (watchKey.isValid()) {
                        for (WatchEvent<?> watchEvent : watchKey.pollEvents()) {
                            Watchable watchable = watchKey.watchable();
                            Path dirPath = (Path) watchable;
                            Path fileRelativePath = (Path) watchEvent.context();
                            if (resourceFileName.equals(fileRelativePath.getFileName().toString())) {
                                Path filePath = dirPath.resolve(fileRelativePath);
                                File file = filePath.toFile();
                                Properties properties = loadMessageProperties(new FileReader(file));
                                synchronized (messageProperties) {
                                    messageProperties.clear();
                                    messageProperties.putAll(properties);
                                }
                            }
                        }
                    }
                } finally {
                    if (watchKey != null) {
                        watchKey.reset();
                    }
                }
            }
        });
    }

    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        String messageFormatPattern = messageProperties.getProperty(code);
        if (StringUtils.hasText(messageFormatPattern)) {
            return new MessageFormat(messageFormatPattern, locale);
        }
        return null;
    }

    private Properties loadMessageProperties() {
        EncodedResource encodedResource = new EncodedResource(this.messagePropertiesResource, ENCODING_TYPE);
        try {
            Reader reader = encodedResource.getReader();
            return loadMessageProperties(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    private Properties loadMessageProperties(Reader reader) {
        Properties properties = new Properties();
        try {
            properties.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return properties;
    }

    private ResourceLoader getResourceLoader() {
        return this.resourceLoader != null ? this.resourceLoader : new DefaultResourceLoader();
    }

    private Resource getMessagePropertiesResource() {
        ResourceLoader resourceLoader = getResourceLoader();
        return resourceLoader.getResource(resourcePath);
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public static void main(String[] args) throws InterruptedException {
        DynamicResourceMessageSource messageSource = new DynamicResourceMessageSource();
        new Thread(() -> {
           while (true) {
               String message = messageSource.getMessage("user.city", new Object[]{}, Locale.getDefault());
               System.out.println(message);
           }
        }).start();
    }
}
