<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<head>
    <meta charset="UTF-8">

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <link rel="stylesheet" href="/css/header.css">
    <link rel="stylesheet" href="/css/index.css">

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
    <script src="/js/header.js"></script>
</head>
<body>
<div class="header"></div>
<div class="container">
    <form method="post" action="doLogin">
        <div id="first-column" style="min-height: 1px">

        </div>
        <div>
            <div id="second-column">
                <div class="mb-3 mt-3">
                    <label for="fiscalCode" class="form-label">Codice fiscale:</label>
                    <input type="text" class="form-control" id="fiscalCode" placeholder="Inserisci il tuo codice fiscale..." name="cf">
                </div>
                <div class="mb-3">
                    <label for="password" class="form-label">Password:</label>
                    <input type="password" class="form-control" id="password" placeholder="Inserisci la tua password..." name="password">
                </div>
                <div class="form-check mb-3">
                    <label class="form-check-label">
                        <input class="form-check-input" type="checkbox" name="rememberMe"> Ricorda le mie credenziali
                    </label>
                </div>
                <button type="submit" class="btn btn-outline-primary">Login</button>
            </div>
            <div id="third-column"></div>
        </div>
    </form>
</div>
<footer class="container-fluid">
    Lorem ipsum dolor sit amet, consectetur
    adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore
    magna aliqua. Lectus mauris ultrices eros in cursus turpis. Egestas
    sed tempus urna et pharetra pharetra massa. Massa sapien faucibus et
    molestie ac feugiat sed. Maecenas ultricies mi eget mauris pharetra
    et. Tellus mauris a diam maecenas sed enim ut sem.
</footer>
</body>
</html>