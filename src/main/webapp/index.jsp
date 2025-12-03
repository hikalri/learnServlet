<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<h2>Hello World!</h2>
<p>当前在线人数: ${applicationScope.onlineCount == null ? 0 : applicationScope.onlineCount}</p>
</body>
</html>
