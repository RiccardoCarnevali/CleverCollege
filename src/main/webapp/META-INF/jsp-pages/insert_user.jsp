<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<head>
    <title>Insert user</title>
    <meta charset="UTF-8">

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/common.css">
    <link rel="stylesheet" href="/css/insert.css">

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
	<script src="//cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script src="/js/common.js"></script>
    <script src="/js/data-model.js"></script>
    <script src="/js/insertUserField.js"></script>
    <script src="/js/generic_error.js" charset="UTF-8"></script>

<body>
<div class="header"></div>
<div class="padding-div">
    <div class="container">
       	<div class="first-column">
            <form>
                <p>    
                	<c:if test="${type_to_edit == null}">Scegli il tipo di utente che desideri inserire:</c:if>
    				<c:if test="${type_to_edit != null}">Il tipo di utente che si sta modificando è:</c:if>
    			</p>
                <div class="radio-item">
                    <input type="radio" id="student" name="kindOfUser" value="student" <c:if test="${type_to_edit == null}">checked</c:if>
					<c:if test="${type_to_edit != null}">
                    	<c:choose>
				    		<c:when test="${type_to_edit == 'student'}">checked</c:when>
				    		<c:otherwise>disabled</c:otherwise>
			    		</c:choose>
			    	</c:if>>
                    <label for="student">Studente</label><br>
                </div>
                <div class="radio-item">
                    <input type="radio" id="professor" name="kindOfUser" value="professor" 
                    <c:if test="${type_to_edit != null}">
                    	<c:choose>
				    		<c:when test="${type_to_edit == 'professor'}">checked</c:when>
				    		<c:otherwise>disabled</c:otherwise>
			    		</c:choose>
			    	</c:if>>
                    <label for="professor">Professore</label><br>
                </div>
                <div class="radio-item">
                    <input type="radio" id="admin" name="kindOfUser" value="admin" 
					<c:if test="${type_to_edit != null}">
                    	<c:choose>
				    		<c:when test="${type_to_edit == 'administrator'}">checked</c:when>
				    		<c:otherwise>disabled</c:otherwise>
			    		</c:choose>
			    	</c:if>>
                    <label for="admin">Amministratore</label>
                </div>
           	</form> 
       	</div>
        <div class="
        			<c:if test="${user_to_edit == null}">second-column</c:if>
        			<c:if test="${user_to_edit != null}">only-column</c:if>   ">
            <form>
                <div class="mb-3 mt-3">
                    <label for="fiscalCodeUser" class="form-label">Codice fiscale:</label>
                    <input type="text" class="form-control insert-user-element" id="fiscalCodeUser" placeholder="Inserisci il codice fiscale..." name="cf" 
                    <c:if test="${user_to_edit != null}">value="${user_to_edit.cf}" disabled</c:if>>
                </div>
                <div class="mb-3">
                    <label for="name" class="form-label">Nome:</label>
                    <input type="text" class="form-control insert-user-element" id="name" placeholder="Inserisci il nome..." name="name"
                     <c:if test="${user_to_edit != null}">value="${user_to_edit.firstName}"</c:if>>
                </div>
                <div class="mb-3">
                    <label for="surname" class="form-label">Cognome:</label>
                    <input type="text" class="form-control insert-user-element" id="surname" placeholder="Inserisci il cognome..." name="surname"
                     <c:if test="${user_to_edit != null}">value="${user_to_edit.lastName}"</c:if>>
                </div>
                <div class="mb-3">
                    <label for="email" class="form-label">Email istituzionale:</label>
                    <input type="email" class="form-control insert-user-element" id="email" placeholder="Inserisci l'email..." name="email"
                     <c:if test="${user_to_edit != null}">value="${user_to_edit.email}"</c:if>>
                </div>
                <div class="mb-3">
                    <label id="label-for-Student" for="idStudent" class="form-label" style="display: block">Matricola:</label>
                    <input type="text" class="form-control insert-user-element" id="idStudent" placeholder="Inserisci la matricola..." name="idStudent" style="display: block"
                     <c:if test="${user_to_edit != null && type_to_edit != null && type_to_edit == 'student'}">value="${user_to_edit.studentNumber}"</c:if>>
                </div>
                <div class="button-container">
                	<c:if test="${user_to_edit == null}">
                    	<button type="button" id="insert-user-button" class="btn btn-outline-primary" disabled>Inserisci utente</button>
                    </c:if>
                    <c:if test="${user_to_edit != null}">
                    	<button type="button" id="insert-user-button" class="btn btn-outline-primary">Modifica utente</button>
                    </c:if>
                    <div class="spinner-border loader" style="display: none;"></div>
                    <c:if test="${user_to_edit == null}">
                    	<button type="button" id="cancel-insertion" class="btn btn-outline-danger danger-button">Annulla inserimento</button>
                    </c:if>
                    <c:if test="${user_to_edit != null}">
                    	<button type="button" id="cancel-insertion" class="btn btn-outline-danger danger-button">Annulla modifica</button>
                    </c:if>
                </div>
                <input type="hidden" id="update" value="
                <c:if test="${user_to_edit == null}">false</c:if>
                <c:if test="${user_to_edit != null}">true</c:if>">
            </form>
        </div>
    </div>
</div>
<div class="footer"></div>
</body>
</html>