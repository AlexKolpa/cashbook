package com.github.alexkolpa.cashbook;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.github.alexkolpa.cashbook.db.DBMigrator;
import com.github.alexkolpa.cashbook.db.JooqModule;
import com.github.alexkolpa.cashbook.endpoints.ApiModule;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.plugins.guice.ModuleProcessor;
import org.jboss.resteasy.plugins.server.netty.NettyJaxrsServer;
import org.jboss.resteasy.spi.Registry;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

@Slf4j
public class Server {

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
		catch (SQLException e) {
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

	private void migrateDb() throws SQLException {
		DBMigrator.Action migrate = Optional.ofNullable(System.getenv().get("MIGRATE"))
				.map(DBMigrator.Action::valueOf)
				.orElse(DBMigrator.Action.NOP);

		injector.getInstance(DBMigrator.class).migrate(migrate);
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
