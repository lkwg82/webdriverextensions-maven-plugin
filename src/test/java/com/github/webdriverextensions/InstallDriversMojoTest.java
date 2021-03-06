package com.github.webdriverextensions;

import java.io.File;
import org.apache.commons.io.FileUtils;
import org.apache.maven.execution.DefaultMavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.project.ProjectBuildingRequest;

import static com.github.webdriverextensions.TestUtils.*;
import static com.github.webdriverextensions.Utils.*;
import static org.assertj.core.api.Assertions.assertThat;

public class InstallDriversMojoTest extends AbstractMojoTestCase {
    private File installationDirectory;

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        if (installationDirectory != null) {
            FileUtils.deleteDirectory(installationDirectory);
        }
    }

    public void test_random_configuration() throws Exception {
        InstallDriversMojo mojo = getMojo("src/test/resources/test-mojo-configuration-pom.xml", "install-drivers");

        mojo.execute();
    }

    public void test_configuration_install_all_latest_drivers() throws Exception {
        InstallDriversMojo mojo = getMojo("src/test/resources/test-mojo-configuration-install-all-latest-drivers-pom.xml", "install-drivers");
        mojo.repositoryUrl = Thread.currentThread().getContextClassLoader().getResource("repository.json");

        mojo.execute();
    }

    public void test_raise_error_when_driver_was_not_found_in_configuration() throws Exception {
        InstallDriversMojo mojo = getMojo("src/test/resources/test-mojo-configuration-pom_not_found_driver.xml", "install-drivers");
        mojo.repositoryUrl = Thread.currentThread().getContextClassLoader().getResource("repository.json");

        try {
            mojo.execute();
            fail("should raise an exception");
        } catch (MojoExecutionException e) {
            assertEquals("Could not find driver: {\"name\":\"phantooomjs\",\"platform\":\"linux\",\"bit\":\"32\",\"version\":\"1.9.7\"}", e.getMessage());
        }
    }

    public void test_configuration_extract_phantom_j_s_driver_from_tar_bz_() throws Exception {
        InstallDriversMojo mojo = getMojo("src/test/resources/test-mojo-configuration-pom_phantomjs-extract.xml", "install-drivers");
        mojo.repositoryUrl = Thread.currentThread().getContextClassLoader().getResource("repository.json");

        mojo.execute();

        File[] files = installationDirectory.listFiles();
        assertThat(files).hasSize(2);
        assertThat(files[0]).isFile();
        assertThat(files[1]).isFile();
    }

    private MavenProject getMavenProject(String pomPath) throws Exception {
        File pom = new File(pomPath);
        MavenExecutionRequest request = new DefaultMavenExecutionRequest();
        request.setPom(pom);
        ProjectBuildingRequest configuration = request.getProjectBuildingRequest();
        return lookup(ProjectBuilder.class).build(pom, configuration).getProject();
    }

    private InstallDriversMojo getMojo(String pomPath, String goal) throws Exception {
        MavenProject project = getMavenProject(pomPath);
        InstallDriversMojo mojo = (InstallDriversMojo) lookupConfiguredMojo(project, goal);

        // some global test preparations
        installationDirectory = mojo.installationDirectory;
        logTestName(mojo);

        return mojo;
    }

    private static void logTestName(InstallDriversMojo mojo) {
        mojo.getLog().info("");
        mojo.getLog().info("");
        mojo.getLog().info("## TEST: " + new Exception().getStackTrace()[2].getMethodName());
    }
}
