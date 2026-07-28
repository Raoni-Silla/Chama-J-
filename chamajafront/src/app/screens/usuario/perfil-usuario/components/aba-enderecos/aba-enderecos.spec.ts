import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AbaEnderecos } from './aba-enderecos';

describe('AbaEnderecos', () => {
  let component: AbaEnderecos;
  let fixture: ComponentFixture<AbaEnderecos>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AbaEnderecos]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AbaEnderecos);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
