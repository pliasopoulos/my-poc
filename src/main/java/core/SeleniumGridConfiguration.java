package core;

import lombok.extern.slf4j.Slf4j;
import org.openqa.grid.common.RegistrationRequest;
import org.openqa.grid.internal.utils.SelfRegisteringRemote;
import org.openqa.grid.internal.utils.configuration.GridHubConfiguration;
import org.openqa.grid.internal.utils.configuration.GridNodeConfiguration;
import org.openqa.grid.web.Hub;
import org.openqa.selenium.remote.server.SeleniumServer;
import org.openqa.selenium.remote.server.log.LoggingManager;

@Slf4j
class SeleniumGridConfiguration {

    private static final String HUB_REGISTER_ADDRESS = "http://localhost:4444/grid/register";
    private final static int hubPort = 4444;
    private static final int firstNodePortNumber = 5555;
    private static final int noOfNodes = 10;

    void hubConfig() {
        try {
            LoggingManager.configureLogging(false);
            GridHubConfiguration gridHubConfig = new GridHubConfiguration();
            gridHubConfig.role = "hub";
            gridHubConfig.host = "localhost";
            gridHubConfig.port = hubPort;

            Hub myHub = new Hub(gridHubConfig);
            myHub.start();
            log.info("Hub configuration is: ({})", myHub.getConfiguration().toString());
            log.info("Selenium Hub Configured Successfully");
        } catch (Exception e) {
            log.info("Selenium Hub failed to configure, here is the exception..:" + e);
        }
    }

    void nodeConfig() {
        int portNumber = firstNodePortNumber;
        log.info("Registering ({}) nodes...", noOfNodes);

        for (int i = 0; i < noOfNodes; i++) {
            try {
                GridNodeConfiguration gridNodeConfig = new GridNodeConfiguration();
                gridNodeConfig.hub = HUB_REGISTER_ADDRESS;
                gridNodeConfig.port = portNumber;
                gridNodeConfig.role = "node";

                RegistrationRequest req = RegistrationRequest.build(gridNodeConfig);
                SelfRegisteringRemote remote = new SelfRegisteringRemote(req);
                remote.setRemoteServer(new SeleniumServer(gridNodeConfig));
                remote.startRemoteServer();
                remote.startRegistrationProcess();
                log.info("Selenium Node: ({}) Configured Successfully at port: ({})", i, portNumber);
            } catch (Exception e) {
                log.info("Selenium Node Configured failed to configure at port..:" + portNumber + " here is the Exception..:" + e);
            }
            portNumber++;
        }
    }
}
