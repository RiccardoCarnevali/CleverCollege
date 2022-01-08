function GetMap() {
    var map = new Microsoft.Maps.Map('#myMap', {
        credentials : config.BING_KEY
    });

    map.setView({
        mapTypeId: Microsoft.Maps.MapTypeId.street,
        center: new Microsoft.Maps.Location(39.357743, 16.226688),
        zoom: 15
    });

    var pin = new Microsoft.Maps.Pushpin(map.getCenter(), {
        title: 'Università della Calabria',
        subTitle: 'Facoltà di informatica',
        text: ''
    });

    map.entities.push(pin);
}