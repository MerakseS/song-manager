import { Injectable } from '@angular/core';
import { OAuthService } from "angular-oauth2-oidc";
import { authConfig } from "../config/auth.config";

@Injectable({
    providedIn: 'root'
})
export class AuthenticationService {

    constructor(private oauthService: OAuthService) {
        oauthService.configure(authConfig);
    }

    get token() {
        return this.oauthService.getAccessToken();
    }

    public isAuthenticated() {
        return this.oauthService.hasValidAccessToken();
    }

    public initLogin() {
        this.oauthService.initCodeFlow();
    }

    public authorize(code: string) {
        return this.oauthService.fetchTokenUsingGrant('authorization_code',
            {
                code,
                'redirect_uri': this.oauthService.redirectUri
            }
        );
    }
}
