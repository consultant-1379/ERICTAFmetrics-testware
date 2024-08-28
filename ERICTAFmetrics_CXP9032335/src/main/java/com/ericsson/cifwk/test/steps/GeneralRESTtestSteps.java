package com.ericsson.cifwk.test.steps;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import com.ericsson.cifwk.operators.GenericRESTOperatorHttp;
import com.ericsson.cifwk.taf.annotations.TestStep;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.Output;
import com.ericsson.cifwk.taf.tools.http.HttpResponse;
import com.ericsson.cifwk.taf.tools.http.constants.HttpStatus;
import com.google.inject.Inject;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class GeneralRESTtestSteps {

    public static final String RUN_REST_CALLS = "CIP-7293_Func_1";
    public static final String RUN_SCM_EVENTS_ELASTICSEARCH = "CIP-8751";
    public static final String RUN_SCM_ENRICHED_EVENTS_ELASTICSEARCH = "CIP-8751_Func_1";
    public static final String RUN_CLME_EVENTS_ELASTICSEARCH = "CIP-9291";
    public static final String RUN_CLME_JOB_EVENTS_ELASTICSEARCH = "CIP-9420";
    public static final String RUN_CLME_DELIVERED_OBSOLETED_EVENTS_ELASTICSEARCH = "CIP-8692";
    public static final String RUN_CLME_KGB_CDB_NO_INITIAL_EVENTS_ELASTICSEARCH = "CIP-9348";
    public static final String RUN_CLME_KGB_CDB_INITIAL_EVENTS_ELASTICSEARCH = "CIP-9986";
    public static final String RUN_GROUPS_EVENTS_ELASTICSEARCH = "CIP-10265";
    public static final String RUN_CNE_EVENTS_ELASTICSEARCH = "CIP-10333";
    public static final String RUN_SCM_TEAM_PERFORMANCE_ELASTICSEARCH = "team-performance-test";
    public static final String RUN_SERVICES_REST = "Services Rest";

    Logger logger = Logger.getLogger(GeneralRESTtestSteps.class);
    HttpResponse result;

    @Inject
    GenericRESTOperatorHttp restHTTPOperator;

    @TestStep(id = RUN_REST_CALLS)
    public void performRESTwithTimeoutCommands(
            @Input("baseUrl") String baseUrl,
            @Input("path") String path,
            @Input("restParameters") String restParameters,
            @Input("type") String type,
            @Input("timeout") int timeOut,
            @Output("expected") boolean expected,
            @Output("expectedOut") String expectedOut,
            @Output("httpResponse") String httpResponse) {

        result = restHTTPOperator.executeREST(baseUrl, path, restParameters,
                type, timeOut);

        if (httpResponse.equals("OK")) {
            assertEquals(result.getResponseCode(), HttpStatus.OK);
        } else {
            assertEquals(result.getResponseCode(), HttpStatus.NOT_FOUND);
        }
        if (expectedOut != null && !expectedOut.isEmpty()) {
            assertEquals(result.getBody().contains(expectedOut), expected);
        }
    }

    @TestStep(id = RUN_SCM_EVENTS_ELASTICSEARCH)
    public void verifyCallRestReturnForPackages(
            @Input("HostName") String hostName,
            @Input("Index") String index,
            @Input("Type") String type,
            @Input("Https") boolean https,
            @Input("RestType") String restType,
            @Input("Action") String action,
            @Input("User") String User,
            @Input("UserValue") String UserValue,
            @Input("CommitMessage") String CommitMessage,
            @Input("CommitMessageValue") String CommitMessageValue,
            @Input("Branch") String Branch,
            @Input("BranchValue") String BranchValue,
            @Input("WorkItem") String WorkItem,
            @Input("WorkItemValue") String WorkItemValue,
            @Input("sha") String sha,
            @Input("shaValue") String shaValue,
            @Input("Repository") String Repository,
            @Input("RepositoryValue") String RepositoryValue,
            @Input("RestParameters") String restParameters,
            @Input("TimeOut") int timeOut,
            @Output("ResponseCode") String responseCode,
            @Output("ExpectedResult") String expectedResult) throws IOException {

        String path = index + "/" + type + "/" + action;
        result = restHTTPOperator.executeREST(hostName, path, restParameters,
                restType, timeOut, https);
        assertTrue(result.getResponseCode().toString().equals(responseCode));

        String restBody = result.getBody();
        logger.debug("REST BODY : " + restBody);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(restBody);
        JsonNode source = rootNode.path("hits").path("hits");
        logger.debug("Source Node = " + source);

        String found = "FALSE";
        try {
            for (final JsonNode element : source) {
                JsonNode sourceNode = element.path("_source");
                if (sourceNode.get("user").asText().equals(UserValue)
                        && sourceNode.get("commitMessage").asText()
                        .equals(CommitMessageValue)
                        && sourceNode.get("branch").asText()
                        .equals(BranchValue)
                        && sourceNode.get("sha").asText().equals(shaValue)
                        && sourceNode.get("repository").asText()
                        .equals(RepositoryValue)
                        && sourceNode.get("workItem").asText()
                        .equals(WorkItemValue)
                        && !sourceNode.get("eventId").isNull()
                        && !sourceNode.get("eventTime").isNull()) {
                    found = "TRUE";
                    break;
                } else {
                    System.out.println(sourceNode.get("user") + "-------" + UserValue);
                    System.out.println(sourceNode.get("commitMessage") + "-------" + CommitMessageValue);
                    System.out.println(sourceNode.get("branch") + "-------" + BranchValue);
                    System.out.println(sourceNode.get("sha") + "-------" + shaValue);
                    System.out.println(sourceNode.get("repository") + "-------" + RepositoryValue);
                    System.out.println(sourceNode.get("workItem") + "-------" + WorkItemValue);
                    System.out.println(sourceNode.get("eventId") + "------- Not Null" );
                    System.out.println(sourceNode.get("eventTime") + "------- Not Null");

                    System.out.println(sourceNode.get("commitMessage").toString());
                    System.out.println(CommitMessageValue.toString());

                }
            }
            assertEquals(expectedResult, found);
        } catch (JSONException e) {
            logger.error("Error with JSON", e);
            assertTrue(false);
        }
    }

    @TestStep(id = RUN_SCM_ENRICHED_EVENTS_ELASTICSEARCH)
    public void verifyCallRestReturnForPackages(
            @Input("HostName") String hostName,
            @Input("Index") String index,
            @Input("Type") String type,
            @Input("Https") boolean https,
            @Input("RestType") String restType,
            @Input("Action") String action,
            @Input("User") String User,
            @Input("UserValue") String UserValue,
            @Input("CommitMessage") String CommitMessage,
            @Input("CommitMessageValue") String CommitMessageValue,
            @Input("Branch") String Branch,
            @Input("BranchValue") String BranchValue,
            @Input("sha") String sha,
            @Input("shaValue") String shaValue,
            @Input("Repository") String Repository,
            @Input("RepositoryValue") String RepositoryValue,
            @Input("WorkItemResolved") String WorkItemResolved,
            @Input("WorkItemResolvedValue") String WorkItemResolvedValue,
            @Input("Team") String Team,
            @Input("TeamValue") String TeamValue,
            @Input("Priority") String Priority,
            @Input("PriorityValue") String PriorityValue,
            @Input("WorkItemCreated") String WorkItemCreated,
            @Input("WorkItemCreatedValue") String WorkItemCreatedValue,
            @Input("WorkItemType") String WorkItemType,
            @Input("WorkItemTypeValue") String WorkItemTypeValue,
            @Input("Artifact") String Artifact,
            @Input("ArtifactValue") String ArtifactValue,
            @Input("WorkItem") String WorkItem,
            @Input("WorkItemValue") String WorkItemValue,
            @Input("FixVersion") String FixVersion,
            @Input("FixVersionValue") String FixVersionValue,
            @Input("RestParameters") String restParameters,
            @Input("TimeOut") int timeOut,
            @Output("ResponseCode") String responseCode,
            @Output("ExpectedResult") String expectedResult) throws IOException {

        String path = index + "/" + type + "/" + action;
        result = restHTTPOperator.executeREST(hostName, path, restParameters,
                restType, timeOut, https);
        assertTrue(result.getResponseCode().toString().equals(responseCode));

        String restBody = result.getBody();
        logger.debug("REST BODY : " + restBody);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(restBody);
        JsonNode source = rootNode.path("hits").path("hits");
        logger.debug("Source Node = " + source);

        String found = "FALSE";
        try {
            for (final JsonNode element : source) {
                JsonNode sourceNode = element.path("_source");
                logger.debug("Source Node Element = " + sourceNode);
                if (sourceNode.get("User").asText().equals(UserValue)
                        && sourceNode.get("commitMessage").asText()
                        .equals(CommitMessageValue)
                        && sourceNode.get("branch").asText()
                        .equals(BranchValue)
                        && sourceNode.get("sha").asText().equals(shaValue)
                        && sourceNode.get("repository").asText()
                        .equals(RepositoryValue)
                        && sourceNode.get("workItemResolved").asText()
                        .equals(WorkItemResolvedValue)
                        && sourceNode.get("team").asText().equals(TeamValue)
                        && sourceNode.get("priority").asText()
                        .equals(PriorityValue)
                        && sourceNode.get("workItemCreated").asText()
                        .equals(WorkItemCreatedValue)
                        && sourceNode.get("workItemType").asText()
                        .equals(WorkItemTypeValue)
                        && sourceNode.get("artifact").asText()
                        .equals(ArtifactValue)
                        && sourceNode.get("workItem").asText()
                        .equals(WorkItemValue)
                        && sourceNode.get("fixVersion").asText()
                        .equals(FixVersionValue)
                        && !sourceNode.get("eventId").isNull()
                        && !sourceNode.get("eventTime").isNull()) {
                    found = "TRUE";
                    break;
                }
            }
            assertEquals(expectedResult, found);
        } catch (JSONException e) {
            logger.error("Error with JSON", e);
            assertTrue(false);
        }
    }

    @TestStep(id = RUN_CLME_DELIVERED_OBSOLETED_EVENTS_ELASTICSEARCH)
    public void verifyCallRestReturnForPackages(
            @Input("HostName") String hostName,
            @Input("Index") String index,
            @Input("Type") String type,
            @Input("Https") boolean https,
            @Input("RestType") String restType,
            @Input("Action") String action,
            @Input("RestParameters") String restParameters,
            @Input("FlowContext") String FlowContext,
            @Input("FlowContextValue") String FlowContextValue,
            @Input("Product") String Product,
            @Input("ProductValue") String ProductValue,
            @Input("ArtifactId") String ArtifactId,
            @Input("ArtifactIdValue") String ArtifactIdValue,
            @Input("Team") String Team,
            @Input("TeamValue") String TeamValue,
            @Input("Version") String Version,
            @Input("VersionValue") String VersionValue,
            @Input("Release") String Release,
            @Input("ReleaseValue") String ReleaseValue,
            @Input("ConfidenceLevels") String ConfidenceLevels,
            @Input("ConfidenceLevelsValue") String ConfidenceLevelsValue,
            @Input("GroupId") String GroupId,
            @Input("GroupIdValue") String GroupIdValue,
            @Input("Drop") String Drop,
            @Input("DropValue") String DropValue,
            @Input("TimeOut") int timeOut,
            @Output("ResponseCode") String responseCode,
            @Output("ExpectedResult") String expectedResult) throws IOException {

        String path = index + "/" + type + "/" + action;
        result = restHTTPOperator.executeREST(hostName, path, restParameters,
                restType, timeOut, https);
        assertTrue(result.getResponseCode().toString().equals(responseCode));
        String restBody = result.getBody();
        logger.debug("REST BODY : " + restBody);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(restBody);
        JsonNode source = rootNode.path("hits").path("hits");
        logger.debug("Source Node = " + source);

        String found = "FALSE";
        try {
            for (final JsonNode element : source) {
                JsonNode sourceNode = element.path("_source");
                logger.debug("Source Node Element = " + sourceNode);
                if (sourceNode.get("flowContext").asText()
                        .equals(FlowContextValue)
                        && sourceNode.get("product").asText()
                        .equals(ProductValue)
                        && sourceNode.get("artifactId").asText()
                        .equals(ArtifactIdValue)
                        && sourceNode.get("team").asText().equals(TeamValue)
                        && sourceNode.get("version").asText()
                        .equals(VersionValue)
                        && sourceNode.get("release").asText()
                        .equals(ReleaseValue)
                        && sourceNode.get("confidenceLevels").asText()
                        .equals(ConfidenceLevelsValue)
                        && sourceNode.get("groupId").asText()
                        .equals(GroupIdValue)
                        && sourceNode.get("drop").asText().equals(DropValue)
                        && !sourceNode.get("eventId").isNull()
                        && !sourceNode.get("eventTime").isNull()) {
                    found = "TRUE";
                    break;
                } else {
                    System.out.println(sourceNode.get("flowContext") + "-------" + FlowContextValue);
                    System.out.println(sourceNode.get("product") + "-------" + ProductValue);
                    System.out.println(sourceNode.get("artifactId") + "-------" + ArtifactIdValue);
                    System.out.println(sourceNode.get("team") + "-------" + TeamValue);
                    System.out.println(sourceNode.get("version") + "-------" + VersionValue);
                    System.out.println(sourceNode.get("release") + "-------" + ReleaseValue);
                    System.out.println(sourceNode.get("confidenceLevels") + "-------" + ConfidenceLevelsValue);
                    System.out.println(sourceNode.get("groupId") + "-------" + GroupIdValue);
                    System.out.println(sourceNode.get("drop") + "-------" + DropValue);
                    System.out.println(sourceNode.get("eventId") + "-------not null");
                    System.out.println(sourceNode.get("eventTime") + "-------not null");
                }
            }
            assertEquals(expectedResult, found);
        } catch (JSONException e) {
            logger.error("Error with JSON", e);
            assertTrue(false);
        }
    }

    @TestStep(id = RUN_CLME_KGB_CDB_NO_INITIAL_EVENTS_ELASTICSEARCH)
    public void verifyCallRestReturnForPackages(
            @Input("HostName") String hostName,
            @Input("Index") String index,
            @Input("Type") String type,
            @Input("Https") boolean https,
            @Input("RestType") String restType,
            @Input("Action") String action,
            @Input("RestParameters") String restParameters,
            @Input("Result") String Result,
            @Input("ResultValue") String ResultValue,
            @Input("FlowContext") String FlowContext,
            @Input("FlowContextValue") String FlowContextValue,
            @Input("Product") String Product,
            @Input("ProductValue") String ProductValue,
            @Input("ArtifactId") String ArtifactId,
            @Input("ArtifactIdValue") String ArtifactIdValue,
            @Input("DeployedScript") String DeployedScript,
            @Input("DeployedScriptValue") String DeployedScriptValue,
            @Input("InputEventIds") String InputEventIds,
            @Input("InputEventIdsValue") String InputEventIdsValue,
            @Input("MessageType") String MessageType,
            @Input("MessageTypeValue") String MessageTypeValue,
            @Input("DurationKnown") String DurationKnown,
            @Input("DurationKnownValue") boolean DurationKnownValue,
            @Input("Version") String Version,
            @Input("VersionValue") String VersionValue,
            @Input("Release") String Release,
            @Input("ReleaseValue") String ReleaseValue,
            @Input("ConfidenceLevels") String ConfidenceLevels,
            @Input("ConfidenceLevelsValue") String ConfidenceLevelsValue,
            @Input("GroupId") String GroupId,
            @Input("GroupIdValue") String GroupIdValue,
            @Input("Drop") String Drop,
            @Input("DropValue") String DropValue,
            @Input("MastVersion") String MastVersion,
            @Input("MastVersionValue") String MastVersionValue,
            @Input("CompletedEvent") String CompletedEvent,
            @Input("CompletedEventValue") boolean CompletedEventValue,
            @Input("ConfidenceLevelType") String ConfidenceLevelType,
            @Input("ConfidenceLevelTypeValue") String ConfidenceLevelTypeValue,
            @Input("MastArtifactId") String MastArtifactId,
            @Input("MastArtifactIdValue") String MastArtifactIdValue,
            @Input("SedVersion") String SedVersion,
            @Input("SedVersionValue") String SedVersionValue,
            @Input("MastGroupId") String MastGroupId,
            @Input("MastGroupIdValue") String MastGroupIdValue,
            @Input("TimeOut") int timeOut,
            @Output("ResponseCode") String responseCode,
            @Output("ExpectedResult") String expectedResult) throws IOException {

        String path = index + "/" + type + "/" + action;
        result = restHTTPOperator.executeREST(hostName, path, restParameters,
                restType, timeOut, https);
        assertTrue(result.getResponseCode().toString().equals(responseCode));

        String restBody = result.getBody();
        logger.debug("REST BODY : " + restBody);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(restBody);
        JsonNode source = rootNode.path("hits").path("hits");
        logger.debug("Source Node = " + source);

        String found = "FALSE";
        try {
            for (final JsonNode element : source) {
                JsonNode sourceNode = element.path("_source");
                logger.debug("Source Node Element = " + sourceNode);
                if (sourceNode.get("result").asText().equals(ResultValue)
                        && sourceNode.get("flowContext").asText()
                        .equals(FlowContextValue)
                        && sourceNode.get("product").asText()
                        .equals(ProductValue)
                        && sourceNode.get("artifactId").asText()
                        .equals(ArtifactIdValue)
                        && sourceNode.get("deployScript").asText()
                        .equals(DeployedScriptValue)
                        && sourceNode.get("inputEventIds").get(0).asText()
                        .equals(InputEventIdsValue)
                        && sourceNode.get("messageType").asText()
                        .equals(MessageTypeValue)
                        && sourceNode.get("durationKnown").asBoolean() == (DurationKnownValue)
                        && sourceNode.get("version").asText()
                        .equals(VersionValue)
                        && sourceNode.get("release").asText()
                        .equals(ReleaseValue)
                        && sourceNode.get("groupId").asText()
                        .equals(GroupIdValue)
                        && sourceNode.get("drop").asText().equals(DropValue)
                        && sourceNode.get("completedEvent").asBoolean() == (CompletedEventValue)
                        && !sourceNode.get("eventId").isNull()
                        && !sourceNode.get("eventTime").isNull()) {
                    found = "TRUE";
                    break;
                }
            }
            assertEquals(expectedResult, found);
        } catch (JSONException e) {
            logger.error("Error with JSON", e);
            assertTrue(false);
        }
    }

    @TestStep(id = RUN_CLME_KGB_CDB_INITIAL_EVENTS_ELASTICSEARCH)
    public void verifyCallRestReturnForPackages(
            @Input("HostName") String hostName,
            @Input("Index") String index,
            @Input("Type") String type,
            @Input("Https") boolean https,
            @Input("RestType") String restType,
            @Input("Action") String action,
            @Input("RestParameters") String restParameters,
            @Input("Result") String Result,
            @Input("ResultValue") String ResultValue,
            @Input("FlowContext") String FlowContext,
            @Input("FlowContextValue") String FlowContextValue,
            @Input("Product") String Product,
            @Input("ProductValue") String ProductValue,
            @Input("ArtifactId") String ArtifactId,
            @Input("ArtifactIdValue") String ArtifactIdValue,
            @Input("DeployedScript") String DeployedScript,
            @Input("DeployedScriptValue") String DeployedScriptValue,
            @Input("InputEventIds") String InputEventIds,
            @Input("InputEventIdsValue") String InputEventIdsValue,
            @Input("MessageType") String MessageType,
            @Input("MessageTypeValue") String MessageTypeValue,
            @Input("DurationKnown") String DurationKnown,
            @Input("DurationKnownValue") boolean DurationKnownValue,
            @Input("Version") String Version,
            @Input("VersionValue") String VersionValue,
            @Input("Release") String Release,
            @Input("ReleaseValue") String ReleaseValue,
            @Input("ConfidenceLevels") String ConfidenceLevels,
            @Input("ConfidenceLevelsValue") String ConfidenceLevelsValue,
            @Input("GroupId") String GroupId,
            @Input("GroupIdValue") String GroupIdValue,
            @Input("Drop") String Drop,
            @Input("DropValue") String DropValue,
            @Input("MastVersion") String MastVersion,
            @Input("MastVersionValue") String MastVersionValue,
            @Input("CompletedEvent") String CompletedEvent,
            @Input("CompletedEventValue") boolean CompletedEventValue,
            @Input("ConfidenceLevelType") String ConfidenceLevelType,
            @Input("ConfidenceLevelTypeValue") String ConfidenceLevelTypeValue,
            @Input("MastArtifactId") String MastArtifactId,
            @Input("MastArtifactIdValue") String MastArtifactIdValue,
            @Input("SedVersion") String SedVersion,
            @Input("SedVersionValue") String SedVersionValue,
            @Input("MastGroupId") String MastGroupId,
            @Input("MastGroupIdValue") String MastGroupIdValue,
            @Input("Duration") String Duration,
            @Input("DurationValue") double DurationValue,
            @Input("FinishedLevel") String FinishedLevel,
            @Input("FinishedLevelValue") String FinishedLevelValue,
            @Input("TimeOut") int timeOut,
            @Output("ResponseCode") String responseCode,
            @Output("ExpectedResult") String expectedResult) throws IOException {

        String path = index + "/" + type + "/" + action;
        result = restHTTPOperator.executeREST(hostName, path, restParameters,
                restType, timeOut, https);
        assertTrue(result.getResponseCode().toString().equals(responseCode));

        String restBody = result.getBody();
        logger.debug("REST BODY : " + restBody);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(restBody);
        JsonNode source = rootNode.path("hits").path("hits");
        logger.debug("Source Node = " + source);
        String found = "FALSE";
        try {
            for (final JsonNode element : source) {
                JsonNode sourceNode = element.path("_source");
                logger.debug("Source Node Element = " + sourceNode);
                if (sourceNode.get("result").asText().equals(ResultValue)
                        && sourceNode.get("flowContext").asText()
                        .equals(FlowContextValue)
                        && sourceNode.get("product").asText()
                        .equals(ProductValue)
                        && sourceNode.get("artifactId").asText()
                        .equals(ArtifactIdValue)
                        && sourceNode.get("inputEventIds").get(0).asText()
                        .equals(InputEventIdsValue)
                        && sourceNode.get("messageType").asText()
                        .equals(MessageTypeValue)
                        && sourceNode.get("durationKnown").asBoolean() == (DurationKnownValue)
                        && sourceNode.get("version").asText()
                        .equals(VersionValue)
                        && sourceNode.get("release").asText()
                        .equals(ReleaseValue)
						&& sourceNode.get("finishedLevels").asText()
                        .equals(ConfidenceLevelsValue)
                        && sourceNode.get("groupId").asText()
                        .equals(GroupIdValue)
                        && sourceNode.get("drop").asText().equals(DropValue)
                        && sourceNode.get("completedEvent").asBoolean() == (CompletedEventValue)
                        && sourceNode.get("sedVersion").asText()
                        .equals(SedVersionValue)
                        && sourceNode.get("finishedLevel").asText()
                        .equals(FinishedLevelValue)
                        && !sourceNode.get("eventId").isNull()
                        && !sourceNode.get("eventTime").isNull()
                        && !sourceNode.get("finishedEventId").isNull()
                        && !sourceNode.get("finishedEventTime").isNull()) {
                    found = "TRUE";
                    break;
                } else {
                    System.out.println("expected      got");
                    System.out.println(sourceNode.get("result") + "-------" + ResultValue);
                    System.out.println(sourceNode.get("flowContext") + "-------" + FlowContextValue);
                    System.out.println(sourceNode.get("product") + "-------" + ProductValue);
                    System.out.println(sourceNode.get("artifactId") + "-------" + ArtifactIdValue);
                    System.out.println(sourceNode.get("deployScript") + "-------" + DeployedScriptValue);
                    System.out.println(sourceNode.get("inputEventIds").get(0) + "-------" + InputEventIdsValue);
                    System.out.println(sourceNode.get("messageType") + "-------" + MessageTypeValue);
                    System.out.println(sourceNode.get("durationKnown") + "-------" + DurationKnownValue);
                    System.out.println(sourceNode.get("version") + "-------" + VersionValue);
                    System.out.println(sourceNode.get("release") + "-------" + ReleaseValue);
                    System.out.println(sourceNode.get("groupId") + "-------" + GroupIdValue);
                    System.out.println(sourceNode.get("drop") + "-------" + DropValue);
                    System.out.println(sourceNode.get("completedEvent") + "-------" + CompletedEventValue);
                    System.out.println(sourceNode.get("sedVersion") + "-------" + SedVersionValue);
                    System.out.println(sourceNode.get("duration") + "-------" + DurationValue);
                    System.out.println(sourceNode.get("finishedLevel") + "-------" + FinishedLevelValue);
                    System.out.println(sourceNode.get("eventId") + "-------not null");
                    System.out.println(sourceNode.get("eventTime") + "-------not null");
                    System.out.println(sourceNode.get("finishedEventId") + "-------not null");
                    System.out.println(sourceNode.get("finishedEventTime") + "-------not null");
                    System.out.println(sourceNode.get("finishedLevels") + "-------" + ConfidenceLevelsValue);

                }
            }
            assertEquals(expectedResult, found);
        } catch (JSONException e) {
            logger.error("Error with JSON", e);
            assertTrue(false);
        }
    }

    @TestStep(id = RUN_CLME_EVENTS_ELASTICSEARCH)
    public void verifyCallRestReturnForPackages(
            @Input("HostName") String hostName,
            @Input("Index") String index,
            @Input("Type") String type,
            @Input("Https") boolean https,
            @Input("RestType") String restType,
            @Input("Action") String action,
            @Input("Field1") String field1,
            @Input("Value1") String value1,
            @Input("Field2") String field2,
            @Input("Value2") String value2,
            @Input("Field3") String field3,
            @Input("Value3") String value3,
            @Input("Field4") String field4,
            @Input("Value4") String value4,
            @Input("RestParameters") String restParameters,
            @Input("TimeOut") int timeOut,
            @Output("ResponseCode") String responseCode,
            @Output("ExpectedResult") String expectedResult)  throws IOException {

        String path = index + "/" + type + "/" + action;

        result = restHTTPOperator.executeREST(hostName, path, restParameters,
                restType, timeOut, https);
        assertTrue(result.getResponseCode().toString().equals(responseCode));

        String restBody = result.getBody();
        logger.debug("REST BODY : " + restBody);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(restBody);
        JsonNode source = rootNode.path("hits").path("hits");
        logger.debug("Source Node = " + source);
        String found = "FALSE";
    try {
        for (final JsonNode element : source) {
            JsonNode sourceNode = element.path("_source");
            logger.debug("Source Node Element = " + sourceNode);
            if (sourceNode.get(field1).asText().equals(value1)
                    && sourceNode.get(field2).asText()
                    .equals(value2)
                    && sourceNode.get(field3).asText()
                    .equals(value3)
                    && sourceNode.get(field4).asText()
                    .equals(value4)
                    ){
            found = "TRUE";
                break;
            } else {
                System.out.println("expected      got");
                System.out.println(sourceNode.get(field1) + "-------" + value1);
                System.out.println(sourceNode.get(field2) + "-------" + value2);
                System.out.println(sourceNode.get(field3) + "-------" + value3);
                System.out.println(sourceNode.get(field4) + "-------" + value4);


            }
        }
        assertEquals(expectedResult, found);
    } catch (JSONException e) {
        logger.error("Error with JSON", e);
        assertTrue(false);
    }
}

    @TestStep(id = RUN_GROUPS_EVENTS_ELASTICSEARCH)
    public void verifyGroupData(
            @Input("HostName") String hostName,
            @Input("Index") String index,
            @Input("Type") String type,
            @Input("Https") boolean https,
            @Input("RestType") String restType,
            @Input("Action") String action,
            @Input("artifactId") String artifactId,
            @Input("artifactValue") String artifactValue,
            @Input("groupTeam") String groupTeam,
            @Input("groupTeamValue") String groupTeamValue,
            @Input("confidenceLevels") String confidenceLevels,
            @Input("confidenceLevelsValue") String confidenceLevelsValue,
            @Input("timeInQueue") String timeInQueue,
            @Input("timeInQueueValue") String timeInQueueValue,
            @Input("artifactCount") String artifactCount,
            @Input("artifactCountValue") String artifactCountValue,
            @Input("queueLength") String queueLength,
            @Input("queueLengthValue") String queueLengthValue,
            @Input("testwareInQueue") String testwareInQueue,
            @Input("testwareInQueueValue") String testwareInQueueValue,
            @Input("testwareInGroup") String testwareInGroup,
            @Input("testwareInGroupValue") String testwareInGroupValue,
            @Input("queueId") String queueId,
            @Input("queueIdValue") String queueIdValue,
            @Input("RestParameters") String restParameters,
            @Input("TimeOut") int timeOut,
            @Output("ResponseCode") String responseCode,
            @Output("ExpectedResult") String expectedResult)



        throws IOException {

            String path = index + "/" + type + "/" + action;
            result = restHTTPOperator.executeREST(hostName, path, restParameters,
                    restType, timeOut, https);
            assertTrue(result.getResponseCode().toString().equals(responseCode));

            String restBody = result.getBody();
            logger.debug("REST BODY : " + restBody);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(restBody);
            JsonNode source = rootNode.path("hits").path("hits");
            logger.debug("Source Node = " + source);
            String found = "FALSE";


    try {
        for (final JsonNode element : source) {
            JsonNode sourceNode = element.path("_source");
            logger.debug("Source Node Element = " + sourceNode);
            if (sourceNode.get(confidenceLevels).asText().equals(confidenceLevelsValue)
                    && sourceNode.get("artifactId").asText()
                    .equals(artifactValue)
                    && sourceNode.get("groupTeam").asText()
                    .equals(groupTeamValue)
                    && sourceNode.get("confidenceLevels").asText()
                    .equals(confidenceLevelsValue)
                    && sourceNode.get("timeInQueue").asText()
                    .equals(timeInQueueValue)

                    && sourceNode.get("artifactsInGroup").asText()
                    .equals(artifactCountValue)
                    && sourceNode.get("queueLength").asText()
                    .equals(queueLengthValue)
                    && sourceNode.get("testwareInGroup").asText()
                    .equals(testwareInGroupValue)
                    && sourceNode.get("testwareInQueue").asText()
                    .equals(testwareInQueueValue)
                    ){
                found = "TRUE";
                break;
            } else {
                System.out.println("expected      got");
                System.out.println(sourceNode.get(confidenceLevels) + "-------" + confidenceLevelsValue);
                System.out.println(sourceNode.get(groupTeam) + "-------" + groupTeamValue);
                System.out.println(sourceNode.get(timeInQueue) + "-------" + timeInQueueValue);
                System.out.println(sourceNode.get(artifactId) + "-------" + artifactValue);
                System.out.println(sourceNode.get(artifactCount) + "-------" + artifactCountValue);
                System.out.println(sourceNode.get(queueLength) + "-------" + queueLengthValue);
                System.out.println(sourceNode.get(testwareInGroup) + "-------" + testwareInGroupValue);
                System.out.println(sourceNode.get(testwareInQueue) + "-------" + testwareInQueueValue);
            }
        }
        assertEquals(expectedResult, found);
    } catch (JSONException e) {
        logger.error("Error with JSON", e);
        assertTrue(false);
    }
}



    @TestStep(id = RUN_CNE_EVENTS_ELASTICSEARCH)
    public void verifyCallRestReturnForPackages(
            @Input("HostName") String hostName,
            @Input("Index") String index,
            @Input("Type") String type,
            @Input("Https") boolean https,
            @Input("RestType") String restType,
            @Input("Action") String action,
            @Input("cneAction") String cneAction,
            @Input("cneActionValue") String cneActionValue,
            @Input("baseline") String baseline,
            @Input("baselineValue") String baselineValue,
            @Input("componentInstance") String componentInstance,
            @Input("componentInstanceValue") String componentInstanceValue,
            @Input("componentType") String componentType,
            @Input("componentTypeValue") String componentTypeValue,
            @Input("reason") String reason,
            @Input("reasonValue") String reasonValue,
            @Input("status") String status,
            @Input("statusValue") String statusValue,
            @Input("drop") String drop,
            @Input("dropValue") String dropValue,
            @Input("release") String release,
            @Input("releaseValue") String releaseValue,
            @Input("product") String product,
            @Input("productValue") String productValue,
            @Input("endTime") String endTime,
            @Input("endTimeValue") String endTimeValue,
            @Input("RestParameters") String restParameters,
            @Input("TimeOut") int timeOut,
            @Output("ResponseCode") String responseCode,
            @Output("ExpectedResult") String expectedResult)

        throws IOException {

            String path = index + "/" + type + "/" + action;
            result = restHTTPOperator.executeREST(hostName, path, restParameters,
                    restType, timeOut, https);
            assertTrue(result.getResponseCode().toString().equals(responseCode));
            String restBody = result.getBody();
            logger.debug("REST BODY : " + restBody);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(restBody);
            JsonNode source = rootNode.path("hits").path("hits");
            logger.debug("Source Node = " + source);
            String found = "FALSE";


            try {
                for (final JsonNode element : source) {
                    JsonNode sourceNode = element.path("_source");
                    logger.debug("Source Node Element = " + sourceNode);
                    if (sourceNode.get(cneAction).asText().equals(cneActionValue)

                    && sourceNode.get("componentInstance").asText()
                    .equals(componentInstanceValue)
                            && sourceNode.get("reason").asText()
                            .equals(reasonValue)
                            && sourceNode.get("drop").asText()
                            .equals(dropValue)
                            && sourceNode.get("release").asText()
                            .equals(releaseValue)
                            && sourceNode.get("product").asText()
                            .equals(productValue)
                            && sourceNode.get("status").asText()
                            .equals(statusValue)
                            )
                   {
                        found = "TRUE";
                        break;
                    } else {
                        System.out.println("expected      got");
                        System.out.println(sourceNode.get(cneAction) + "-------" + cneActionValue);
                        System.out.println(sourceNode.get(componentInstance) + "-------" + componentInstanceValue);
                        System.out.println(sourceNode.get(reason) + "-------" + reasonValue);
                        System.out.println(sourceNode.get(drop) + "-------" + dropValue);
                        System.out.println(sourceNode.get(release) + "-------" + releaseValue);
                        System.out.println(sourceNode.get(product) + "-------" + productValue);
                        System.out.println(sourceNode.get(status) + "-------" + statusValue);
                    }
                }
            assertEquals(expectedResult, found);
            found = "FALSE";
        } catch (JSONException e) {
            logger.error("Error with JSON", e);
            assertTrue(false);
        }
    }
    
	@TestStep(id = RUN_SCM_TEAM_PERFORMANCE_ELASTICSEARCH)
	public void testTeamPerformance(@Input("HostName") String hostName,
			@Input("Https") boolean https, @Input("RestType") String restType,
			@Input("URL") String url, @Input("EndPoint") String endPoint,
			@Input("RestParameters") String restParameters,
			@Input("TimeOut") int timeOut, @Output("Team") String team,
			@Output("Commits") int commits,
			@Output("ExpectedResult") boolean expectedResult,
			@Output("ResponseCode") String responseCode) throws IOException {

		String path = url + "/" + endPoint;
		result = restHTTPOperator.executeREST(hostName, path, restParameters,
				restType, timeOut, https);

		String restBody = result.getBody();
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode rootNode = objectMapper.readTree(restBody);
		
		boolean found = false;
		try {
			for (final JsonNode element : rootNode) {
				if(element.get("team").asText().equals(team)){
					if(element.get("commit").asInt() == commits){
						found = true;
					}
				}
			}
			assertEquals(true, found);
		} catch (JSONException e) {
			logger.error("Error with JSON", e);
		}
	}
        @TestStep(id = RUN_SERVICES_REST)
        public void testServicesRest(@Input("HostName") String hostName,
                @Input("Https") boolean https, @Input("RestType") String restType,
                @Input("URL") String url,
                @Input("EndPoint") String endPoint,
                @Input("TestId") String testId,
                @Input("RestParameters") String restParameters,
                @Input("TimeOut") int timeOut, @Output("ex1") String expected1,

                @Output("ExpectedResult") boolean expectedResult,
                @Output("ResponseCode") String responseCode) throws IOException {

                String path = url + "/" + endPoint;

                result = restHTTPOperator.executeREST(hostName, path, restParameters,
                        restType, timeOut, https);
                String restBody = result.getBody();
                boolean found = true;
                try {
                        if (!restBody.contains(expected1)){   found = false;}
                       // }

                        if (!found){
                                System.out.println("failed test id " + testId);
                                System.out.println("Response "+restBody);
                                System.out.println("expected "+expected1);
                        }
                        assertEquals(found,true);
                } catch (JSONException e) {
                        logger.error("Error with JSON", e);
                }
        }
}
