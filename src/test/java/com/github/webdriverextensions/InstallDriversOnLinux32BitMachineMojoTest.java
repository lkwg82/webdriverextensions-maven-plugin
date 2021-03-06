package com.github.webdriverextensions;

import static com.github.webdriverextensions.TestUtils.*;

public class InstallDriversOnLinux32BitMachineMojoTest extends AbstractInstallDriverMojoTest {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        fakePlatformToBeLinux();
        fakeBitToBe32();
    }

    public void test_that_no_configuration_downloads_the_latest_driver_for_the_current_platform() throws Exception {
        // Given
        InstallDriversMojo mojo = getMojo("src/test/resources/test-mojo-no-configuration-pom.xml", "install-drivers");
        mojo.repositoryUrl = Thread.currentThread().getContextClassLoader().getResource("repository.json");
        fakePlatformToBeLinux();
        fakeBitToBe32();

        // When
        mojo.execute();

        // Then
        assertDriverIsInstalled("chromedriver-linux-32bit", mojo.installationDirectory);
        assertDriverIsInstalled("phantomjs-linux-32bit", mojo.installationDirectory);
        assertDriverIsNotInstalled("chromedriver-linux-64bit", mojo.installationDirectory);
        assertDriverIsNotInstalled("chromedriver-mac-32bit", mojo.installationDirectory);
        assertDriverIsNotInstalled("chromedriver-windows-32bit.exe", mojo.installationDirectory);
        assertDriverIsNotInstalled("internetexplorerdriver-windows-32bit.exe", mojo.installationDirectory);
        assertDriverIsNotInstalled("internetexplorerdriver-windows-64bit.exe", mojo.installationDirectory);
    }

    public void test_that_driver_configuration_with_no_platform_downloads_the_driver_only_for_the_current_platform() throws Exception {
        // Given
        InstallDriversMojo mojo = getMojo("src/test/resources/test-mojo-no-platform-pom.xml", "install-drivers");
        mojo.repositoryUrl = Thread.currentThread().getContextClassLoader().getResource("repository.json");
        fakePlatformToBeLinux();

        // When
        mojo.execute();

        assertDriverIsInstalled("chromedriver-linux-32bit", mojo.installationDirectory);
        assertDriverIsInstalled("chromedriver-linux-64bit", mojo.installationDirectory);
        assertDriverIsInstalled("phantomjs-linux-32bit", mojo.installationDirectory);
        assertDriverIsNotInstalled("chromedriver-mac-32bit", mojo.installationDirectory);
        assertDriverIsNotInstalled("chromedriver-windows-32bit.exe", mojo.installationDirectory);
        assertDriverIsNotInstalled("internetexplorerdriver-windows-32bit.exe", mojo.installationDirectory);
        assertDriverIsNotInstalled("internetexplorerdriver-windows-64bit.exe", mojo.installationDirectory);
    }

}
