/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.handlers;

import java.util.Collections;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import org.w3c.dom.NodeList;

/**
 *
 * @author michellanet
 */
public class DisneyFilterHandler implements SOAPHandler<SOAPMessageContext> {
    
    public boolean handleMessage(SOAPMessageContext messageContext) {
        
        boolean outbound = (boolean)messageContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
    if(!outbound){
        SOAPMessage msg = messageContext.getMessage();
        try {
            
            NodeList titles = msg.getSOAPBody().getElementsByTagName("title");
            
            if(titles.item(0) != null){
                if(titles.item(0).getTextContent().toLowerCase().contains("disney")){
                    return false;
                }
            }
            
            
        } catch (SOAPException ex) {
            Logger.getLogger(DisneyFilterHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    else{
    }
        return true;
    }
    
    public Set<QName> getHeaders() {
        return Collections.EMPTY_SET;
    }
    
    public boolean handleFault(SOAPMessageContext messageContext) {
        return false;
    }
    
    public void close(MessageContext context) {
    }
    
}
