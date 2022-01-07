<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<head>
    <title>Insert data</title>
    <meta charset="UTF-8">

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
                <p>Scegli il tipo di dato che desideri inserire:</p>
                <div class="radio-item">
                    <input type="radio" id="place" name="kindOfData" value="place" <c:if test="${data_type != null && data_type == 'location'}">checked</c:if>>
                    <label for="place">Luogo</label><br>
                </div>
                
                <div class="radio-item">
                    <input type="radio" id="course" name="kindOfData" value="course" <c:if test="${data_type != null && data_type == 'course'}">checked</c:if>>
                    <label for="course">Corso di laurea</label><br>
                </div>
                <div id="kindOfPlace" style="display: none">
                    <p>Seleziona se il tipo di luogo è un'aula:</p>
                    <input type="checkbox" id="classroom" name="kindOfPlace" value="classroom">
                    <label for="classroom">Aula</label><br>
                </div>
            </form>
        </div>
        <div class="second-column">
            <form>
                <div class="mb-3 mt-3">
                    <label for="dataName" class="form-label">Nome:</label>
                    <input type="text" class="form-control" id="dataName" placeholder="Inserisci il nome..." name="dataName">
                </div>
                <div class="mb-3" id="div-for-professor">
                    <label for="professor">Professore titolare:</label> <br/>
                    <input type="text" class="form-control" id="professor" placeholder="Inserisci il professore titolare..." name="professorField">
                    <div class="professor-list">
                    </div>
                </div>
                <div class="mb-3" id="div-for-place" style="display: none;">
                    <label for="capacity">Capacità massima:</label> <br/>
                    <input type="number" id="capacity" name="capacity" min="1" max="10000" value="100">
                </div>
                <div class="button-container">
                    <button type="submit" id="insert-other-data-button" class="btn btn-outline-primary" disabled>Inserisci dato</button>
                    <div class="spinner-border loader" style="display: none;"></div>
                    
                    <a href="<c:if test="${data_type != null && data_type == 'location'}">/locations</c:if>
                    <c:if test="${data_type != null && data_type == 'course'}">/courses</c:if>" id="cancel-insertion" class="btn btn-outline-danger danger-button">Annulla inserimento</a>
                </div>
            </form>
        </div>
    </div>
</div>
<div class="footer"></div>
</body>
</html>