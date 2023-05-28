//package org.nextlevel.bulkload;
//
//import com.google.api.client.auth.oauth2.Credential;
//import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
//import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
//import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
//import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
//import com.google.api.client.http.HttpRequestInitializer;
//import com.google.api.client.http.javanet.NetHttpTransport;
//import com.google.api.client.json.gson.GsonFactory;
//import com.google.api.client.util.store.MemoryDataStoreFactory;
//import com.google.api.services.sheets.v4.Sheets;
//import com.google.api.services.sheets.v4.SheetsScopes;
//import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.security.GeneralSecurityException;
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Component
//public class ParseAttendanceSheets {
//    private static final String APPLICATION_NAME = "NextLevel School Mgmt App";
//    private static String SPREADSHEET_ID = "1hEsHNUOerAMPz3wpzPYsR9qWK-uiVdVrEvY4ro8oUU0"; //"1fg9aYaA6280yj3f1SX4pTf_D5puALZHiCbLn3eBXCms";
//
//    @Value("${google.api.key}")
//    private static String googleAPIKey;
//
//    private static String API_KEY="AIzaSyBFFksPSn8SgcMykmba8uefA3B93z95JWs";
//
//    private static GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
//
//    private static Sheets getSheetsService2() {
//        NetHttpTransport transport = new NetHttpTransport.Builder().build();
//        GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
//        HttpRequestInitializer httpRequestInitializer = request -> {
//            request.setInterceptor(intercepted -> intercepted.getUrl().set("key", API_KEY));
//        };
//
//        return new Sheets.Builder(transport, jsonFactory, httpRequestInitializer)
//                .setApplicationName(APPLICATION_NAME)
//                .build();
//    }
//
//    public static Sheets getSheetsService() throws IOException, GeneralSecurityException {
//        Credential credential = ParseAttendanceSheets.authorize();
//        System.out.println(googleAPIKey);
//        return new Sheets.Builder(
//                GoogleNetHttpTransport.newTrustedTransport(),
//                GsonFactory.getDefaultInstance(),
//                credential)
//                //null).setSheetsRequestInitializer(new SheetsRequestInitializer("AIzaSyBFFksPSn8SgcMykmba8uefA3B93z95JWs"))
//                .setApplicationName(APPLICATION_NAME)
//                .build();
//    }
//
////    public static Credential authorize() throws IOException, GeneralSecurityException {
////        InputStream in = ParseAttendanceSheets.class.getResourceAsStream("/credentials3.json");
////        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(GsonFactory.getDefaultInstance(), new InputStreamReader(in));
////
////        List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS_READONLY);
////
////        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance(), clientSecrets, scopes).setDataStoreFactory(new MemoryDataStoreFactory())
////                .setAccessType("offline").build();
////        Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
////
////        return credential;
////
//////        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JacksonFactory.getDefaultInstance(), new InputStreamReader(in));
//////
//////        List<String> scopes = Collections.singletonList(SheetsScopes.SPREADSHEETS);
//////
//////        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
//////                GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), clientSecrets, scopes)
//////                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(tokensDirectoryPath)))
//////                .setAccessType("offline")
//////                .build();
//////
//////        LocalServerReceiver localServerReceiver = new LocalServerReceiver.Builder().setPort(8888).build();
//////
//////        return new AuthorizationCodeInstalledApp(
//////                flow, localServerReceiver)
//////                .authorize("user");
////    }
//
//    public void parseSheet() throws GeneralSecurityException, IOException {
//        Sheets sheetsService = ParseAttendanceSheets.getSheetsService();
//        List<String> ranges = Arrays.asList("Term 1!C12:D");
//        BatchGetValuesResponse readResult = sheetsService.spreadsheets().values()
//                .batchGet(SPREADSHEET_ID)
//                .setRanges(ranges)
//                .execute();
//
//        Collection<Object> names = readResult.values();
//        List<Object> studentNames = names.stream().collect(Collectors.toList());
//        for (Object name:studentNames
//             ) {
//            System.out.println(name);
//        }
//    }
//
//
//    /** Authorizes the installed application to access user's protected data. */
//    private static Credential authorize() throws Exception {
//        // load client secrets
//        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
//                new InputStreamReader(ParseAttendanceSheets.class.getResourceAsStream("/client_secrets.json")));
//        // set up authorization code flow
//        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
//                httpTransport, JSON_FACTORY, clientSecrets,
//                Collections.singleton(CalendarScopes.CALENDAR)).setDataStoreFactory(dataStoreFactory)
//                .build();
//        // authorize
//        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
//    }
//}
