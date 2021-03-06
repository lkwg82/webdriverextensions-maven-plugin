package com.github.webdriverextensions;

import static com.github.webdriverextensions.TestUtils.*;
import static com.github.webdriverextensions.Utils.isLinux;

public class InstallDriversOnMac32BitMachineMojoTest extends AbstractInstallDriverMojoTest {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        fakePlatformToBeMac();
        fakeBitToBe32();
    }

    public void test_that_no_configuration_downloads_the_latest_driver_for_the_current_platform() throws Exception {
        // Given
        InstallDriversMojo mojo = getMojo("src/test/resources/test-mojo-no-configuration-pom.xml", "install-drivers");
        mojo.repositoryUrl = Thread.currentThread().getContextClassLoader().getResource("repository.json");
        fakePlatformToBeMac();

        // When
        mojo.execute();

        // Then
        assertDriverIsInstalled("chromedriver-mac-32bit", mojo.installationDirectory);
        assertDriverIsNotInstalled("chromedriver-linux-32bit", mojo.installationDirectory);
        assertDriverIsNotInstalled("chromedriver-linux-64bit", mojo.installationDirectory);
        assertDriverIsNotInstalled("phantomjs-linux-32bit", mojo.installationDirectory);
        assertDriverIsNotInstalled("chromedriver-windows-32bit.exe", mojo.installationDirectory);
        assertDriverIsNotInstalled("internetexplorerdriver-windows-32bit.exe", mojo.installationDirectory);
        assertDriverIsNotInstalled("internetexplorerdriver-windows-64bit.exe", mojo.installationDirectory);
    }

    public void test_that_driver_configuration_with_no_platform_downloads_the_driver_only_for_the_current_platform() throws Exception {
        // Given
        InstallDriversMojo mojo = getMojo("src/test/resources/test-mojo-no-platform-pom.xml", "install-drivers");
        mojo.repositoryUrl = Thread.currentThread().getContextClassLoader().getResource("repository.json");
        fakePlatformToBeMac();

        if (isLinux()) {
            mojo.getLog().info("skipping test, because there is some special test for this platform");
            return;
        }

        // When
        mojo.execute();

        // Then
        assertDriverIsInstalled("chromedriver-mac-32bit", mojo.installationDirectory);
        assertDriverIsNotInstalled("chromedriver-linux-32bit", mojo.installationDirectory);
        assertDriverIsNotInstalled("chromedriver-linux-64bit", mojo.installationDirectory);
        assertDriverIsNotInstalled("phantomjs-linux-32bit", mojo.installationDirectory);
        assertDriverIsNotInstalled("chromedriver-windows-32bit.exe", mojo.installationDirectory);
        assertDriverIsNotInstalled("internetexplorerdriver-windows-32bit.exe", mojo.installationDirectory);
        assertDriverIsNotInstalled("internetexplorerdriver-windows-64bit.exe", mojo.installationDirectory);
    }

}
