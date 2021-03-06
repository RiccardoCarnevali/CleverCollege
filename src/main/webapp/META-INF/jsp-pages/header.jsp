<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:if test="${user == null}">${user_type = null}</c:if>

<a id="logo" href="/"> <img alt="Clever College" src="/assets/images/cc-logo.png"></a>

<div class="header-top"></div>
<div class="navbar navbar-light navbar-expand-lg">
	<button class="navbar-toggler" type="button" data-toggle="collapse"
		data-target="#navbarMain" aria-controls="navbarMain"
		aria-expanded="false" aria-label="Toggle navigation">
		<span class="navbar-toggler-icon"></span>
	</button>
	<div class="collapse navbar-collapse" id="navbarMain">
		<ul class="navbar-nav">
			<li class="nav-item"><a class="nav-link" href="/">Home</a></li>
			<c:if test="${user_type != null && user_type == 'professor'}">
				<li class="nav-item">
					<a class="nav-link" href="/activities/handle_activities">Attività</a>
				</li>
			</c:if>
			
			<c:if test="${user_type != null && user_type == 'student'}">
				<li class="nav-item dropdown">
					<a class="nav-link dropdown-toggle clickable" id="navbarDropdown" 
					role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">Attività</a>
					<div class="dropdown-menu" aria-labelledby="navbarDropdown">
						<a class="dropdown-item" href="/activities/my-bookings">Le mie prenotazioni</a>
						<a class="dropdown-item" href="/activities/book-lessons">Lezioni</a>
						<a class="dropdown-item" href="/activities/book-seminars">Seminari</a>
					</div>
				</li>
			</c:if>
			
			
			<c:if test="${user_type != null && user_type == 'admin'}">
				<li class="nav-item dropdown">
					<a class="nav-link dropdown-toggle clickable" id="navbarDropdown" 
					role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">Gestisci dati</a>
					<div class="dropdown-menu" aria-labelledby="navbarDropdown">
						<a class="dropdown-item" href="/users">Utenti</a>
						<a class="dropdown-item" href="/courses">Corsi</a>
						<a class="dropdown-item" href="/locations">Luoghi</a>
					</div>
				</li>
			</c:if>
			<c:if test="${user != null}">
				<li class="nav-item">
					<a href="/check-in" class="nav-link">Check-in</a>
				</li>
				<li class="nav-item">
					<a href="/myprofile" class="nav-link">Area personale</a>
				</li>
			</c:if>

			<c:if test="${user != null}">
				<li class="nav-item ml-auto">
					<div>
						<c:if test="${user_type != null && user_type != 'admin'}">
							<a href="/allMyChat" class="nav-link far fa-comment" style="font-size:25px"></a>
						</c:if>
						<span class="clickable nav-link" id="notification-bell"></span>
					</div>
				</li>
			</c:if>
			
			<li class="nav-item <c:if test="${user == null}">ml-auto</c:if>">
				<a <c:if test="${user != null}">href="/doLogout"</c:if> <c:if test="${user == null}">href="/login"</c:if>
					 class="btn btn-outline-primary" id="loginButton">
				<c:if test="${user != null}">Logout</c:if>
				<c:if test="${user == null}">Login</c:if>
				</a>
			</li>
		</ul>
	</div>
</div>