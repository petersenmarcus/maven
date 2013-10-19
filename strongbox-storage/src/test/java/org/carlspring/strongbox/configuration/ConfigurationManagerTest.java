package org.carlspring.strongbox.configuration;

import org.carlspring.strongbox.storage.Storage;
import org.carlspring.strongbox.storage.repository.Repository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.*;

/**
 * @author mtodorov
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/META-INF/spring/strongbox-*-context.xml"})
public class ConfigurationManagerTest
{

    public static final String TEST_CLASSES = "target/test-classes";

    public static final String CONFIGURATION_BASEDIR = TEST_CLASSES + "/etc";

    public static final String CONFIGURATION_FILE = CONFIGURATION_BASEDIR + "/configuration.xml";

    public static final String CONFIGURATION_OUTPUT_FILE = CONFIGURATION_BASEDIR + "/configuration-saved-cm.xml";

    public static final String STORAGE_BASEDIR = TEST_CLASSES + "/storages/storage0";

    @Autowired
    private ConfigurationManager configurationManager;


    @Test
    public void testParseConfiguration()
            throws IOException
    {
        final Configuration configuration = configurationManager.getConfiguration();

        assertNotNull(configuration);
        assertNotNull(configuration.getStorages());

        for (String key : configuration.getStorages().keySet())
        {
            assertNotNull("Storage key was null!", key);
            assertTrue("No repositories were parsed!", !configuration.getStorages().get(key).getRepositories().isEmpty());
        }

        assertEquals("Unexpected number of storages!", 1, configuration.getStorages().size());
        assertEquals("Incorrect version!", "1.0", configuration.getVersion());
        assertEquals("Incorrect port number!", 48080, configuration.getPort());
        assertNotNull("No resolvers found!", configuration.getResolvers());
        assertEquals("Incorrect number of resolvers found!", 2, configuration.getResolvers().size());
        assertEquals("Repository should have required authentication!",
                     true,
                     configuration.getStorages().get("storage0").getRepositories().get("snapshots").isSecured());
    }

    @Test
    public void testStoreConfiguration()
            throws IOException
    {
        String resolver1 = "org.carlspring.strongbox.storage.resolvers.InMemoryLocationResolver";
        String resolver2 = "org.carlspring.strongbox.storage.resolvers.FSLocationResolver";

        Repository repository1 = new Repository("snapshots");
        Repository repository2 = new Repository("releases");

        Storage storage = new Storage();
        storage.setBasedir(STORAGE_BASEDIR);
        storage.addRepository(repository1);
        storage.addRepository(repository2);

        Configuration configuration = new Configuration();
        configuration.addStorage(storage);
        configuration.addResolver(resolver1);
        configuration.addResolver(resolver2);

        File outputFile = new File(CONFIGURATION_OUTPUT_FILE);

        ConfigurationParser parser = new ConfigurationParser();
        parser.store(configuration, outputFile.getCanonicalPath());

        assertTrue("Failed to store the produced XML!", outputFile.length() > 0);
    }

}