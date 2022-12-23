/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author nazif
 */
public class StringHelper {

//    public static String[] splitStringsArray(String originalString, String splitter) {
//        boolean contains = originalString.contains(splitter);
//
//        if (contains) {
//            return originalString.split(splitter);
//        } else {
//            return new String[]{originalString};
//        }
//    }
    
    public static List<String> splitStrings(String originalString, String splitter) {
        boolean contains = originalString.contains(splitter);

        if (contains) {
            return Arrays.asList(originalString.split(splitter));
        } else {
            return Arrays.asList(new String[]{originalString});
        }
    }

}
