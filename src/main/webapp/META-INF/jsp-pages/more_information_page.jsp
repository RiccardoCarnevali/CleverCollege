<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
	<head>
	    <meta charset="UTF-8">
	    <meta content="maximum-scale=1.0, initial-scale=1.0, width=device-width" name="viewport">
	
	    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
	    <link rel="stylesheet" href="/css/common.css">
	    <link rel="stylesheet" href="/css/information.css">
	
	    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
	    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
	    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
	    <script src="//cdn.jsdelivr.net/npm/sweetalert2@11"></script>
	    <script type='text/javascript' src='http://www.bing.com/api/maps/mapcontrol?callback=GetMap' async defer></script>
	
	    <script src="https://www.gstatic.com/firebasejs/8.2.4/firebase-app.js"></script>
		<script src="https://www.gstatic.com/firebasejs/8.2.4/firebase-messaging.js"></script>
		<script src="/js/notification.js" charset="UTF-8"></script>
		<script src="/js/common.js"></script>
		<script src="/js/generic_error.js" charset="UTF-8"></script>
	    <script src="/js/config.js"></script>
	    <script src="/js/createMap.js"></script>
	
	    <title>About us</title>
	</head>
	<body>
	    <div class="header"></div>
	        <div>
	            <div class="container">
	                <div>
	                    <h3>Chi siamo?</h3>
	                </div>
	                <div>
	                    <p>Siamo quattro studenti del terzo anno della facoltà di informatica dell'Università della Calabria
	                        (UNICAL). Il nostro gruppo è formato da tre ragazzi, Riccardo, Matteo e Giacomo, e una ragazza,
	                        Alessia Donata, tutti e quattro impegnati equamente nella realizzazione di questo progetto,
	                        creato e implementato come requisito per gli esami di Web Computing e Ingegneria del Software.
	                    </p>
	                </div>
	            </div>
	            <div class="container">
	                <div>
	                    <h3>Dove trovarci?</h3>
	                </div>
	                <div id="myMap"></div>
	            </div>
	            <div class="container">
	                <div>
	                    <h3>Da dove nasce l'idea?</h3>
	                </div>
	                <div>
	                    <p>Per monitorare i contagi e i possibili focolai all'interno del Campus, la nostra Università ha messo
	                        a nostra disposizione un'applicazione di nome Smart Campus, utile per tracciare gli spostamenti
	                        degli studenti all'interno dei luoghi universitari. Tale applicazione, però, non è gradita da tutti
	                        gli studenti della nostra università, così abbiamo deciso di provare a realizzare un sito web simile
	                        a tale progetto ma che si differenziasse in ciò che gli studenti credono una "lacuna". Quel che è
	                        venuto fuori è Clever College.
	                    </p>
	                </div>
	            </div>
	            <div class="container">
	                <div>
	                    <h3>Come contattarci?</h3>
	                </div>
	                <div>
	                    <p>Puoi contattarci per segnalare eventuali anomalie o suggerimenti presso tale email:
	                        <a href="mailto:alessiadonatacamarda@gmail.com" id="mailTo">alessiadonatacamarda@gmail.com</a>
	                    </p>
	                </div>
	            </div>
	        </div>
	    <div class="footer"></div>
	</body>
</html>