package guthub.backend;

import org.testcontainers.containers.PostgreSQLContainer;

public class CustomPostgreSQLContainer extends PostgreSQLContainer<CustomPostgreSQLContainer>
{
    private static final String IMAGE_VERSION = "postgres:16.0";

    private static CustomPostgreSQLContainer container;

    private CustomPostgreSQLContainer() {
        super(IMAGE_VERSION);
    }

    public static CustomPostgreSQLContainer getInstance() {
        if (container == null) {
            container = new CustomPostgreSQLContainer();
        }

        return container;
    }

    @Override
    public void stop() {
        // do nothing, JVM handles shutdown
    }
}
