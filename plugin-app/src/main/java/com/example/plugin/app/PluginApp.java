package com.example.plugin.app;

import com.example.sdk.BinaryOperation;
import com.example.sdk.Consts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * A sample App capable of registering plugins and invoking them.
 * This is an example created to demonstrate the plugin registration
 */
public class PluginApp implements Closeable {

    private static final Logger LOG = LoggerFactory.getLogger(PluginApp.class);

    private Map<String, BinaryOperation> plugins;
    private URLClassLoader appClassLoader;

    public PluginApp() {
        this.plugins = new HashMap<>();
        // app classloader doesnt have any additional urls
        this.appClassLoader = new URLClassLoader(new URL[0]);
    }

    /**
     * Initialises the app from given plugin directory
     * @param pluginRepoPath : path to plugins directory
     * @throws Exception when an error occurs
     */
    public void init(String pluginRepoPath) throws Exception {
        LOG.info("Initialising PluginApp from {}", pluginRepoPath);
        File repo = new File(pluginRepoPath);
        // a simple filter to get all plugin files
        File[] pluginFiles = repo.listFiles(file ->
                file.getName().toLowerCase().endsWith(".jar"));
        if(pluginFiles == null || pluginFiles.length == 0) {
            LOG.error("No plugins found at {}", repo.getAbsolutePath());
            return;
        }
        LOG.info("Found {} plugins at {}", pluginFiles.length, repo.getAbsolutePath());
        for (File pluginFile : pluginFiles) {
            registerPlugin(pluginFile);
        }
    }

    /**
     * Registers a plugin which has implemented {@link com.example.sdk.BinaryOperation}
     * and offers a {@link com.example.sdk.Consts#MANIFEST_FILE} manifest file
     * containing {@link com.example.sdk.Consts#CLASSNAME} entry
     * @param pluginFile : plugin file adhering to plugin specification
     * @throws Exception when an error happens
     */
    public void registerPlugin(File pluginFile) throws Exception {
        String pluginPath = pluginFile.getAbsolutePath();
        LOG.info("Initialising a plugin from {}", pluginPath);
        // classes of plugin present in a jar
        URL[] pluginUrls = new URL[]{pluginFile.toURI().toURL()};
        // each plugin gets its own class loader inherited from app class loader
        URLClassLoader pluginClassLoader = new URLClassLoader(pluginUrls, appClassLoader);

        // get the manifest from the classLoader
        InputStream manifestStream =
                pluginClassLoader.getResourceAsStream(Consts.MANIFEST_FILE);
        if(manifestStream == null) {
            LOG.error("{} plugin is skipped because it doesnt contain valid manifest." +
                    " Looked for {} file inside plugin.",
                    pluginPath, Consts.MANIFEST_FILE);
            return;
        }
        Properties manifest = new Properties();
        manifest.load(manifestStream);
        manifestStream.close();

        // get the class name from the manifest
        if(!manifest.containsKey(Consts.CLASSNAME)){
            LOG.error("Plugin {} doesnt contain valid manifest. Looked for {} ",
                    pluginPath, Consts.CLASSNAME);
            return;
        }
        String pluginClassName = manifest.getProperty(Consts.CLASSNAME);
        LOG.info("Going to load plugin class {}", pluginClassName);
        try {
            Class<?> loadedClass = Class.forName(pluginClassName, true, pluginClassLoader);
            Class<? extends BinaryOperation> pluginClass = (Class<? extends BinaryOperation>) loadedClass;
            BinaryOperation pluginInstance = pluginClass.newInstance();
            String name = pluginInstance.getName();
            if(plugins.containsKey(name)) {
                LOG.warn("Duplicate registration for {}. Most recent one will be used", name);
            }
            plugins.put(name, pluginInstance);
            LOG.info(">>Registered plugin '{}' for '{}'", pluginClass, name);
        } catch (ClassNotFoundException e) {
            LOG.error("Error : {} , Plugin Class {} or its dependent not found",
                    pluginPath, pluginClassName);
            LOG.error(e.getMessage(), e);
        }
    }

    /**
     * Runs all the plugins with given input
     * @param number1 : demo operand 1
     * @param number2 : demo operand 2
     * @return mapping from plugin name and its result
     */
    public Map<String, Number> runAllOperations(Number number1, Number number2) {
        Map<String, Number> result = new HashMap<>(plugins.size());
        for (Map.Entry<String, BinaryOperation> entry : plugins.entrySet()) {
            String name = entry.getKey();
            BinaryOperation operation = entry.getValue();
            Number value = operation.operate(number1, number2);
            result.put(name, value);
        }
        return result;
    }


    @Override
    public void close() throws IOException {
        LOG.info("Closing the app");
        //TODO: close plugins if required
        plugins.clear();
    }

    /**
     * Invokes all the plugins and prints the result in a nice format
     * @param num1 operand 1
     * @param num2 operand 2
     */
    public void calculateAndPrint(Number num1, Number num2){
        Map<String, Number> result = runAllOperations(num1, num2);
        System.out.println("");
        for (Map.Entry<String, Number> entry : result.entrySet()) {
            System.out.printf("%s\t%s\t%s\t=\t%s\n", num1, entry.getKey(), num2, entry.getValue());
        }
    }

    public static void main( String[] args ) throws Exception {
        PluginApp app = new PluginApp();
        app.init("./plugins");
        app.calculateAndPrint(10, 50
        );
    }
}
