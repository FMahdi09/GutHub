package Guthub.Backend;

import Guthub.Backend.Categories.IntegrationTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.TestcontainersConfiguration;

@IntegrationTest
@Import(TestcontainersConfiguration.class)
@Testcontainers
public abstract class BaseIntegrationTest
{
    @Container
    @ServiceConnection
    static PostgreSQLContainer<CustomPostgreSQLContainer> postgreSQLContainer =
            CustomPostgreSQLContainer.getInstance();
}
