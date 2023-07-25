export const environment = {
    baseUrl: window['env']['baseUrl'] || 'http://localhost:8765/songs',
    issuerUrl: window['env']['issuerUrl'] || 'http://localhost:9000',
    clientId: window['env']['clientId'] || 'songs-client',
    clientSecret: window['env']['clientSecret'] || 'secret'
};
