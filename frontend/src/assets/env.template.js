(function (window) {
    window['env'] = window['env'] || {};

    window['env']['baseUrl'] = '${BASE_URL}';
    window['env']['issuerUrl'] = '${ISSUER_ENDPOINT}';
    window['env']['clientId'] = '${CLIENT_ID}';
    window['env']['clientSecret'] = '${CLIENT_SECRET}';
})(this);
