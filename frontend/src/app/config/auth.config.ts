import { AuthConfig } from "angular-oauth2-oidc";
import { environment } from "../environments/environment";

export const authConfig: AuthConfig = {
    issuer: environment.issuerUrl,
    loginUrl: environment.issuerUrl + '/oauth2/authorize',
    tokenEndpoint: environment.issuerUrl + '/oauth2/token',
    redirectUri: window.location.origin + '/authorized',

    clientId: environment.clientId,
    dummyClientSecret: environment.clientSecret,
    useHttpBasicAuth: true,

    responseType: 'code',
    disablePKCE: true
}
