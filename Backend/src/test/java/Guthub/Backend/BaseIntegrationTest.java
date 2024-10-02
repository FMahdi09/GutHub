package Guthub.Backend;

import Guthub.Backend.Categories.IntegrationTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.TestcontainersConfiguration;

@IntegrationTest
@Import(TestcontainersConfiguration.class)
@SpringBootTest
@Testcontainers
@Sql(scripts = "/scripts/initRoles.sql")
public class BaseIntegrationTest
{
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.0");
}
