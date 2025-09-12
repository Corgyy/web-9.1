<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <title>Sign up</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css"/>
</head>
<body class="bg">
  <div class="center">
    <div class="card glass" style="text-align:center">
      <h1 class="title">Sign up</h1>

      <form class="form" method="post"
            action="${pageContext.request.contextPath}/auth?action=register"
            accept-charset="UTF-8">
        <label>Email</label>
        <input class="input" type="email" name="email" placeholder="e.g. user@example.com" required/>

        <label style="margin-top:12px">Password</label>
        <input class="input" type="password" name="password" placeholder="At least 1 character" required/>

        <div class="actions">
          <button class="btn primary" type="submit">Sign up</button>
        </div>
      </form>

      <%
        String ok = request.getParameter("ok");
        String err = request.getParameter("error");
        if (ok != null) {
      %>
        <div class="alert">Sign up successful! You can sign in now.</div>
      <% } else if (err != null && !err.isEmpty()) { %>
        <div class="alert error"><%= err %></div>
      <% } %>

      <div class="helper">Already have an account?
        <a href="${pageContext.request.contextPath}/login.jsp">Sign in</a>
      </div>
    </div>
  </div>
</body>
</html>
