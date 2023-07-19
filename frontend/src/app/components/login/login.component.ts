import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from "../../services/authentication.service";

@Component({
    selector: 'app-login',
    template: '',
    providers: [AuthenticationService]
})
export class LoginComponent implements OnInit {

    constructor(private authService: AuthenticationService) {
    }

    ngOnInit(): void {
        this.authService.initLogin()
    }
}
