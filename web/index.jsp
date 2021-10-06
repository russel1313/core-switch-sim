<%@ page language="java" pageEncoding="windows-1256" contentType="text/html;charset=windows-1256" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>

<html:html>
  <head><title>Tcp Monitor Start Page</title></head>
  <body>
        <br>
        <br>
        <a href="CardTcpMonitor.jsp"><u>Tcp Monitor</u></a>
        <br>
        <br>
        <font  size="1" color="gray">Release Date: 2021/10/06</font>
        <logic:present name="response">
            <P>Response : </P> 
            <bean:write name="response"></bean:write>
        </logic:present>
  </body>
</html:html>