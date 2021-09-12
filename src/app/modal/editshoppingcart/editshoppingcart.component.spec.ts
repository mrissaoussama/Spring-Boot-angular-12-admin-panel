import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditshoppingcartComponent } from './editshoppingcart.component';

describe('EditshoppingcartComponent', () => {
  let component: EditshoppingcartComponent;
  let fixture: ComponentFixture<EditshoppingcartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [EditshoppingcartComponent]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EditshoppingcartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
