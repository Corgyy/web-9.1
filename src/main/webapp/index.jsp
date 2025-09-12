<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <title>Home</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css"/>
</head>
<body class="bg">
  <div class="center">
    <div class="card glass" style="text-align:center">
      <h1 class="title">Welcome!</h1>
      <div class="row" style="justify-content:center; margin-top:20px">
        <a class="btn primary" href="${pageContext.request.contextPath}/login.jsp">Sign in</a>
        <a class="btn" href="${pageContext.request.contextPath}/register.jsp">Sign up</a>
      </div>
    </div>
  </div>
</body>
</html>
