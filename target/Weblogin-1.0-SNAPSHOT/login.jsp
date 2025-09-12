<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <title>Sign in</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
</head>
<body class="bg">
  <div class="center">
    <div class="card glass" style="text-align:center">
      <h1 class="title">Sign in</h1>

      <form class="form" method="post"
            action="${pageContext.request.contextPath}/auth?action=login"
            accept-charset="UTF-8">
        <label>Email</label>
        <input class="input" type="email" name="email" placeholder="e.g. user@example.com" required/>
        <label style="margin-top:12px">Password</label>
        <input class="input" type="password" name="password" placeholder="••••••" required/>
        <button class="btn primary" type="submit" style="margin-top:14px">Sign in</button>
      </form>

<%
  String error = request.getParameter("error");
  if (error != null) {
    String msg;
    switch (error) {
      case "email_not_exist": msg = "Email does not exist."; break;
      case "wrong_password":  msg = "Wrong password."; break;
      default:                msg = error;
    }
%>
  <div class="alert error"><%= msg %></div>
<% } %>

      <div class="helper">Don't have an account?
        <a href="${pageContext.request.contextPath}/register.jsp">Sign up</a>
      </div>
    </div>
  </div>
</body>
</html>
