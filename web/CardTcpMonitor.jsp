<%@ page language="java" pageEncoding="windows-1256" contentType="text/html;charset=windows-1256" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>

<html:html>
  <head><title>Tcp Monitor</title></head>
  <body>
        <br>
        <p>
            Tcp Monitor Test
        </p>
        <html:form action="/cardTcpMonitor" method="POST">
            <p style="color:red;">Tcp Monitor Fields</p>
            <br>
            <br>
            <table>
                <tr>
                    <td>
                        Server Listening Port :
                    </td>
                    <td>
                        <html:text property="cardTcpListenerPort"/>
                    </td>
                </tr>
            </table>
            <br>
            <br>
            <html:submit property="method.startMonitor" title="start" value="Start">
            </html:submit>
            <html:submit property="method.stopMonitor" title="stop" value="Stop">
        </html:submit>
            <html:submit property="method.cancel" title="cancel" value="Cancel">
            </html:submit>
        </html:form>
        <br>
        <font  size="1" color="gray">Release Date: 2021/10/06</font>
  </body>
</html:html>