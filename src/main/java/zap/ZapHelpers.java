package zap;

import com.sun.javafx.binding.StringFormatter;
import org.zaproxy.clientapi.core.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.utils.ReportGenerator.saveReport;

public class ZapHelpers {
    public static String ZAP_ADDRESS= "localhost";
    public static int ZAP_PORT=8090;
    public static String  ZAP_API_KEY=null;
//    public static final String TARGET = "http://localhost:3000";


    public  static void aScanUrl(String TARGET)  {
        ClientApi api = new ClientApi(ZAP_ADDRESS, ZAP_PORT, ZAP_API_KEY);
        List urls=new ArrayList();
       try{
           api.spider.scan(TARGET,"2","2",null,null);
           ApiResponseList response= (ApiResponseList) api.spider.allUrls();
           urls=response.getItems();
       }catch (Exception e){
           System.out.println("Exception : " + e.getMessage());
           e.printStackTrace();
       }


        try {
            String scanid;
            int progress;

            ApiResponse resp1 = api.pscan.enableAllScanners();
            List<String> urlList= (List<String>) urls.stream().map(url -> {
                String text=url.toString();
                if (!(text.contains("node_modules")||text.contains(".js")||text.contains(".css")||text.contains("/images/"))){
                    return text;
                }else{return null;}}).collect(Collectors.toList());
            urlList=urlList.stream().filter(value -> value != null).collect(Collectors.toList());

            for (String url: urlList) {
                    Thread.sleep(2000);
                    System.out.println("Active scan : " + url);
                    ApiResponse resp = api.ascan.scan(url, "False", "True", null, null, null);
                    // The scan now returns a scan id to support concurrent scanning
                    scanid = ((ApiResponseElement) resp).getValue();
                    // Poll the status until it completes
                    while (true) {
                        Thread.sleep(5000);
                        progress = Integer.parseInt(((ApiResponseElement) api.ascan.status(scanid)).getValue());
                        System.out.println("Active Scan progress : " + progress + "%");
                        if (progress >= 100) {
                            break;
                        }
                    }
                }




            System.out.println("#################Active Scan complete############");
            System.out.println("Alerts:");
            System.out.println("################XML report####################");
            System.out.println(new String(api.core.xmlreport()));
            System.out.println("################HTML report####################");
            System.out.println(new String(api.core.htmlreport()));
            System.out.println("################JSON report####################");
            System.out.println(new String(api.core.jsonreport()).toString());

            String newFile="report";
            String content=new String(api.core.htmlreport());
            saveReport(newFile,content);

        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
            e.printStackTrace();
        }
    }

//    public  static void pScanUrl(String TARGET) {
//        ClientApi api = new ClientApi(ZAP_ADDRESS, ZAP_PORT, ZAP_API_KEY);
//        try {
//            System.out.println("Spider : " + TARGET);
//            String scanid;
//            int progress;
//            Thread.sleep(2000);
//
//            System.out.println("Active scan : " + TARGET);
////                    ascan.scan(TARGET, "False", "True", null, null, null);
//
//            // The scan now returns a scan id to support concurrent scanning
////            scanid = ((ApiResponseElement) resp).getValue();
//
//            // Poll the status until it completes
//            while (true) {
////                Thread.sleep(5000);
////                progress = Integer.parseInt(((ApiResponseElement) api.ascan.status(scanid)).getValue());
////                System.out.println("Active Scan progress : " + progress + "%");
////                if (progress >= 100) {
//                    break;
//                }
//            }
//            System.out.println("#################Active Scan complete############");
//            System.out.println("Alerts:");
//            System.out.println(new String(api.core.xmlreport()));
//            System.out.println("################XML report####################");
//            System.out.println(new String(api.core.htmlreport()));
//            System.out.println("################HTML report####################");
//            System.out.println(new String(api.core.jsonreport()));
//            System.out.println("################JSON report####################");
//
//            String newFile="report";
//            String content=new String(api.core.htmlreport());
//            saveReport(newFile,content);
//
//        } catch (Exception e) {
//            System.out.println("Exception : " + e.getMessage());
//            e.printStackTrace();
//        }
//    }

}
