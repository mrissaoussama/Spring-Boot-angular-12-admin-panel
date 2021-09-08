import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/_services/auth.service';
import { TokenStorageService } from 'src/app/_services/token-storage.service';
import { FormGroup, FormControl, ValidatorFn, AbstractControl, ValidationErrors } from '@angular/forms';
import { isDevMode } from '@angular/core';
import { Validators } from '@angular/forms';
@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent implements OnInit {
  isSuccessful: boolean;
  confirm_password: boolean;
  currentUser = this.tokenStorage.getUser();
  profileForm = new FormGroup({

    password: new FormControl('', [
      Validators.required,
      Validators.minLength(6)]),
    name: new FormControl(''),
    age: new FormControl(''),
    surname: new FormControl('', [
      Validators.minLength(3)]),
    address: new FormControl(''),
    city: new FormControl(''),
    country: new FormControl(''),
    job: new FormControl(''),
    description: new FormControl(''),
  });
  profileimg: string;
  id: number = this.currentUser.id;
  username: string = this.currentUser.username;

  imageForm = new FormGroup({

    image: new FormControl('')

  }
  );
  constructor(private tokenStorage: TokenStorageService, private authService: AuthService) { }
  // convenience getter for easy access to form fields
  get f() {
    return this.profileForm.controls

      ;
  }
  ngOnInit(): void {


    this.profileForm.patchValue({

      name: this.currentUser.name,
      age: this.currentUser.age,
      surname: this.currentUser.surname,
      address: this.currentUser.address,
      city: this.currentUser.city,
      country: this.currentUser.country,
      job: this.currentUser.job,
      description: this.currentUser.description,
    });


    {
      this.profileimg = '/assets/userimages/' + this.currentUser.id + '/profile.jpg';
    }

    console.log('after init');
    console.log("on profile");

    console.log(this.profileForm.value);
    console.log("on image");

    console.log(this.imageForm.value);
    console.log("current");

    console.log(this.currentUser.value);

  }



  public onFileChanged(event) {
    //Select File
    this.imageForm.patchValue({ image: event.target.files[0] });
    if (this.imageForm.get('image').value != null && this.profileForm.get('password').value?.length > 5)
      this.update();

  }

  update(): void {

    const image: FormData = new FormData();

    image.append('image', this.imageForm.get('image').value);
    image.append('username', this.currentUser.username);
    image.append('password', this.profileForm.get('password').value);
    console.log(image);

    this.authService.updateprofileimage(image).subscribe(
      data => {
        this.isSuccessful = true;
        window.location.reload();
      },
      err => {
        (err);
      }
    );
  }


  onSubmit(): boolean {
    console.log("on submit");
    console.log("on profile");

    console.log(this.profileForm.value);
    console.log("on image");

    console.log(this.imageForm.value);
    console.log("current");

    console.log(this.currentUser.value);
    if (!this.profileForm.valid) {
      this.profileForm.controls['password'].markAsTouched();
      return false;
    }
    {
      this.authService.update(this.currentUser["id"], this.currentUser["username"], this.profileForm).subscribe(
        data => {
          this.tokenStorage.saveToken(data.accessToken);
          this.tokenStorage.saveUser(data);
          window.location.reload();
          console.log("after submit");
          console.log("on profile");

          console.log(this.profileForm.value);
          console.log("on image");

          console.log(this.imageForm.value);
          console.log("current");

          console.log(this.currentUser.value);

        },
        err => {
          (err);
        }
      );
    }
  }


}
