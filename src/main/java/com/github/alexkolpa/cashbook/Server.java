package com.github.alexkolpa.cashbook;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.github.alexkolpa.cashbook.db.JooqModule;
import com.github.alexkolpa.cashbook.db.LiquibaseMigrator;
import com.github.alexkolpa.cashbook.endpoints.ApiModule;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import liquibase.exception.LiquibaseException;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.plugins.guice.ModuleProcessor;
import org.jboss.resteasy.plugins.server.netty.NettyJaxrsServer;
import org.jboss.resteasy.spi.Registry;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

@Slf4j
public class Server {

	private static final String CHANGELOG = "changelog.xml";
	private static final int PORT = 8080;

	public static void main(String[] args) {
		new Server().start();
	}

	private NettyJaxrsServer nettyService;
	private Injector injector;

	private void start() {
		log.info("Starting server");
		System.setProperty("org.jboss.logging.provider", "slf4j");
		createInjector();
		try {
			migrateDb();
		}
		catch (LiquibaseException | SQLException e) {
			log.error("Unable to migrate DB", e);
			return;
		}

		Runtime.getRuntime().addShutdownHook(new Thread(this::stop, "shutdown"));
		startServer();
		log.info("Server started");
	}

	private void stop() {
		log.info("Stopping server");
		nettyService.stop();
		log.info("Server stopped");
	}

	private void createInjector() {
		String url = System.getenv().get("JDBC_URL");
		String user = System.getenv().get("JDBC_USER");
		String pass = System.getenv().get("JDBC_PASS");

		List<Module> modules = Lists.newArrayList();
		modules.add(new JooqModule(url, user, pass));
		modules.add(new ApiModule());

		injector = Guice.createInjector(modules);
	}

	private void migrateDb() throws SQLException, LiquibaseException {
		LiquibaseMigrator.Action migrate = Optional.ofNullable(System.getenv().get("MIGRATE"))
				.map(LiquibaseMigrator.Action::valueOf)
				.orElse(LiquibaseMigrator.Action.NOP);

		Set<String> dbFiles = Sets.newHashSet(CHANGELOG);
		Optional.ofNullable(System.getenv().get("FIXTURES"))
				.map(s -> s.split(","))
				.map(Sets::newHashSet)
				.ifPresent(dbFiles::addAll);

		injector.getInstance(LiquibaseMigrator.class).migrate(migrate, dbFiles);
	}

	private void startServer() {
		ResteasyDeployment deployment = new ResteasyDeployment();

		nettyService = new NettyJaxrsServer();
		nettyService.setDeployment(deployment);
		nettyService.setPort(PORT);

		nettyService.start();
		ResteasyProviderFactory providerFactory = deployment.getProviderFactory();
		Registry registry = deployment.getRegistry();
		ModuleProcessor processor = new ModuleProcessor(registry, providerFactory);
		processor.processInjector(injector);
	}
}
