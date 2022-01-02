<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<a id="logo" href="/"> <img alt="Clever College"
							src="/assets/images/cc-logo.png">
</a>

<div class="header-top"></div>
<div class="navbar navbar-light navbar-expand-lg">
	<button class="navbar-toggler" type="button" data-toggle="collapse"
		data-target="#navbarMain" aria-controls="navbarMain"
		aria-expanded="false" aria-label="Toggle navigation">
		<span class="navbar-toggler-icon"></span>
	</button>
	<div class="collapse navbar-collapse" id="navbarMain">
		<ul class="navbar-nav mr-auto">
			<li class="nav-item"><a class="nav-link" href="/">Home</a></li>
			<li class="nav-item"><a class="nav-link<c:if test="${user_type == 'admin' || user_type == null}"> hide</c:if>" 
				href=<c:if test="${user == null}">"/login"</c:if>
					<c:if test="${user != null && user_type == 'professor'}">"/activities/handle_activities"</c:if>>Attività</a></li>
			<li class="nav-item dropdown"><a
				class="nav-link dropdown-toggle" href="#" id="navbarDropdown"
				role="button" data-toggle="dropdown" aria-haspopup="true"
				aria-expanded="false"> Dropdown </a>
				<div class="dropdown-menu" aria-labelledby="navbarDropdown">
					<a class="dropdown-item" href="#">Action 1</a> <a
						class="dropdown-item" href="#">Action 2</a>
				</div>
			</li>
			<li class="nav-item">
				<a <c:if test="${user != null}">href="/doLogout"</c:if> <c:if test="${user == null}">href="/login"</c:if>
					 class="btn btn-outline-primary" id="loginButton">
				<c:if test="${user != null}">Logout</c:if>
				<c:if test="${user == null}">Login</c:if>
			</a>
			</li>
		</ul>
	</div>
</div>