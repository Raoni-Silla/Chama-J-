import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AbaConta } from './aba-conta';

describe('AbaConta', () => {
  let component: AbaConta;
  let fixture: ComponentFixture<AbaConta>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AbaConta]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AbaConta);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
