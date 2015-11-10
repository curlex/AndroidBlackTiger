//package abt.androidblacktiger;
//
//import com.google.android.gms.location.places.Place;
//import com.google.api.client.http.GenericUrl;
//import com.google.api.client.http.HttpRequest;
//import com.google.api.client.http.HttpResponseException;
//
//import javax.servlet.http.*;
///**
// * Created by User on 02/11/2015.
// */
//public class PlaceSearch {
//    public void performSearch() throws Exception {
//        try {
//            System.out.println("Perform Search ....");
//            System.out.println("-------------------");
//            HttpReuqestFactory httpRequestFactory = createRequestFactory(transport);
//            HttpRequest request = httpRequestFactory.buildGetRequest(new GenericUrl(PLACES_SEARCH_URL));
//            request.url.put("key", API_KEY);
//            request.url.put("location", latitude + "," + longitude);
//            request.url.put("radius", 500);
//            request.url.put("sensor", "false");
//
//            if (PRINT_AS_STRING) {
//                System.out.println(request.execute().parseAsString());
//            } else {
//
//                PlacesList places = request.execute().parseAs(PlacesList.class);
//                System.out.println("STATUS = " + places.status);
//                for (Place place : places.results) {
//                    System.out.println(place);
//                }
//            }
//
//        } catch (HttpResponseException e) {
//            System.err.println(e.response.parseAsString());
//            throw e;
//        }
//    }
//
//}