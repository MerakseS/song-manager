import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from "../../services/authentication.service";
import { ActivatedRoute, Router } from "@angular/router";

@Component({
    selector: 'app-authorized',
    template: '',
    providers: [AuthenticationService]
})
export class AuthorizedComponent implements OnInit {

    constructor(private authService: AuthenticationService,
        private activatedRoute: ActivatedRoute,
        private router: Router) {
    }

    ngOnInit(): void {
        this.activatedRoute.queryParams.subscribe(params => {
            const code = params['code'];
            this.authService.authorize(code)
                .then(() => {
                    console.log(this.authService.token);
                    return this.router.navigate(['songs']);
                })
        })
    }
}
