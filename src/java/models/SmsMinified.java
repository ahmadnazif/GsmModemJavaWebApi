/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 *
 * @author nazif
 */
public class SmsMinified implements Serializable {
    
    @SerializedName(value = "transId", alternate = {"TransId", "transid", "TRANSID"})
    public String transId;
    
    @SerializedName(value = "from", alternate = {"From", "FROM"})
    public String from;
    
    @SerializedName(value = "to", alternate = {"To", "TO"})
    public String to;
    
    @SerializedName(value = "content", alternate = {"Content", "CONTENT"})
    public  String content; 
}
