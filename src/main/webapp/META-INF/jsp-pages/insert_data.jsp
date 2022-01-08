<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<head>
    <title>
    	<c:if test="${course_to_edit == null && location_to_edit == null}">
             <c:if test="${data_type != null && data_type == 'course'}">Inserisci Corso</c:if>
             <c:if test="${data_type != null && data_type == 'location'}">Inserisci Luogo</c:if>
        </c:if>
        <c:if test="${course_to_edit != null || location_to_edit != null}">
             <c:if test="${course_to_edit != null}">Modifica Corso</c:if>
             <c:if test="${location_to_edit != null}">Modifica Luogo</c:if>
        </c:if>
    </title>
    <meta charset="UTF-8">
	<meta content="maximum-scale=1.0, initial-scale=1.0, width=device-width" name="viewport">

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/common.css">
    <link rel="stylesheet" href="/css/insert.css">

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
    <script src="//cdn.jsdelivr.net/npm/sweetalert2@11"></script>	
    <script src="/js/common.js"></script>
    <script src="/js/data-model.js"></script>
    <script src="/js/insertDataField.js"></script>
    <script src="/js/generic_error.js" charset="UTF-8"></script>

<body>
<div class="header"></div>
<div class="padding-div">
    <div class="container">
        <div class="first-column">
              <form>
            	<c:if test="${course_to_edit == null && location_to_edit == null}">
                	<p>Scegli il tipo di dato che desideri inserire:</p>
                </c:if>
                <c:if test="${course_to_edit != null || location_to_edit != null}">
                	<p>Il tipo di dato che si sta modificando è:</p>
                </c:if>
                <div class="radio-item">
                    <input type="radio" id="place" name="kindOfData" value="place" <c:if test="${data_type != null && data_type == 'location'}">checked</c:if>
                    <c:if test="${course_to_edit != null || location_to_edit != null}">
                    	<c:choose>
				    		<c:when test="${location_to_edit != null}">checked</c:when>
				    		<c:otherwise>disabled</c:otherwise>
			    		</c:choose>
			    	</c:if>>
                    <label for="place">Luogo</label><br>
                </div>
                
                <div class="radio-item">
                    <input type="radio" id="course" name="kindOfData" value="course" <c:if test="${data_type != null && data_type == 'course'}">checked</c:if>
                    <c:if test="${course_to_edit != null || location_to_edit != null}">
                    	<c:choose>
				    		<c:when test="${course_to_edit != null}">checked</c:when>
				    		<c:otherwise>disabled</c:otherwise>
			    		</c:choose>
			    	</c:if>>
                    <label for="course">Corso di laurea</label><br>
                </div>
                <div id="kindOfPlace" style="display: none">
                	<c:if test="${location_to_edit == null}">
	                    <p>Seleziona se il tipo di luogo è un'aula:</p>
	                    <input type="checkbox" id="classroom" name="kindOfPlace" value="classroom">
	                    <label for="classroom">Aula</label><br>
                    </c:if>
                </div>
            </form>
        </div>
        <div class="second-column">
            <form>
                <div class="mb-3 mt-3">
                    <label for="dataName" class="form-label">Nome:</label>
                    <input type="text" class="form-control" id="dataName" placeholder="Inserisci il nome..." name="dataName" 
                    <c:if test="${course_to_edit != null}">value="${course_to_edit.name}"</c:if>
                    <c:if test="${location_to_edit != null}">value="${location_to_edit.name}"</c:if>>
                </div>
                <div class="mb-3" id="div-for-professor">
                    <label for="professor">Professore titolare:</label> <br/>
                    <input type="text" class="form-control" id="professor" placeholder="Inserisci il professore titolare..." name="professorField"
                     <c:if test="${course_to_edit != null}">value="${course_to_edit.lecturer.firstName} ${course_to_edit.lecturer.lastName}"</c:if>>
                    <div class="professor-list">
                    </div>
                </div>
                <div class="mb-3" id="div-for-place" style="display: none;">
                    <label for="capacity">Capacità massima:</label> <br/>
                    <input type="number" id="capacity" name="capacity" min="1" max="10000"
                     <c:if test="${location_to_edit == null}">value="100"</c:if>
                     <c:if test="${location_to_edit != null}">value="${location_to_edit.capacity}"</c:if>>
                </div>
                <div class="button-container">
                	<c:if test="${course_to_edit == null && location_to_edit == null}">
                		<button type="submit" id="insert-other-data-button" class="btn btn-outline-primary" disabled>Inserisci dato</button>
                	</c:if>
                	<c:if test="${course_to_edit != null || location_to_edit != null}">
                		<button type="submit" id="insert-other-data-button" class="btn btn-outline-primary" disabled>Modifica dato</button>
                	</c:if>
                    <div class="spinner-border loader" style="display: none;"></div>
                    
                    <a href="<c:if test="${(data_type != null && data_type == 'location') || location_to_edit != null}">/locations</c:if>
                    <c:if test="${(data_type != null && data_type == 'course') || course_to_edit != null}">/courses</c:if>" id="cancel-insertion" class="btn btn-outline-danger danger-button">
                    <c:if test="${course_to_edit == null && location_to_edit == null}">
                		Annulla inserimento
                	</c:if>
                	<c:if test="${course_to_edit != null || location_to_edit != null}">
                		Annulla modifica
                	</c:if>
                    </a>
                </div>
                
                <c:if test="${course_to_edit != null}">
					<input type="hidden" id="id" value="${course_to_edit.id}">
					<input type="hidden" id="lecturer" value="${course_to_edit.lecturer.cf}">
				</c:if>
				<c:if test="${location_to_edit != null}">
					<input type="hidden" id="id" value="${location_to_edit.id}">
				</c:if>
            </form>
        </div>
    </div>
</div>
<div class="footer"></div>
</body>
</html>