package com.russel.web.action;

import com.russel.transport.tcp.CardTcpListener;
import com.russel.web.form.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

/**
 * @author Rassul Hessampour
 * @version $Revision: 1.1.0 $
 */
public class CardTcpMonitorAction extends DispatchAction {

    protected final Log log = LogFactory.getLog(getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {
        if (isCancelled(request)) {
            try {
                getMethod ("cancel");
                return dispatchMethod(mapping, form, request, response, "cancel");
            } catch (NoSuchMethodException n) {
                log.warn("No 'cancel' method found, returning null");
                return cancelled(mapping, form, request, response);
            }
        }

        // Check to see if methodName indicated by request parameter
        String actionMethod = getActionMethodWithMapping(request, mapping);

        if (actionMethod != null) {
            return dispatchMethod(mapping, form, request, response, actionMethod);
        } else {
            String[] rules = {"edit", "save", "send", "view"};
            for (String rule : rules) {
                // apply the rules for automatically appending the method name
                if (request.getServletPath().indexOf(rule) > -1) {
                    return dispatchMethod(mapping, form, request, response, rule);
                }
            }
        }

        return super.execute(mapping, form, request, response);
    }

    private String getActionMethodWithMapping(HttpServletRequest request, ActionMapping mapping) {
        return getActionMethod(request, mapping.getParameter());
    }

    /**
     * Gets the method name based on the prepender passed to it.
     */
    protected String getActionMethod(HttpServletRequest request, String prepend) {
        String name = null;

        // for backwards compatibility, try with no prepend first
        name = request.getParameter(prepend);
        if (name != null) {
            // trim any whitespace around - this might happen on buttons
            name = name.trim();
            // lowercase first letter
            return name.replace(name.charAt(0), Character.toLowerCase(name.charAt(0)));
        }

        Enumeration e = request.getParameterNames();

        while (e.hasMoreElements()) {
            String currentName = (String) e.nextElement();

            if (currentName.startsWith(prepend + ".")) {
                if (log.isDebugEnabled()) {
                    log.debug("calling method: " + currentName);
                }

                String[] parameterMethodNameAndArgs = StringUtils.split(currentName, ".");
                name = parameterMethodNameAndArgs[1];
                break;
            }
        }

        return name;
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm form,
                              HttpServletRequest request,
                              HttpServletResponse response)
            throws Exception {
        return (mapping.findForward("cancel"));
    }

    public ActionForward startMonitor(ActionMapping mapping, ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response)
            throws Exception {
        CardTcpMonitorActionForm cardTcpMonitorActionForm = (CardTcpMonitorActionForm)form;
        CardTcpListener cardTcpListener = new CardTcpListener(Integer.valueOf(cardTcpMonitorActionForm.getCardTcpListenerPort()));
        cardTcpListener.start();
        return (mapping.findForward("startMonitor"));
    }

    public ActionForward stopMonitor(ActionMapping mapping, ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response)
            throws Exception {
        CardTcpMonitorActionForm cardTcpMonitorActionForm = (CardTcpMonitorActionForm)form;
        System.out.println("*************************Stop Monitor*****************");
        return (mapping.findForward("stopMonitor"));
    }

}