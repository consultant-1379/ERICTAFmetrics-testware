package com.ericsson.cifwk.setup;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.tools.cli.CLICommandHelper;
import com.ericsson.cifwk.taf.tools.cli.TimeoutException;
import com.google.inject.Inject;
import org.testng.annotations.Test;
import java.io.IOException;
import org.apache.log4j.Logger;

public class Initialise {
    Logger logger = Logger.getLogger(Initialise.class);

    public static final String CLIHOST = "metrics";
    @Inject
    private Host host = DataHandler.getHostByName(CLIHOST);
    @TestId(id = "CIP-9665", title = "download and install")
    @Context(context = {Context.CLI})
    @Test(groups={"Acceptance"})



    public void initialise() throws TimeoutException, IOException, InterruptedException {
        CLICommandHelper cmdHelper = new CLICommandHelper(host);
        logger.info("Starting Initialise");
        String latestVersion = DataHandler.getAttribute("metrics.latestVersion").toString();
        String servicesVersion = DataHandler.getAttribute("metrics.servicesVersion").toString();
        logger.info("version info");
        logger.info("EPE: "+latestVersion);
        logger.info("services: " + servicesVersion);
        cmdHelper.execute(
            "curl   https://arm1s11-eiffel004.eiffel.gic.ericsson.se:8443/nexus/content/repositories/3pptools/com/ericsson/3ppinternal/metrics_taf_scripts/1.0.7/metrics_taf_scripts-1.0.7.tar --output latestscripts.tar"); //Execute some script which echos output
        cmdHelper.execute("[ -d docker ] && rm -rf docker"); //delete rpm folder
        cmdHelper.execute("[ -d /docker_mount ] && rm -rf /docker_mount"); //delete docker mountpoint
        cmdHelper.execute("mkdir /docker_mount"); //create docker mountpoint
        cmdHelper.execute("tar -xvf latestscripts.tar"); //delete rpm folder
        cmdHelper.execute("/root/docker/taf_helper/terminate_delete"); //terminate and delete docker containers
        cmdHelper.execute("/root/docker/taf_helper/launch_docker"); //launch docker containers
        cmdHelper.execute(
            "curl   https://arm1s11-eiffel004.eiffel.gic.ericsson.se:8443/nexus/content/repositories/releases/com/ericsson/cifwk/ERICmetricsepe_CXP9032332/"
                + latestVersion + "/ERICmetricsepe_CXP9032332-" + latestVersion + ".rpm --output latestmetrics.rpm"); //Execute some script which echos output
        logger.info("EPE download complete");
        cmdHelper.execute("[ -d opt/ericsson ] && rm -rf opt/ericsson"); //delete rpm folder
        cmdHelper.execute("rpm2cpio latestmetrics.rpm| cpio -idmv"); //Extract RPM
        logger.info("epe extracted");
        cmdHelper.execute("cp -R /root/docker/taf_helper/* /docker_mount"); //copy files
        cmdHelper.execute("cp -R /root/docker/wiremock/ /docker_mount"); //copy files
        cmdHelper.execute("mkdir /docker_mount/jars"); //create jar directory
        cmdHelper.execute("cp -R opt/ericsson/job-delivery-events*  /docker_mount/jars"); //copy jar
        cmdHelper.execute("cp -R opt/ericsson/job-scm-events*  /docker_mount/jars"); //copy jar
        cmdHelper.execute("cp -R opt/ericsson/job-cne-events*  /docker_mount/jars"); //copy jar
        cmdHelper.execute("cat /etc/hosts | grep rabbitmq || echo \"127.0.0.1     rabbitmq\" >>/etc/hosts"); //add rabbitmq to hosts
        cmdHelper.execute("cat /root/.bashrc | grep METRICSEPE_HOME || /root/docker/taf_helper/set_home"); //add metrics home to GW
        logger.info("Waiting for Containers to come up");

        Thread.sleep(60000); //wait for docker containers to come up
        logger.info("Run update hosts");
        cmdHelper.execute("/root/docker/taf_helper/update_hosts"); //update hosts files
        logger.info("Run wiremock");
        cmdHelper.execute("/root/docker/taf_helper/run_wiremock"); //update hosts files
        logger.info("Start flink cluster");
        cmdHelper.execute("/root/docker/taf_helper/run_flink"); //update hosts files
        logger.info("update ES mapping");
        cmdHelper.execute(
            "curl -i -u guest:guest -H \"content-type:application/json\" -XPUT -d'{\"type\":\"topic\",\"durable\":true}' http://localhost:15672/api/exchanges/%2f/eiffel.exchange"); //create exchange
        logger.info("run clme");
        long timeout = 90;
        cmdHelper.write("/root/docker/taf_helper/run_clme_flink " + latestVersion + "\n");
        logger.info("run scm");
        cmdHelper.expect("switched to RUNNING", timeout);
        cmdHelper.disconnect(); // Closes all open shell instances and closes all sessions.
        cmdHelper = new CLICommandHelper(host);
        cmdHelper.write("/root/docker/taf_helper/run_scm_flink " + latestVersion + "\n");
        logger.info("run cne");
        cmdHelper.expect("switched to RUNNING", timeout);
        cmdHelper.disconnect(); // Closes all open shell instances and closes all sessions.
        cmdHelper = new CLICommandHelper(host);
        cmdHelper.write("/root/docker/taf_helper/run_cne_flink " + latestVersion + "\n");
        cmdHelper.expect("switched to RUNNING", timeout);
        logger.info("all running in flink");

        cmdHelper.disconnect(); // Closes all open shell instances and closes all sessions.
        logger.info("copy services");
        cmdHelper.execute(
            "curl   https://arm1s11-eiffel004.eiffel.gic.ericsson.se:8443/nexus/content/repositories/releases/com/ericsson/cifwk/ERICmetricsservices_CXP9032471/"
                + servicesVersion + "/ERICmetricsservices_CXP9032471-" + servicesVersion
                + ".jar --output ERICmetricsservices_CXP9032471-" + servicesVersion + ".jar"); //Execute some script which echos output
         cmdHelper.execute("cp ERICmetricsservices_CXP9032471-" + servicesVersion + ".jar  /docker_mount/jars"); //copy jar
        logger.info("run services");
        cmdHelper.write("/root/docker/taf_helper/run_metrics_services " + servicesVersion + "\n");
        cmdHelper.expect("Starting metrics-and-visualization", timeout);
        logger.info("services running");
        logger.info("update es mapping2");
        cmdHelper.execute("curl   -XPUT 'http://192.168.0.223:9200/ci-events/_mapping/multiclme?update_all_types' -d '"
            + Constants.MULTI_CLME_INIT+"'"); //Execute some script which echos output
        logger.info("done init");
    }

}

