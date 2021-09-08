import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/_services/auth.service';
import { TokenStorageService } from 'src/app/_services/token-storage.service';
import { ActivatedRoute, Params} from '@angular/router';
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  form: any = {
    username: null,
    password: null
  };
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';
  roles: string[] = [];
tokenavailable:Boolean=false;
tokenmessage:String;
  constructor(private authService: AuthService,private activatedRoute: ActivatedRoute, private tokenStorage: TokenStorageService,private router: Router) { }

  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe((params: Params) => {
      if (params['token']!=undefined)
      this.authService.confirmUserAccount(params['token']).subscribe(

        data=>{
this.tokenavailable=true;
this.tokenmessage=data.message;
        },
        err=>{
        this.tokenavailable=true;
this.tokenmessage=err.error.message;}
      )
    });
    if (this.tokenStorage.getToken()) {
      this.isLoggedIn = true;
      this.roles = this.tokenStorage.getUser().roles;

    }
  }

  onSubmit(): void {
    const { username, password } = this.form;
this.tokenmessage="";
    this.authService.login(username, password).subscribe(
      data => {
        this.tokenStorage.saveToken(data.accessToken);
        this.tokenStorage.saveUser(data);
        console.log("data");
        console.log(data);

        this.isLoginFailed = false;
        this.isLoggedIn = true;
        this.roles = this.tokenStorage.getUser().roles;
        this.router.navigate(['/user-profile']);
      },
      err => {
        this.errorMessage = err.error.message;
        this.isLoginFailed = true;
      }
    );
  }

  reloadPage(): void {
    window.location.reload();
  }
}
