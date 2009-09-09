<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%--
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
    <head>
        <title>Certificate generation</title>
    </head>
    <body>
        NO NATIONAL CAHARACTERS!
        <form action="" method="post">
            Tile:<input type="text" name="X509Principal.T" value="${T}"/><br/>
            Country:<input type="text" name="X509Principal.C" value="LT"  maxlength="2"/><br/>
            Organisation:<input type="text" name="X509Principal.O"  maxlength="64"/><br/>
            Organisation Unit:<input type="text" name="X509Principal.OU"  maxlength="64"/><br/>
            Locality:<input type="text" name="X509Principal.L" maxlength="64"/><br/>
            Common Name:<input type="text" name="X509Principal.CN" value="${USER}"  maxlength="64"/><br/>
            EmailAddress:<input type="text" name="X509Principal.EmailAddress" value="${EMAIL}"/><br/>
            Password:<input type="text" name="X509Principal.PWD"/><br/>
            <input type="submit"/>
        </form>
    </body>
</html>
