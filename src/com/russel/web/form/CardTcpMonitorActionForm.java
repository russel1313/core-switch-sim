package com.russel.web.form;

import org.apache.struts.action.ActionForm;

/**
 * @author Rassul Hessampour
 * @version $Revision: 1.1.0 $
 */
public class CardTcpMonitorActionForm extends ActionForm {

    private String cardTcpListenerPort;

    public String getCardTcpListenerPort() {
        return cardTcpListenerPort;
    }

    public void setCardTcpListenerPort(String cardTcpListenerPort) {
        this.cardTcpListenerPort = cardTcpListenerPort;
    }
}