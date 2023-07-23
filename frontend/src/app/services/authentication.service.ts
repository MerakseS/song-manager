import { Injectable } from '@angular/core';
import { OAuthService } from "angular-oauth2-oidc";
import { authConfig } from "../config/auth.config";
import jwtDecode from "jwt-decode";

@Injectable({
    providedIn: 'root'
})
export class AuthenticationService {

    constructor(private oauthService: OAuthService) {
        oauthService.configure(authConfig);
    }

    public get token() {
        return this.oauthService.getAccessToken();
    }

    public get roles() {
        const claims = jwtDecode(this.token);
        return claims['roles'];
    }

    public initLogin() {
        this.oauthService.initCodeFlow();
    }

    public authorize(code: string) {
        return this.oauthService.fetchTokenUsingGrant('authorization_code', {
                code,
                'redirect_uri': this.oauthService.redirectUri
            }
        );
    }

    public isAuthenticated() {
        return this.oauthService.hasValidAccessToken();
    }

    public hasAuthority(role: string) {
        return this.roles.includes(role);
    }

    public logout() {
        this.oauthService.logOut({_method: "POST"});
    }
}
